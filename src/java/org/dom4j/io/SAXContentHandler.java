package org.dom4j.io;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.Entity;
import org.dom4j.TreeException;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;

/** <p><code>SAXHandler</code> builds a DOM4J tree via SAX events.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SAXContentHandler extends DefaultHandler implements LexicalHandler {

    /** The document that is being built */
    private Document document;

    /** stack of <code>Element</code> objects */
    private ElementStack elementStack;

    /** the <code>ElementHandler</code> called as the elements are complete */
    private ElementHandler elementHandler;

    /** Used when inside an entity block */
    private Entity entity;
    
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
    
    
    public SAXContentHandler() {
    }
    
    public SAXContentHandler(Document document, ElementHandler elementHandler) {
        this.document = document;
        this.elementHandler = elementHandler;
    }

    public SAXContentHandler(Document document, ElementHandler elementHandler, ElementStack elementStack) {
        this.document = document;
        this.elementHandler = elementHandler;
        this.elementStack = elementStack;
    }

    
    // ContentHandler interface
    
    public void processingInstruction(String target, String data) throws SAXException {
        elementStack.peekElement().addProcessingInstruction(target, data);
    }

    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        addDeclaredNamespace( getNamespace(prefix, uri) );
    }

    public void endPrefixMapping(String prefix, String uri) throws SAXException {
        removeDeclaredNamespace( getNamespace(prefix, uri) );
    }

    public void startDocument() throws SAXException {
        if ( elementStack == null ) {
            elementStack = createElementStack();
        }
        else {
            elementStack.clear();
        }
    }
    
    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        Namespace namespace = null;
        if (namespaceURI != null && namespaceURI.length() > 0) {
            String prefix = "";
            if (localName != qName) {
                int index = qName.indexOf(":");
                if (index > 0) {
                    prefix = qName.substring(0, index);
                }
            }
            namespace = getNamespace(prefix, namespaceURI);
            removeDeclaredNamespace(namespace);
            addAvailableNamespace(namespace);
        }
            
        Element element = (namespace != null) 
            ? createElement(localName, namespace)
            : createElement(localName);

        // add all declared namespaces
        addDeclaredNamespaces(element);

        // now lets add all attribute values
        int size = attributes.getLength();
        for ( int i = 0; i < size; i++ ) {
            String attributeLocalName = attributes.getLocalName(i);
            String attributeQName = attributes.getQName(i);
            String attributeValue = attributes.getValue(i);
            
            namespace = null;
            if (attributeLocalName != attributeQName) {
                String attributePrefix = "";
                int index = attributeQName.indexOf(":");
                if ( index > 1 ) {
                    attributePrefix = attributeQName.substring(0, index);
                }
                namespace = getAvailableNamespace(attributePrefix);
            }
            if (namespace != null) {
                element.setAttributeValue(attributeLocalName, attributeValue, namespace);
            }
            else {
                element.setAttributeValue(attributeLocalName, attributeValue);
            }
        }

        elementStack.pushElement(element);
    }

    
    public void endElement(String namespaceURI, String localName, String qName) {
        Element element = elementStack.popElement();
        if (element != null) {
            
            // fire handler if we have one
            if ( elementHandler != null ) {
                elementHandler.handle( element );
            }
            
            // remove namespaces defined by the element
            List list = element.getAdditionalNamespaces();
            int size = list.size();
            for ( int i = 0; i < size; i++ ) {
                Namespace namespace = (Namespace) list.get(i);
                removeAvailableNamespace(namespace);
            }
        }
    }

    public void characters(char[] ch, int start, int end) throws SAXException {
        String text = new String(ch, start, end);
        Element element = elementStack.peekElement();
        
        if (entity != null) {
            // #### should we append context or replace?
            entity.setText(text);
        }
        else if (insideCDATASection) {
            element.addCDATA(text);
        }
        else {
            element.addText(text);
        }
    }

    public void error(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void warning(SAXParseException exception) throws SAXException {
        throw exception;
    }

    public void fatalError(SAXParseException exception) throws SAXException {
        throw exception;
    }

    // LexicalHandler interface
    
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
        Document document = getDocument();
        if (document != null) {
            document.setDocType(name, publicId, systemId);
        }
        insideDTDSection = true;
    }

    public void endDTD() throws SAXException {
        insideDTDSection = false;
    }

    public void startEntity(String name) throws SAXException {
        // Ignore DTD references
        if (! insideDTDSection && ! getIgnoreEntityNames().contains(name)) {
            Element element = elementStack.peekElement();
            entity = element.addEntity(name);
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
            Element element = elementStack.peekElement();
            element.addComment(text);
        }
    }

    
    // Implementation methods
    

    /** @return the current document 
      */
    protected Document getDocument() {
        return document;
    }
    
    protected void setRootElement(Element element) {
        Document document = getDocument();
        if (document != null) {
            document.setRootElement(element);
        }
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
    
    
    
    

    /** Make all of the additional namepaces in the given 
      * element available to other elements and attributes
      */
    protected void addDeclaredNamespaces(Element element) {        
        int size = declaredNamespaceList.size();
        for ( int i = 0; i < size; i++ ) {
            Namespace namespace = (Namespace) declaredNamespaceList.get(i);
            addAvailableNamespace(namespace);
            element.addAdditionalNamespace(namespace.getPrefix(), namespace.getURI());
        }
        declaredNamespaceList.clear();
    }

    protected void addDeclaredNamespace(Namespace namespace) {        
        if ( namespace != null ) {
            declaredNamespaceList.add(namespace);
        }
    }

    protected void removeDeclaredNamespace(Namespace namespace) {        
        if ( namespace != null ) {
            declaredNamespaceList.remove(namespace);
        }
    }

    protected void addAvailableNamespace(Namespace namespace) {
        if ( namespace != null ) {
            availableNamespaceMap.put(namespace.getPrefix(), namespace);
        }
    }
    
    protected void removeAvailableNamespace(Namespace namespace) {
        if ( namespace != null ) {
            availableNamespaceMap.remove(namespace.getPrefix());
        }
    }
    
    protected Namespace getAvailableNamespace(String prefix) {
        return (Namespace) availableNamespaceMap.get(prefix);
    }


    protected Namespace getNamespace(String prefix, String uri) {
        return null;
    }

    protected Element createElement(String localName, Namespace namespace) {
        Element parent = elementStack.peekElement();
        return (parent != null)
            ? parent.addElement(localName, namespace)
            : getDocument().addElement(localName, namespace);
    }
    
    protected Element createElement(String localName) {
        Element parent = elementStack.peekElement();
        return (parent != null)
            ? parent.addElement(localName)
            : getDocument().addElement(localName);
    }
    
    protected ElementStack createElementStack() {
        return new ElementStack();
    }
    
}
