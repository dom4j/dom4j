/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.xpath;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.InvalidXPathException;
import org.dom4j.rule.Pattern;
import org.dom4j.xpath.impl.Context;
import org.dom4j.xpath.impl.Expr;
import org.dom4j.xpath.impl.DefaultXPathFactory;

import org.saxpath.XPathReader;
import org.saxpath.SAXPathException;
import org.saxpath.helpers.XPathReaderFactory;

import org.jaxpath.JAXPathHandler;

import java.io.StringReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** <p><code>XPathPattern</code> is an implementation of Pattern
  * which uses an XPath expression.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XPathPattern implements Pattern {
    
    private String text;
    private Expr expression;
    private Context context;

    
    public XPathPattern(String text) {
        this.text = text;
        this.expression = parse( text );
    }

    public boolean matches( Node node ) {
        return expression != null && expression.matches( getContext( node ), node );
    }
    
    public String getText() {
        return text;
    }
    
    public double getPriority()  {
        return ( expression != null ) 
            ? expression.getPriority() : Pattern.DEFAULT_PRIORITY;
    }
    
    public Pattern[] getUnionPatterns() {
        return ( expression != null ) 
            ? expression.getUnionPatterns() : null;
    }

    public short getMatchType() {
        return ( expression != null ) 
            ? expression.getMatchType() : ANY_NODE;
    }

    public String getMatchesNodeName() {
        return ( expression != null ) 
            ? expression.getMatchesNodeName() : null;
    }

    public Context getContext( Node node ) {
        Context context = getContext();
        context.setNodeSet( node );
        return context;
    }
    
    public Context getContext() {
        if ( context == null ) {
            context = createContext();
        }
        return context;
    }
    
    public void setContext(Context Context) {
        this.context = context;
    }
    
    public String toString() {
        return "[XPathPattern: text: " + text + " XPath: " + expression + "]";
    }

    

    // Implementation methods
    
    protected Context createContext() {
        return new Context();
    }
    

    private Expr parse( String text ) {
	  Expr expr = null;
        try {
            XPathReader reader = XPathReaderFactory.createReader();
            
            JAXPathHandler handler = new JAXPathHandler();
            
            handler.setXPathFactory( new DefaultXPathFactory() );
            
            reader.setXPathHandler( handler );
            
            reader.parse( text );
            
            org.jaxpath.expr.XPath xpath = handler.getXPath(true);
            expr = (Expr) xpath.getRootExpr();
        }
        catch (SAXPathException e) {
            throw new InvalidXPathException( text, e.getMessage() );
        }
        if ( expr == null ) {
            throw new InvalidXPathException( text );
        }
        return expr;
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
