package org.dom4j.tree;

import org.dom4j.Element;

/** <p><code>XPathText</code> implements a doubly linked node which 
  * supports the parent relationship and is mutable.
  * It is useful when evalutating XPath expressions.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XPathText extends DefaultText {

    /** The parent of this node */
    private Element parent;

    /** @param text is the Text text
      */
    public XPathText(String text) {
	super(text);
    }

    /** @param parent is the parent element
      * @param text is the Text text
      */
    public XPathText(Element parent, String text) {
	super(text);
        this.parent = parent;
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
