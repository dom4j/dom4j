
package org.dom4j.xpath.impl;

public class VariableExpr extends Expr {
    
    private String _name = null;
    
    public VariableExpr(String name) {
        _name = name;
    }
    
    public Object evaluate(Context context) {
        return context.getVariableValue(_name);
    }
}
