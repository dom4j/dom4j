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
import java.util.Iterator;
import java.util.List;

import junit.framework.*;
import junit.textui.TestRunner;

import org.dom4j.io.SAXReader;

/** Test the use of namespaces
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TestNamespaces extends AbstractTestCase {

    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestNamespaces.class );
    }
    
    public TestNamespaces(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testNamespaces() throws Exception {
        Document doc2 = (Document) document.clone();
        
        Element root = doc2.getRootElement();
        assertNamespace( root.getNamespace(), "", "http://www.w3.org/2001/XMLSchema" );
        
        List additionalNS = root.additionalNamespaces();
        assertTrue( "at least one additional namespace", additionalNS != null && additionalNS.size() > 0 );
        
        Namespace ns = (Namespace) additionalNS.get(0);
        assertNamespace( ns, "t", "http://www.w3.org/namespace/" );
        
        Node node = root.node(0);
        assertTrue( "First node is a namespace", node instanceof Namespace );
        
        // now lets try change the namespace
        root.remove(ns);
        root.addNamespace( "t", "myNewURI" );
        
        additionalNS = root.additionalNamespaces();
        assertTrue( "at least one additional namespace", additionalNS != null && additionalNS.size() > 0 );
        
        ns = (Namespace) additionalNS.get(0);
        assertNamespace( ns, "t", "myNewURI" );
        
        // lets test the list is backed
        additionalNS.remove(0);
        additionalNS.add( Namespace.get("t", "myNewURI-2" ) );

        additionalNS = root.additionalNamespaces();
        assertTrue( "at least one additional namespace", additionalNS != null && additionalNS.size() > 0 );
        
        ns = (Namespace) additionalNS.get(0);
        assertNamespace( ns, "t", "myNewURI-2" );
        
        additionalNS.clear();
        root.addNamespace( "t", "myNewURI" );
        
        additionalNS = root.additionalNamespaces();
        assertTrue( "at least one additional namespace", additionalNS != null && additionalNS.size() > 0 );
        
        ns = (Namespace) additionalNS.get(0);
        assertNamespace( ns, "t", "myNewURI" );
        
        
        log( "Namespaces: " + additionalNS );
        log( "XML is now" );
        log( root.asXML() );
    }
    
    public void testNamespaceForPrefix() throws Exception {
        Element root = document.getRootElement();
        
        Namespace ns = root.getNamespaceForPrefix( "t" );
        
        assertNamespace( ns, "t", "http://www.w3.org/namespace/" );
        
        Element element = (Element) root.elements().get(0);
        Namespace ns2 = element.getNamespaceForPrefix( "t" );
        
        assertNamespace( ns2, "t", "http://www.w3.org/namespace/" );
        
        assertTrue( "Same namespace instance returned", ns == ns2 );
        
        log( "found: " + ns.asXML() );
    }
    
    public void testNamespaceForURI() throws Exception {
        Element root = document.getRootElement();
        
        Namespace ns = root.getNamespaceForURI( "http://www.w3.org/namespace/" );
        
        assertNamespace( ns, "t", "http://www.w3.org/namespace/" );
        
        Element element = (Element) root.elements().get(0);
        Namespace ns2 = element.getNamespaceForURI( "http://www.w3.org/namespace/" );
        
        assertNamespace( ns2, "t", "http://www.w3.org/namespace/" );
        
        assertTrue( "Same namespace instance returned", ns == ns2 );
        
        log( "found: " + ns.asXML() );
    }
    
        
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void setUp() throws Exception {
        SAXReader reader = new SAXReader();
        document = reader.read( "xml/test/test_schema.xml" );
    }
    
    protected void assertNamespace(Namespace ns, String prefix, String uri) throws Exception {
        assertEquals( "namespace prefix", prefix, ns.getPrefix() );        
        assertEquals( "namespace URI", uri, ns.getURI() );
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
 *    trademark of MetaStuf, Ltd.
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
