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

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.io.SAXReader;

/** Tests the getNodeNameType() method
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TestNodeTypeName extends AbstractTestCase {
    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestNodeTypeName.class );
    }
    
    public TestNodeTypeName(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testDocument() throws Exception {
        testDocument(document);
    }
    
    public void testCDATA() throws Exception {
        testDocument( "xml/cdata.xml" );
    }
    
    public void testNamespaces() throws Exception {
        testDocument( "xml/namespaces.xml" );
        testDocument( "xml/testNamespaces.xml" );
    }
    public void testPI() throws Exception {
        testDocument( "xml/testPI.xml" );
    }

    public void testInline() throws Exception {
        testDocument( "xml/inline.xml" );
    }
        
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void testDocument(String fileName) throws Exception {
        SAXReader reader = new SAXReader();
        Document document = reader.read( fileName );
        testDocument(document);
    }

    protected void testDocument(Document document) throws Exception {
        assertEquals( document.getNodeTypeName(), "Document" );
        DocumentType docType = document.getDocType();
        if ( docType != null ) {
            assertEquals( docType.getNodeTypeName(), "DocumentType" );
        }
        
        testElement( document.getRootElement() );
    }
        
    protected void testElement( Element element ) {
        assertEquals( element.getNodeTypeName(), "Element" );

        for ( Iterator iter = element.attributeIterator(); iter.hasNext(); ) {
            Attribute attribute = (Attribute) iter.next();
            assertEquals( attribute.getNodeTypeName(), "Attribute" );
        }
        
        for ( Iterator iter = element.nodeIterator(); iter.hasNext(); ) {
            Node node = (Node) iter.next();
            String nodeTypeName = node.getNodeTypeName();
            
            if ( node instanceof Attribute ) {
                assertEquals( nodeTypeName, "Attribute" );                
            }
            else if ( node instanceof CDATA ) {
                assertEquals( nodeTypeName, "CDATA" );                
            }
            else if ( node instanceof Comment ) {
                assertEquals( nodeTypeName, "Comment" );                
            }
            else if ( node instanceof Element ) {
                assertEquals( nodeTypeName, "Element" );                
                testElement( (Element) node );
            }
            else if ( node instanceof Entity ) {
                assertEquals( nodeTypeName, "Entity" );                
            }
            else if ( node instanceof Element ) {
                assertEquals( nodeTypeName, "Element" );                
            }
            else if ( node instanceof Namespace ) {
                assertEquals( nodeTypeName, "Namespace" );                
            }
            else if ( node instanceof ProcessingInstruction ) {
                assertEquals( nodeTypeName, "ProcessingInstruction" );                
            }
            else if ( node instanceof Text ) {
                assertEquals( nodeTypeName, "Text" );                
            }
            else {
                assertTrue( "Invalid node type: " + nodeTypeName + " for node: " + node, false );
            }
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
