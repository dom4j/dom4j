
package org.dom4j.xpath.impl;

import java.util.List;
import java.util.Iterator;

class OpNodeSetAny {
    
    static Object evaluate(
        Context context, Op op, Object lhsValue, Object rhsValue 
    ) {
        Object result = null;
        
        List nodeSet = null;
        Object other = null;
        
        if (lhsValue instanceof List) {
            nodeSet = (List) lhsValue;
            other   = rhsValue;
        }
        else {
            nodeSet = (List) rhsValue;
            other   = lhsValue;
        }
        
        Iterator nodeIter = nodeSet.iterator();
        
        while ( nodeIter.hasNext() ) {
            result = Operator.evaluate( context, op, nodeIter.next(), other );
            
            if (Boolean.TRUE.equals(result)) {
                break;
            }
        }        
        return result;
    }
}
