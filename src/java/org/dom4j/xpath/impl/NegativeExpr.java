
package org.dom4j.xpath.impl;

import java.util.List;

public class NegativeExpr extends Expr {
    
    private Expr _expr = null;
    
    public NegativeExpr(Expr expr) {
        _expr = expr;
    }
    
    public Object evaluate(Context context) {
        return null;
    }
}
