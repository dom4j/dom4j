/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.io.File;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.io.SAXReader;
import org.dom4j.util.UserDataAttribute;
import org.dom4j.util.UserDataDocumentFactory;
import org.dom4j.util.UserDataElement;

/** Tests the UserDataDocumentFactory
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TestUserData extends AbstractTestCase {

    /** Input XML file to read */
    protected static String INPUT_XML_FILE = "xml/web.xml";
    
    private Object userData = new Double( 1.23456 );
        
    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestUserData.class );
    }
    
    public TestUserData(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testSetData() throws Exception {
        Element root = getRootElement();
        
        assertTrue( "Element instanceof UserDataElement", root instanceof UserDataElement );
       
        root.setData( userData );
        
        assertTrue( "Stored user data!", root.getData() == userData );
        
        log( "root: " + root );        
        
        assertUserData( root, userData );
        
        Element cloned = (Element) root.clone();
        assertTrue( "Cloned new instance", cloned != root );
        assertUserData( cloned, userData );

        cloned = root.createCopy();
        assertTrue( "Cloned new instance", cloned != root );
        assertUserData( cloned, userData );
    }
        
    public void testNewAdditions() throws Exception {
        Element root = getRootElement();
        
        Element newElement = root.addElement( "foo1234" );        
        assertTrue( "New Element is a UserDataElement", newElement instanceof UserDataElement );
        
        root.addAttribute( "bar456", "123" );
        
        Attribute newAttribute = root.attribute( "bar456" );
        
        assertTrue( "New Attribute is a UserDataAttribute", newAttribute instanceof UserDataAttribute );
    }
        
    public void testNewDocument() throws Exception {
        DocumentFactory factory = UserDataDocumentFactory.getInstance();
        Document document = factory.createDocument();
        
        Element root = document.addElement( "root" );
        assertTrue( "Root Element is a UserDataElement", root instanceof UserDataElement );
        
        Element newElement = root.addElement( "foo1234" );        
        assertTrue( "New Element is a UserDataElement", newElement instanceof UserDataElement );
        
        root.addAttribute( "bar456", "123" );
        
        Attribute newAttribute = root.attribute( "bar456" );
        
        assertTrue( "New Attribute is a UserDataAttribute", newAttribute instanceof UserDataAttribute );
    }
        
        
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void assertUserData( Element root, Object userData ) throws Exception {
        Object result = root.getData();
        
        assertTrue( "No user data!", result != null );
        assertTrue( "Stored user data correctly", userData.equals( result ) );
    }
        
    protected void setUp() throws Exception {
        SAXReader reader = new SAXReader();
        reader.setDocumentFactory( UserDataDocumentFactory.getInstance() );
        document = reader.read( new File( INPUT_XML_FILE ) );
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
