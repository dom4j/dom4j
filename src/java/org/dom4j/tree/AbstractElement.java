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

    public String getNamespacePrefix() {
        Namespace namespace = getNamespace();
        return (namespace != null) ? namespace.getPrefix() : "";
    }

    public String getNamespaceURI() {
        Namespace namespace = getNamespace();
        return (namespace != null) ? namespace.getPrefix() : "";
    }

    public String getQualifiedName() {
        Namespace namespace = getNamespace();
        if (namespace != null ) {
            String prefix = namespace.getPrefix();
            if (prefix != null && prefix.length() > 0) {
                return prefix + ":" + getName();
            }
        }
        return getName();
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
            Namespace answer = getContentModel().getNamespaceForPrefix(prefix);
            if ( answer == null ) {
                Element parent = getParent();
                if ( parent != null ) {
                    answer = parent.getNamespaceForPrefix(prefix);
                }
            }
            return answer;
        }
    }

    public Namespace getNamespaceForURI(String uri) {
        if ( uri == null || uri.length() <= 0 ) {
            return Namespace.NO_NAMESPACE;
        }
        else if ( uri.equals( getNamespaceURI() ) ) {
            return getNamespace();
        }
        else {
            Namespace answer = getContentModel().getNamespaceForURI(uri);
            if ( answer == null ) {
                Element parent = getParent();
                if ( parent != null ) {
                    answer = parent.getNamespaceForURI(uri);
                }
            }
            return answer;
        }
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
    
    
    
    // AttributeModel methods
        
    public List getAttributes() {
        return getAttributeModel().getAttributes();
    }
    
    public void setAttributes(List attributes) {
        getAttributeModel().setAttributes(attributes);
    }

    public Attribute getAttribute(String name) {
        return getAttributeModel().getAttribute(name);
    }
    
    public Attribute getAttribute(String name, Namespace namespace) {
        return getAttributeModel().getAttribute(name, namespace);
    }

    public String getAttributeValue(String name) {
        return getAttributeModel().getAttributeValue(name);
    }

    public String getAttributeValue(String name, Namespace namespace) {
        return getAttributeModel().getAttributeValue(name, namespace);
    }

    public void setAttributeValue(String name, String value) {
        getAttributeModel().setAttributeValue(getContentFactory(), name, value);
    }
    
    public void setAttributeValue(String name, String value, Namespace namespace) {
        getAttributeModel().setAttributeValue(getContentFactory(), name, value, namespace);
    }

    public boolean removeAttribute(String name) {
        return getAttributeModel().removeAttribute(name);
    }
    
    public boolean removeAttribute(String name, Namespace namespace) {
        return getAttributeModel().removeAttribute(name, namespace);
    }
    
    public void add(Attribute attribute) {
        if (attribute.getParent() != null) {
            String message = 
                "The Attribute already has an existing parent \"" 
                + attribute.getParent().getQualifiedName() + "\"";
            
            throw new IllegalAddNodeException( this, attribute, message );
        }        
        getAttributeModel().add(attribute);
        childAdded(attribute);
    }
    
    public boolean remove(Attribute attribute) {
        return getAttributeModel().remove(attribute);
    }


    
    // Content Model methods
    
    public List getAdditionalNamespaces() {
        return getContentModel().getAdditionalNamespaces( getNamespaceURI() );
    }

    public boolean hasMixedContent() {
        return getContentModel().hasMixedContent();
    }

    public Element getElementByID(String elementID) {
        return getContentModel().getElementByID(elementID);
    }
    
    public Element getElement(String name) {
        return getContentModel().getElement(name);
    }
    
    public Element getElement(String name, Namespace namespace) {
        return getContentModel().getElement(name, namespace);
    }
    
    public List getElements() {
        return getContentModel().getElements();
    }
    
    public List getElements(String name) {
        return getContentModel().getElements(name);
    }
    
    public List getElements(String name, Namespace namespace) {
        return getContentModel().getElements(name, namespace);
    }


    
    
    public CDATA addCDATA(String cdata) {
        CDATA node = getContentModel().addCDATA(getContentFactory(), cdata);
        childAdded(node);
        return node;
    }
    
    public Text addText(String text) {
        Text node = getContentModel().addText(getContentFactory(), text);
        childAdded(node);
        return node;
    }
    
    public Entity addEntity(String name) {
        Entity node = getContentModel().addEntity(getContentFactory(), name);
        childAdded(node);
        return node;
    }
    
    public Entity addEntity(String name, String text) {
        Entity node = getContentModel().addEntity(getContentFactory(), name, text);
        childAdded(node);
        return node;
    }
    
    public Namespace addAdditionalNamespace(String prefix, String uri) {
        Namespace node = getContentModel().addAdditionalNamespace(getContentFactory(), prefix, uri);
        childAdded(node);
        return node;
    }

    public void setText(String text) {
        getContentModel().setText(getContentFactory(), text);
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
    public void addAttributes(Element element) {
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
        
    public void addContent(Element element) {
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
        
    public void addAddtionalNamespaces(Element element) {
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
        clone.addAttributes(this);
        clone.addContent(this);
        clone.addAddtionalNamespaces(this);
        return clone;
    }

    public Element createCopy() {
        Element clone = createElement(getName(), getNamespace());
        clone.addAttributes(this);
        clone.addContent(this);
        clone.addAddtionalNamespaces(this);
        return clone;
    }
    
    public Element createCopy(String name) {
        Element clone = createElement(name);
        clone.addAttributes(this);
        clone.addContent(this);
        clone.addAddtionalNamespaces(this);
        return clone;
    }
    
    public Element createCopy(String name, Namespace namespace) {
        Element clone = createElement(name, namespace);
        clone.addAttributes(this);
        clone.addContent(this);
        clone.addAddtionalNamespaces(this);
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
        }
    }
    
    /** Allows derived classes to override the attribute model */
    protected abstract AttributeModel getAttributeModel();

}
