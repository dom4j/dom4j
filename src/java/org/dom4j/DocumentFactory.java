/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import org.dom4j.tree.DefaultDocument;

/** <p><code>DocumentFactory</code> is a collection of factory methods to allow
  * easy custom building of DOM4J trees. The default tree that is built uses
  * a doubly linked tree. </p>
  *
  * <p>The tree built allows full XPath expressions from anywhere on the 
  * tree.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DocumentFactory {

    /** The Singleton instance */
    protected static DocumentFactory singleton = new DocumentFactory();
 
    /** The <code>{@link XPathEngine}</code> used to resolve XPath expressions */
    protected XPathEngine XPathEngine;
    
    // Some static helper methods
    
    public static Document create() {
        return singleton.createDocument();
    }
    
    public static Document create(Element rootElement) {
        return singleton.createDocument(rootElement);
    }
    
    /** <p>Access to singleton implementation of DocumentFactory which 
      * is used if no DocumentFactory is specified when building using the 
      * standard builders.</p>
      *
      * @return the default singleon instance
      */
    public static DocumentFactory getInstance() {
        return singleton;
    }
    
    
    
    /** <p>This will return the <code>{@link XPathEngine}</code>
      * instance which will be used for all <code>Document</code>
      * instances created by this factory.</p>
      *
      * @return the resolver of XPath expressions
      */
    public XPathEngine getXPathEngine() {
        if ( XPathEngine == null ) {
            XPathEngine = createXPathEngine();
        }
        return XPathEngine;
    }

    /** <p>This will set the <code>{@link XPathEngine}</code>
      * instance which will be used for all <code>Document</code>
      * instances created by this factory.</p>
      *
      * @param XPathEngine <code>XPathEngine</code> - the resolver of XPath expressions
      */
    public void setXPathEngine(XPathEngine XPathEngine) {
        this.XPathEngine = XPathEngine;
    }

    
    // Factory methods
    
    public Document createDocument() {
        Document answer = new DefaultDocument();
        answer.setXPathEngine( getXPathEngine() );
        return answer;
    }
    
    
    public Document createDocument(Element rootElement) {
        Document answer = createDocument();
        answer.setRootElement(rootElement);
        return answer;
    }
    
    
    /** <p>A Factory Method pattern to create the default 
      * <code>{@link XPathEngine}</code> instance which will be used for 
      * all <code>Document</code> instances created by this factory.</p>
      *
      * @return the new resolver of XPath expressions
      */
    protected XPathEngine createXPathEngine() {
        // use the default
        return XPathHelper.getDefaultXPathEngine();
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
