/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


package org.dom4j.xpath.impl;

import org.dom4j.Node;
import org.dom4j.rule.Pattern;
import org.dom4j.xpath.function.StringFunction;
import org.dom4j.xpath.impl.Context;

import java.util.ArrayList;
import java.util.List;

public abstract class Expr implements org.jaxpath.expr.Expr {    
    
    public String getText() {
        return "[N/I]";
    }
    
    public org.jaxpath.expr.Expr simplify() {
        return this;
    }
    
    
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
