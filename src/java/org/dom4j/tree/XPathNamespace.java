package org.dom4j.tree;

import org.dom4j.Node;
import org.dom4j.Element;

/** <p><code>XPathNamespace</code> implements a doubly linked node which 
  * supports the parent relationship and is mutable.
  * It is useful when evalutating XPath expressions.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XPathNamespace extends DefaultNamespace {

    /** The parent of this node */
    private Element parent;

    /** @param prefix is the prefix for this namespace
      * @param uri is the URI for this namespace
      */
    public XPathNamespace(String prefix, String uri) {
        super( prefix, uri );
    }

    /** @param parent is the parent element
      * @param prefix is the prefix for this namespace
      * @param uri is the URI for this namespace
      */
    public XPathNamespace(Element parent, String prefix, String uri) {
        super( prefix, uri );
        this.parent = parent;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }
    
    public boolean supportsParent() {
        return true;
    }
}
