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
import org.dom4j.Comment;
import org.dom4j.ContentFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.IllegalAddNodeException;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractElement</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractElement extends AbstractBranch implements Element {

    public AbstractElement() { 
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
            writer.output(this, out);
            return out.toString();
        } 
        catch (IOException e) {
            throw new RuntimeException("Wierd IOException while generating textual representation: " + e.getMessage());
        }
    }

    public void writeXML(PrintWriter out) {
        try {
            writer.output(this, out);
        }
        catch (IOException e) {
            throw new RuntimeException("Wierd IOException while generating textual representation: " + e.getMessage());
        }
    }
        
    /** <p><code>accept</code> method is the <code>Visitor Pattern</code> method.
      * </p>
      *
      * @param visitor <code>TreeVisitor</code> is the visitor.
      */
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
        
        // visit attributes
        List attributes = getAttributes();
        if (attributes != null) {
            for ( Iterator iter = attributes.iterator(); iter.hasNext(); ) {
                Attribute attribute = (Attribute) iter.next();
                visitor.visit(attribute);
            }            
        }
        
        // visit content
        List content = getContent();
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
        return super.toString() + " [Element: <" + getQualifiedName() + "/>]";
    }
    

    public Node getXPathNode(int index) {
        Node answer = getNode(index);
        if (answer != null && !answer.supportsParent()) {
            return answer.asXPathNode(this);
        }
        return answer;
    }
    
    public int getXPathNodeCount() {
        return getNodeCount();
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

    
    
    
    
    // Attribute methods
        
    public Iterator attributeIterator() {
        return getAttributes().iterator();
    }
    
    public Attribute getAttribute(String name) {
        return getAttribute(name, Namespace.NO_NAMESPACE);
    }
    
    public boolean removeAttribute(String name) {
        return removeAttribute(name, Namespace.NO_NAMESPACE);
    }
    

    public String getAttributeValue(String name) {
        return getAttributeValue(name, Namespace.NO_NAMESPACE);
    }
    
    public String getAttributeValue(String name, Namespace ns) {
        Attribute attrib = getAttribute(name, ns);
        if (attrib == null) {
            return null;
        } 
        else {
            return attrib.getValue();
        }
    }

    public void setAttributeValue(String name, String value) {
        Attribute attribute = getAttribute(name);
        if (attribute == null ) {
            add(getContentFactory().createAttribute(name, value));
        }
        else if (attribute.isReadOnly()) {
            remove(attribute);
            add(getContentFactory().createAttribute(name, value));
        }
        else {
            attribute.setValue(value);
        }
    }

    public void setAttributeValue(String name, String value, Namespace namespace) {
        Attribute attribute = getAttribute(name, namespace);
        if (attribute == null ) {
            add(getContentFactory().createAttribute(name, value, namespace));
        }
        else if (attribute.isReadOnly()) {
            remove(attribute);
            add(getContentFactory().createAttribute(name, value, namespace));
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
            
            throw new IllegalAddNodeException( this, attribute, message );
        }        
        getAttributes().add(attribute);
        childAdded(attribute);
    }
    

    
    // Content Model methods
    
    
    public CDATA addCDATA(String cdata) {
        CDATA node = getContentFactory().createCDATA(cdata);
        add(node);
        return node;
    }
    
    public Text addText(String text) {
        Text node = getContentFactory().createText(text);
        add(node);
        return node;
    }
    
    public Entity addEntity(String name) {
        Entity node = getContentFactory().createEntity(name);
        add(node);
        return node;
    }
    
    public Entity addEntity(String name, String text) {
        Entity node = getContentFactory().createEntity(name, text);
        add(node);
        return node;
    }
    
    public Namespace addAdditionalNamespace(String prefix, String uri) {
        Namespace node = getContentFactory().createNamespace(prefix, uri);
        add(node);
        return node;
    }


    // typesafe versions using node classes
    public void add(CDATA cdata) {
        addNode(cdata);
    }
    
    public void add(Entity entity) {
        addNode(entity);
    }
    
    public void add(Text text) {
        addNode(text);
    }
    
    public void add(Namespace namespace) {
        addNode(namespace);
    }
    
    public boolean remove(CDATA cdata) {
        return removeNode(cdata);
    }
    
    public boolean remove(Entity entity) {
        return removeNode(entity);
    }
    
    public boolean remove(Namespace namespace) {
        return removeNode(namespace);
    }
    
    
    
    // Helper methods
    
    public void setText(String text) {
        clearContent();
        addText(text);
    }

    public Element getElement(String name) {
        return getElement(name, Namespace.NO_NAMESPACE);
    }
    
    public List getElements(String name) {
        return getElements(name, Namespace.NO_NAMESPACE);
    }
    
    public Iterator elementIterator(String name) {
        return elementIterator(name, Namespace.NO_NAMESPACE);
    }
        
    public String getAttributeValue(String name, String defaultValue) {
        String answer = getAttributeValue(name);
        return (answer != null) ? answer : defaultValue;
    }

    public String getAttributeValue(String name, Namespace namespace, String defaultValue) {
        String answer = getAttributeValue(name, namespace);
        return (answer != null) ? answer : defaultValue;
    }
    
    public String getElementText(String name) {
        Element element = getElement(name);
        return (element != null) ? element.getText() : null;
    }
        
    public String getElementText(String name, Namespace namespace) {
        Element element = getElement(name);
        return (element != null) ? element.getText() : null;
    }
    
    
    public String getElementTextTrim(String name) {
        Element element = getElement(name);
        return (element != null) ? element.getTextTrim() : null;
    }
    
    public String getElementTextTrim(String name, Namespace namespace) {
        Element element = getElement(name, namespace);
        return (element != null) ? element.getTextTrim() : null;
    }
    

    // add to me content from another element
    // analagous to the addAll(collection) methods in Java 2 collections
    public void appendAttributes(Element element) {
        for (Iterator i = getAttributes().iterator(); i.hasNext(); ) {
            Attribute attribute = (Attribute) i.next();
            if ( attribute.supportsParent() ) {
                Namespace namespace = attribute.getNamespace();
                if (namespace != null) {
                    setAttributeValue(attribute.getName(), attribute.getValue(), namespace);
                }
                else {
                    setAttributeValue(attribute.getName(), attribute.getValue(), namespace);
                }
            }
            else {
                element.add(attribute);
            }
        }
    }
        
    public void appendContent(Element element) {
        for (Iterator iter = element.getContent().iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if (object instanceof String) {
                element.addText((String) object);
            } else if (object instanceof Text) {
                add((Text)((Text) object).clone());
            } else if (object instanceof Comment) {
                add((Comment)((Comment) object).clone());
            } else if (object instanceof Entity) {
                add((Entity)((Entity) object).clone());
            } else if (object instanceof Element) {
                add((Element)((Element) object).clone());
            } else if (object instanceof CDATA) {
                add((CDATA)((CDATA) object).clone());
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
        Element clone = createElement(getName(), getNamespace());
        clone.appendAttributes(this);
        clone.appendContent(this);
        clone.appendAddtionalNamespaces(this);
        return clone;
    }

    public Element createCopy() {
        Element clone = createElement(getName(), getNamespace());
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
    
    public Element createCopy(String name, Namespace namespace) {
        Element clone = createElement(name, namespace);
        clone.appendAttributes(this);
        clone.appendContent(this);
        clone.appendAddtionalNamespaces(this);
        return clone;
    }

    
    protected Element createElement(String name) {
        return getContentFactory().createElement(name);
    }
    
    protected Element createElement(String name, Namespace namespace) {
        return getContentFactory().createElement(name, namespace);
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
