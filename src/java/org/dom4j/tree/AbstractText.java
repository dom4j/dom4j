package org.dom4j.tree;

import org.dom4j.Text;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractText</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractText extends AbstractCharacterData implements Text {

    public AbstractText() {
    }
    
    public String toString() {
        return super.toString() + " [Text: \"" + getText() + "\"]";
    }

    public String asXML() {
        return getText();
    }
    
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
    }
}
