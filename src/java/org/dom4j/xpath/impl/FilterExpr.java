
package org.dom4j.xpath.impl;

import java.util.List;
import java.util.ArrayList;

import org.dom4j.Node;

public class FilterExpr extends PathExpr {
    
    private Expr _expr       = null;
    private List _predicates = null;
    
    private LocationPath _path = null;
    
    public FilterExpr(Expr expr) {
        _expr = expr;
    }
    
    public void addPredicate(Predicate pred) {
        if ( _predicates == null ) {
            _predicates = new ArrayList();
        }        
        _predicates.add(pred);
    }
    
    public void setLocationPath(LocationPath path) {
        _path = path;
        _path.setIsAbsolute(false);
    }
    
    public Object evaluate(Context context) {
        Object answer = _expr.evaluate( context );
        if ( _path != null ) {
            if ( answer instanceof List ) {
                context.setNodeSet( (List) answer );
            }
            else {
                context.setNodeSet( (Node) answer );
            }
            answer = _path.evaluate( context );
        }
        return answer;
    }
    
    public String toString() {
        return "[Filter: " + _expr + " predicates: " + _predicates + " locationPath: " + _path + "]";
    }
}
