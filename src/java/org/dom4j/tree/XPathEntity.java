package org.dom4j.tree;

import org.dom4j.Node;
import org.dom4j.Element;

/** <p><code>XPathEntity</code> implements a doubly linked node which 
  * supports the parent relationship and is mutable.
  * It is useful when evalutating XPath expressions.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XPathEntity extends DefaultEntity {

    /** The parent of this node */
    private Element parent;

    /** Creates the <code>Entity</code> with the specified name
      *
      * @param name is the name of the entity
      */
    public XPathEntity(String name) {
        super( name );
    }

    /** Creates the <code>Entity</code> with the specified name
      * and text.
      *
      * @param name is the name of the entity
      * @param text is the text of the entity
      */
    public XPathEntity(String name, String text) {
        super( name, text );
    }
    
    
    /** Creates the <code>Entity</code> with the specified name
      * and text.
      *
      * @param parent is the parent element
      * @param name is the name of the entity
      * @param text is the text of the entity
      */
    public XPathEntity(Element parent, String name, String text) {
        super( name, text );
        this.parent = parent;
    }

    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setText(String text) {
        this.text = text;
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
