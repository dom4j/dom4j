package org.dom4j.tree;

import org.dom4j.Namespace;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractNamespace</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractNamespace extends AbstractNode implements Namespace {
    
    public AbstractNamespace() {
    }

    public int hashCode() {
        return getPrefix().hashCode() ^ getURI().hashCode();
    }
    
    /** Implements equality test. Two namespaces are equal if and only if
      * their prefixes and URIs are equal.
      * 
      * @param that is the object to compare this to
      * @return true if this and that are equal namespaces
      */
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that instanceof Namespace) {
            Namespace other = (Namespace) that;
            return getPrefix().equals( other.getPrefix() ) 
                && getURI().equals( other.getURI() );
        }
        return false;
    }

    public String toString() {
        return super.toString() + " [Namespace: prefix " + getPrefix() 
            + " mapped to URI \"" + getURI() + "\"]";
    }

    public String asXML() {
        return "xmlns:" + getPrefix() + "=\"" + getURI() + "\"";
    }
    
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
    }
}
