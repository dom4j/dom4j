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
import java.io.PrintWriter;
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

/** <p><code>AbstractElement</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractElement extends AbstractBranch implements Element {

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
        
    public String asXML() {
        try {
            StringWriter out = new StringWriter();
            writer.write(this, out);
            return out.toString();
        } 
        catch (IOException e) {
            throw new RuntimeException("Wierd IOException while generating textual representation: " + e.getMessage());
        }
    }

    public void write(PrintWriter out) {
        try {
            writer.write(this, out);
        }
        catch (IOException e) {
            throw new RuntimeException("Wierd IOException while generating textual representation: " + e.getMessage());
        }
    }
        
    /** <p><code>accept</code> method is the <code>Visitor Pattern</code> method.
      * </p>
      *
      * @param visitor <code>Visitor</code> is the visitor.
      */
    public void accept(Visitor visitor) {
        visitor.visit(this);
        
        // visit attributes
        List attributes = getAttributeList();
        if (attributes != null) {
            for ( Iterator iter = attributes.iterator(); iter.hasNext(); ) {
                Attribute attribute = (Attribute) iter.next();
                visitor.visit(attribute);
            }            
        }
        
        // visit content
        List content = getContentList();
        if (content != null) {
            for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
                Object object = iter.next();
                if (object instanceof String) {
                    DefaultText text = new DefaultText((String) object);
                    visitor.visit(text);
                } 
                else {
                    Node node = (Node) object;
                    node.accept(visitor);
                }
            }            
        }
    }
    
    public String toString() {
        return super.toString() + " [Element: <" + getQualifiedName() 
            + " attributes: " + getAttributeList()
            + " content: " + getContentList() + " />]";
    }
    

    // QName methods
    
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
    
    
    
    
    // Attribute methods
        
    public Iterator attributeIterator() {
        return getAttributeList().iterator();
    }
    
    public Attribute getAttribute(int index) {
        return (Attribute) getAttributeList().get(index);
    }
            
    public int getAttributeCount() {
        return getAttributeList().size();
    }
    
    public String getAttributeValue(String name) {
        Attribute attrib = getAttribute(name);
        if (attrib == null) {
            return null;
        } 
        else {
            return attrib.getValue();
        }
    }
    
    public String getAttributeValue(QName qName) {
        Attribute attrib = getAttribute(qName);
        if (attrib == null) {
            return null;
        } 
        else {
            return attrib.getValue();
        }
    }
    
    public String getAttributeValue(String name, String defaultValue) {
        String answer = getAttributeValue(name);
        return (answer != null) ? answer : defaultValue;
    }

    public String getAttributeValue(QName qName, String defaultValue) {
        String answer = getAttributeValue(qName);
        return (answer != null) ? answer : defaultValue;
    }
    
    public void setAttributeValue(String name, String value) {
        Attribute attribute = getAttribute(name);
        if (attribute == null ) {
            add(getDocumentFactory().createAttribute(name, value));
        }
        else if (attribute.isReadOnly()) {
            remove(attribute);
            add(getDocumentFactory().createAttribute(name, value));
        }
        else {
            attribute.setValue(value);
        }
    }

    public void setAttributeValue(QName qName, String value) {
        Attribute attribute = getAttribute(qName);
        if (attribute == null ) {
            add(getDocumentFactory().createAttribute(qName, value));
        }
        else if (attribute.isReadOnly()) {
            remove(attribute);
            add(getDocumentFactory().createAttribute(qName, value));
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
        getAttributeList().add(attribute);
        childAdded(attribute);
    }
    

    
    // Content Model methods
    
    public Node getXPathNode(int index) {
        Node answer = getNode(index);
        if (answer != null && !answer.supportsParent()) {
            return answer.asXPathNode(this);
        }
        return answer;
    }
       
    
    public void addCDATA(String cdata) {
        CDATA node = getDocumentFactory().createCDATA(cdata);
        add(node);
    }
    
    public void addText(String text) {
        Text node = getDocumentFactory().createText(text);
        add(node);
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
    
    public void setText(String text) {
        clearContent();
        addText(text);
    }

    public String getElementText(String name) {
        Element element = getElement(name);
        return (element != null) ? element.getText() : null;
    }
        
    public String getElementText(QName qName) {
        Element element = getElement(qName);
        return (element != null) ? element.getText() : null;
    }
        
    
    public String getElementTextTrim(String name) {
        Element element = getElement(name);
        return (element != null) ? element.getTextTrim() : null;
    }
    
    public String getElementTextTrim(QName qName) {
        Element element = getElement(qName);
        return (element != null) ? element.getTextTrim() : null;
    }
        

    // add to me content from another element
    // analagous to the addAll(collection) methods in Java 2 collections
    public void appendAttributes(Element element) {
        for (Iterator i = attributeIterator(); i.hasNext(); ) {
            Attribute attribute = (Attribute) i.next();
            if ( attribute.supportsParent() ) {
                setAttributeValue(attribute.getQName(), attribute.getValue());
            }
            else {
                add(attribute);
            }
        }
    }
        
    public void appendContent(Element element) {
        for (Iterator iter = element.nodeIterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if (object instanceof String) {
                addText((String) object);
            } 
            else if (object instanceof Node) {
                Node node = (Node) object;
                add( (Node) node.clone() );
            }
        }
    }
        
    public void appendAddtionalNamespaces(Element element) {
        for (Iterator i = element.getAdditionalNamespaces().iterator(); i.hasNext(); ) {
            Namespace namespace = (Namespace) i.next();
            add( namespace );
        }
    }


    
    // creates a copy
    
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
        clone.appendAddtionalNamespaces(this);
        return clone;
    }

    public Element createCopy() {
        Element clone = createElement(getQName());
        clone.appendAttributes(this);
        clone.appendContent(this);
        clone.appendAddtionalNamespaces(this);
        return clone;
    }
    
    public Element createCopy(String name) {
        Element clone = createElement(name);
        clone.appendAttributes(this);
        clone.appendContent(this);
        clone.appendAddtionalNamespaces(this);
        return clone;
    }
    
    public Element createCopy(QName qName) {
        Element clone = createElement(qName);
        clone.appendAttributes(this);
        clone.appendContent(this);
        clone.appendAddtionalNamespaces(this);
        return clone;
    }
    
    protected Element createElement(String name) {
        return getDocumentFactory().createElement(name);
    }
    
    protected Element createElement(QName qName) {
        return getDocumentFactory().createElement(qName);
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
    protected abstract List getAttributeList();
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
