/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


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
            return (obj != null ) ? obj.toString() : "";
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




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
