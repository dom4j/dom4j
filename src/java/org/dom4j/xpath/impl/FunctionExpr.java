
package org.dom4j.xpath.impl;

import org.dom4j.xpath.function.Function;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class FunctionExpr extends Expr {
    
    private String _name = null;
    private List   _args = null;
    
    public FunctionExpr(String name, List args) {
        _name = name;
        _args = args;
    }
    
    public Object evaluate(Context context) {
        Function func = context.getContextSupport().getFunction(_name);
        
        if (func == null) {
            // FIXME: toss an exception
            return null;
        }
        
        List resolvedArgs = resolveArgs( context );
        
        return func.call( context, resolvedArgs );
    }
    
    private List resolveArgs(Context context){
        if ( _args == null || _args.size() == 0 )
        {
            return Collections.EMPTY_LIST;
        }
        
        List resolved = new ArrayList(_args.size());
        
        Iterator exprIter = _args.iterator();
        Expr each = null;
        
        while (exprIter.hasNext())
        {
            each = (Expr) exprIter.next();
            
            resolved.add( each.evaluate( context ) );
        }
        
        return resolved;
    }
    
    public String toString() {
        String argsText = (_args != null) ? _args.toString() : "()";
        return "[FunctionExpr: " + _name + argsText + " ]";
    }
}
