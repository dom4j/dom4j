/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** A test harness to test the parent relationship and use of the
  * {@link Node#asXPathResult} method.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TestParent extends AbstractTestCase {
    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestParent.class );
    }
    
    public TestParent(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testDocument() throws Exception {        
        testParentRelationship( document.getRootElement() );
    }
        
    public void testFragment() throws Exception {        
        
        DocumentFactory factory = new DocumentFactory();
        Element root = factory.createElement( "root" );
        Element first = root.addElement( "child" );
        Element second = root.addElement( "child" );
        
        testXPathNode( root, first );
        testXPathNode( root, second );
    }
        
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void testParentRelationship( Element parent, List content ) {
        for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if ( object instanceof Element ) {
                testParentRelationship( (Element) object );
            }
            testXPathNode( parent, (Node) object );
        }
    }
    
    protected void testParentRelationship( Element element ) {
        testParentRelationship( element, element.attributes() );
        testParentRelationship( element, element.content() );
    }    
    
    
    protected void testXPathNode( Element parent, Node node ) {
        if ( node.supportsParent() ) {
            log( "Node: " + node );
            log( "Parent: " + parent );
            log( "getParent(): " + node.getParent() );
            
            assertTrue( "getParent() returns parent for: " + node, node.getParent() == parent );
        }
        else {
            // lets create an XPath node
            Node xpathNode = node.asXPathResult( parent );
            assertTrue( "XPath Node supports parent for: " + xpathNode, xpathNode.supportsParent() );
            assertTrue( "getParent() returns parent for: " + xpathNode, xpathNode.getParent() == parent );
        }
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
