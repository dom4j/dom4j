/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: AbstractDemo.java,v 1.4 2005/01/29 14:52:57 maartenc Exp $
 */

package org.dom4j.samples;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * An abstract base class for the demo programs.
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public abstract class AbstractDemo {

    /** The format of XML / HTML that is output by the demo program */
    protected OutputFormat format = new OutputFormat();

    /** The writer of XML */
    protected XMLWriter writer;

    public AbstractDemo() {
    }

    protected static void run(AbstractDemo demo, String[] args) {
        try {
            demo.run(args);
        } catch (DocumentException e) {
            System.out.println("Exception occurred: " + e);
            Throwable nestedException = e.getNestedException();
            if (nestedException != null) {
                System.out.println("NestedException: " + nestedException);
                nestedException.printStackTrace();
            } else {
                e.printStackTrace();
            }
        } catch (Throwable t) {
            System.out.println("Exception occurred: " + t);
            t.printStackTrace();
        }
    }

    public void run(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage("no XML document URL specified");
            return;
        }

        int idx = format.parseOptions(args, 0);
        if (idx >= args.length) {
            printUsage("no XML document URL specified");
            return;
        } else {
            writer = createXMLWriter();

            Document document = parse(args[idx]);
            process(document);
        }
    }

    protected Document parse(String xmlFile) throws Exception {
        throw new RuntimeException(
                "parse(String xmlFile) not implemented in this demo");
    }

    protected void process(Document document) throws Exception {
        getXMLWriter().write(document);
        getXMLWriter().flush();
    }

    protected void print(String text) {
        System.out.print(text);
    }

    protected void println(String text) {
        System.out.println(text);
    }

    protected void printUsage(String text) {
        println("Usage: java " + getClass().getName() + " " + text);
    }

    protected XMLWriter getXMLWriter() throws Exception {
        if (writer == null) {
            writer = createXMLWriter();
        }
        return writer;
    }

    /**
     * A Factory Method to create an <code>XMLWriter</code> instance allowing
     * derived classes to change this behaviour
     */
    protected XMLWriter createXMLWriter() throws Exception {
        return new XMLWriter(System.out, format);
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
 * $Id: AbstractDemo.java,v 1.4 2005/01/29 14:52:57 maartenc Exp $
 */
