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

/** A test harness to test the detach() method on root elements
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TestDetach extends AbstractTestCase {

    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestDetach.class );
    }
    
    public TestDetach(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testRoot() throws Exception {
        document.setName( "doc1" );
        
        Element root = document.getRootElement();
        assertTrue( "Has root element", root != null );
        assertTrue( "Root has no parent", root.getParent() == null );
        
        root.detach();
        
        assertTrue( "Detached root now has no document", root.getDocument() == null );
        assertTrue( "Original doc now has no root element", document.getRootElement() == null );
        
        Document doc2 = DocumentHelper.createDocument();
        doc2.setName( "doc2" );
        
        assertTrue( "Doc2 has no root element", doc2.getRootElement() == null );
        
        doc2.setRootElement( root );
        
        assertTrue( "Doc2 has now has root element", doc2.getRootElement() == root );        
        assertTrue( "Root element now has document", root.getDocument() == doc2 );
        
        
        Document doc3 = DocumentHelper.createDocument();
        doc3.setName( "doc3" );
        doc3.addElement( "foo" );
        
        assertTrue( "Doc3 has root element", doc3.getRootElement() != null );
        
        root = doc2.getRootElement();
        root.detach();
        doc3.setRootElement( root );
        
        assertTrue( "Doc3 now has root element", doc3.getRootElement() == root );        
        assertTrue( "Root element now has a document", root.getDocument() == doc3 );
        assertTrue( "Doc2 has no root element", doc2.getRootElement() == null );        
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
