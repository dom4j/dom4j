/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.xpath;

import junit.textui.TestRunner;

import java.util.List;

import org.dom4j.AbstractTestCase;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;

/**
 * Test harness for the valueOf() function
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.3 $
 */
public class ValueOfTest extends AbstractTestCase {
    protected static String[] paths = {"/root", "//author", "//author/@name",
            "/root/author[1]", "/root/author[1]/@name", "/root/author[2]",
            "/root/author[2]/@name", "/root/author[3]",
            "/root/author[3]/@name", "name()", "name(.)", "name(..)",
            "name(child::node())", "name(parent::*)", "name(../*)",
            "name(../child::node())", "local-name()", "local-name(..)",
            "local-name(parent::*)", "local-name(../*)", "parent::*",
            "name(/.)", "name(/child::node())", "name(/*)", ".", "..", "../*",
            "../child::node()", "/.", "/*", "*", "/child::node()"};

    public static void main(String[] args) {
        TestRunner.run(ValueOfTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testXPaths() throws Exception {
        Element root = document.getRootElement();
        List children = root.elements("author");
        Element child1 = (Element) children.get(0);

        testXPath(document);
        testXPath(root);
        testXPath(child1);
    }

    protected void testXPath(Node node) throws Exception {
        log("Testing XPath on: " + node);
        log("===============================");

        int size = paths.length;

        for (int i = 0; i < size; i++) {
            testXPath(node, paths[i]);
        }
    }

    protected void testXPath(Node node, String xpathExpr) throws Exception {
        try {
            XPath xpath = node.createXPath(xpathExpr);
            String value = xpath.valueOf(node);

            log("valueOf: " + xpathExpr + " is: " + value);
        } catch (Throwable e) {
            e.printStackTrace();
            assertTrue("Failed with exception: " + e, false);
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
