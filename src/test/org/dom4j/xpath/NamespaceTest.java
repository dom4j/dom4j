/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.xpath;

import junit.textui.TestRunner;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.dom4j.AbstractTestCase;
import org.dom4j.DocumentHelper;
import org.dom4j.Namespace;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;

/**
 * Test harness for the namespace axis
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class NamespaceTest extends AbstractTestCase {
    protected static String[] paths = {"namespace::*",
            "/Template/Application1/namespace::*",
            "/Template/Application1/namespace::xplt", "//namespace::*"};

    public static void main(String[] args) {
        TestRunner.run(NamespaceTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testXPaths() throws Exception {
        int size = paths.length;

        for (int i = 0; i < size; i++) {
            testXPath(paths[i]);
        }
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void testXPath(String xpathText) {
        XPath xpath = DocumentHelper.createXPath(xpathText);
        List list = xpath.selectNodes(document);

        log("Searched path: " + xpathText + " found: " + list.size()
                + " result(s)");

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object object = iter.next();

            log("Found Result: " + object);

            assertTrue("Results should be Namespace objects",
                    object instanceof Namespace);

            Namespace namespace = (Namespace) object;

            log("Parent node: " + namespace.getParent());

            assertTrue("Results should support the parent relationship",
                    namespace.supportsParent());
            assertTrue(
                    "Results should contain reference to the parent element",
                    namespace.getParent() != null);
            assertTrue("Results should contain reference to the document",
                    namespace.getDocument() != null);
        }
    }

    protected void setUp() throws Exception {
        super.setUp();
        document = new SAXReader().read(new File("xml/testNamespaces.xml"));
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
