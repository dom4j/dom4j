package org.dom4j.tree;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractNamespace</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractAttribute extends AbstractNode implements Attribute {


    public void setNamespace(Namespace namespace) {
        throw new UnsupportedOperationException("This Attribute is read only and cannot be changed" );
    }
    
    public String getText() {
        return getValue();
    }

    public void setText(String text) {
        setValue(text);
    }

    public void setValue(String value) {
        throw new UnsupportedOperationException("This Attribute is read only and cannot be changed" );
    }
    
    public String toString() {
        return super.toString() + " [Attribute: name " + getQualifiedName() 
            + " value \"" + getValue() + "\"]";
    }

    public String asXML() {
        return getQualifiedName() + "=\"" + getValue() + "\"";
    }
    
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
    }
    
    // NameModel methods
    
    public Namespace getNamespace() {
        return getNameModel().getNamespace();
    }
    
    public String getName() {
        return getNameModel().getName();
    }
    
    public String getNamespacePrefix() {
        return getNameModel().getNamespacePrefix();
    }

    public String getNamespaceURI() {
        return getNameModel().getNamespaceURI();
    }

    public String getQualifiedName() {
        return getNameModel().getQualifiedName();
    }
    
    protected Node createXPathNode(Element parent) {
        return new XPathAttribute(parent, getNameModel(), getValue());
    }
    
    /** Allows derived classes to override how the attribute is named */
    protected abstract NameModel getNameModel();
}
