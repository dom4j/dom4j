
package org.dom4j.xpath.impl;

class OpStringAny extends Operator {
    
    static Object evaluate(
        Context context, Op op, Object lhsValue, Object rhsValue
    ) {
        String lhs = convertToString(lhsValue);
        String rhs = convertToString(rhsValue);        
        if ( op == Op.EQUAL ) {
            return ( ( lhs.equals(rhs) )
                ? Boolean.TRUE
                : Boolean.FALSE
            );
        }
        else if ( op == Op.NOT_EQUAL ) {
            return ( ( ! lhs.equals(rhs) )
                ? Boolean.TRUE
                : Boolean.FALSE
            );
        }
        return null;
    }
}
