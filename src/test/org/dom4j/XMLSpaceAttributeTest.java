/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import junit.textui.TestRunner;

import java.io.IOException;

/**
 * A test harness to test the xml:space attribute for preserve. If it is
 * preserve, then keep whitespace.
 * 
 * @author <a href="mailto:ddlucas@lse.com">David Lucas </a>
 * @version $Revision$
 */
public class XMLSpaceAttributeTest extends AbstractTestCase {
    public static void main(String[] args) {
        TestRunner.run(XMLSpaceAttributeTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testWithTextTrimOn() throws Exception {
        String xmlString = "<top >"
                + "<row><col>   This is a test!</col></row>"
                + "<row><col xml:space=\'preserve\' >   This is a test!</col>"
                + "</row><row><col>   This is a test!</col></row>" + "</top>";
        Document doc1 = DocumentHelper.parseText(xmlString);
        Element c2 = (Element) doc1.selectSingleNode("/top/row[2]/col");
        String expected = "   New Text TrimOn! ";
        c2.setText(expected);

        String xml = rewriteToXmlString(doc1, true);

        Document doc2 = DocumentHelper.parseText(xml);
        Element c4 = (Element) doc2.selectSingleNode("/top/row[2]/col");
        String actual = c4.getText();

        assertEquals("compared element text expecting whitespace", expected,
                actual);

        expected = expected.trim();
        actual = c4.getTextTrim();
        assertEquals("compared element getTextTrim", expected, actual);

        expected = "This is a test!";

        Element c5 = (Element) doc2.selectSingleNode("/top/row[3]/col");
        actual = c5.getText();
        assertEquals("compared element text expecting trimmed whitespace",
                expected, actual);
    }

    // -------------------------------------------------------------------------
    public void testWithTextTrimOff() throws Exception {
        String xmlString = "<top >"
                + "<row><col>   This is a test!</col></row>"
                + "<row><col xml:space=\'preserve\' >   This is a test!</col>"
                + "</row><row><col>   This is a test!</col></row>" + "</top>";
        Document doc1 = DocumentHelper.parseText(xmlString);
        Element c2 = (Element) doc1.selectSingleNode("/top/row[2]/col");
        String expected = "   New Text TrimOff! ";
        c2.setText(expected);

        String xml = rewriteToXmlString(doc1, false);

        Document doc2 = DocumentHelper.parseText(xml);
        Element c4 = (Element) doc2.selectSingleNode("/top/row[2]/col");
        String actual = c4.getText();

        assertEquals("compared element text expecting whitespace", expected,
                actual);
    }

    // -------------------------------------------------------------------------
    public void testWithTextTrimOnFollow() throws Exception {
        String xmlString = "<top >"
                + "<row><col>   This is a test!</col></row>" + "<row>"
                + "<col xml:space=\'preserve\' >"
                + "<a><b>   This is embedded!</b></a>"
                + "<a><b>   This is space=preserve too!</b></a>" + "</col>"
                + "</row>" + "<row><col>   This is a test!</col></row>"
                + "</top>";
        Document doc1 = DocumentHelper.parseText(xmlString);
        Element c2 = (Element) doc1.selectSingleNode("/top/row[2]/col/a[1]/b");
        String expected = "   New Text TrimOnFollow! ";
        c2.setText(expected);

        String xml = rewriteToXmlString(doc1, true);

        Document doc2 = DocumentHelper.parseText(xml);

        Element c4 = (Element) doc2.selectSingleNode("/top/row[2]/col/a[1]/b");
        String actual = c4.getText();

        assertEquals("compared element text expecting whitespace", expected,
                actual);

        Element c8 = (Element) doc2.selectSingleNode("/top/row[2]/col/a[2]/b");

        expected = "   This is space=preserve too!";
        actual = c8.getText();
        assertEquals("compared element text follow trimmed whitespace",
                expected, actual);

        expected = expected.trim();
        actual = c8.getTextTrim();
        assertEquals("compared element getTextTrim", expected, actual);

        Element c12 = (Element) doc2.selectSingleNode("/top/row[3]/col");

        expected = "This is a test!";
        actual = c12.getText();
        assertEquals("compared element text follow trimmed whitespace",
                expected, actual);
    }

    // -------------------------------------------------------------------------
    public void testWithTextTrimOnNested() throws Exception {
        String xmlString = "<top >"
                + "<row><col>   This is a test!</col></row>" + "<row>"
                + "<col xml:space='preserve' >" + "<a>"
                + "<b>   This is embedded! </b>"
                + "<b xml:space='default' >   This should do global default! "
                + "</b><b>   This is embedded! </b>" + "</a>" + "</col>"
                + "</row>" + "<row><col>   This is a test!</col></row>"
                + "</top>";
        Document doc1 = DocumentHelper.parseText(xmlString);
        Element c2 = (Element) doc1.selectSingleNode("/top/row[2]/col/a[1]/b");
        String expected = "   New Text TrimOnNested! ";
        c2.setText(expected);

        String xml = rewriteToXmlString(doc1, true);

        Document doc2 = DocumentHelper.parseText(xml);

        Element c4 = (Element) doc2
                .selectSingleNode("/top/row[2]/col/a[1]/b[1]");
        String actual = c4.getText();
        assertEquals("compared element text expecting whitespace", expected,
                actual);

        Element c8 = (Element) doc2
                .selectSingleNode("/top/row[2]/col/a[1]/b[2]");
        expected = "This should do global default!";
        actual = c8.getText();
        assertEquals("compared element text nested trimmed whitespace",
                expected, actual);

        Element c12 = (Element) doc2
                .selectSingleNode("/top/row[2]/col/a[1]/b[3]");
        expected = "   This is embedded! ";
        actual = c12.getText();
        assertEquals("compared element text nested preserved whitespace",
                expected, actual);
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    private String rewriteToXmlString(Document doc, boolean trimOn)
            throws IOException {
        org.dom4j.io.OutputFormat of = org.dom4j.io.OutputFormat
                .createCompactFormat();
        of.setIndent(true);
        of.setNewlines(true);
        of.setExpandEmptyElements(false);
        of.setSuppressDeclaration(false);
        of.setOmitEncoding(false);
        of.setEncoding("UTF-8");
        of.setTrimText(trimOn);

        java.io.ByteArrayOutputStream os = new java.io.ByteArrayOutputStream();
        java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(os);
        org.dom4j.io.XMLWriter xmlWriter = new org.dom4j.io.XMLWriter(of);

        xmlWriter.setOutputStream(bos);
        xmlWriter.write(doc);
        xmlWriter.close();

        String xml = os.toString();

        // System.out.println("***** xml out *****\n"+xml);
        return xml;
    }

    // -------------------------------------------------------------------------
    public void testWithEscapeTextTrimOn() throws Exception {
        String xmlString = "<top >"
                + "<row><col>   This is a test!</col></row>"
                + "<row><col xml:space=\'preserve\' >   This is a test!\r\n"
                + "With a new line, special character like &amp; , and\t tab."
                + "</col></row><row><col>   This is a test!\r\nWith a new line,"
                + " special character like &amp; , and\t tab.</col></row>"
                + "</top>";
        Document doc1 = DocumentHelper.parseText(xmlString);
        String xml = rewriteToXmlString(doc1, true);
        Document doc2 = DocumentHelper.parseText(xml);

        Element c2 = (Element) doc2.selectSingleNode("/top/row[2]/col");
        String expected = "   This is a test!\nWith a new line, special "
                + "character like & , and\t tab.";
        String actual = c2.getText();
        assertEquals("compared element text expecting whitespace", expected,
                actual);

        Element c4 = (Element) doc2.selectSingleNode("/top/row[3]/col");
        expected = "This is a test! With a new line, special character "
                + "like & , and tab.";
        actual = c4.getText();
        assertEquals("compared element text expecting whitespace", expected,
                actual);
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
