/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.io.SAXReader;

/** Tests that a dom4j document is Serializable
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TestSerialize extends AbstractTestCase {

    protected static final boolean VERBOSE = false;
    
    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestSerialize.class );
    }
    
    public TestSerialize(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testSerializePeriodicTable() throws Exception {
        testSerialize( "xml/periodic_table.xml" );
    }
    
    public void testSerializeMuchAdo() throws Exception {
        testSerialize( "xml/much_ado.xml" );
    }
    
    public void testSerializeTestSchema() throws Exception {
        testSerialize( "xml/test/schema/personal.xsd" );
    }
    
    public void testSerializeXPath() throws Exception {
        Map uris = new HashMap();
        uris.put( "SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/" );
        uris.put( "m", "urn:xmethodsBabelFish" );        

        DocumentFactory factory = new DocumentFactory();
        factory.setXPathNamespaceURIs( uris );

        // now parse a document using my factory
        SAXReader reader = new SAXReader();
        reader.setDocumentFactory( factory );
        Document doc = reader.read( "xml/soap.xml" );

        // now lets use the prefixes
        Node element = doc.selectSingleNode( "/SOAP-ENV:Envelope/SOAP-ENV:Body/m:BabelFish" );
        assertTrue( "Found valid element", element != null );
        
        XPath xpath = factory.createXPath( "/SOAP-ENV:Envelope/SOAP-ENV:Body/m:BabelFish" );
        element = xpath.selectSingleNode( doc );
        assertTrue( "Found valid element", element != null );
        
        // now serialize
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream( bytesOut );
        out.writeObject( xpath );
        out.close();
        
        byte[] data = bytesOut.toByteArray();
        
        ObjectInputStream in = new ObjectInputStream( new ByteArrayInputStream( data ) );
        XPath xpath2 = (XPath) in.readObject();
        in.close();        
        
        element = xpath2.selectSingleNode( doc );
        assertTrue( "Found valid element", element != null );        
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void testSerialize(String xmlFile) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read( xmlFile );
        String text = document.asXML();
        
        ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream( bytesOut );
        out.writeObject( document );
        out.close();
        
        byte[] data = bytesOut.toByteArray();
        
        ObjectInputStream in = new ObjectInputStream( new ByteArrayInputStream( data ) );
        Document doc2 = (Document) in.readObject();
        in.close();        
        
        String text2 = doc2.asXML();
        
        assertEquals( "Documents text are equal", text, text2 );        
        
        assertTrue( "Read back document after serialization", doc2 != null && doc2 instanceof Document );
        
        assertDocumentsEqual( document, (Document) doc2 );        
        
        // now lets try add something to the document...
        
        doc2.getRootElement().addElement( "new" );
    }            
    
    protected void setUp() throws Exception {
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
