/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import junit.textui.TestRunner;

import java.util.List;

/**
 * A test harness to test XPath expression evaluation in DOM4J
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class XPathBugTest extends AbstractTestCase {
    public static void main(String[] args) {
        TestRunner.run(XPathBugTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testXPaths() throws Exception {
        Document document = getDocument("/xml/rabo1ae.xml");
        Element root = (Element) document
                .selectSingleNode("/m:Msg/m:Contents/m:Content");

        assertTrue("root is not null", root != null);

        Namespace ns = root.getNamespaceForPrefix("ab");

        assertTrue("Found namespace", ns != null);

        System.out.println("Found: " + ns.getURI());

        Element element = (Element) root
                .selectSingleNode("ab:RaboPayLoad[@id='1234123']");

        assertTrue("element is not null", element != null);

        String value = element.valueOf("ab:AccountingEntry/ab:RateType");

        assertEquals("RateType is correct", "CRRNT", value);
    }

    /**
     * A bug found by Rob Lebowitz
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testRobLebowitz() throws Exception {
        String text = "<ul>" + "    <ul>" + "        <li/>"
                + "            <ul>" + "                <li/>"
                + "            </ul>" + "        <li/>" + "    </ul>" + "</ul>";

        Document document = DocumentHelper.parseText(text);
        List lists = document.selectNodes("//ul | //ol");

        int count = 0;

        for (int i = 0; i < lists.size(); i++) {
            Element list = (Element) lists.get(i);
            List nodes = list.selectNodes("ancestor::ul");

            if ((nodes != null) && (nodes.size() > 0)) {
                continue;
            }

            nodes = list.selectNodes("ancestor::ol");

            if ((nodes != null) && (nodes.size() > 0)) {
                continue;
            }
        }
    }

    /**
     * A bug found by Stefan which results in IndexOutOfBoundsException for
     * empty results
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testStefan() throws Exception {
        String text = "<foo>hello</foo>";
        Document document = DocumentHelper.parseText(text);
        XPath xpath = DocumentHelper.createXPath("/x");
        Object value = xpath.evaluate(document);
    }

    /**
     * Test found by Mike Skells
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testMikeSkells() throws Exception {
        Document top = DocumentFactory.getInstance().createDocument();
        Element root = top.addElement("root");
        root.addElement("child1").addElement("child11");
        root.addElement("child2").addElement("child21");
        System.out.println(top.asXML());

        XPath test1 = top.createXPath("/root/child1/child11");
        XPath test2 = top.createXPath("/root/child2/child21");
        Node position1 = test1.selectSingleNode(root);
        Node position2 = test2.selectSingleNode(root);

        System.out.println("test1= " + test1);
        System.out.println("test2= " + test2);
        System.out.println("Position1 Xpath = " + position1.getUniquePath());
        System.out.println("Position2 Xpath = " + position2.getUniquePath());

        System.out.println("test2.matches(position1) : "
                + test2.matches(position1));

        assertTrue("test1.matches(position1)", test1.matches(position1));
        assertTrue("test2.matches(position2)", test2.matches(position2));

        assertTrue("test2.matches(position1) should be false", !test2
                .matches(position1));
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
