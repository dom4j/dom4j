/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.DocumentFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.IllegalAddException;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.Visitor;
import org.dom4j.io.XMLWriter;

/** <p><code>AbstractElement</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractElement extends AbstractBranch implements Element {

    /** The <code>DocumentFactory</code> instance used by default */
    private static final DocumentFactory DOCUMENT_FACTORY = DocumentFactory.getInstance();
    
    protected static final List EMPTY_LIST = Collections.EMPTY_LIST;
    protected static final Iterator EMPTY_ITERATOR = EMPTY_LIST.iterator();
    
    
    protected static final int DEFAULT_CONTENT_LIST_SIZE = 5;
    
    protected static final boolean VERBOSE_TOSTRING = false;
        
    
    public AbstractElement() { 
    }

    public short getNodeType() {
        return ELEMENT_NODE;
    }
    
    public boolean isRootElement() {
        Document document = getDocument();
        if ( document != null ) {
            Element root = document.getRootElement();
            if ( root == this ) {
                return true;
            }
        }
        return false;
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("The name and namespace of this Element cannot be changed" );
    }
    
    public void setNamespace(Namespace namespace) {
        throw new UnsupportedOperationException("The name and namespace of this Element cannot be changed" );
    }
        
    public String getPath() {
        Element parent = getParent();
        if ( parent == null ) {
            return "/" + getQualifiedName();
        }
        return parent.getPath() + "/" + getQualifiedName();
    }
    
    public String getUniquePath() {
        Element parent = getParent();
        if ( parent == null ) {
            return "/" + getQualifiedName();
        }
        StringBuffer buffer = new StringBuffer( parent.getPath() );
        buffer.append( "/" );
        buffer.append( getQualifiedName() );
        List mySiblings = parent.elements( getQName() );
        if ( mySiblings.size() > 1 ) {
            int idx = mySiblings.indexOf( this );
            if ( idx > 0 ) {
                buffer.append( "[" );
                buffer.append( Integer.toString( ++idx ) );
                buffer.append( "]" );
            }
            
        }
        return buffer.toString();
    }
    
    public String asXML() {
        try {
            StringWriter out = new StringWriter();
            XMLWriter writer = new XMLWriter( out, outputFormat );
            writer.write(this);
            return out.toString();
        } 
        catch (IOException e) {
            throw new RuntimeException("Wierd IOException while generating textual representation: " + e.getMessage());
        }
    }

    public void write(Writer out) throws IOException {
        XMLWriter writer = new XMLWriter( out, outputFormat );
        writer.write(this);
    }
        
    /** <p><code>accept</code> method is the <code>Visitor Pattern</code> method.
      * </p>
      *
      * @param visitor <code>Visitor</code> is the visitor.
      */
    public void accept(Visitor visitor) {
        visitor.visit(this);
        
        // visit attributes
        for ( int i = 0, size = attributeCount(); i < size; i++ ) {
            Attribute attribute = attribute(i);
            visitor.visit(attribute);
        }
        
        // visit content
        for ( int i = 0, size = nodeCount(); i < size; i++ ) {
            Node node = node(i);
            node.accept(visitor);
        }
    }
    
    public String toString() {
        if ( VERBOSE_TOSTRING ) {
            return super.toString() + " [Element: <" + getQualifiedName() 
                + " uri: " + getNamespaceURI()
                + " attributes: " + attributeList()
                + " content: " + contentList() + " />]";
        }
        else {
            return super.toString() + " [Element: <" + getQualifiedName() 
                + " uri: " + getNamespaceURI()
                + " attributes: " + attributeList() + "/>]";
        }
    }
    

    // QName methods
    //-------------------------------------------------------------------------    
    
    public Namespace getNamespace() {
        return getQName().getNamespace();
    }
    
    public String getName() {
        return getQName().getName();
    }
    
    public String getNamespacePrefix() {
        return getQName().getNamespacePrefix();
    }

    public String getNamespaceURI() {
        return getQName().getNamespaceURI();
    }

    public String getQualifiedName() {
        return getQName().getQualifiedName();
    }

    
    public Object getData() {
        return getText();
    }
    
    public void setData(Object data) {
        // ignore this method
    }
    
    
    
    
    

    // Node methods
    //-------------------------------------------------------------------------    
    public Node node(int index) {
        if ( index >= 0 ) {
            List list = contentList();
            if ( index >= list.size() ) {
                return null;
            }
            Object node = list.get(index);
            if (node != null) {
                if (node instanceof Node) {
                    return (Node) node;
                }
                else {
                    return new DefaultText(node.toString());
                }
            }
        }
        return null;
    }
    
    public int indexOf(Node node) {
        return contentList().indexOf( node );
    }
    
    public int nodeCount() {
        return contentList().size();
    }
    
    public Iterator nodeIterator() {
        return contentList().iterator();
    }

    
    
    
    // Element methods
    //-------------------------------------------------------------------------    
    
    public Element element(String name) {
        List list = contentList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof Element ) {
                Element element = (Element) object;
                if ( name.equals( element.getName() ) ) {
                    return element;
                }
            }
        }
        return null;
    }
    
    public Element element(QName qName) {
        List list = contentList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof Element ) {
                Element element = (Element) object;
                if ( qName.equals( element.getQName() ) ) {
                    return element;
                }
            }
        }
        return null;
    }

    public Element element(String name, Namespace namespace) {
        return element( getDocumentFactory().createQName( name, namespace ) );
    }
    
    
    
    public List elements() {
        List list = contentList();
        BackedList answer = createResultList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof Element ) {
                answer.addLocal( object );
            }
        }
        return answer;
    }
    
    public List elements(String name) {
        List list = contentList();
        BackedList answer = createResultList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof Element ) {
                Element element = (Element) object;
                if ( name.equals( element.getName() ) ) {
                    answer.addLocal( element );
                }
            }
        }
        return answer;
    }
    
    public List elements(QName qName) {
        List list = contentList();
        BackedList answer = createResultList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof Element ) {
                Element element = (Element) object;
                if ( qName.equals( element.getQName() ) ) {
                    answer.addLocal( element );
                }
            }
        }
        return answer;
    }
    
    public List elements(String name, Namespace namespace) {
        return elements( getDocumentFactory().createQName(name, namespace ) );
    }
    
    public Iterator elementIterator() {
        List list = contentList();
        return new ElementIterator(list.iterator());
    }
        
    public Iterator elementIterator(String name) {
        List list = contentList();
        return new ElementNameIterator(list.iterator(), name);
    }
    
    public Iterator elementIterator(QName qName) {
        List list = contentList();
        return new ElementQNameIterator(list.iterator(), qName);
    }
    
    public Iterator elementIterator(String name, Namespace namespace) {
        return elementIterator( getDocumentFactory().createQName( name, namespace ) );
    }

    
    
    
    
    
    // Attribute methods
    //-------------------------------------------------------------------------    
    
    
    public List attributes() {
        return new ContentListFacade(this, attributeList());
    }
    

    public Iterator attributeIterator() {
        return attributeList().iterator();
    }
    
    public Attribute attribute(int index) {
        return (Attribute) attributeList().get(index);
    }
            
    public int attributeCount() {
        return attributeList().size();
    }
    
    public Attribute attribute(String name) {
        List list = attributeList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Attribute attribute = (Attribute) list.get(i);
            if ( name.equals( attribute.getName() ) ) {
                return attribute;
            }
        }
        return null;
    }

    public Attribute attribute(QName qName) {
        List list = attributeList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Attribute attribute = (Attribute) list.get(i);
            if ( qName.equals( attribute.getQName() ) ) {
                return attribute;
            }
        }
        return null;
    }

    public Attribute attribute(String name, Namespace namespace) {
        return attribute( getDocumentFactory().createQName( name, namespace ) );
    }

    public String attributeValue(String name) {
        Attribute attrib = attribute(name);
        if (attrib == null) {
            return null;
        } 
        else {
            return attrib.getValue();
        }
    }
    
    public String attributeValue(QName qName) {
        Attribute attrib = attribute(qName);
        if (attrib == null) {
            return null;
        } 
        else {
            return attrib.getValue();
        }
    }
    
    public String attributeValue(String name, String defaultValue) {
        String answer = attributeValue(name);
        return (answer != null) ? answer : defaultValue;
    }

    public String attributeValue(QName qName, String defaultValue) {
        String answer = attributeValue(qName);
        return (answer != null) ? answer : defaultValue;
    }
    
    public void setAttributeValue(String name, String value) {
        Attribute attribute = attribute(name);
        if (attribute == null ) {
            add(getDocumentFactory().createAttribute(this, name, value));
        }
        else if (attribute.isReadOnly()) {
            remove(attribute);
            add(getDocumentFactory().createAttribute(this, name, value));
        }
        else {
            attribute.setValue(value);
        }
    }

    public void setAttributeValue(QName qName, String value) {
        Attribute attribute = attribute(qName);
        if (attribute == null ) {
            add(getDocumentFactory().createAttribute(this, qName, value));
        }
        else if (attribute.isReadOnly()) {
            remove(attribute);
            add(getDocumentFactory().createAttribute(this, qName, value));
        }
        else {
            attribute.setValue(value);
        }
    }
    
    public void add(Attribute attribute) {
        if (attribute.getParent() != null) {
            String message = 
                "The Attribute already has an existing parent \"" 
                + attribute.getParent().getQualifiedName() + "\"";
            
            throw new IllegalAddException( this, attribute, message );
        }        
        attributeList().add(attribute);
        childAdded(attribute);
    }
    

    public boolean remove(Attribute attribute) {
        List list = attributeList();
        boolean answer = list.remove(attribute);
        if ( answer ) {
            childRemoved(attribute);
        }
        else {
            // we may have a copy of the attribute
            Attribute copy = attribute( attribute.getQName() );
            if ( copy != null ) {
                list.remove( copy );
                answer = true;
            }
        }

        return answer;
    }
    

    
    // Processing instruction API
    //-------------------------------------------------------------------------    
    
    public List processingInstructions() {
        List list = contentList();
        BackedList answer = createResultList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof ProcessingInstruction ) {
                answer.addLocal( object );
            }
        }
        return answer;
    }
    
    public List processingInstructions(String target) {
        List list = contentList();
        BackedList answer = createResultList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof ProcessingInstruction ) {
                ProcessingInstruction pi = (ProcessingInstruction) object;
                if ( target.equals( pi.getName() ) ) {                  
                    answer.addLocal( pi );
                }
            }
        }
        return answer;
    }
    
    public ProcessingInstruction processingInstruction(String target) {
        List list = contentList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof ProcessingInstruction ) {
                ProcessingInstruction pi = (ProcessingInstruction) object;
                if ( target.equals( pi.getName() ) ) {                  
                    return pi;
                }
            }
        }
        return null;
    }
    
    public boolean removeProcessingInstruction(String target) {
        List list = contentList();
        for ( Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if ( object instanceof ProcessingInstruction ) {
                ProcessingInstruction pi = (ProcessingInstruction) object;
                if ( target.equals( pi.getName() ) ) {                  
                    iter.remove();
                    return true;
                }
            }
        }
        return false;
    }

    
    
    
    // Content Model methods
    //-------------------------------------------------------------------------    
    
    public Node getXPathResult(int index) {
        Node answer = node(index);
        if (answer != null && !answer.supportsParent()) {
            return answer.asXPathResult(this);
        }
        return answer;
    }
       
    
    public void addCDATA(String cdata) {
        CDATA node = getDocumentFactory().createCDATA(cdata);
        add(node);
    }
    
    public Element addElement(String name) {
        DocumentFactory factory = getDocumentFactory();
        
        // should we inherit the parents namespace?
/*        
        
        String prefix = getPrefix();
        Element node = null;
        if ( prefix == null || prefix.length() <= 0 ) {
            QName qname = factory.createQName( name, getNamespace() );
            node = factory.createElement( qname );
        }
        else {
            node = factory.createElement( name );
        }
*/        
        Element node = factory.createElement( name );
        add( node );
        return node;
    }
    
    public Entity addEntity(String name) {
        Entity node = getDocumentFactory().createEntity(name);
        add(node);
        return node;
    }
    
    public Entity addEntity(String name, String text) {
        Entity node = getDocumentFactory().createEntity(name, text);
        add(node);
        return node;
    }
    
    public Namespace addNamespace(String prefix, String uri) {
        Namespace node = getDocumentFactory().createNamespace(prefix, uri);
        add(node);
        return node;
    }

    public void addText(String text) {
        Text node = getDocumentFactory().createText(text);
        add(node);
    }
    

    // polymorphic node methods    
    public void add(Node node) {
        switch ( node.getNodeType() ) {
            case ELEMENT_NODE:
                add((Element) node);
                break;
            case ATTRIBUTE_NODE:
                add((Attribute) node);
                break;
            case TEXT_NODE:
                add((Text) node);
                break;
            case CDATA_SECTION_NODE:
                add((CDATA) node);
                break;
            case ENTITY_REFERENCE_NODE:
                add((Entity) node);
                break;
            case PROCESSING_INSTRUCTION_NODE:
                add((ProcessingInstruction) node);
                break;
            case COMMENT_NODE:
                add((Comment) node);
                break;
/*  XXXX: to do!              
            case DOCUMENT_TYPE_NODE:
                add((DocumentType) node);
                break;
*/
            case NAMESPACE_NODE:
                add((Namespace) node);
                break;
            default:
                invalidNodeTypeAddException(node);
        }
    }
    
    public boolean remove(Node node) {
        switch ( node.getNodeType() ) {
            case ELEMENT_NODE:
                return remove((Element) node);
            case ATTRIBUTE_NODE:
                return remove((Attribute) node);
            case TEXT_NODE:
                return remove((Text) node);
            case CDATA_SECTION_NODE:
                return remove((CDATA) node);
            case ENTITY_REFERENCE_NODE:
                return remove((Entity) node);
            case PROCESSING_INSTRUCTION_NODE:
                return remove((ProcessingInstruction) node);
            case COMMENT_NODE:
                return remove((Comment) node);
/*                
            case DOCUMENT_TYPE_NODE:
                return remove((DocumentType) node);
*/
            case NAMESPACE_NODE:
                return remove((Namespace) node);
            default:
                return false;
        }
    }
    
    // typesafe versions using node classes
    public void add(CDATA cdata) {
        addNode(cdata);
    }
    
    public void add(Comment comment) {
        addNode(comment);
    }
    
    public void add(Element element) {
        addNode(element);
    }
    
    public void add(Entity entity) {
        addNode(entity);
    }
    
    public void add(Namespace namespace) {
        addNode(namespace);
    }
    
    public void add(ProcessingInstruction pi) {
        addNode(pi);
    }
    
    public void add(Text text) {
        addNode(text);
    }
    

    public boolean remove(CDATA cdata) {
        return removeNode(cdata);
    }
    
    public boolean remove(Comment comment) {
        return removeNode(comment);
    }
    
    public boolean remove(Element element) {
        return removeNode(element);
    }
    
    public boolean remove(Entity entity) {
        return removeNode(entity);
    }
    
    public boolean remove(Namespace namespace) {
        return removeNode(namespace);
    }
    
    public boolean remove(ProcessingInstruction pi) {
        return removeNode(pi);
    }
    
    public boolean remove(Text text) {
        return removeNode(text);
    }
    
    
    
    // Helper methods
    //-------------------------------------------------------------------------    
    
    public boolean hasMixedContent() {
        List content = contentList();
        if (content == null || content.isEmpty() || content.size() < 2) {
            return false;
        }

        Class prevClass = null;
        for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            Class newClass = object.getClass();
            if (newClass != prevClass) {
               if (prevClass != null) {
                  return true;
               }
               prevClass = newClass;
            }
        }
        return false;
    }
    
    public boolean isTextOnly() {
        List content = contentList();
        if (content == null || content.isEmpty()) {
            return true;
        }
        for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if ( ! ( object instanceof CharacterData) && ! ( object instanceof String ) ) {
                return false;
            }
        }
        return true;
    }
    
    public void setText(String text) {
        clearContent();
        addText(text);
    }

    /**
     * Puts all <code>Text</code> nodes in the full depth of the sub-tree 
     * underneath this <code>Node</code>, including attribute nodes, into a 
     * "normal" form where only structure (e.g., elements, comments, 
     * processing instructions, CDATA sections, and entity references) 
     * separates <code>Text</code> nodes, i.e., there are neither adjacent 
     * <code>Text</code> nodes nor empty <code>Text</code> nodes. This can 
     * be used to ensure that the DOM view of a document is the same as if 
     * it were saved and re-loaded, and is useful when operations (such as 
     * XPointer  lookups) that depend on a particular document tree 
     * structure are to be used.In cases where the document contains 
     * <code>CDATASections</code>, the normalize operation alone may not be 
     * sufficient, since XPointers do not differentiate between 
     * <code>Text</code> nodes and <code>CDATASection</code> nodes.
     * @version DOM Level 2
     */
    public void normalize() {
        List content = contentList();
        Text previousText = null;
        int i = 0;
        while ( i < content.size() ) {
            Node node = (Node) content.get(i);
            if ( node instanceof Text ) {
                Text text = (Text) node;
                if ( previousText != null ) {
                    previousText.appendText( text.getText() );
                    remove(text);
                }
                else {
                    String value = text.getText();
                    // only remove empty Text nodes, not whitespace nodes
                    //if ( value == null || value.trim().length() <= 0 ) {
                    if ( value == null || value.length() <= 0 ) {
                        remove(text);
                    }
                    else {
                        previousText = text;
                    }
                }
            }
            else {
                if ( node instanceof Element ) {
                    Element element = (Element) node;
                    element.normalize();
                }
                previousText = null;
                i++;
            }
        }
    }
    
    public String elementText(String name) {
        Element element = element(name);
        return (element != null) ? element.getText() : null;
    }
        
    public String elementText(QName qName) {
        Element element = element(qName);
        return (element != null) ? element.getText() : null;
    }
        
    
    public String elementTextTrim(String name) {
        Element element = element(name);
        return (element != null) ? element.getTextTrim() : null;
    }
    
    public String elementTextTrim(QName qName) {
        Element element = element(qName);
        return (element != null) ? element.getTextTrim() : null;
    }
        

    // add to me content from another element
    // analagous to the addAll(collection) methods in Java 2 collections
    public void appendAttributes(Element element) {
        for ( int i = 0, size = element.attributeCount(); i < size; i++ ) {
            Attribute attribute = element.attribute(i);
            if ( attribute.supportsParent() ) {
                setAttributeValue(attribute.getQName(), attribute.getValue());
            }
            else {
                add(attribute);
            }
        }
    }
        
    
    
    /** <p>This returns a deep clone of this element.
      * The new element is detached from its parent, and getParent() on the 
      * clone will return null.</p>
      *
      * @return the clone of this element
      */
    public Object clone() {
        Element clone = createElement(getQName());
        clone.appendAttributes(this);
        clone.appendContent(this);
        return clone;
    }

    public Element createCopy() {
        Element clone = createElement(getQName());
        clone.appendAttributes(this);
        clone.appendContent(this);
        return clone;
    }
    
    public Element createCopy(String name) {
        Element clone = createElement(name);
        clone.appendAttributes(this);
        clone.appendContent(this);
        return clone;
    }
    
    public Element createCopy(QName qName) {
        Element clone = createElement(qName);
        clone.appendAttributes(this);
        clone.appendContent(this);
        return clone;
    }

    
    
    public Namespace getNamespaceForPrefix(String prefix) {
        if ( prefix == null || prefix.length() <= 0 ) {
            return Namespace.NO_NAMESPACE;
        }
        else if ( prefix.equals( getNamespacePrefix() ) ) {
            return getNamespace();
        }
        else if ( prefix.equals( "xml" ) ) {
            return Namespace.XML_NAMESPACE;
        }
        else {
            List list = contentList();
            int size = list.size();
            for ( int i = 0; i < size; i++ ) {
                Object object = list.get(i);
                if ( object instanceof Namespace ) {
                    Namespace namespace = (Namespace) object;
                    if ( prefix.equals( namespace.getPrefix() ) ) {
                        return namespace;
                    }
                }
            }
        }
        return null;
    }
   
    public Namespace getNamespaceForURI(String uri) {
        if ( uri == null || uri.length() <= 0 ) {
            return Namespace.NO_NAMESPACE;
        }
        else if ( uri.equals( getNamespaceURI() ) ) {
            return getNamespace();
        }
        else {
            List list = contentList();
            int size = list.size();
            for ( int i = 0; i < size; i++ ) {
                Object object = list.get(i);
                if ( object instanceof Namespace ) {
                    Namespace namespace = (Namespace) object;
                    if ( uri.equals( namespace.getURI() ) ) {
                        return namespace;
                    }
                }
            }
            return null;
        }
    }
    
    public List declaredNamespaces() {
        BackedList answer = createResultList();
        if ( getNamespaceURI().length() > 0 ) {
            answer.addLocal( getNamespace() );
        }
        List list = contentList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof Namespace ) {
                answer.addLocal( object );
            }
        }
        return answer;
    }
    
    public List additionalNamespaces() {
        List list = contentList();
        int size = list.size();
        BackedList answer = createResultList();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof Namespace ) {
                Namespace namespace = (Namespace) object;
                answer.addLocal( namespace );
            }
        }
        return answer;
    }
    
    public List additionalNamespaces(String defaultNamespaceURI) {
        List list = contentList();
        BackedList answer = createResultList();
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = list.get(i);
            if ( object instanceof Namespace ) {
                Namespace namespace = (Namespace) object;
                if ( ! defaultNamespaceURI.equals( namespace.getURI() ) ) {
                    answer.addLocal( namespace );
                }
            }
        }
        return answer;
    }
    
    
    // Implementation methods
    //-------------------------------------------------------------------------    

    
    protected Element createElement(String name) {
        return getDocumentFactory().createElement(name);
    }
    
    protected Element createElement(QName qName) {
        return getDocumentFactory().createElement(qName);
    }
    
    
    protected void addNode(Node node) {
        if (node.getParent() != null) {
            // XXX: could clone here
            String message = "The Node already has an existing parent of \"" 
                + node.getParent().getQualifiedName() + "\"";
            throw new IllegalAddException(this, node, message);
        }
        
        contentList().add( node );

        childAdded(node);
    }

    protected boolean removeNode(Node node) {
        boolean answer = contentList().remove(node);
        if (answer) {
            childRemoved(node);
        }
        return answer;
    }

    /** Called when a new child node is added to
      * create any parent relationships
      */
    protected void childAdded(Node node) {
        if (node != null ) {
            node.setParent(this);
        }
    }
    
    protected void childRemoved(Node node) {
        if ( node != null ) {
            node.setParent(null);
            node.setDocument(null);
        }
    }

    /** @return the internal List used to store attributes
      */
    protected abstract List attributeList();
    
    protected DocumentFactory getDocumentFactory() {
        QName qName = getQName();
        // QName might be null as we might not have been constructed yet
        if ( qName != null ) {            
            DocumentFactory factory = qName.getDocumentFactory();
            if ( factory != null ) {
                return factory;
            }
        }
        return DOCUMENT_FACTORY;
    }
    
    /** A Factory Method pattern which creates 
      * a List implementation used to store content
      */
    protected List createContentList() {
        return new ArrayList( DEFAULT_CONTENT_LIST_SIZE );
    }
    
    /** A Factory Method pattern which creates 
      * a List implementation used to store attributes
      */
    protected List createAttributeList() {
        return new ArrayList( DEFAULT_CONTENT_LIST_SIZE );
    }
    
    /** A Factory Method pattern which creates 
      * a List implementation used to store attributes
      */
    protected List createAttributeList( int size ) {
        return new ArrayList( size );
    }
    
    /** A Factory Method pattern which creates 
      * a BackedList implementation used to store results of 
      * a filtered content query such as 
      * {@link #processingInstructions} or
      * {@link #elements} which changes are reflected in the content
      */
    protected BackedList createResultList() {
        return new BackedList( this, contentList() );
    }
    
    /** A Factory Method pattern which creates 
      * a BackedList implementation which contains a single result
      */
    protected List createSingleResultList( Object result ) {
        BackedList list = new BackedList( this, contentList(), 1 );
        list.addLocal( result );
        return list;
    }
    
    /** A Factory Method pattern which creates an empty
      * a BackedList implementation
      */
    protected List createEmptyList() {
        return new BackedList( this, contentList(), 0 );
    }
    
    
    protected Iterator createSingleIterator( Object result ) {
        return new SingleIterator( result );
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
