/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** <p><code>XPathHelper</code> contains some helper methods for using 
  * and creating {@link XPathEngine} instances.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XPathHelper {

    /** The singleton XPath engine for this JVM. */
    private static XPathEngine singleton;
    
    
    /** <p><code>getInstance</code> returns the singleton
      * implementation of the XPath engine.
      * By default the implementation is loaded from the value
      * of the <code>org.dom4j.xpath.driver</code> system property.
      * If the system property is not set it defaults to the
      * Werken XPath implementation.</p>
      *
      * @return a new <code>XPathEngine</code> instance
      */
    public static XPathEngine getInstance() {
        if ( singleton == null ) {
            singleton = createXPathEngine();
        }
        return singleton;
    }
    
    /** <p><code>createXPathEngine</code> creates a new XPath engine.
      * The implementation class used is taken from the value
      * of the <code>org.dom4j.xpath.driver</code> system property.
      * If the system property is not set it defaults to the
      * Werken XPath implementation.</p>
      *
      * @param className is the name of the class which implements the
      * {@link XPathEngine} interface
      * @return a new <code>XPathEngine</code> instance or 
      * null if it could not be loaded.
      */
    public static XPathEngine createXPathEngine() {
        String className = System.getProperty( 
            "org.dom4j.xpath.driver", 
            "org.dom4j.xpath.DefaultXPathEngine" 
        );
        return createXPathEngine( className );
    }
    
        
    /** <p><code>createXPathEngine</code> creates a new XPath engine
      * from the given XPath engine class name.</p>
      *
      * @param className is the name of the class which implements the
      * {@link XPathEngine} interface
      * @return a new <code>XPathEngine</code> instance or 
      * null if it could not be loaded.
      */
    public static XPathEngine createXPathEngine(String className) {
        // let's try and class load an implementation?
        try {
            // I'll use the current class loader
            // that loaded me to avoid problems in J2EE and web apps
            Class theClass = Class.forName( 
                className, 
                true, 
                XPathHelper.class.getClassLoader() 
            );
            return (XPathEngine) theClass.newInstance();
        }
        catch (Throwable e) {
            System.out.println( "WARNING: Cannot load XPathEngine: " + className );
            return null;
        }
    }    
    

    // Static helper methods using the singleton
    
    /** <p><code>createXPath</code> parses an XPath expression
      * and creates a new XPath <code>XPath</code> instance
      * using the singleton {@link XPathEngine}.</p>
      *
      * @param xpathExpression is the XPath expression to create
      * @return a new <code>XPath</code> instance
      */
    public static XPath createXPath(String xpathExpression) {
        return getInstance().createXPath(xpathExpression);
    }
    
    /** <p><code>NodeFilter</code> parses a NodeFilter
      * from the given XPath filter expression using the singleton
      * {@link XPathEngine}.
      * XPath filter expressions occur within XPath expressions such as
      * <code>self::node()[ filterExpression ]</code></p>
      *
      * @param xpathFilterExpression is the XPath filter expression 
      * to create
      * @return a new <code>NodeFilter</code> instance
      */
    public static NodeFilter createXPathFilter(String xpathFilterExpression) {
        return getInstance().createXPathFilter(xpathFilterExpression);
    }
    
    /** <p><code>selectNodes</code> performs the given XPath
      * expression on the {@link List} of {@link Node} instances appending
      * all the results together into a single list.</p>
      *
      * @param xpathFilterExpression is the XPath filter expression 
      * to evaluate
      * @param nodes is the list of nodes on which to evalute the XPath
      * @return the results of all the XPath evaluations as a single list
      */
    public static List selectNodes(String xpathFilterExpression, List nodes) {
        XPath xpath = createXPath( xpathFilterExpression );
        return xpath.selectNodes( nodes );
    }
    
    /** <p><code>selectNodes</code> performs the given XPath
      * expression on the {@link List} of {@link Node} instances appending
      * all the results together into a single list.</p>
      *
      * @param xpathFilterExpression is the XPath filter expression 
      * to evaluate
      * @param node is the Node on which to evalute the XPath
      * @return the results of all the XPath evaluations as a single list
      */
    public static List selectNodes(String xpathFilterExpression, Node node) {
        XPath xpath = createXPath( xpathFilterExpression );
        return xpath.selectNodes( node );
    }
    
    /** <p><code>sort</code> sorts the given List of Nodes
      * using an XPath expression as a {@link Comparator}.
      *
      * @param list is the list of Nodes to sort
      * @param xpathExpression is the XPath expression used for comparison
      */
    public void sort( List list, String xpathExpression ) {
        XPath xpath = createXPath( xpathExpression );
        xpath.sort( list );
    }
    
    /** <p><code>sort</code> sorts the given List of Nodes
      * using an XPath expression as a {@link Comparator}
      * and optionally removing duplicates.</p>
      *
      * @param list is the list of Nodes to sort
      * @param xpathExpression is the XPath expression used for comparison
      * @param distinct if true then duplicate values (using the sortXPath for 
      *     comparisions) will be removed from the List
      */
    public void sort( List list, String xpathExpression, boolean distinct ) {
        XPath xpath = createXPath( xpathExpression );
        xpath.sort( list, distinct );
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
