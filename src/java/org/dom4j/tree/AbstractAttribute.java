package org.dom4j.tree;

import org.dom4j.Attribute;
import org.dom4j.Namespace;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractNamespace</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractAttribute extends AbstractNode implements Attribute {

    public String getQualifiedName() {
        StringBuffer buffer = new StringBuffer();

        // Add prefix, if needed
        Namespace namespace = getNamespace();
        if (namespace != null) {
            String prefix = namespace.getPrefix();
            if (prefix != null && prefix.length() > 0) {
                buffer.append(prefix);
                buffer.append(":");
            }
        }
        buffer.append(getName());
        return buffer.toString();
    }

    public String getNamespacePrefix() {
        return getNamespace().getPrefix();
    }

    public String getNamespaceURI() {
        return getNamespace().getURI();
    }

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
}
