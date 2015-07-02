/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: XPathTool.java,v 1.4 2005/01/29 14:52:57 maartenc Exp $
 */

package org.dom4j.samples;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * A simple program that parsers a document and allows XPath expressions to be
 * evaluated on the document.
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.4 $
 */
public class XPathTool extends SAXDemo {

    protected Document document;

    protected XMLWriter xmlWriter;

    protected boolean verbose;

    public static void main(String[] args) {
        run(new XPathTool(), args);
    }

    public XPathTool() {
    }

    public void run(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage("{options} <xml file>");
            return;
        }

        for (int i = 0, size = args.length; i < size; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                readOptions(arg);
            } else {
                println("Parsing: " + arg);
                document = parse(arg);
                break;
            }
        }

        xmlWriter = new XMLWriter(System.out, new OutputFormat("  ", true));
        userLoop();
    }

    protected void userLoop() throws Exception {
        println("Enter XPath expressions to evaluate or 'quit' to stop");

        BufferedReader reader = new BufferedReader(new InputStreamReader(
                System.in));

        while (true) {
            print("XPath> ");
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            line = line.trim();
            if (line.equalsIgnoreCase("quit")) {
                break;
            }
            evaluateCommand(line);
        }

        println("Bye");
    }

    protected void evaluateCommand(String xpath) throws Exception {
        println("Results...");
        Object results = document.selectObject(xpath);
        printResult(results);
        xmlWriter.flush();
    }

    protected void printResult(Object results) throws Exception {
        if (results instanceof Node) {
            Node node = (Node) results;
            if (node instanceof Document) {
                Document document = (Document) node;
                println("Document: " + document.getName());
            } else if (node instanceof Element) {
                Element element = (Element) node;
                xmlWriter.writeOpen(element);
                xmlWriter.println();
            } else {
                xmlWriter.write(node);
                xmlWriter.println();
            }
        } else if (results instanceof List) {
            List list = (List) results;
            println("List of " + list.size() + " item(s)");
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                printResult(iter.next());
            }
        } else {
            if (results == null) {
                println("null");
            } else {
                println(results + " (" + results.getClass().getName() + ")");
            }
        }
    }

    protected void readOptions(String arg) {
        if (arg.indexOf('v') >= 0) {
            verbose = true;
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
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * $Id: XPathTool.java,v 1.4 2005/01/29 14:52:57 maartenc Exp $
 */
