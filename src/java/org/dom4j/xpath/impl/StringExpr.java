
package org.dom4j.xpath.impl;

public class StringExpr extends Expr {
    
    private String _text = null;
    
    public StringExpr(String text) {
        _text = text;
    }
    
    public String toString() {
        return ("[(StringExpr) " + _text + "]");
    }
    
    public Object evaluate(Context context) {
        return _text;
    }
    
}
