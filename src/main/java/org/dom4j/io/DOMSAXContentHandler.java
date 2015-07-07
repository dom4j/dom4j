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
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.dom.DOMAttribute;
import org.dom4j.dom.DOMCDATA;
import org.dom4j.dom.DOMComment;
import org.dom4j.dom.DOMDocumentFactory;
import org.dom4j.dom.DOMElement;
import org.dom4j.dom.DOMText;
import org.dom4j.tree.NamespaceStack;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.ext.Locator2;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * <code>SAXContentHandler</code> builds W3C DOM object via SAX events.
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @author Todd Wolff
 * 
 * </p>
 */
public class DOMSAXContentHandler extends DefaultHandler implements LexicalHandler {
	
    /** The factory used to create new <code>Document</code> instances */
    private DOMDocumentFactory documentFactory;

    /** The document that is being built */
    private Document document;

    /** stack of <code>Element</code> objects */
    private ElementStack elementStack;

    /** stack of <code>Namespace</code> and <code>QName</code> objects */
    private NamespaceStack namespaceStack;

    /** the Locator */
    private Locator locator;

    /** Flag used to indicate that we are inside a CDATA section */
    private boolean insideCDATASection;

    /**
     * buffer to hold contents of cdata section across multiple characters
     * events
     */
    private StringBuffer cdataText;

    /** The number of namespaces that are declared in the current scope */
    private int declaredNamespaceIndex;

    private InputSource inputSource;

    /** The current element we are on */
    private Element currentElement;
	
    /** The entity resolver */
    private EntityResolver entityResolver;

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

    public DOMSAXContentHandler() {
        this((DOMDocumentFactory)DOMDocumentFactory.getInstance());
    }

    public DOMSAXContentHandler(DOMDocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
		this.elementStack = createElementStack();
        this.namespaceStack = new NamespaceStack(documentFactory);
    }

    /**
     * Retrieves w3c document object built via generated sax events.
     * 
     * @return the document that has been or is being built
     */
    public org.w3c.dom.Document getDocument() {
        if (document == null) {
            document = createDocument();
        }

        return (org.w3c.dom.Document)document;
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
		ProcessingInstruction pi = (ProcessingInstruction)documentFactory.createProcessingInstruction(target, data);
        if (currentElement != null) {
			((org.w3c.dom.Element)currentElement).appendChild(pi);
        } else {
			getDocument().appendChild(pi);
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
        document = null;
        currentElement = null;

        elementStack.clear();

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
            branch = (org.dom4j.Document)getDocument();
        }
		
		Element element = new DOMElement(qName);
        branch.add(element);

        // add all declared namespaces
        addDeclaredNamespaces(element);

        // now lets add all attribute values
        addAttributes(element, attributes);

        elementStack.pushElement(element);
        currentElement = element;

    }

    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {
        if (mergeAdjacentText && textInTextBuffer) {
            completeCurrentTextNode();
        }

        elementStack.popElement();
        currentElement = elementStack.peekElement();
    }

    public void characters(char[] ch, int start, int end) throws SAXException {
        if (end == 0) {
            return;
        }

        if (currentElement != null) {
            if (insideCDATASection) {
                if (mergeAdjacentText && textInTextBuffer) {
                    completeCurrentTextNode();
                }
                cdataText.append(new String(ch, start, end));
            } else {
                if (mergeAdjacentText) {
                    textBuffer.append(ch, start, end);
                    textInTextBuffer = true;
                } else {
					DOMText text = new DOMText(new String(ch, start, end));
					((DOMElement)currentElement).add(text);
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
		// not supported
    }

    public void endDTD() throws SAXException {
		// not supported	
    }

    public void startEntity(String name) throws SAXException {
		// not supported
    }

    public void endEntity(String name) throws SAXException {
		// not supported
    }

    public void startCDATA() throws SAXException {
        insideCDATASection = true;
        cdataText = new StringBuffer();
    }

    public void endCDATA() throws SAXException {
        insideCDATASection = false;
		DOMCDATA cdata = new DOMCDATA(cdataText.toString());
		((DOMElement)currentElement).add(cdata);
    }

    public void comment(char[] ch, int start, int end) throws SAXException {
        if (!ignoreComments) {
            if (mergeAdjacentText && textInTextBuffer) {
                completeCurrentTextNode();
            }

            String text = new String(ch, start, end);

            if (text.length() > 0) {
				DOMComment domComment = new DOMComment(text);
                if (currentElement != null) {
					((DOMElement)currentElement).add(domComment);
                } else {
                    getDocument().appendChild(domComment);
                }
            }
        }
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
				DOMText domText = new DOMText(textBuffer.toString());
                ((DOMElement)currentElement).add(domText);
            }
        } else {
			DOMText domText = new DOMText(textBuffer.toString());
            ((DOMElement)currentElement).add(domText);
        }

        textBuffer.setLength(0);
        textInTextBuffer = false;
    }

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

        if (locator instanceof Locator2) {
            return ((Locator2) locator).getEncoding();
        }

        // couldn't determine encoding, returning null...
        return null;
    }

    protected void addDeclaredNamespaces(Element element) {
        for (int size = namespaceStack.size(); declaredNamespaceIndex < size; 
                declaredNamespaceIndex++) {
            Namespace namespace = namespaceStack
                    .getNamespace(declaredNamespaceIndex);
			String attributeName = attributeNameForNamespace(namespace);
			((DOMElement)element).setAttribute(attributeName, namespace.getURI());
		}
    }

    protected void addAttributes(Element element, Attributes attributes) {
		int size = attributes.getLength();
		for (int i = 0; i < size; i++) {
            String attributeQName = attributes.getQName(i);
            if (!attributeQName.startsWith("xmlns")) {
                String attributeURI = attributes.getURI(i);
                String attributeLocalName = attributes.getLocalName(i);
                String attributeValue = attributes.getValue(i);
                QName qName = namespaceStack.getAttributeQName(
                        attributeURI, attributeLocalName, attributeQName);
				DOMAttribute domAttribute = new DOMAttribute(qName, attributeValue);
                ((DOMElement)element).setAttributeNode(domAttribute);
            }
        }
    }

    protected ElementStack createElementStack() {
        return new ElementStack();
    }
	
    protected String attributeNameForNamespace(Namespace namespace) {
        String xmlns = "xmlns";
        String prefix = namespace.getPrefix();

        if (prefix.length() > 0) {
            return xmlns + ":" + prefix;
        }

        return xmlns;
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
