
package org.dom4j.xpath.impl;

import org.dom4j.xpath.ContextSupport;

import org.dom4j.xpath.function.BooleanFunction;

import org.dom4j.Element;
import org.dom4j.Attribute;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class UnionExpr extends Expr {
    
    private Expr  _lhs  = null;
    private Expr  _rhs  = null;
    
    public UnionExpr(Expr lhs, Expr rhs) {
        _lhs = lhs;
        _rhs = rhs;
    }
    
    public Object evaluate(Context context) {        
        Object lhsValue = null;
        Object rhsValue = null;
        
        if ( _lhs != null ) {
            lhsValue = _lhs.evaluate( context );
        }
        
        if ( _rhs != null ) {
            rhsValue = _rhs.evaluate( context.duplicate() );
        }
        
        List answer = null;        
        if ( lhsValue instanceof List ) {
            answer = (List) lhsValue;
        }
        else {
            answer = new ArrayList();
            if ( lhsValue != null ) {
                answer.add( lhsValue );
            }
        }
        
        if ( rhsValue instanceof List ) {
            List rhsList = (List) rhsValue;
            answer.addAll( rhsList );
        }
        else {
            if ( rhsValue != null ) {
                answer.add( rhsValue );
            }
        }        
        return answer;
    }
    
    public String toString() {
        return "[UnionExpr: " + _lhs + " | " + _rhs +  " ]";
    }
}
