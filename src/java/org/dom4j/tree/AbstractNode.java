/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.Visitor;
import org.dom4j.XPath;
import org.dom4j.XPathEngine;
import org.dom4j.XPathHelper;

/** <p><code>AbstractNode</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
 *
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
 * @version $Revision$
 */
public abstract class AbstractNode implements Node, Cloneable, Serializable {
    
    public AbstractNode() {
    }

    public Document getDocument() {
        Element element = getParent();
        return ( element != null ) ? element.getDocument() : null;
    }
    
    public void setDocument(Document document) {
    }
    
    public Element getParent() {
        return null;
    }

    public void setParent(Element parent) {
    }
    
    public boolean supportsParent() {
        return false;
    }

    public boolean isReadOnly() {
        return true;
    }

    public Object clone() {
        if ( ! isReadOnly() ) {
            throw new RuntimeException( "clone() not implemented yet for "
                + "this type which is not read-only" );
        }
        return this;
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {
        throw new UnsupportedOperationException( "This node cannot be modified" );
    }

    public String getText() {
        return null;
    }
    
    public void setText(String text) {
        throw new UnsupportedOperationException( "This node cannot be modified" );
    }
    
    
    public void writeXML(PrintWriter writer) {
        writer.print( asXML() );
    }
        

    // XPath methods
    
    public XPathEngine getXPathEngine() {
        XPathEngine answer = null;
        Document document = getDocument();
        if ( document != null ) {
            answer = document.getXPathEngine();
        }
        if ( answer == null ) {
            answer = XPathHelper.getInstance();
        }
        return answer;
    }
    
    public List selectNodes(XPath xpath) {
        return getXPathEngine().selectNodes(this, xpath);
    }
    
    public List selectNodes(String xpathExpression) {
        XPathEngine engine = getXPathEngine();
        XPath xpath = engine.createXPath(xpathExpression);
        return engine.selectNodes(this, xpath);
    }
    
    public Node selectSingleNode(XPath xpath) {
        return getXPathEngine().selectSingleNode(this, xpath);
    }
    
    public Node selectSingleNode(String xpathExpression) {
        XPathEngine engine = getXPathEngine();
        XPath xpath = engine.createXPath(xpathExpression);
        return engine.selectSingleNode(this, xpath);
    }
    
    public Node asXPathNode(Element parent) {
        if (supportsParent()) {
            return this;
        }
        return createXPathNode(parent);
    }
    
    protected Node createXPathNode(Element parent) {
        throw new RuntimeException("asXPathNode() not yet implemented fully for: " + this );
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
