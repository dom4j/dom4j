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
import java.net.URL;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.io.DOMReader;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.SAXContentHandler;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;
import org.dom4j.io.XMLWriter;
import org.w3c.tidy.Tidy;

/** A test harness to test the the round trips of Documents.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class TestRoundTrip extends AbstractTestCase {
    
    protected String[] testDocuments = {
        //"xml/test/encode.xml",
        "xml/fibo.xml",
        "xml/test/schema/personal-prefix.xsd",
        //"xml/test/soap2.xml",
        "xml/test/test_schema.xml",
    };
    
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
        for ( int i = 0, size = testDocuments.length; i < size; i++ ) {
            Document doc = parseDocument( testDocuments[i] );
            roundTripText( doc );
        }
    }
    
    public void testSAXRoundTrip() throws Exception {
        for ( int i = 0, size = testDocuments.length; i < size; i++ ) {
            Document doc = parseDocument( testDocuments[i] );
            roundTripSAX( doc );
        }
    }
    
    public void testDOMRoundTrip() throws Exception {
        for ( int i = 0, size = testDocuments.length; i < size; i++ ) {
            Document doc = parseDocument( testDocuments[i] );
            roundTripDOM( doc );
        }
    }
    
    public void testJAXPRoundTrip() throws Exception {
        for ( int i = 0, size = testDocuments.length; i < size; i++ ) {
            Document doc = parseDocument( testDocuments[i] );
            roundTripJAXP( doc );
        }
    }
    
    public void testFullRoundTrip() throws Exception {        
        for ( int i = 0, size = testDocuments.length; i < size; i++ ) {
            Document doc = parseDocument( testDocuments[i] );
            roundTripFull( doc );
        }
    }

    public void testJTidyRoundTrip() throws Exception {
        Document document = loadHTML( "readme.html" );
  
        //Document doc1 = roundTripText( document );
        Document doc1 = roundTripSAX( document );
        Document doc2 = roundTripDOM( doc1);
        Document doc3 = roundTripSAX( doc2 );
        //Document doc4 = roundTripText( doc3 );
        //Document doc5 = roundTripDOM( doc4 );
        Document doc5 = roundTripDOM( doc3 );
        
        assertDocumentsEqual( document, doc5 );
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void setUp() throws Exception {
    }

    protected Document parseDocument(String file) throws Exception {
        SAXReader reader = new SAXReader();
        return reader.read( file );
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
    
    protected Document roundTripJAXP(Document document) throws Exception {
        // output the document to a text buffer via JAXP
        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();

        StringWriter buffer = new StringWriter();        
        StreamResult streamResult = new StreamResult(buffer);
        DocumentSource documentSource = new DocumentSource(document);

        transformer.transform(documentSource, streamResult);

        // now lets parse it back again via JAXP
        DocumentResult documentResult = new DocumentResult();
        StreamSource streamSource = new StreamSource( new StringReader( buffer.toString() ) );

        transformer.transform(streamSource, documentResult);

        Document newDocument = documentResult.getDocument();
            
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
    
    protected Document roundTripFull(Document document) throws Exception {
        Document doc2 = roundTripDOM( document );
        Document doc3 = roundTripSAX( doc2 );
        Document doc4 = roundTripText( doc3 );

        assertDocumentsEqual( document, doc4 );
        
        return doc4;
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
