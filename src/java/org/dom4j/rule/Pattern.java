/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.rule;

import org.dom4j.Node;
import org.dom4j.NodeFilter;


/** <p><code>Pattern</code> defines the behaviour for pattern in
  * the XSLT processing model.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface Pattern extends NodeFilter {

    // These node numbers are compatable with DOM4J's Node types

    /** Matches any node */
    public static final short ANY_NODE = 0;
    
    /** Matches no nodes */
    public static final short NONE = 9999;
    
    /** Count of the number of node types */
    public static final short NUMBER_OF_TYPES = Node.UNKNOWN_NODE;

    /** According to the 
      * <a href="http://www.w3.org/TR/xslt11/#conflict">spec</a>
      * we should return 0.5 if we cannot determine the priority
      */
    public static final double DEFAULT_PRIORITY = 0.5;
    
    
    /** @return true if the pattern matches the given 
      * DOM4J node.
      */
    public boolean matches( Node node );
    
    /** Returns the default resolution policy of the pattern according to the
      * <a href="http://www.w3.org/TR/xslt11/#conflict">
      * XSLT conflict resolution spec</a>. 
      * 
      */
    public double getPriority();
    
    /** If this pattern is a union pattern then this
      * method should return an array of patterns which
      * describe the union pattern, which should contain more than one pattern.
      * Otherwise this method should return null.
      *
      * @return an array of the patterns which make up this union pattern
      * or null if this pattern is not a union pattern
      */
    public Pattern[] getUnionPatterns();

    
    /** @return the type of node the pattern matches
      * which by default should return ANY_NODE if it can
      * match any kind of node.
      */
    public short getMatchType();


    /** For patterns which only match an ATTRIBUTE_NODE or an 
      * ELEMENT_NODE then this pattern may return the name of the
      * element or attribute it matches. This allows a more efficient
      * rule matching algorithm to be performed, rather than a brute 
      * force approach of evaluating every pattern for a given Node.
      *
      * @return the name of the element or attribute this pattern matches
      * or null if this pattern matches any or more than one name.
      */
    public String getMatchesNodeName();
    
    
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
