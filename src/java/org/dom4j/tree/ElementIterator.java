package org.dom4j.tree;

import java.util.Iterator;

import org.dom4j.Element;

/** <p><code>ElementIterator</code> is a filtering {@link Iterator} which 
  * filters out objects which do not implement the {@link Element} 
  * interface.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class ElementIterator extends FilterIterator {
    
    public ElementIterator(Iterator proxy) {
        super(proxy);
    }


    /** @return true if the given element implements the {@link Element} 
      * interface
      */
    protected boolean matches(Object element) {
        return element instanceof Element;
    }
}
