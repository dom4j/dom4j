/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j;

import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.tree.DefaultElement;

/** A test harness to test XPath expression evaluation in DOM4J
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */

public class TestXPath extends AbstractTestCase {

    protected static boolean VERBOSE = true;

    protected static String[] paths =
        {
            ".",
            "*",
            "/",
            "/.",
            "/*",
            "/node()",
            "/child::node()",
            "/self::node()",
            "root",
            "/root",
            "/root/author",
            "text()",
            "//author",
            "//author/text()",
            "//@location",
            "//attribute::*",
            "//namespace::*",
            "normalize-space(/root)",
            "//author[@location]",
            "//author[@location='UK']",
            "root|author",
            "//*[.='James Strachan']",
            "//root/author[1]",
            "normalize-space(/root/author)",
            "normalize-space(' a  b  c  d ')",
            "//root|//author[1]|//author[2]",
            "//root/author[2]",
            "//root/author[3]" };

    public static void main(String[] args) {

        TestRunner.run(suite());

    }

    public static Test suite() {

        return new TestSuite(TestXPath.class);

    }

    public TestXPath(String name) {

        super(name);

    }

    // Test case(s)

    //-------------------------------------------------------------------------                    

    public void testXPaths() throws Exception {

        int size = paths.length;

        for (int i = 0; i < size; i++) {

            testXPath(paths[i]);

        }

    }

    public void testCreateXPathBug() throws Exception {

        Element element = new DefaultElement( "foo" );
        XPath xpath = element.createXPath( "//bar" );        

        assertTrue("created a valid XPath: " + xpath != null );
    }

    // Implementation methods

    //-------------------------------------------------------------------------                    

    protected void testXPath(String xpathExpression) {

        log("Searched path: " + xpathExpression);

        XPath xpath = DocumentHelper.createXPath(xpathExpression);

        List list = xpath.selectNodes(document);

        log("Found        : " + list.size() + " result(s)");

        if (VERBOSE) {

            log("...........................................");

            log("XPath:       :" + xpath);

            log("...........................................");

        }

        log("Results");

        if (list == null) {

            log("null");

        }

        else {

            log("[");

            for (int i = 0, size = list.size(); i < size; i++) {

                Object object = list.get(i);

                String text = "null";

                if (object instanceof Node) {

                    Node node = (Node) object;

                    text = node.asXML();

                }

                else if (object != null) {

                    text = object.toString();

                }

                log("    " + text);

            }

            log("]");

        }

        log("...........................................");

    }

}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
