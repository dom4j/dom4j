/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: SAXDOMDemo.java,v 1.4 2005/01/29 14:52:58 maartenc Exp $
 */

package org.dom4j.samples.dom;

import org.dom4j.samples.AbstractDemo;

import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.SAXContentHandler;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXWriter;

/**
 * This sample program parses an XML document as a DOM4J tree using SAX, it then
 * creates a W3C DOM tree which is then used as input for creating a new DOM4J
 * tree which is then output to SAX which is then parsed into another DOM4J tree
 * which is then output as XML.
 * 
 * This is clearly not terribly useful but demonstrates how to convert from SAX
 * <->DOM4J and DOM4J <->DOM and DOM4J <->text
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class SAXDOMDemo extends AbstractDemo {

    public static void main(String[] args) {
        run(new SAXDOMDemo(), args);
    }

    public SAXDOMDemo() {
    }

    protected Document parse(String url) throws Exception {
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(url);

        println("Parsed to DOM4J tree using SAX: " + document);

        // now lets make a DOM object
        DOMWriter domWriter = new DOMWriter();
        org.w3c.dom.Document domDocument = domWriter.write(document);

        println("Converted to DOM tree: " + domDocument);

        // now lets read it back as a DOM4J object
        DOMReader domReader = new DOMReader();
        document = domReader.read(domDocument);

        println("Converted to DOM4J tree using DOM: " + document);

        // now lets write it back as SAX events to
        // a SAX ContentHandler which should build up a new document
        SAXContentHandler contentHandler = new SAXContentHandler();
        SAXWriter saxWriter = new SAXWriter(contentHandler, null,
                contentHandler);

        saxWriter.write(document);
        document = contentHandler.getDocument();

        println("Converted DOM4J to SAX events then back to DOM4J: " + document);

        return document;
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
 * $Id: SAXDOMDemo.java,v 1.4 2005/01/29 14:52:58 maartenc Exp $
 */
