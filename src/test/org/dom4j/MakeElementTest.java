/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import junit.textui.TestRunner;

/**
 * A test harness to test the DocumentHelper.makeElement() methodt
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class MakeElementTest extends AbstractTestCase {
    public static void main(String[] args) {
        TestRunner.run(MakeElementTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testMakeElement() throws Exception {
        Document doc = DocumentHelper.createDocument();

        Element c = DocumentHelper.makeElement(doc, "a/b/c");
        assertTrue("Should return a valid element", c != null);

        Element c2 = DocumentHelper.makeElement(doc, "a/b/c");

        assertTrue("Found same element again", c == c2);

        c.addAttribute("x", "123");

        Node found = doc.selectSingleNode("/a/b/c[@x='123']");

        assertEquals("Found same node via XPath", c, found);

        Element b = c.getParent();

        Element e = DocumentHelper.makeElement(b, "c/d/e");

        assertTrue("Should return a valid element", e != null);

        Element e2 = DocumentHelper.makeElement(b, "c/d/e");

        assertTrue("Found same element again", e == e2);

        e.addAttribute("y", "456");

        found = b.selectSingleNode("c/d/e[@y='456']");

        assertEquals("Found same node via XPath", e, found);
    }

    public void testMakeQualifiedElement() throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("root");
        root.addNamespace("", "defaultURI");
        root.addNamespace("foo", "fooURI");
        root.addNamespace("bar", "barURI");

        Element c = DocumentHelper.makeElement(doc, "root/foo:b/bar:c");
        assertTrue("Should return a valid element", c != null);

        assertEquals("c has a valid namespace", "barURI", c.getNamespaceURI());

        Element b = c.getParent();

        assertEquals("b has a valid namespace", "fooURI", b.getNamespaceURI());

        log("Created: " + c);

        Element c2 = DocumentHelper.makeElement(doc, "root/foo:b/bar:c");
        assertTrue("Found same element again", c == c2);
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
