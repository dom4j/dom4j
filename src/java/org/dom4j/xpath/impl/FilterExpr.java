/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


package org.dom4j.xpath.impl;

import java.util.List;
import java.util.ArrayList;

import org.dom4j.Node;
import org.dom4j.xpath.impl.Context;

public class FilterExpr extends PathExpr implements org.jaxen.expr.PathExpr, org.jaxen.expr.FilterExpr  {
    
    private Expr _expr;
    private List _predicates;
    
    private LocationPath _path;
    
    public FilterExpr(org.jaxen.expr.FilterExpr expr, org.jaxen.expr.LocationPath path) {
        _expr = (Expr) expr;
        _path = (LocationPath) path;
    }
    
    public FilterExpr(Expr expr) {
        _expr = expr;
    }
    
    
    public org.jaxen.expr.Expr simplify() {
        if ( _predicates == null || _predicates.size() <= 0 ) {
            if ( _expr == null ) {
                return _path.simplify();
            }
            else if ( _path == null || _path.isEmpty() ) {
                return _expr.simplify();
            }
        }
        return this;
    }
    
    public void addPredicate(org.jaxen.expr.Predicate pred) {
        if ( _predicates == null ) {
            _predicates = new ArrayList();
        }        
        _predicates.add(pred);
    }
    
    public List getPredicates() {
        return _predicates;
    }
    
    public org.jaxen.expr.LocationPath getLocationPath() {
        return _path;
    }

    public org.jaxen.expr.Expr getFilterExpr() {
        return _expr;
    }
    
    public void setFilterExpr(org.jaxen.expr.Expr expr) {
        _expr = (Expr) expr;
    }
    
    public void setLocationPath(LocationPath path) {
        _path = path;
        //_path.setAbsolute(false);
    }
    
    public Object evaluate(Context context) {
        if ( _expr == null ) {
            //System.out.println( "Warning: null expression: " + toString() );
            if ( _path != null ) {
                return _path.evaluate( context );
            }
            return null;
        }
        Object answer = _expr.evaluate( context );
        if ( _path != null ) {
            if ( answer instanceof List ) {
                context.setNodeSet( (List) answer );
            }
            else if ( answer instanceof Node ) {
                context.setNodeSet( (Node) answer );
            }
            answer = _path.evaluate( context );
        }
        return answer;
    }
    
    public String toString() {
        return "[Filter: " + _expr + " predicates: " + _predicates + " locationPath: " + _path + "]";
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
