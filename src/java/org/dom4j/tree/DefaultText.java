package org.dom4j.tree;

import org.dom4j.Text;
import org.dom4j.TreeVisitor;

/** <p><code>DefaultText</code> is the default DOM4J implementation of a 
  * singly linked read only XML Text.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultText extends AbstractText implements Text {

    /** Text of the <code>Text</code> node */
    protected String text;

    /** @param text is the Text text
      */
    public DefaultText(String text) {
	this.text = text;
    }

    public String getText() {
	return text;
    }
    
}
