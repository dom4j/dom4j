package org.dom4j.tree;

import org.dom4j.Comment;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractComment</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractComment extends AbstractCharacterData implements Comment {

    public AbstractComment() {
    }
    
    public String toString() {
        return super.toString() + " [Comment: \"" + getText() + "\"]";
    }

    public String asXML() {
        return "<!--" + getText() + "-->";
    }
    
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
    }
}
