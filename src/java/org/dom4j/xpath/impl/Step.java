
package org.dom4j.xpath.impl;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;

import org.dom4j.Node;

public abstract class Step extends Expr {
    
    private boolean _isAbsolute = false;
    
    public void setIsAbsolute(boolean isAbsolute) {
        _isAbsolute = isAbsolute;
    }
    
    public boolean isAbsolute() {
        return _isAbsolute;
    }
    
    public Object evaluate(Context context) {
        return applyTo( context );
    }
    
    public Context applyTo(Context context) {
        context.setNodeSet( Collections.EMPTY_LIST );        
        return context;
    }    
    
    /** @return the next context on which a search should be performed
      */
    public Context nextMatchContext( Context context, Node node ) {
        return context;
    }
    
}
