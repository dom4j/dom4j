/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.dtd.AttributeDecl;
import org.dom4j.dtd.ElementDecl;
import org.dom4j.dtd.ExternalEntityDecl;
import org.dom4j.dtd.InternalEntityDecl;
import org.dom4j.tree.AbstractElement;
import org.dom4j.tree.NamespaceStack;

import org.xml.sax.Attributes;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * <code>SAXContentHandler</code> builds a dom4j tree via SAX events.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class SAXContentHandler extends DefaultHandler implements
        LexicalHandler, DeclHandler, DTDHandler {
    /** The factory used to create new <code>Document</code> instances */
    private DocumentFactory documentFactory;

    /** The document that is being built */
    private Document document;

    /** stack of <code>Element</code> objects */
    private ElementStack elementStack;

    /** stack of <code>Namespace</code> and <code>QName</code> objects */
    private NamespaceStack namespaceStack;

    /** the <code>ElementHandler</code> called as the elements are complete */
    private ElementHandler elementHandler;

    /** the Locator */
    private Locator locator;

    /** The name of the current entity */
    private String entity;

    /** Flag used to indicate that we are inside a DTD section */
    private boolean insideDTDSection;

    /** Flag used to indicate that we are inside a CDATA section */
    private boolean insideCDATASection;

    /**
     * buffer to hold contents of cdata section across multiple characters
     * events
     */
    private StringBuffer cdataText;

    /** namespaces that are available for use */
    private Map availableNamespaceMap = new HashMap();

    /** declared namespaces that are not yet available for use */
    private List declaredNamespaceList = new ArrayList();

    /** internal DTD declarations */
    private List internalDTDDeclarations;

    /** external DTD declarations */
    private List externalDTDDeclarations;

    /** The number of namespaces that are declared in the current scope */
    private int declaredNamespaceIndex;

    /** The entity resolver */
    private EntityResolver entityResolver;

    private InputSource inputSource;

    /** The current element we are on */
    private Element currentElement;

    /** Should internal DTD declarations be expanded into a List in the DTD */
    private boolean includeInternalDTDDeclarations = false;

    /** Should external DTD declarations be expanded into a List in the DTD */
    private boolean includeExternalDTDDeclarations = false;

    /** The number of levels deep we are inside a startEntity/endEntity call */
    private int entityLevel;

    /** Are we in an internal DTD subset? */
    private boolean internalDTDsubset = false;

    /** Whether adjacent text nodes should be merged */
    private boolean mergeAdjacentText = false;

    /** Have we added text to the buffer */
    private boolean textInTextBuffer = false;

    /** Should we ignore comments */
    private boolean ignoreComments = false;

    /** Buffer used to concatenate text together */
    private StringBuffer textBuffer;

    /** Holds value of property stripWhitespaceText. */
    private boolean stripWhitespaceText = false;

    public SAXContentHandler() {
        this(DocumentFactory.getInstance());
    }

    public SAXContentHandler(DocumentFactory documentFactory) {
        this(documentFactory, null);
    }

    public SAXContentHandler(DocumentFactory documentFactory,
            ElementHandler elementHandler) {
        this(documentFactory, elementHandler, null);
        this.elementStack = createElementStack();
    }

    public SAXContentHandler(DocumentFactory documentFactory,
            ElementHandler elementHandler, ElementStack elementStack) {
        this.documentFactory = documentFactory;
        this.elementHandler = elementHandler;
        this.elementStack = elementStack;
        this.namespaceStack = new NamespaceStack(documentFactory);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the document that has been or is being built
     */
    public Document getDocument() {
        if (document == null) {
            document = createDocument();
        }

        return document;
    }

    // ContentHandler interface
    // -------------------------------------------------------------------------
    public void setDocumentLocator(Locator documentLocator) {
        this.locator = documentLocator;
    }

    public void processingInstruction(String target, String data)
            throws SAXException {
        if (mergeAdjacentText && textInTextBuffer) {
            completeCurrentTextNode();
        }

        if (currentElement != null) {
            currentElement.addProcessingInstruction(target, data);
        } else {
            getDocument().addProcessingInstruction(target, data);
        }
    }

    public void startPrefixMapping(String prefix, String uri)
            throws SAXException {
        namespaceStack.push(prefix, uri);
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        namespaceStack.pop(prefix);
        declaredNamespaceIndex = namespaceStack.size();
    }

    public void startDocument() throws SAXException {
        // document = createDocument();
        document = null;
        currentElement = null;

        elementStack.clear();

        if ((elementHandler != null)
                && (elementHandler instanceof DispatchHandler)) {
            elementStack.setDispatchHandler((DispatchHandler) elementHandler);
        }

        namespaceStack.clear();
        declaredNamespaceIndex = 0;

        if (mergeAdjacentText && (textBuffer == null)) {
            textBuffer = new StringBuffer();
        }

        textInTextBuffer = false;
    }

    public void endDocument() throws SAXException {
        namespaceStack.clear();
        elementStack.clear();
        currentElement = null;
        textBuffer = null;
    }

    public void startElement(String namespaceURI, String localName,
            String qualifiedName, Attributes attributes) throws SAXException {
        if (mergeAdjacentText && textInTextBuffer) {
            completeCurrentTextNode();
        }

        QName qName = namespaceStack.getQName(namespaceURI, localName,
                qualifiedName);

        Branch branch = currentElement;

        if (branch == null) {
            branch = getDocument();
        }

        Element element = branch.addElement(qName);

        // add all declared namespaces
        addDeclaredNamespaces(element);

        // now lets add all attribute values
        addAttributes(element, attributes);

        elementStack.pushElement(element);
        currentElement = element;

        entity = null; // fixes bug527062

        if (elementHandler != null) {
            elementHandler.onStart(elementStack);
        }
    }

    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        if (mergeAdjacentText && textInTextBuffer) {
            completeCurrentTextNode();
        }

        if ((elementHandler != null) && (currentElement != null)) {
            elementHandler.onEnd(elementStack);
        }

        elementStack.popElement();
        currentElement = elementStack.peekElement();
    }

    public void characters(char[] ch, int start, int end) throws SAXException {
        if (end == 0) {
            return;
        }

        if (currentElement != null) {
            if (entity != null) {
                if (mergeAdjacentText && textInTextBuffer) {
                    completeCurrentTextNode();
                }

                currentElement.addEntity(entity, new String(ch, start, end));
                entity = null;
            } else if (insideCDATASection) {
                if (mergeAdjacentText && textInTextBuffer) {
                    completeCurrentTextNode();
                }

                cdataText.append(new String(ch, start, end));
            } else {
                if (mergeAdjacentText) {
                    textBuffer.append(ch, start, end);
                    textInTextBuffer = true;
                } else {
                    currentElement.addText(new String(ch, start, end));
                }
            }
        }
    }

    // ErrorHandler interface
    // -------------------------------------------------------------------------

    /**
     * This method is called when a warning occurs during the parsing of the
     * document. This method does nothing.
     * 
     * @param exception
     *            DOCUMENT ME!
     * 
     * @throws SAXException
     *             DOCUMENT ME!
     */
    public void warning(SAXParseException exception) throws SAXException {
        // ignore warnings by default
    }

    /**
     * This method is called when an error is detected during parsing such as a
     * validation error. This method rethrows the exception
     * 
     * @param exception
     *            DOCUMENT ME!
     * 
     * @throws SAXException
     *             DOCUMENT ME!
     */
    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    /**
     * This method is called when a fatal error occurs during parsing. This
     * method rethrows the exception
     * 
     * @param exception
     *            DOCUMENT ME!
     * 
     * @throws SAXException
     *             DOCUMENT ME!
     */
    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    // LexicalHandler interface
    // -------------------------------------------------------------------------
    public void startDTD(String name, String publicId, String systemId)
            throws SAXException {
        getDocument().addDocType(name, publicId, systemId);
        insideDTDSection = true;
        internalDTDsubset = true;
    }

    public void endDTD() throws SAXException {
        insideDTDSection = false;

        DocumentType docType = getDocument().getDocType();

        if (docType != null) {
            if (internalDTDDeclarations != null) {
                docType.setInternalDeclarations(internalDTDDeclarations);
            }

            if (externalDTDDeclarations != null) {
                docType.setExternalDeclarations(externalDTDDeclarations);
            }
        }

        internalDTDDeclarations = null;
        externalDTDDeclarations = null;
    }

    public void startEntity(String name) throws SAXException {
        ++entityLevel;

        // Ignore DTD references
        entity = null;

        if (!insideDTDSection) {
            if (!isIgnorableEntity(name)) {
                entity = name;
            }
        }

        // internal DTD subsets can only appear outside of a
        // startEntity/endEntity block
        // see the startDTD method in
        // http://dom4j.org/javadoc/org/xml/sax/ext/LexicalHandler.html
        internalDTDsubset = false;
    }

    public void endEntity(String name) throws SAXException {
        --entityLevel;
        entity = null;

        if (entityLevel == 0) {
            internalDTDsubset = true;
        }
    }

    public void startCDATA() throws SAXException {
        insideCDATASection = true;
        cdataText = new StringBuffer();
    }

    public void endCDATA() throws SAXException {
        insideCDATASection = false;
        currentElement.addCDATA(cdataText.toString());
    }

    public void comment(char[] ch, int start, int end) throws SAXException {
        if (!ignoreComments) {
            if (mergeAdjacentText && textInTextBuffer) {
                completeCurrentTextNode();
            }

            String text = new String(ch, start, end);

            if (!insideDTDSection && (text.length() > 0)) {
                if (currentElement != null) {
                    currentElement.addComment(text);
                } else {
                    getDocument().addComment(text);
                }
            }
        }
    }

    // DeclHandler interface
    // -------------------------------------------------------------------------

    /**
     * Report an element type declaration.
     * 
     * <p>
     * The content model will consist of the string "EMPTY", the string "ANY",
     * or a parenthesised group, optionally followed by an occurrence indicator.
     * The model will be normalized so that all parameter entities are fully
     * resolved and all whitespace is removed,and will include the enclosing
     * parentheses. Other normalization (such as removing redundant parentheses
     * or simplifying occurrence indicators) is at the discretion of the parser.
     * </p>
     * 
     * @param name
     *            The element type name.
     * @param model
     *            The content model as a normalized string.
     * 
     * @exception SAXException
     *                The application may raise an exception.
     */
    public void elementDecl(String name, String model) throws SAXException {
        if (internalDTDsubset) {
            if (includeInternalDTDDeclarations) {
                addDTDDeclaration(new ElementDecl(name, model));
            }
        } else {
            if (includeExternalDTDDeclarations) {
                addExternalDTDDeclaration(new ElementDecl(name, model));
            }
        }
    }

    /**
     * Report an attribute type declaration.
     * 
     * <p>
     * Only the effective (first) declaration for an attribute will be reported.
     * The type will be one of the strings "CDATA", "ID", "IDREF", "IDREFS",
     * "NMTOKEN", "NMTOKENS", "ENTITY", "ENTITIES", a parenthesized token group
     * with the separator "|" and all whitespace removed, or the word "NOTATION"
     * followed by a space followed by a parenthesized token group with all
     * whitespace removed.
     * </p>
     * 
     * <p>
     * Any parameter entities in the attribute value will be expanded, but
     * general entities will not.
     * </p>
     * 
     * @param eName
     *            The name of the associated element.
     * @param aName
     *            The name of the attribute.
     * @param type
     *            A string representing the attribute type.
     * @param valueDefault
     *            A string representing the attribute default ("#IMPLIED",
     *            "#REQUIRED", or "#FIXED") or null if none of these applies.
     * @param val
     *            A string representing the attribute's default value, or null
     *            if there is none.
     * 
     * @exception SAXException
     *                The application may raise an exception.
     */
    public void attributeDecl(String eName, String aName, String type,
            String valueDefault, String val) throws SAXException {
        if (internalDTDsubset) {
            if (includeInternalDTDDeclarations) {
                addDTDDeclaration(new AttributeDecl(eName, aName, type,
                        valueDefault, val));
            }
        } else {
            if (includeExternalDTDDeclarations) {
                addExternalDTDDeclaration(new AttributeDecl(eName, aName, type,
                        valueDefault, val));
            }
        }
    }

    /**
     * Report an internal entity declaration.
     * 
     * <p>
     * Only the effective (first) declaration for each entity will be reported.
     * All parameter entities in the value will be expanded, but general
     * entities will not.
     * </p>
     * 
     * @param name
     *            The name of the entity. If it is a parameter entity, the name
     *            will begin with '%'.
     * @param value
     *            The replacement text of the entity.
     * 
     * @exception SAXException
     *                The application may raise an exception.
     * 
     * @see #externalEntityDecl
     * @see org.xml.sax.DTDHandler#unparsedEntityDecl
     */
    public void internalEntityDecl(String name, String value)
            throws SAXException {
        if (internalDTDsubset) {
            if (includeInternalDTDDeclarations) {
                addDTDDeclaration(new InternalEntityDecl(name, value));
            }
        } else {
            if (includeExternalDTDDeclarations) {
                addExternalDTDDeclaration(new InternalEntityDecl(name, value));
            }
        }
    }

    /**
     * Report a parsed external entity declaration.
     * 
     * <p>
     * Only the effective (first) declaration for each entity will be reported.
     * </p>
     * 
     * @param name
     *            The name of the entity. If it is a parameter entity, the name
     *            will begin with '%'.
     * @param publicId
     *            The declared public identifier of the entity, or null if none
     *            was declared.
     * @param sysId
     *            The declared system identifier of the entity.
     * 
     * @exception SAXException
     *                The application may raise an exception.
     * 
     * @see #internalEntityDecl
     * @see org.xml.sax.DTDHandler#unparsedEntityDecl
     */
    public void externalEntityDecl(String name, String publicId, String sysId)
            throws SAXException {
        ExternalEntityDecl declaration = new ExternalEntityDecl(name, publicId,
                sysId);

        if (internalDTDsubset) {
            if (includeInternalDTDDeclarations) {
                addDTDDeclaration(declaration);
            }
        } else {
            if (includeExternalDTDDeclarations) {
                addExternalDTDDeclaration(declaration);
            }
        }
    }

    // DTDHandler interface
    // -------------------------------------------------------------------------

    /**
     * Receive notification of a notation declaration event.
     * 
     * <p>
     * It is up to the application to record the notation for later reference,
     * if necessary.
     * </p>
     * 
     * <p>
     * At least one of publicId and systemId must be non-null. If a system
     * identifier is present, and it is a URL, the SAX parser must resolve it
     * fully before passing it to the application through this event.
     * </p>
     * 
     * <p>
     * There is no guarantee that the notation declaration will be reported
     * before any unparsed entities that use it.
     * </p>
     * 
     * @param name
     *            The notation name.
     * @param publicId
     *            The notation's public identifier, or null if none was given.
     * @param systemId
     *            The notation's system identifier, or null if none was given.
     * 
     * @exception SAXException
     *                Any SAX exception, possibly wrapping another exception.
     * 
     * @see #unparsedEntityDecl
     * @see org.xml.sax.AttributeList
     */
    public void notationDecl(String name, String publicId, String systemId)
            throws SAXException {
        // #### not supported yet!
    }

    /**
     * Receive notification of an unparsed entity declaration event.
     * 
     * <p>
     * Note that the notation name corresponds to a notation reported by the
     * {@link #notationDecl notationDecl}event. It is up to the application to
     * record the entity for later reference, if necessary.
     * </p>
     * 
     * <p>
     * If the system identifier is a URL, the parser must resolve it fully
     * before passing it to the application.
     * </p>
     * 
     * @param name
     *            The unparsed entity's name.
     * @param publicId
     *            The entity's public identifier, or null if none was given.
     * @param systemId
     *            The entity's system identifier.
     * @param notationName
     *            The name of the associated notation.
     * 
     * @exception SAXException
     *                Any SAX exception, possibly wrapping another exception.
     * 
     * @see #notationDecl
     * @see org.xml.sax.AttributeList
     */
    public void unparsedEntityDecl(String name, String publicId,
            String systemId, String notationName) throws SAXException {
        // #### not supported yet!
    }

    // Properties
    // -------------------------------------------------------------------------
    public ElementStack getElementStack() {
        return elementStack;
    }

    public void setElementStack(ElementStack elementStack) {
        this.elementStack = elementStack;
    }

    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    public InputSource getInputSource() {
        return inputSource;
    }

    public void setInputSource(InputSource inputSource) {
        this.inputSource = inputSource;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return whether internal DTD declarations should be expanded into the
     *         DocumentType object or not.
     */
    public boolean isIncludeInternalDTDDeclarations() {
        return includeInternalDTDDeclarations;
    }

    /**
     * Sets whether internal DTD declarations should be expanded into the
     * DocumentType object or not.
     * 
     * @param include
     *            whether or not DTD declarations should be expanded and
     *            included into the DocumentType object.
     */
    public void setIncludeInternalDTDDeclarations(boolean include) {
        this.includeInternalDTDDeclarations = include;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return whether external DTD declarations should be expanded into the
     *         DocumentType object or not.
     */
    public boolean isIncludeExternalDTDDeclarations() {
        return includeExternalDTDDeclarations;
    }

    /**
     * Sets whether DTD external declarations should be expanded into the
     * DocumentType object or not.
     * 
     * @param include
     *            whether or not DTD declarations should be expanded and
     *            included into the DocumentType object.
     */
    public void setIncludeExternalDTDDeclarations(boolean include) {
        this.includeExternalDTDDeclarations = include;
    }

    /**
     * Returns whether adjacent text nodes should be merged together.
     * 
     * @return Value of property mergeAdjacentText.
     */
    public boolean isMergeAdjacentText() {
        return mergeAdjacentText;
    }

    /**
     * Sets whether or not adjacent text nodes should be merged together when
     * parsing.
     * 
     * @param mergeAdjacentText
     *            New value of property mergeAdjacentText.
     */
    public void setMergeAdjacentText(boolean mergeAdjacentText) {
        this.mergeAdjacentText = mergeAdjacentText;
    }

    /**
     * Sets whether whitespace between element start and end tags should be
     * ignored
     * 
     * @return Value of property stripWhitespaceText.
     */
    public boolean isStripWhitespaceText() {
        return stripWhitespaceText;
    }

    /**
     * Sets whether whitespace between element start and end tags should be
     * ignored.
     * 
     * @param stripWhitespaceText
     *            New value of property stripWhitespaceText.
     */
    public void setStripWhitespaceText(boolean stripWhitespaceText) {
        this.stripWhitespaceText = stripWhitespaceText;
    }

    /**
     * Returns whether we should ignore comments or not.
     * 
     * @return boolean
     */
    public boolean isIgnoreComments() {
        return ignoreComments;
    }

    /**
     * Sets whether we should ignore comments or not.
     * 
     * @param ignoreComments
     *            whether we should ignore comments or not.
     */
    public void setIgnoreComments(boolean ignoreComments) {
        this.ignoreComments = ignoreComments;
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * If the current text buffer contains any text then create a new text node
     * with it and add it to the current element
     */
    protected void completeCurrentTextNode() {
        if (stripWhitespaceText) {
            boolean whitespace = true;

            for (int i = 0, size = textBuffer.length(); i < size; i++) {
                if (!Character.isWhitespace(textBuffer.charAt(i))) {
                    whitespace = false;

                    break;
                }
            }

            if (!whitespace) {
                currentElement.addText(textBuffer.toString());
            }
        } else {
            currentElement.addText(textBuffer.toString());
        }

        textBuffer.setLength(0);
        textInTextBuffer = false;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the current document
     */
    protected Document createDocument() {
        String encoding = getEncoding();
        Document doc = documentFactory.createDocument(encoding);

        // set the EntityResolver
        doc.setEntityResolver(entityResolver);

        if (inputSource != null) {
            doc.setName(inputSource.getSystemId());
        }

        return doc;
    }

    private String getEncoding() {
        if (locator == null) {
            return null;
        }

        // use reflection to avoid dependency on Locator2
        // or other locator implemenations.
        try {
            Method m = locator.getClass().getMethod("getEncoding",
                    new Class[] {});

            if (m != null) {
                return (String) m.invoke(locator, null);
            }
        } catch (Exception e) {
            // do nothing
        }

        // couldn't determine encoding, returning null...
        return null;
    }

    /**
     * a Strategy Method to determine if a given entity name is ignorable
     * 
     * @param name
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected boolean isIgnorableEntity(String name) {
        return "amp".equals(name) || "apos".equals(name) || "gt".equals(name)
                || "lt".equals(name) || "quot".equals(name);
    }

    /**
     * Add all namespaces declared before the startElement() SAX event to the
     * current element so that they are available to child elements and
     * attributes
     * 
     * @param element
     *            DOCUMENT ME!
     */
    protected void addDeclaredNamespaces(Element element) {
        Namespace elementNamespace = element.getNamespace();

        for (int size = namespaceStack.size(); declaredNamespaceIndex < size; 
                declaredNamespaceIndex++) {
            Namespace namespace = namespaceStack
                    .getNamespace(declaredNamespaceIndex);

            // if ( namespace != elementNamespace ) {
            element.add(namespace);

            // }
        }
    }

    /**
     * Add all the attributes to the given elements
     * 
     * @param element
     *            DOCUMENT ME!
     * @param attributes
     *            DOCUMENT ME!
     */
    protected void addAttributes(Element element, Attributes attributes) {
        // XXXX: as an optimisation, we could deduce this value from the current
        // SAX parser settings, the SAX namespaces-prefixes feature
        boolean noNamespaceAttributes = false;

        if (element instanceof AbstractElement) {
            // optimised method
            AbstractElement baseElement = (AbstractElement) element;
            baseElement.setAttributes(attributes, namespaceStack,
                    noNamespaceAttributes);
        } else {
            int size = attributes.getLength();

            for (int i = 0; i < size; i++) {
                String attributeQName = attributes.getQName(i);

                if (noNamespaceAttributes
                        || !attributeQName.startsWith("xmlns")) {
                    String attributeURI = attributes.getURI(i);
                    String attributeLocalName = attributes.getLocalName(i);
                    String attributeValue = attributes.getValue(i);

                    QName qName = namespaceStack.getAttributeQName(
                            attributeURI, attributeLocalName, attributeQName);
                    element.addAttribute(qName, attributeValue);
                }
            }
        }
    }

    /**
     * Adds an internal DTD declaration to the list of declarations
     * 
     * @param declaration
     *            DOCUMENT ME!
     */
    protected void addDTDDeclaration(Object declaration) {
        if (internalDTDDeclarations == null) {
            internalDTDDeclarations = new ArrayList();
        }

        internalDTDDeclarations.add(declaration);
    }

    /**
     * Adds an external DTD declaration to the list of declarations
     * 
     * @param declaration
     *            DOCUMENT ME!
     */
    protected void addExternalDTDDeclaration(Object declaration) {
        if (externalDTDDeclarations == null) {
            externalDTDDeclarations = new ArrayList();
        }

        externalDTDDeclarations.add(declaration);
    }

    protected ElementStack createElementStack() {
        return new ElementStack();
    }
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
