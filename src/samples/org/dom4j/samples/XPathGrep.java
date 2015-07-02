/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: XPathGrep.java,v 1.5 2005/01/29 14:52:57 maartenc Exp $
 */

package org.dom4j.samples;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.XPath;

/**
 * A utility program which performs XPath expressions on one or more XML files
 * and outputs the matches. It is similar to the <code>grep</code> command on
 * Unix but uses XPath expressions for matching
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.5 $
 */
public class XPathGrep extends SAXDemo {

    protected XPath xpath;

    protected boolean verbose;

    public static void main(String[] args) {
        run(new XPathGrep(), args);
    }

    public XPathGrep() {
    }

    public void run(String[] args) throws Exception {
        if (args.length < 2) {
            printUsage("{options} <XPath expression> <xml file(s)>");
            return;
        }

        for (int i = 0, size = args.length; i < size; i++) {
            String arg = args[i];
            if (arg.startsWith("-")) {
                readOptions(arg);
            } else {
                if (xpath == null) {
                    setXPath(arg);
                } else {
                    Document document = parse(arg);
                    process(document);
                }
            }
        }
    }

    public void setXPath(String xpathExpression) {
        xpath = DocumentHelper.createXPath(xpathExpression);
    }

    protected void process(Document document) throws Exception {
        // perform XPath
        if (verbose) {
            println("About to evalute: " + xpath);
            println("Results:");
        }

        Object object = xpath.evaluate(document);

        if (object instanceof List) {
            List list = (List) object;
            for (Iterator iter = list.iterator(); iter.hasNext();) {
                getXMLWriter().write(iter.next());
                getXMLWriter().println();
            }
            getXMLWriter().flush();
        } else {
            println((object != null) ? object.toString() : "null");
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
 * $Id: XPathGrep.java,v 1.5 2005/01/29 14:52:57 maartenc Exp $
 */
