/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.util.Comparator;
import java.util.List;

/** <p><code>XPath</code> represents an XPath expression after 
  * it has been parsed from a String.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface XPath {

    /** <p><code>getText</code> will return the textual version of 
      * the XPath expression.</p>
      *
      * @return the textual format of the XPath expression.
      */
    public String getText();
        
    /** <p><code>selectNodes</code> performs this XPath
      * expression on the {@link List} of {@link Node} instances appending
      * all the results together into a single list.</p>
      *
      * @param nodes is the list of nodes on which to evalute the XPath
      * @return the results of all the XPath evaluations as a single list
      */
    public List selectNodes(List nodes);
    
    /** <p><code>selectNodes</code> evaluates an XPath expression
      * on the current node and returns the result as a <code>List</code> of 
      * <code>Node</code> instances.</p>
      *
      * @param node is the context node on which to 
      *     process the XPath expression
      * @param xpath is the XPath expression to evaluate
      * @return a list of <code>Node</code> instances 
      */
    public List selectNodes(Node node);
    
    /** <p><code>selectNodes</code> evaluates the XPath expression
      * on the current node and returns the result as a <code>List</code> of 
      * <code>Node</code> instances sorted by another XPath expression.</p>
      *
      * @param node is the context node on which to 
      *     process the XPath expression
      * @param sortXPath is the XPath expression to sort by
      * @param distinct specifies whether or not duplicate values of the 
      *     sort expression are allowed. If this parameter is true then only 
      *     distinct sort expressions values are included in the result
      * @return a list of <code>Node</code> instances 
      */
    public List selectNodes(Node node, XPath sortXPath, boolean distinct);
    
    /** <p><code>selectSingleNode</code> evaluates this XPath expression
      * on a given node and returns the result as a single
      * <code>Node</code> instance.</p>
      *
      * @param node is the context node on which to 
      *     process the XPath expression
      * @return a single matching <code>Node</code> instance
      */
    public Node selectSingleNode(Node node);
    
    /** <p><code>selectSingleNode</code> evaluates this XPath expression
      * on a given node and returns the result as a single
      * <code>Node</code> instance.</p>
      *
      * @param nodes is the list of nodes on which to evalute the XPath
      * @return a single matching <code>Node</code> instance
      */
    public Node selectSingleNode(List nodes);
    

    
    /** <p><code>valueOf</code> evaluates this XPath expression
      * and returns the textual representation of the results using the 
      * XPath string() function.</p>
      *
      * @param node is the context node on which to 
      *     process the XPath expression
      * @return the string representation of the results of the XPath expression
      */
    public String valueOf(Node node);
    
    /** <p><code>sort</code> sorts the given List of Nodes
      * using this XPath expression as a {@link Comparator}.</p>
      *
      * @param list is the list of Nodes to sort
      */
    public void sort( List list );
    
    /** <p><code>sort</code> sorts the given List of Nodes
      * using this XPath expression as a {@link Comparator} 
      * and optionally removing duplicates.</p>
      *
      * @param list is the list of Nodes to sort
      * @param distinct if true then duplicate values (using the sortXPath for 
      *     comparisions) will be removed from the List
      */
    public void sort( List list, boolean distinct );
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
