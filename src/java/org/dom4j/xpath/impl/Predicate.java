
package org.dom4j.xpath.impl;

import org.dom4j.Node;
import org.dom4j.xpath.ContextSupport;
import org.dom4j.xpath.function.BooleanFunction;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class Predicate {
    
    private Expr _expr = null;
    
    public Predicate(Expr expr) {
        _expr = expr;
    }
    
    public List evaluateOn(List nodeSet, ContextSupport support, String axis) {
        Context context = new Context( nodeSet, support );
        int max = context.getSize();
        List results = new ArrayList();        
        
        for ( int position = 1; position <= max; position++ ) {
            Context duplicateContext = context.duplicate();
            duplicateContext.setPosition( position );
            
            if ( evaluateOnNode( duplicateContext, axis ) ) {
                results.add( context.getNode( position ) );
            }
        }        
        return results;
    }
    
    public boolean evaluateOnNode(Context context, String axis) {
        Object exprResult = _expr.evaluate( context );
        
        //System.err.println("pred-expr == " + _expr);
        
        if ( exprResult instanceof Number ) {
            Number n = (Number) exprResult;
            int i = n.intValue();
            return ( i == context.getPosition() );
        } 
        else  {
            return BooleanFunction.evaluate(exprResult).booleanValue();
        }
    }    

    public boolean matches(Context context, Node node) {
        Object exprResult = _expr.evaluate( context );
        if ( exprResult instanceof Number ) {
            Number n = (Number) exprResult;
            int i = n.intValue();
            return ( i == context.getPosition() );
        } 
        else  {
            return BooleanFunction.evaluate(exprResult).booleanValue();
        }
    }    
    
    
    public String toString()
    {
        return "[Prediate: " + _expr + "]";
    }
}
