package org.dom4j.tree;

import java.util.Iterator;

/** <p><code>FilterIterator</code> is an abstract base class which is useful
  * for implementors of {@link Iterator} which filter an existing iterator.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class FilterIterator implements Iterator {
    
    protected Iterator proxy;
    private Object next;
    
    public FilterIterator(Iterator proxy) {
        this.proxy = proxy;
    }


    public boolean hasNext() {
        if ( proxy == null ) {
            return false;
        }
        while (proxy.hasNext()) {
            next = proxy.next();
            if ( matches(next) ) {
                return true;
            }
        }
        proxy = null;
        next = null;
        return false;
    }

    public Object next() {
        return next;
    }

    public void remove() {
        if (proxy != null) {
            proxy.remove();
        }
    }
    
    /** Filter method to perform some matching on the given element.
      * 
      * @return true if the given element matches the filter
      * and should be appear in the iteration
      */
    protected abstract boolean matches(Object element);
}
