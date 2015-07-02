/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import junit.textui.TestRunner;

import java.io.File;
import java.io.FileReader;
import java.io.StringWriter;

import javax.xml.stream.XMLInputFactory;

import org.dom4j.AbstractTestCase;
import org.dom4j.Document;

/**
 * Tests STAX->DOM4J functionality.
 * 
 * @author <a href="mailto:maartenc@sourceforge.net">Maarten Coene </a>
 * @author Christian Niles
 */
public class StaxTest extends AbstractTestCase {
    public static void main(String[] args) {
        TestRunner.run(StaxTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------

    /**
     * Tests that the encoding specified in the XML declaration is exposed in
     * the Document read via StAX, and also that it gets output when writing.
     */
    public void testEncoding() {
        /*
         * only execute if a reference implementation is available
         */
        try {
            XMLInputFactory.newInstance();
        } catch (javax.xml.stream.FactoryConfigurationError e) {
            // no implementation found, stop the test.
            return;
        }

        try {
            File file = getFile("/xml/russArticle.xml");
            STAXEventReader xmlReader = new STAXEventReader();
            Document doc = xmlReader.readDocument(new FileReader(file));

            assertEquals("russArticle.xml encoding wasn't correct", "koi8-r",
                    doc.getXMLEncoding());

            StringWriter writer = new StringWriter();
            STAXEventWriter xmlWriter = new STAXEventWriter(writer);
            xmlWriter.writeDocument(doc);

            String output = writer.toString();
            String xmlDecl = output.substring(0, output.indexOf("?>") + 2);
            String expected = "<?xml version=\'1.0\' encoding=\'koi8-r\'?>";
            assertEquals("Unexpected xml declaration", expected, xmlDecl);
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
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
