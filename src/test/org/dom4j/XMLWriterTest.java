/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import junit.textui.TestRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.BaseElement;
import org.dom4j.tree.DefaultDocument;

import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * A simple test harness to check that the XML Writer works
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class XMLWriterTest extends AbstractTestCase {
    protected static final boolean VERBOSE = false;

    public static void main(String[] args) {
        TestRunner.run(XMLWriterTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testBug1180791() throws Exception {
        String xml = "<?xml version=\"1.0\"?><root><foo>bar</foo></root>";

        SAXReader reader = new SAXReader();
        Document doc = reader.read(new StringReader(xml));
        // of with newlines
        OutputFormat format = new OutputFormat();
        format.setNewlines(true);
        //format.setTrimText(true);
        // first time
        StringWriter writer = new StringWriter();
        XMLWriter xmlwriter = new XMLWriter(writer, format);
        xmlwriter.write(doc);
        System.out.println(writer.toString());

        // 2nd time
        doc = reader.read(new StringReader(writer.toString()));
        writer = new StringWriter();
        xmlwriter = new XMLWriter(writer, format);
        xmlwriter.write(doc);
        System.out.println(writer.toString());
    }

    public void testBug1119733() throws Exception {
        Document doc = DocumentHelper
                .parseText("<root><code>foo</code> bar</root>");

        StringWriter out = new StringWriter();
        XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
        writer.write(doc);
        writer.close();

        String xml = out.toString();

        System.out.println(xml);
        assertEquals("whitespace problem", -1, xml.indexOf("</code>bar"));
    }

    public void testBug1119733WithSAXEvents() throws Exception {
        StringWriter out = new StringWriter();
        XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
        writer.startDocument();
        writer.startElement(null, "root", "root", new AttributesImpl());
        writer.startElement(null, "code", "code", new AttributesImpl());
        writer.characters(new char[] { 'f', 'o', 'o' }, 0, 3);
        writer.endElement(null, "code", "code");
        writer.characters(new char[] { ' ', 'b', 'a', 'r' }, 0, 4);
        writer.endElement(null, "root", "root");
        writer.endDocument();
        writer.close();

        String xml = out.toString();

        System.out.println(xml);
        assertEquals("whitespace problem", -1, xml.indexOf("</code>bar"));
    }

    public void testWriter() throws Exception {
        Object object = document;
        StringWriter out = new StringWriter();

        XMLWriter writer = new XMLWriter(out);
        writer.write(object);
        writer.close();

        String text = out.toString();

        if (VERBOSE) {
            log("Text output is [");
            log(text);
            log("]. Done");
        }

        assertTrue("Output text is bigger than 10 characters",
                text.length() > 10);
    }

    public void testEncodingFormats() throws Exception {
        testEncoding("UTF-8");
        testEncoding("UTF-16");
        testEncoding("ISO-8859-1");
    }

    public void testWritingEmptyElement() throws Exception {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element grandFather = doc.addElement("grandfather");
        Element parent1 = grandFather.addElement("parent");
        Element child1 = parent1.addElement("child1");
        Element child2 = parent1.addElement("child2");
        child2.setText("test");

        Element parent2 = grandFather.addElement("parent");
        Element child3 = parent2.addElement("child3");
        child3.setText("test");

        StringWriter buffer = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(buffer, format);
        writer.write(doc);

        String xml = buffer.toString();

        System.out.println(xml);

        assertTrue("child2 not present",
                xml.indexOf("<child2>test</child2>") != -1);
    }

    protected void testEncoding(String encoding) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding(encoding);

        XMLWriter writer = new XMLWriter(out, format);
        writer.write(document);
        writer.close();

        log("Wrote to encoding: " + encoding);
    }

    public void testWriterBug() throws Exception {
        Element project = new BaseElement("project");
        Document doc = new DefaultDocument(project);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        XMLWriter writer = new XMLWriter(out, new OutputFormat("\t", true,
                "ISO-8859-1"));
        writer.write(doc);

        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
        SAXReader reader = new SAXReader();
        Document doc2 = reader.read(in);

        assertTrue("Generated document has a root element", doc2
                .getRootElement() != null);
        assertEquals("Generated document has corrent named root element", doc2
                .getRootElement().getName(), "project");
    }

    public void testNamespaceBug() throws Exception {
        Document doc = DocumentHelper.createDocument();

        Element root = doc.addElement("root", "ns1");
        Element child1 = root.addElement("joe", "ns2");
        child1.addElement("zot", "ns1");

        StringWriter out = new StringWriter();
        XMLWriter writer = new XMLWriter(out, OutputFormat.createPrettyPrint());
        writer.write(doc);

        String text = out.toString();

        // System.out.println( "Generated:" + text );
        Document doc2 = DocumentHelper.parseText(text);
        root = doc2.getRootElement();
        assertEquals("root has incorrect namespace", "ns1", root
                .getNamespaceURI());

        Element joe = (Element) root.elementIterator().next();
        assertEquals("joe has correct namespace", "ns2", joe.getNamespaceURI());

        Element zot = (Element) joe.elementIterator().next();
        assertEquals("zot has correct namespace", "ns1", zot.getNamespaceURI());
    }

    /**
     * This test harness was supplied by Lari Hotari
     * 
     * @throws Exception DOCUMENT ME!
     */
    public void testContentHandler() throws Exception {
        StringWriter out = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("iso-8859-1");

        XMLWriter writer = new XMLWriter(out, format);
        generateXML(writer);
        writer.close();

        String text = out.toString();

        if (VERBOSE) {
            log("Created XML");
            log(text);
        }

        // now lets parse the output and test it with XPath
        Document doc = DocumentHelper.parseText(text);
        String value = doc.valueOf("/processes[@name='arvojoo']");
        assertEquals("Document contains the correct text", "jeejee", value);
    }

    /**
     * This test was provided by Manfred Lotz
     * 
     * @throws Exception DOCUMENT ME!
     */
    public void testWhitespaceBug() throws Exception {
        String notes = "<notes> This is a      multiline\n\rentry</notes>";
        Document doc = DocumentHelper.parseText(notes);

        OutputFormat format = new OutputFormat();
        format.setEncoding("UTF-8");
        format.setIndentSize(4);
        format.setNewlines(true);
        format.setTrimText(true);
        format.setExpandEmptyElements(true);

        StringWriter buffer = new StringWriter();
        XMLWriter writer = new XMLWriter(buffer, format);
        writer.write(doc);

        String xml = buffer.toString();
        log(xml);

        Document doc2 = DocumentHelper.parseText(xml);
        String text = doc2.valueOf("/notes");
        String expected = "This is a multiline entry";

        assertEquals("valueOf() returns the correct text padding", expected,
                text);

        assertEquals("getText() returns the correct text padding", expected,
                doc2.getRootElement().getText());
    }

    /**
     * This test was provided by Manfred Lotz
     * 
     * @throws Exception DOCUMENT ME!
     */
    public void testWhitespaceBug2() throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("root");
        Element meaning = root.addElement("meaning");
        meaning.addText("to li");
        meaning.addText("ve");

        OutputFormat format = new OutputFormat();
        format.setEncoding("UTF-8");
        format.setIndentSize(4);
        format.setNewlines(true);
        format.setTrimText(true);
        format.setExpandEmptyElements(true);

        StringWriter buffer = new StringWriter();
        XMLWriter writer = new XMLWriter(buffer, format);
        writer.write(doc);

        String xml = buffer.toString();
        log(xml);

        Document doc2 = DocumentHelper.parseText(xml);
        String text = doc2.valueOf("/root/meaning");
        String expected = "to live";

        assertEquals("valueOf() returns the correct text padding", expected,
                text);

        assertEquals("getText() returns the correct text padding", expected,
                doc2.getRootElement().element("meaning").getText());
    }

    public void testPadding() throws Exception {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element root = doc.addElement("root");
        root.addText("prefix    ");
        root.addElement("b");
        root.addText("      suffix");

        OutputFormat format = new OutputFormat("", false);
        format.setOmitEncoding(true);
        format.setSuppressDeclaration(true);
        format.setExpandEmptyElements(true);
        format.setPadText(true);
        format.setTrimText(true);

        StringWriter buffer = new StringWriter();
        XMLWriter writer = new XMLWriter(buffer, format);
        writer.write(doc);

        String xml = buffer.toString();

        System.out.println("xml: " + xml);

        String expected = "<root>prefix <b></b> suffix</root>";
        assertEquals(expected, xml);
    }

    public void testPadding2() throws Exception {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element root = doc.addElement("root");
        root.addText("prefix");
        root.addElement("b");
        root.addText("suffix");

        OutputFormat format = new OutputFormat("", false);
        format.setOmitEncoding(true);
        format.setSuppressDeclaration(true);
        format.setExpandEmptyElements(true);
        format.setPadText(true);
        format.setTrimText(true);

        StringWriter buffer = new StringWriter();
        XMLWriter writer = new XMLWriter(buffer, format);
        writer.write(doc);

        String xml = buffer.toString();

        System.out.println("xml: " + xml);

        String expected = "<root>prefix<b></b>suffix</root>";
        assertEquals(expected, xml);
    }

    /*
     * This must be tested manually to see if the layout is correct.
     */
    public void testPrettyPrinting() throws Exception {
        Document doc = DocumentFactory.getInstance().createDocument();
        doc.addElement("summary").addAttribute("date", "6/7/8").addElement(
                "orderline").addText("puffins").addElement("ranjit")
                .addComment("Ranjit is a happy Puffin");

        XMLWriter writer = new XMLWriter(System.out, OutputFormat
                .createPrettyPrint());
        writer.write(doc);

        doc = DocumentFactory.getInstance().createDocument();
        doc.addElement("summary").addAttribute("date", "6/7/8").addElement(
                "orderline").addText("puffins").addElement("ranjit")
                .addComment("Ranjit is a happy Puffin").addComment(
                        "another comment").addElement("anotherElement");
        writer.write(doc);
    }

    public void testAttributeQuotes() throws Exception {
        Document doc = DocumentFactory.getInstance().createDocument();
        doc.addElement("root").addAttribute("test", "text with ' in it");

        StringWriter out = new StringWriter();
        XMLWriter writer = new XMLWriter(out, OutputFormat
                .createCompactFormat());
        writer.write(doc);

        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<root test=\"text with ' in it\"/>";
        assertEquals(expected, out.toString());
    }

    public void testBug868408() throws Exception {
        Document doc = getDocument("/xml/web.xml");
        Document doc2 = DocumentHelper.parseText(doc.asXML());
        assertEquals(doc.asXML(), doc2.asXML());
    }

    public void testBug923882() throws Exception {
        Document doc = DocumentFactory.getInstance().createDocument();
        Element root = doc.addElement("root");
        root.addText("this is ");
        root.addText(" sim");
        root.addText("ple text ");
        root.addElement("child");
        root.addText(" contai");
        root.addText("ning spaces and");
        root.addText(" multiple textnodes");

        OutputFormat format = new OutputFormat();
        format.setEncoding("UTF-8");
        format.setIndentSize(4);
        format.setNewlines(true);
        format.setTrimText(true);
        format.setExpandEmptyElements(true);

        StringWriter buffer = new StringWriter();
        XMLWriter writer = new XMLWriter(buffer, format);
        writer.write(doc);

        String xml = buffer.toString();
        log(xml);

        int start = xml.indexOf("<root");
        int end = xml.indexOf("/root>") + 6;
        String eol = "\n"; // System.getProperty("line.separator");
        String expected = "<root>this is simple text" + eol
                + "    <child></child>containing spaces and multiple textnodes"
                + eol + "</root>";
        System.out.println("Expected:");
        System.out.println(expected);
        System.out.println("Obtained:");
        System.out.println(xml.substring(start, end));
        assertEquals(expected, xml.substring(start, end));
    }

    public void testEscapeXML() throws Exception {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        OutputFormat format = new OutputFormat(null, false, "ISO-8859-2");
        format.setSuppressDeclaration(true);

        XMLWriter writer = new XMLWriter(os, format);

        Document document = DocumentFactory.getInstance().createDocument();
        Element root = document.addElement("root");
        root.setText("bla &#c bla");

        writer.write(document);

        String result = os.toString();
        System.out.println(result);

        Document doc2 = DocumentHelper.parseText(result);
        doc2.normalize(); // merges adjacant Text nodes
        System.out.println(doc2.getRootElement().getText());
        assertNodesEqual(document, doc2);
    }

    public void testWriteEntities() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n"
                + "<!DOCTYPE xml [<!ENTITY copy \"&#169;\"> "
                + "<!ENTITY trade \"&#8482;\"> "
                + "<!ENTITY deg \"&#x00b0;\"> " + "<!ENTITY gt \"&#62;\"> "
                + "<!ENTITY sup2 \"&#x00b2;\"> "
                + "<!ENTITY frac14 \"&#x00bc;\"> "
                + "<!ENTITY quot \"&#34;\"> "
                + "<!ENTITY frac12 \"&#x00bd;\"> "
                + "<!ENTITY euro \"&#x20ac;\"> "
                + "<!ENTITY Omega \"&#937;\"> ]>\n" + "<root />";

        SAXReader reader = new SAXReader("org.apache.xerces.parsers.SAXParser");
        reader.setIncludeInternalDTDDeclarations(true);

        Document doc = reader.read(new StringReader(xml));
        StringWriter wr = new StringWriter();
        XMLWriter writer = new XMLWriter(wr);
        writer.write(doc);

        String xml2 = wr.toString();
        System.out.println(xml2);

        Document doc2 = DocumentHelper.parseText(xml2);

        assertNodesEqual(doc, doc2);
    }

    public void testEscapeChars() throws Exception {
        Document document = DocumentFactory.getInstance().createDocument();
        Element root = document.addElement("root");
        root.setText("blahblah " + '\u008f');

        XMLWriter writer = new XMLWriter();
        StringWriter strWriter = new StringWriter();
        writer.setWriter(strWriter);
        writer.setMaximumAllowedCharacter(127);
        writer.write(document);

        String xml = strWriter.toString();
    }

    public void testEscapeText() throws SAXException {
        StringWriter writer = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(writer);
        xmlWriter.setEscapeText(false);

        String txt = "<test></test>";

        xmlWriter.startDocument();
        xmlWriter.characters(txt.toCharArray(), 0, txt.length());
        xmlWriter.endDocument();

        String output = writer.toString();
        System.out.println(output);
        assertTrue(output.indexOf("<test>") != -1);
    }

    public void testNullCData() {
        Element e = DocumentHelper.createElement("test");
        e.add(DocumentHelper.createElement("another").addCDATA(null));

        Document doc = DocumentHelper.createDocument(e);

        assertEquals(-1, e.asXML().indexOf("null"));
        assertEquals(-1, doc.asXML().indexOf("null"));

        System.out.println(e.asXML());
        System.out.println(doc.asXML());
    }

    protected void generateXML(ContentHandler handler) throws SAXException {
        handler.startDocument();

        AttributesImpl attrs = new AttributesImpl();
        attrs.clear();
        attrs.addAttribute("", "", "name", "CDATA", "arvojoo");
        handler.startElement("", "", "processes", attrs);

        String text = "jeejee";
        char[] textch = text.toCharArray();
        handler.characters(textch, 0, textch.length);
        handler.endElement("", "", "processes");
        handler.endDocument();
    }
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
