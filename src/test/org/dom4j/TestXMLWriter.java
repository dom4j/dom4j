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
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.DefaultDocument;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/** A simple test harness to check that the XML Writer works
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TestXMLWriter extends AbstractTestCase {

    protected static final boolean VERBOSE = false;
    
    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestXMLWriter.class );
    }
    
    public TestXMLWriter(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testWriter() throws Exception {
        Object object = document;
        StringWriter out = new StringWriter();
        
        XMLWriter writer = new XMLWriter( out );
        writer.write( object );
        writer.close();
        
        String text = out.toString();
            
        if ( VERBOSE ) {
            log( "Text output is ["  );
            log( text );
            log( "]. Done" );
        }
        
        assertTrue( "Output text is bigger than 10 characters", text.length() > 10 );
    }        
    
    public void testEncodingFormats() throws Exception {
        testEncoding( "UTF-8" );
        testEncoding( "UTF-16" );
        testEncoding( "ISO-8859-1" );
    }

    protected void testEncoding(String encoding) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding( encoding );
        XMLWriter writer = new XMLWriter( out, format );
        writer.write( document );
        writer.close();
        
        log( "Wrote to encoding: " + encoding );
    }
    
    public void testWriterBug() throws Exception {        
        Element project = new BaseElement("project"); 
        Document doc = new DefaultDocument(project); 

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLWriter writer = new XMLWriter(out, new OutputFormat("\t", true, "ISO-8859-1")); 
        writer.write(doc); 
        
        ByteArrayInputStream in = new ByteArrayInputStream( out.toByteArray() );
        SAXReader reader = new SAXReader();
        Document doc2 = reader.read( in );
        
        assertTrue( "Generated document has a root element", doc2.getRootElement() != null );
        assertEquals( "Generated document has corrent named root element", doc2.getRootElement().getName(), "project" );
    }
    
    public void testNamespaceBug() throws Exception {        
        Document doc = DocumentHelper.createDocument();
        
        Element root = doc.addElement("root","ns1");
        Element child1 = root.addElement("joe","ns2");
        child1.addElement("zot","ns1");
        
        StringWriter out = new StringWriter();
        XMLWriter writer = new XMLWriter(
            out,
            OutputFormat.createPrettyPrint() 
        );
        writer.write(doc);
        String text = out.toString();
        
        //System.out.println( "Generated:" + text );
        
        Document doc2 = DocumentHelper.parseText( text );
        root = doc2.getRootElement();
        assertEquals( "root has correct namespace", "ns1", root.getNamespaceURI() );
        
        Element joe = (Element) root.elementIterator().next();
        assertEquals( "joe has correct namespace", "ns2", joe.getNamespaceURI() );
        
        Element zot = (Element) joe.elementIterator().next();
        assertEquals( "zot has correct namespace", "ns1", zot.getNamespaceURI() );
    }
    
    /** This test harness was supplied by Lari Hotari */
    public void testContentHandler() throws Exception {
        StringWriter out = new StringWriter();
    OutputFormat format = OutputFormat.createPrettyPrint();
    format.setEncoding("iso-8859-1");
    XMLWriter writer = new XMLWriter(out, format);
        generateXML(writer);
    writer.close();
    String text = out.toString();

        if ( VERBOSE ) {
            log( "Created XML" );
            log( text );
        }
        
        // now lets parse the output and test it with XPath
        Document doc = DocumentHelper.parseText( text );
        String value = doc.valueOf( "/processes[@name='arvojoo']" );
        assertEquals( "Document contains the correct text", "jeejee", value );
    }

    /** This test was provided by Manfred Lotz */
    public void testWhitespaceBug() throws Exception {
        Document doc = DocumentHelper.parseText(
            "<notes> This is a      multiline\n\rentry</notes>"
        );
        
        OutputFormat format = new OutputFormat();
        format.setEncoding("UTF-8");
        format.setIndentSize(4);
        format.setNewlines(true);
        format.setTrimText(true);
        format.setExpandEmptyElements(true);
        
        StringWriter buffer = new StringWriter();
        XMLWriter writer = new XMLWriter(buffer, format);
        writer.write( doc );
        
        String xml = buffer.toString();
        log( xml );
        
        Document doc2 = DocumentHelper.parseText( xml );
        String text = doc2.valueOf( "/notes" );
        String expected = "This is a multiline entry";
        
        assertEquals( "valueOf() returns the correct text padding", expected, text );
        
        assertEquals( "getText() returns the correct text padding", expected, doc2.getRootElement().getText() );
    }
    
    /** This test was provided by Manfred Lotz */
    public void testWhitespaceBug2() throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement( "root" );
        Element meaning = root.addElement( "meaning" );
        meaning.addText( "to li" );
        meaning.addText( "ve" );
        
        OutputFormat format = new OutputFormat();
        format.setEncoding("UTF-8");
        format.setIndentSize(4);
        format.setNewlines(true);
        format.setTrimText(true);
        format.setExpandEmptyElements(true);
        
        StringWriter buffer = new StringWriter();
        XMLWriter writer = new XMLWriter(buffer, format);
        writer.write( doc );
        
        String xml = buffer.toString();
        log( xml );
        
        Document doc2 = DocumentHelper.parseText( xml );
        String text = doc2.valueOf( "/root/meaning" );
        String expected = "to live";
        
        assertEquals( "valueOf() returns the correct text padding", expected, text );
        
        assertEquals( "getText() returns the correct text padding", expected, doc2.getRootElement().element("meaning").getText() );
    }
    
    /*
     * This must be tested manually to see if the layout is correct.
     */
    public void testPrettyPrinting() throws Exception {
        Document doc = DocumentFactory.getInstance().createDocument(); 
        doc.addElement("summary").addAttribute("date", "6/7/8").addElement("orderline").addText("puffins").addElement("ranjit").addComment("Ranjit is a happy Puffin"); 
        XMLWriter writer = new XMLWriter(System.out, OutputFormat.createPrettyPrint()); 
        writer.write(doc);

        doc = DocumentFactory.getInstance().createDocument(); 
        doc.addElement("summary").addAttribute("date", "6/7/8").addElement("orderline").addText("puffins").addElement("ranjit").addComment("Ranjit is a happy Puffin").addComment("another comment").addElement("anotherElement"); 
        writer.write(doc);
    }

    
    protected void generateXML(ContentHandler handler) throws SAXException {
    handler.startDocument();
    AttributesImpl attrs = new AttributesImpl();
    attrs.clear();
    attrs.addAttribute("","","name","CDATA", "arvojoo");
    handler.startElement("","","processes",attrs);
    String text="jeejee";
    char textch[] = text.toCharArray();
    handler.characters(textch,0,textch.length);
    handler.endElement("","","processes" );
    handler.endDocument();
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
