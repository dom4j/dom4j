
package org.dom4j.xpath.impl;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;

class Operator {
    
    static Object evaluate(
        Context context, Op op, Object lhsValue, Object rhsValue
    ) {
        Object result = null;
        
        if ( op == Op.OR ) {
            
        }
        else if ( op == Op.AND ) {
            
        }
        else {
            // This cascading-if implments section 3.4 ("Booleans") of
            // the XPath-REC spec, for all operators which do not perform
            // short-circuit evaluation (meaning everything except AND and OR);
            
            if (Operator.bothAreNodeSets(lhsValue, rhsValue)) {
                // result = opNodeSetNodeSet();
                result = OpNodeSetNodeSet.evaluate(
                    context, op, lhsValue, rhsValue
                );
            }
            else if (Operator.eitherIsNodeSet(lhsValue, rhsValue)) {
                result = OpNodeSetAny.evaluate(
                    context, op, lhsValue, rhsValue
                );
            }
            else if (Operator.eitherIsBoolean(lhsValue, rhsValue)) {
                result = OpBooleanAny.evaluate(
                    context, op, lhsValue, rhsValue
                );
            }
            else if (Operator.eitherIsNumber(lhsValue, rhsValue)) {
                result = OpNumberAny.evaluate(
                    context, op, lhsValue, rhsValue
                );
            }
            else if (Operator.eitherIsString(lhsValue, rhsValue)) {
                result = OpStringAny.evaluate(
                    context, op, lhsValue, rhsValue
                );
            }
        }        
        //return Collections.EMPTY_LIST;
        return result;
    }
    
    protected static boolean eitherIsNodeSet(Object lhs, Object rhs) {
        return ( (lhs instanceof List) || (rhs instanceof List) );
    }
    
    protected static boolean bothAreNodeSets(Object lhs, Object rhs) {
        return ( (lhs instanceof List) && (rhs instanceof List) );
    }
    
    protected static boolean eitherIsNumber(Object lhs, Object rhs) {
        return ( (lhs instanceof Number) || (rhs instanceof Number) );
    }
    
    protected static boolean eitherIsString(Object lhs, Object rhs ) {
        return ( (lhs instanceof String) || (rhs instanceof String) );
    }
    
    protected static boolean eitherIsBoolean(Object lhs, Object rhs) {
        return ( (lhs instanceof Boolean) || (rhs instanceof Boolean) );
    }
    
    protected static String convertToString(Object obj) {
        if (obj instanceof Node) {
            Node node = (Node) obj;
            return node.getString();
        }
        else {
            return obj.toString();
        }
    }
    
    protected static Double convertToNumber(Object obj) {
        if (obj instanceof Double) {
            return (Double) obj;
        }
        else {
            return Double.valueOf( convertToString(obj) );
        }
    }
    
    protected static Boolean convertToBoolean(Object obj) {
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        else {
            return Boolean.valueOf( convertToString(obj) );
        }        
    }
    
}
