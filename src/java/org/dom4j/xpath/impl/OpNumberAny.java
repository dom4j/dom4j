
package org.dom4j.xpath.impl;

class OpNumberAny extends Operator {
    
    static Object evaluate(
        Context context, Op op, Object lhsValue, Object rhsValue
    ) {
        Double lhs = convertToNumber(lhsValue);
        Double rhs = convertToNumber(rhsValue);
        
        if ( (op == Op.EQUAL)
            || (op == Op.LT_EQUAL)
            || (op == Op.GT_EQUAL) ) {
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
        else if ( (op == Op.LT) || (op == Op.LT_EQUAL) ) {
            return ( ( lhs.compareTo(rhs) < 0 )
                ? Boolean.TRUE
                : Boolean.FALSE
            );
        }
        else if ( (op == Op.GT)
            || (op == Op.GT_EQUAL ) ) {
            return ( ( lhs.compareTo(rhs) > 0 )
                ? Boolean.TRUE
                : Boolean.FALSE
            );
        }        
        return null;
    }
}
