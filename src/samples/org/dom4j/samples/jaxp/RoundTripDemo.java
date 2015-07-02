/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: RoundTripDemo.java,v 1.4 2005/01/29 14:53:13 maartenc Exp $
 */

package org.dom4j.samples.jaxp;

import org.dom4j.samples.SAXDemo;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.XMLWriter;

/**
 * A program demonstrating a round trip from XML to dom4j to text to dom4j again
 * using JAXP to convert the XML.
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class RoundTripDemo extends SAXDemo {

    public static void main(String[] args) {
        run(new RoundTripDemo(), args);
    }

    public RoundTripDemo() {
    }

    protected void outputDocument(Document document, Writer out) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();

            Transformer transformer = factory.newTransformer();

            StreamResult result = new StreamResult(out);
            DocumentSource source = new DocumentSource(document);

            transformer.transform(source, result);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    protected Document parseDocument(Reader in) {
        try {
            TransformerFactory factory = TransformerFactory.newInstance();

            Transformer transformer = factory.newTransformer();

            DocumentResult result = new DocumentResult();
            StreamSource source = new StreamSource(in);

            transformer.transform(source, result);

            return result.getDocument();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    /** Outputs the document to a buffer, parse it back again then output it */
    protected void process(Document document) throws Exception {

        System.out.println("about to output: " + document);

        StringWriter out = new StringWriter();
        outputDocument(document, out);

        Document doc2 = parseDocument(new StringReader(out.toString()));

        System.out.println("parsed back again: " + doc2);

        System.out.println("Writing it out...");

        XMLWriter writer = new XMLWriter(System.out);
        writer.write(doc2);

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
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * $Id: RoundTripDemo.java,v 1.4 2005/01/29 14:53:13 maartenc Exp $
 */
