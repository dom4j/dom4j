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
import org.dom4j.xpath.ContextSupport;
import org.dom4j.xpath.function.BooleanFunction;
import org.dom4j.xpath.impl.Context;


import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class BinaryExpr extends Expr implements org.jaxpath.expr.BinaryExpr {
    
    private Op    _op   = null;
    private Expr  _lhs  = null;
    private Expr  _rhs  = null;
    
    public BinaryExpr(Op op, org.jaxpath.expr.Expr lhs, org.jaxpath.expr.Expr rhs) {
        this( op, (Expr) lhs, (Expr) rhs );
    }
    
    public BinaryExpr(Op op, Expr lhs, Expr rhs) {
        _op  = op;
        _lhs = lhs;
        _rhs = rhs;
    }
    
    public org.jaxpath.expr.Expr getLHS() {
        return _lhs;
    }
    
    public org.jaxpath.expr.Expr getRHS() {
        return _rhs;
    }
    
    public Object evaluate(Context context) {
        
        //System.err.println( "Evaluating: " + _op + " " + _lhs + " " + _rhs );
        
        Context duplicateContext = context.duplicate();
        
        Object result = null;
        
        Object lhsValue = _lhs.evaluate( context );
        
        Object rhsValue = null;
        
        
        // Short-circuit the boolean AND and OR operators
        // returning early if LHS is all that is needed
        // to determine truthfulness.
        
        if ( _op == Op.OR ) {
            
            if (BooleanFunction.evaluate(lhsValue).booleanValue()) {
                return Boolean.TRUE;
            }
            
            rhsValue = _rhs.evaluate( duplicateContext );
            
            if (BooleanFunction.evaluate(rhsValue).booleanValue()) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        else if ( _op == Op.AND ) {
            
            if ( ! BooleanFunction.evaluate(lhsValue).booleanValue()) {
                return Boolean.FALSE;
            }
            
            rhsValue = _rhs.evaluate( duplicateContext );
            
            if ( ! BooleanFunction.evaluate(rhsValue).booleanValue()) {
                return Boolean.FALSE;
            }
            return Boolean.TRUE;
        }
        else {
            rhsValue = _rhs.evaluate( duplicateContext );
            
            result = Operator.evaluate(
                duplicateContext, _op, lhsValue, rhsValue
            );
        }
        return result;
    }

    
    // Pattern methods
    
    public boolean matches( Context context, Node node ) {
        // Short-circuit the boolean AND and OR operators
        // returning early if LHS is all that is needed
        // to determine truthfulness.
        
        Context duplicateContext = context.duplicate();
        
        if ( _op == Op.OR ) {
            if ( _lhs.matches( context, node ) ) {
                return true;
            }
            return _rhs.matches( duplicateContext, node );
        }
        else if ( _op == Op.AND ) {
            if ( ! _lhs.matches( context, node ) ) {
                return false;
            }
            else {
                return _rhs.matches( duplicateContext, node );
            }
        }
        else {
            Boolean lhsValue = _lhs.matches( context, node ) 
                ? Boolean.TRUE : Boolean.FALSE;
                
            Boolean rhsValue = _rhs.matches( duplicateContext, node )
                ? Boolean.TRUE : Boolean.FALSE;
                
            Object result = Operator.evaluate(
                duplicateContext.duplicate(), _op, lhsValue, rhsValue
            );
            if ( result instanceof Boolean ) {
                Boolean b = (Boolean) result;
                return b.booleanValue();
            }
        }
        return false;
    }
    
    public String toString() {
        return "[BinaryExpr: " + _lhs + " " + _op + " " + _rhs +  " ]";
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
