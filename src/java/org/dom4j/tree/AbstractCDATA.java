package org.dom4j.tree;

import org.dom4j.CDATA;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractCDATA</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractCDATA extends AbstractCharacterData implements CDATA {

    public AbstractCDATA() {
    }
    
    public String toString() {
        return super.toString() + " [CDATA: \"" + getText() + "\"]";
    }

    public String asXML() {
        return "<![CDATA[" + getText() + "]]>";
    }
    
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
    }
}
