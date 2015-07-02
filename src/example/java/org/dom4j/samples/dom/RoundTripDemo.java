/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: RoundTripDemo.java,v 1.4 2005/01/29 14:52:58 maartenc Exp $
 */

package org.dom4j.samples.dom;

import org.dom4j.samples.SAXDemo;

import org.dom4j.Document;
import org.dom4j.io.DOMReader;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.XMLWriter;

/**
 * A simple program demonstrating a round trip from XML to dom4j to DOM to dom4j
 * again
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

    /** Outputs the document to DOM, parse it back again then output it */
    protected void process(Document document) throws Exception {

        System.out.println("about to output: " + document);

        // output the document to DOM
        DOMWriter writer = new DOMWriter();
        org.w3c.dom.Document domDocument = writer.write(document);

        // parse back again
        DOMReader reader = new DOMReader();
        Document doc2 = reader.read(domDocument);

        System.out.println("parsed back again: " + doc2);

        // now lets output it again
        XMLWriter xmlWriter = new XMLWriter(System.out);
        xmlWriter.write(doc2);
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
 * $Id: RoundTripDemo.java,v 1.4 2005/01/29 14:52:58 maartenc Exp $
 */
