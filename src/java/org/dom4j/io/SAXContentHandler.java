/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.ProcessingInstruction;
import org.dom4j.DocumentException;

import org.dom4j.tree.AbstractElement;
import org.dom4j.tree.NamespaceStack;

import org.xml.sax.Attributes;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/** <p><code>SAXHandler</code> builds a DOM4J tree via SAX events.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class SAXContentHandler extends DefaultHandler implements LexicalHandler {

    /** Should standard entities be passed through? */
    private static final boolean SHOW_STANDARD_ENTITIES = true;
    
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

    /** The name of the current entity */
    private String entity;
    
    /** Flag used to indicate that we are inside a DTD section */
    private boolean insideDTDSection;

    /** Flag used to indicate that we are inside a CDATA section */
    private boolean insideCDATASection;
    
    /** namespaces that are available for use */
    private Map availableNamespaceMap = new HashMap();

    /** declared namespaces that are not yet available for use */
    private List declaredNamespaceList = new ArrayList();

    /** A <code>Set</code> of the entity names we should ignore */
    private Set ignoreEntityNames;

    /** The number of namespaces that are declared in the current scope */
    private int declaredNamespaceIndex;
    
    private EntityResolver entityResolver;
    
    private InputSource inputSource;
    
    /** The current element we are on */
    private Element currentElement;
    
    
    public SAXContentHandler() {
        this( DocumentFactory.getInstance() );
    }
    
    public SAXContentHandler(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
        this.namespaceStack = new NamespaceStack(documentFactory);
    }

    public SAXContentHandler(DocumentFactory documentFactory, ElementHandler elementHandler) {
        this.documentFactory = documentFactory;
        this.elementHandler = elementHandler;
        this.namespaceStack = new NamespaceStack(documentFactory);
    }

    public SAXContentHandler(DocumentFactory documentFactory, ElementHandler elementHandler, ElementStack elementStack) {
        this.documentFactory = documentFactory;
        this.elementHandler = elementHandler;
        this.elementStack = elementStack;
        this.namespaceStack = new NamespaceStack(documentFactory);
    }

    /** @return the document that has been or is being built 
      */
    public Document getDocument() {
        if ( document == null ) {
            document = createDocument();
        }
        return document;
    }
    
    // ContentHandler interface
    //-------------------------------------------------------------------------
    
    public void processingInstruction(String target, String data) throws SAXException {
        if ( currentElement != null ) {
            currentElement.addProcessingInstruction(target, data);
        }
        else {
            document.addProcessingInstruction(target, data);
        }
    }
    
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        namespaceStack.push( prefix, uri );
    }

    public void endPrefixMapping(String prefix) throws SAXException {
        namespaceStack.pop( prefix );
        declaredNamespaceIndex = namespaceStack.size();
    }

    public void startDocument() throws SAXException {
        document = createDocument();
        currentElement = null;
        
        if ( elementStack == null ) {
            elementStack = createElementStack();
        }
        else {
            elementStack.clear();
        }
        if ( (elementHandler != null) &&
             (elementHandler instanceof DispatchHandler) ) {
            elementStack.setDispatchHandler((DispatchHandler)elementHandler);   
        }
        
        namespaceStack.clear();
        declaredNamespaceIndex = 0;
    }
    
    public void endDocument() throws SAXException {
        namespaceStack.clear();
        elementStack.clear();
        currentElement = null;        
    }
    
    public void startElement(String namespaceURI, String localName, String qualifiedName, Attributes attributes) throws SAXException {
        QName qName = namespaceStack.getQName( 
            namespaceURI, localName, qualifiedName 
        );
        
        Branch branch = currentElement;
        if ( branch == null ) {
            branch = document;
        }
        Element element = branch.addElement(qName);
        
        // add all declared namespaces
        addDeclaredNamespaces(element);

        // now lets add all attribute values
        addAttributes( element, attributes );
        
        elementStack.pushElement(element);
        currentElement = element;

        
        if ( elementHandler != null ) {
            elementHandler.onStart(elementStack);   
        }
    }

    public void endElement(String namespaceURI, String localName, String qName) {
        if ( elementHandler != null && currentElement != null ) {
            elementHandler.onEnd(elementStack);
        }
        elementStack.popElement();
        currentElement = elementStack.peekElement();
    }

    public void characters(char[] ch, int start, int end) throws SAXException {
        String text = new String(ch, start, end);
        if ( currentElement != null ) {
            if (entity != null) {
                currentElement.addEntity(entity, text);
                entity = null;
            }
            else if (insideCDATASection) {
                currentElement.addCDATA(text);
            }
            else {
                currentElement.addText(text);
            }
        }
    }

    // ErrorHandler interface
    //-------------------------------------------------------------------------
    
    /** This method is called when a warning occurs during the parsing
      * of the document. 
      * This method does nothing.
      */
    public void warning(SAXParseException exception) throws SAXException {
        // ignore warnings by default
    }

    /** This method is called when an error is detected during parsing
      * such as a validation error.
      * This method rethrows the exception
      */
    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    /** This method is called when a fatal error occurs during parsing.
      * This method rethrows the exception
      */
    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    // LexicalHandler interface
    //-------------------------------------------------------------------------
    
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        if (document != null) {
            document.addDocType(name, publicId, systemId);
        }
        insideDTDSection = true;
    }

    public void endDTD() throws SAXException {
        insideDTDSection = false;
    }

    public void startEntity(String name) throws SAXException {
        // Ignore DTD references
        entity = null;
        if (! insideDTDSection ) {
            if ( SHOW_STANDARD_ENTITIES || ! getIgnoreEntityNames().contains(name)) {
                entity = name;
            }
        }
    }

    public void endEntity(String name) throws SAXException {
        entity = null;
    }

    public void startCDATA() throws SAXException {
        insideCDATASection = true;
    }

    public void endCDATA() throws SAXException {
        insideCDATASection = false;
    }
    
    public void comment(char[] ch, int start, int end) throws SAXException {
        String text = new String(ch, start, end);
        if (!insideDTDSection && text.length() > 0) {
            if ( currentElement != null ) {
                currentElement.addComment(text);
            }
            else {
                document.addComment(text);
            }
        }
    }

    
    // Properties
    //-------------------------------------------------------------------------
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

    
    // Implementation methods
    //-------------------------------------------------------------------------

    /** @return the current document 
      */
    protected Document createDocument() {
        Document document = documentFactory.createDocument();
        
        // set the EntityResolver
        document.setEntityResolver(entityResolver);
        if ( inputSource != null ) {
            document.setName( inputSource.getSystemId() );
        }
            
        return document;
    }
    
    /** @return the set of entity names which are ignored
      */
    protected Set getIgnoreEntityNames() {
        if ( ignoreEntityNames == null ) {
            ignoreEntityNames = createIgnoreEntityNames();
        }
        return ignoreEntityNames;
    }
    
    /** a Factory Method to create a set of entity names which are ignored
      */
    protected Set createIgnoreEntityNames() {
        HashSet answer = new HashSet();
        answer.add("amp");
        answer.add("apos");
        answer.add("gt");
        answer.add("lt");
        answer.add("quot");
        return answer;
    }
    
    
    
    

    /** Add all namespaces declared before the startElement() SAX event
      * to the current element so that they are available to child elements 
      * and attributes
      */
    protected void addDeclaredNamespaces(Element element) {        
        Namespace elementNamespace = element.getNamespace();
        for ( int size = namespaceStack.size(); declaredNamespaceIndex < size; declaredNamespaceIndex++ ) {
            Namespace namespace = namespaceStack.getNamespace(declaredNamespaceIndex);
            if ( namespace != elementNamespace ) {
                String prefix = namespace.getPrefix();
                if ( prefix != null && prefix.length() > 0 ) {
                    element.add( namespace );
                }
            }
        }
    }

    /** Add all the attributes to the given elements
      */
    protected void addAttributes( Element element, Attributes attributes ) {
        if ( element instanceof AbstractElement ) {
            // optimised method
            AbstractElement baseElement = (AbstractElement) element;
            baseElement.setAttributes( attributes, namespaceStack );
        }
        else {
            int size = attributes.getLength();
            for ( int i = 0; i < size; i++ ) {
                String attributeQualifiedName = attributes.getQName(i);
                String attributeURI = attributes.getURI(i);
                String attributeLocalName = attributes.getLocalName(i);
                String attributeValue = attributes.getValue(i);

                QName attributeQName = namespaceStack.getQName( 
                    attributeURI, attributeLocalName, attributeQualifiedName 
                );
                element.addAttribute(attributeQName, attributeValue);
            }
        }
    }


    
    protected ElementStack createElementStack() {
        return new ElementStack();
    }
       
}




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
