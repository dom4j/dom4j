/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.List;
import java.net.URL;

import junit.framework.*;
import junit.textui.TestRunner;

import org.w3c.tidy.Tidy;

import org.dom4j.io.DOMReader;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.SAXContentHandler;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.NodeComparator;

/** A test harness to test the the round trips of Documents.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TestRoundTrip extends AbstractTestCase {
    
    //protected String xmlFile = "xml/test/encode.xml";
    //protected String xmlFile = "xml/fibo.xml";
    protected String xmlFile = "xml/test/test_schema.xml";
    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestRoundTrip.class );
    }
    
    public TestRoundTrip(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testTextRoundTrip() throws Exception {
        roundTripText( document );
    }

    
    public void testSAXRoundTrip() throws Exception {
        roundTripSAX( document );
    }
    
    public void testDOMRoundTrip() throws Exception {
        roundTripDOM( document );
    }
    
    public void testFullRoundTrip() throws Exception {        
        Document doc2 = roundTripDOM( document );
        Document doc3 = roundTripSAX( doc2 );
        Document doc4 = roundTripText( doc3 );

        assertDocumentsEqual( document, doc4 );
    }

    public void testJTidyRoundTrip() throws Exception {
        Document document = loadHTML( "readme.html" );
  
        //Document doc1 = roundTripText( document );
        Document doc1 = roundTripSAX( document );
        Document doc2 = roundTripDOM( doc1 );
        Document doc3 = roundTripSAX( doc2 );
        Document doc4 = roundTripText( doc3 );
        Document doc5 = roundTripDOM( doc4 );
        
        assertDocumentsEqual( document, doc5 );
    }
    
    
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void setUp() throws Exception {
        SAXReader reader = new SAXReader();
        document = reader.read( xmlFile );
    }
    
    protected Document loadHTML( String xmlFile ) throws Exception {
        InputStream in = openStream( xmlFile );
        Tidy tidy = new Tidy();
        tidy.setXHTML(true);
        tidy.setDocType("omit");
        org.w3c.dom.Document domDocument = tidy.parseDOM( in, null );
        
        DOMReader domReader = new DOMReader();
        return domReader.read( domDocument );
    }
    
    protected InputStream openStream(String xmlFile) throws Exception {
        File file = new File( xmlFile );
        if ( file.exists() ) {
            return new BufferedInputStream( new FileInputStream( file ) );
        }
        return new URL( xmlFile ).openStream();
    }
    
    protected Document roundTripDOM(Document document) throws Exception {
        // now lets make a DOM object
        DOMWriter domWriter = new DOMWriter();
        org.w3c.dom.Document domDocument = domWriter.write(document);
        
        // now lets read it back as a DOM4J object
        DOMReader domReader = new DOMReader();        
        Document newDocument = domReader.read( domDocument );
        
        // lets ensure names are same
        newDocument.setName( document.getName() );
        
        assertDocumentsEqual( document, newDocument );
        
        return newDocument;
    }
    
    protected Document roundTripSAX(Document document) throws Exception {
        
        // now lets write it back as SAX events to
        // a SAX ContentHandler which should build up a new document
        SAXContentHandler contentHandler = new SAXContentHandler();
        SAXWriter saxWriter = new SAXWriter( contentHandler, null, contentHandler );
        
        saxWriter.write( document );
        Document newDocument = contentHandler.getDocument();
        
        // lets ensure names are same
        newDocument.setName( document.getName() );
        
        assertDocumentsEqual( document, newDocument );
        
        return newDocument;
    }
        
    protected Document roundTripText(Document document) throws Exception {
        StringWriter out = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(out);
        
        xmlWriter.write( document );
        
        // now lets read it back
        String xml = out.toString();
        
        StringReader in = new StringReader( xml );
        SAXReader reader = new SAXReader();
        Document newDocument = reader.read(in);
        
        // lets ensure names are same
        newDocument.setName( document.getName() );
        
        assertDocumentsEqual( document, newDocument );
        
        return newDocument;
    }
    
    protected void assertDocumentsEqual(Document doc1, Document doc2) throws Exception {
        assert( "Doc1 not null", doc1 != null );
        assert( "Doc2 not null", doc2 != null );
 
        doc1.normalize();
        doc2.normalize();
        
        assertNodesEqual(doc1, doc2);
        
        NodeComparator comparator = new NodeComparator();
        assert( "Documents are equal", comparator.compare( doc1, doc2 ) == 0 );
    }

    
    protected void assertNodesEqual( Document n1, Document n2 ) {
        assertEquals( "Document names", n1.getName(), n2.getName() );        
        assertNodesEqual( n1.getDocType(), n2.getDocType() );        
        assertNodesEqualContent( n1, n2 );
    }
    
    protected void assertNodesEqual( Element n1, Element n2 ) {
        assertNodesEqual( n1.getQName(), n2.getQName() );
        
        int c1 = n1.attributeCount();
        int c2 = n2.attributeCount();
        
        assertEquals( 
            "Elements have same number of attributes (" + c1 + ", " + c2 
                + " for: " + n1 + " and " + n2,
            c1, c2 
        );
        
        for ( int i = 0; i < c1; i++ ) {
            Attribute a1 = n1.attribute(i);
            Attribute a2 = n2.attribute(i);
            assertNodesEqual( a1, a2 );
        }
        
        assertNodesEqualContent( n1, n2 );
    }
    
    protected void assertNodesEqual( Attribute n1, Attribute n2 ) {
        assertNodesEqual( n1.getQName(), n2.getQName() );
        
        assertEquals( 
            "Attribute values for: " + n1 + " and " + n2,
            n1.getValue(), n2.getValue() 
        );
    }
    
    protected void assertNodesEqual( QName n1, QName n2 ) {
        assertEquals( 
            "URIs equal for: " + n1.getQualifiedName() + " and " + n2.getQualifiedName(),
            n1.getNamespaceURI(), n2.getNamespaceURI() 
        );
        assertEquals( 
            "qualified names equal",
            n1.getQualifiedName(), n2.getQualifiedName() 
        );
    }
    
    protected void assertNodesEqual( CharacterData t1, CharacterData t2 ) {
        assertEquals( 
            "Text equal for: " + t1 + " and " + t2,
            t1.getText(), t2.getText() 
        );
    }
    
    protected void assertNodesEqual( DocumentType o1, DocumentType o2 ) {
        if ( o1 != o2 ) {
            if ( o1 == null ) {
                assert( "Missing DocType: " + o2, false );
            }
            else if ( o2 == null ) {
                assert( "Missing DocType: " + o1, false );
            }
            else {
                assertEquals( "DocType name equal", o1.getName(), o2.getName() );
                assertEquals( "DocType publicID equal", o1.getPublicID(), o2.getPublicID() );
                assertEquals( "DocType systemID equal", o1.getSystemID(), o2.getSystemID() );
            }
        }
    }
    
    protected void assertNodesEqual( Entity o1, Entity o2 ) {
        assertEquals( "Entity names equal", o1.getName(), o2.getName() );
        assertEquals( "Entity values equal", o1.getText(), o2.getText() );
    }
    
    protected void assertNodesEqual( ProcessingInstruction n1, ProcessingInstruction n2 ) {
        assertEquals( "PI targets equal", n1.getTarget(), n2.getTarget() );
        assertEquals( "PI text equal", n1.getText(), n2.getText() );
    }
    
    protected void assertNodesEqual( Namespace n1, Namespace  n2 ) {
        assertEquals( "Namespace prefixes equal", n1.getPrefix(), n2.getPrefix() );
        assertEquals( "Namespace URIs equal", n1.getURI(), n2.getURI() );
    }
    
    protected void assertNodesEqualContent( Branch b1, Branch b2 ) {
        int c1 = b1.nodeCount();
        int c2 = b2.nodeCount();
        
        if ( c1 != c2 ) {
            log( "Content of: " + b1 );
            log( "is: " + b1.content() );
            log( "Content of: " + b2 );
            log( "is: " + b2.content() );
        }
        
        assertEquals( 
            "Branches have same number of children (" + c1 + ", " + c2 
                + " for: " + b1 + " and " + b2,
            c1, c2 
        );
        for ( int i = 0; i < c1; i++ ) {
            Node n1 = b1.node(i);
            Node n2 = b2.node(i);
            assertNodesEqual( n1, n2 );
        }
    }
    
    protected void assertNodesEqual( Node n1, Node n2 ) {
        int nodeType1 = n1.getNodeType();
        int nodeType2 = n2.getNodeType();
        assert( "Nodes are of same type: ", nodeType1 == nodeType2 );
        
        switch (nodeType1) {
            case Node.ELEMENT_NODE:
                assertNodesEqual((Element) n1, (Element) n2);
                break;
            case Node.DOCUMENT_NODE:
                assertNodesEqual((Document) n1, (Document) n2);
                break;
            case Node.ATTRIBUTE_NODE:
                assertNodesEqual((Attribute) n1, (Attribute) n2);
                break;
            case Node.TEXT_NODE:
                assertNodesEqual((Text) n1, (Text) n2);
                break;
            case Node.CDATA_SECTION_NODE:
                assertNodesEqual((CDATA) n1, (CDATA) n2);
                break;
            case Node.ENTITY_REFERENCE_NODE:
                assertNodesEqual((Entity) n1, (Entity) n2);
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                assertNodesEqual((ProcessingInstruction) n1, (ProcessingInstruction) n2);
                break;
            case Node.COMMENT_NODE:
                assertNodesEqual((Comment) n1, (Comment) n2);
                break;
            case Node.DOCUMENT_TYPE_NODE:
                assertNodesEqual((DocumentType) n1, (DocumentType) n2);
                break;
            case Node.NAMESPACE_NODE:
                assertNodesEqual((Namespace) n1, (Namespace) n2);
                break;
            default:
                assert( "Invalid node types. node1: " + n1 + " and node2: " + n2, false );
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
