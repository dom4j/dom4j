/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import junit.textui.TestRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

import org.dom4j.AbstractTestCase;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * A test harness to test the content API in DOM4J
 * 
 * @author <a href="mailto:maartenc@sourceforge.net">Maarten Coene </a>
 */
public class SAXReaderTest extends AbstractTestCase {
    public static void main(String[] args) {
        TestRunner.run(SAXReaderTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------

    /**
     * Test bug reported by Christian Oetterli http://tinyurl.com/6po8v
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testReadFile() throws Exception {
        File file = getFile("/xml/#.xml");
        new SAXReader().read(file);
    }
    
    public void testEncoding() throws Exception {
        String xml = "<?xml version='1.0' encoding='ISO-8859-1'?><root/>";
        SAXReader reader = new SAXReader();
        reader.setEncoding("ISO-8859-1");
        Document doc = reader.read(new StringReader(xml));
        
        assertEquals("encoding incorrect", "ISO-8859-1", doc.getXMLEncoding());
    }

    public void testRussian() throws Exception {
        Document doc = getDocument("/xml/russArticle.xml");

        assertEquals("encoding not correct", "koi8-r", doc.getXMLEncoding());

        Element el = doc.getRootElement();

        StringWriter writer = new StringWriter();
        XMLWriter xmlWriter = new XMLWriter(writer);
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("koi8-r");
        xmlWriter.write(doc);
        log(writer.toString());
    }

    public void testRussian2() throws Exception {
        Document doc = getDocument("/xml/russArticle.xml");
        XMLWriter xmlWriter = new XMLWriter(new OutputFormat("", false,
                "koi8-r"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        xmlWriter.setOutputStream(out);
        xmlWriter.write(doc);
        xmlWriter.flush();
        xmlWriter.close();
        log(out.toString());
    }

    public void testBug833765() throws Exception {
        SAXReader reader = new SAXReader();
        reader.setIncludeExternalDTDDeclarations(true);
        getDocument("/xml/dtd/external.xml", reader);
    }

    public void testBug527062() throws Exception {
        Document doc = getDocument("/xml/test/test.xml");
        List l = doc.selectNodes("//broked/junk");

        for (int i = 0; i < l.size(); i++) {
            System.out.println("Found node: "
                    + ((Element) l.get(i)).getStringValue());
        }

        assertEquals("hi there", ((Element) l.get(0)).getStringValue());
        assertEquals("hello world", ((Element) l.get(1)).getStringValue());
    }

    public void testEscapedComment() throws Exception {
        String txt = "<eg>&lt;!-- &lt;head> &amp; &lt;body> --&gt;</eg>";
        Document doc = DocumentHelper.parseText(txt);
        Element eg = doc.getRootElement();
        System.out.println(doc.asXML());
        assertEquals("<!-- <head> & <body> -->", eg.getText());
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
