
package org.dom4j.xpath.impl;

import org.dom4j.Node;
import org.dom4j.rule.Pattern;
import org.dom4j.xpath.function.StringFunction;

import java.util.ArrayList;
import java.util.List;

public abstract class Expr 
{
    public abstract Object evaluate(Context context);
  
    public String valueOf(Context context)  {
        Object value = evaluate(context);
        return StringFunction.evaluate(value);
    }
    
    // Pattern methods
    
    public boolean matches( Context context, Node node ) {
        ArrayList nodeSet = new ArrayList(1);
        nodeSet.add( node );        
        context.setNodeSet( nodeSet );
        Object value = evaluate( context );
        if ( value instanceof List ) {
            List list = (List) value;
            return list.contains( node );
        }
        else {
            return node == value;
        }
    }
    
    public double getPriority() {
        return Pattern.DEFAULT_PRIORITY;
    }
    
    public Pattern[] getUnionPatterns() {
        return null;
    }

    /** @return the type of node which this expression matches
     * defined in the {@link Node} interface or if no specific
     * type of node is matched then {@link Pattern#ANY_NODE} is returned.
      */
    public short getMatchType() {
        return Pattern.ANY_NODE;
    }

    /** @return the name of the node that this expression matches
     * or null if there is no particular name of node that this 
     * expression matches. For example the XPath expression "//a"
     * should return "a"
     */
    public String getMatchesNodeName() {
        return null;
    }
}
