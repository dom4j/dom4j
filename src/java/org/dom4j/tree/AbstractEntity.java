package org.dom4j.tree;

import org.dom4j.Entity;
import org.dom4j.TreeVisitor;


/** <p><code>AbstractEntity</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractEntity extends AbstractNode implements Entity {

    public AbstractEntity() {
    }
    
    public String toString() {
        return super.toString() + " [Entity: &" + getName() + ";]";
    }

    public String asXML() {
        return "&" + getName() + ";";
    }
    
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
    }
    
}
