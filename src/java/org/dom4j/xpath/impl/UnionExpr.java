/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


package org.dom4j.xpath.impl;

import org.dom4j.xpath.ContextSupport;

import org.dom4j.xpath.function.BooleanFunction;
import org.dom4j.xpath.impl.Context;

import org.dom4j.Element;
import org.dom4j.Attribute;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class UnionExpr extends Expr implements org.jaxen.expr.UnionExpr {
    
    private Expr  _lhs  = null;
    private Expr  _rhs  = null;
    
    public UnionExpr(Expr lhs, Expr rhs) {
        _lhs = lhs;
        _rhs = rhs;
    }
    
    public org.jaxen.expr.Expr getLHS() {
        return _lhs;
    }
    
    public org.jaxen.expr.Expr getRHS() {
        return _rhs;
    }

    public org.jaxen.expr.Expr simplify() {
        if ( _lhs != null ) {
            _lhs = (Expr) _lhs.simplify();
        }
        if ( _rhs != null ) {
            _rhs = (Expr) _rhs.simplify();
        }
        return this;
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
