package org.dom4j.tree;

import java.util.Iterator;

import org.dom4j.Element;

/** <p><code>SingleIterator</code> is an {@link Iterator} over a single 
  * object instance.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SingleIterator implements Iterator {

    private boolean first = true;
    private Object object;
    
    public SingleIterator(Object object) {
        this.object = object;
    }

    public boolean hasNext() {
        if ( first ) {
            first = false;
            return true;
        }
        else {
            object = null;
            return false;
        }
    }

    public Object next() {
        return object;
    }

    public void remove() {
        throw new UnsupportedOperationException( "remove() is not supported by this iterator" );
    }

}
