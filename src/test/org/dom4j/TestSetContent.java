/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/** Tests the setContent method
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TestSetContent extends AbstractTestCase {

    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestSetContent.class );
    }
    
    public TestSetContent(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testDocument() throws Exception {
        document.setName( "doc1" );
                
        Element oldRoot = document.getRootElement();
        
        assertTrue( "Current root has document", oldRoot.getDocument() == document );
        
        Document doc2 = DocumentHelper.createDocument();
        doc2.setName( "doc2" );
        
        assertTrue( "Doc2 has no root element", doc2.getRootElement() == null );
        
        doc2.setContent( document.content() );
        
        Element newRoot = doc2.getRootElement();
        
        assertTrue( "Current root has document", oldRoot.getDocument() == document );
        
        assertTrue( "Doc2 has now has root element", newRoot != null );        
        assertTrue( "Doc2 has different root element", newRoot != oldRoot );        
        assertTrue( "Root element now has document", newRoot.getDocument() == doc2 );
        
        testParent( doc2.getRootElement() );
        testDocument( doc2, doc2 );
        
        doc2.clearContent();
        
        assertTrue( "New Doc has no root", doc2.getRootElement() == null );
        assertTrue( "New root has no document", newRoot.getDocument() == null );
    }
        
        
    public void testRootElement() throws Exception {
        Document doc3 = DocumentHelper.createDocument();
        doc3.setName( "doc3" );
        Element root = doc3.addElement( "root" );
        Element oldElement = root.addElement( "old" );
        
        assertTrue( "Doc3 has root element", root != null );
        
        root.setContent( document.getRootElement().content() );
        
        assertTrue( "Doc3's root now has content", root.nodeCount() > 0 );
        assertTrue( "Old element has no parent now", oldElement.getParent() == null );
        assertTrue( "Old element has no document now", oldElement.getDocument() == null );
        
        testParent( root );
        testDocument( root, doc3 );
    }
    
    /** Tests all the children of the branch have the correct parent */
    protected void testParent( Branch parent ) {
        for ( int i = 0, size = parent.nodeCount(); i < size; i++ ) {
            Node node = parent.node(i);
            assertTrue( "Child node of root has parent of root", node.getParent() == parent );
        }
    }
    
    /** Tests all the children of the branch have the correct document */
    protected void testDocument( Branch branch, Document doc ) {
        for ( int i = 0, size = branch.nodeCount(); i < size; i++ ) {
            Node node = branch.node(i);
            assertTrue( "Node has correct document", node.getDocument() == doc );
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
