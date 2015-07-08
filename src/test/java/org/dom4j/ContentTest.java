/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import java.util.List;

/**
 * A test harness to test the content API in DOM4J
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.3 $
 */
public class ContentTest extends AbstractTestCase {
    protected DocumentFactory factory = new DocumentFactory();

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testRoot() throws Exception {
        Element root = document.getRootElement();
        assertNotNull("Has root element", root);

        List<Element> authors = root.elements("author");
        assertNotNull("Root has children", authors);
        assertTrue("Root has children", authors.size() == 2);

        Element author1 = authors.get(0);
        Element author2 = authors.get(1);

        assertTrue("Author1 is James", author1.attributeValue("name").equals(
                "James"));
        assertTrue("Author2 is Bob", author2.attributeValue("name").equals(
                "Bob"));

        testGetAttributes(author1);
        testGetAttributes(author2);
    }

    public void testContent() throws Exception {
        Element root = document.getRootElement();
        assertNotNull("Has root element", root);

        List<Node> content = root.content();
        assertNotNull("Root has content", content);
        assertTrue("Root has content", content.size() >= 2);

        boolean iterated = false;

        for (Node object : content) {
            iterated = true;
        }

        assertTrue("Iteration completed", iterated);
    }

    public void testGetNode() throws Exception {
        Element root = document.getRootElement();
        assertNotNull("Has root element", root);

        int count = root.nodeCount();
        assertTrue("Root has correct node count", count == 2);

        boolean iterated = false;

        for (int i = 0; i < count; i++) {
            Node node = root.node(i);
            assertTrue("Valid node returned from node()", node != null);
            iterated = true;
        }

        assertTrue("Iteration completed", iterated);
    }

    public void testGetXPathNode() throws Exception {
        Element root = document.getRootElement();
        assertNotNull("Has root element", root);

        int count = root.nodeCount();
        assertTrue("Root has correct node count", count == 2);

        boolean iterated = false;

        for (int i = 0; i < count; i++) {
            Node node = root.getXPathResult(i);
            assertNotNull("Valid node returned from node()", node);
            assertTrue("Node supports the parent relationship", node.supportsParent());
            iterated = true;
        }

        assertTrue("Iteration completed", iterated);
    }

    public void testOrderOfPI() throws Exception {
        Document document = factory.createDocument();
        document.addProcessingInstruction("xml-stylesheet",
                "type=\"text/xsl\" href=\"...\"");
        document.addElement("root");

        List<Node> list = document.content();

        assertNotNull(list);
        assertEquals(2, list.size());

        Node pi = list.get(0);
        Node root = list.get(1);

        assertTrue("First element is not a PI",
                pi instanceof ProcessingInstruction);
        assertTrue("Second element is an element", root instanceof Element);

        String xml = "<?xml version=\"1.0\" ?>\n"
                + "<?xml-stylesheet type=\"text/xsl\" href=\"foo\" ?>\n"
                + "<root/>";
        document = DocumentHelper.parseText(xml);

        list = document.content();

        assertNotNull(list);
        assertEquals(2, list.size());
        pi = list.get(0);
        root = list.get(1);

        assertTrue("First element is not a PI",
                pi instanceof ProcessingInstruction);
        assertTrue("Second element is an element", root instanceof Element);
    }

    public void testAddingInTheMiddle() throws Exception {
        Document doc = factory.createDocument();
        Element root = doc.addElement("html");
        Element header = root.addElement("header");
        Element footer = root.addElement("footer");

        // now lets add <foo> in between header & footer
        List<Node> list = root.content();
        Element foo = factory.createElement("foo");
        list.add(1, foo);

        // assertions
        assertTrue(list.size() == 3);
        assertTrue(list.get(0) == header);
        assertTrue(list.get(1) == foo);
        assertTrue(list.get(2) == footer);
    }

    public void testAddAtIndex() throws Exception {
        Document doc = factory.createDocument();
        Element root = doc.addElement("html");
        Element header = root.addElement("header");
        Element body = root.addElement("body");

        Element foo = factory.createElement("foo");
        Element bar = factory.createElement("bar");

        List<Node> content = header.content();
        content.add(0, foo);
        content.add(0, bar);

        assertEquals("foo", header.node(1).getName());
        assertEquals("bar", header.node(0).getName());

        foo = factory.createElement("foo");
        bar = factory.createElement("bar");

        content = body.content();
        content.add(0, foo);
        content.add(1, bar);

        assertEquals("foo", body.node(0).getName());
        assertEquals("bar", body.node(1).getName());
    }

    public void testAddAtIndex2() throws Exception {
        Document doc = factory.createDocument();
        Element parent = doc.addElement("parent");
        Element child = parent.addElement("child");
        Element anotherChild = factory.createElement("child2");

        List<Element> elements = parent.elements();
        int index = elements.indexOf(child);

        assertEquals(0, index);

        elements.add(1, anotherChild);
        elements = parent.elements();
        assertEquals(child, elements.get(0));
        assertEquals(anotherChild, elements.get(1));
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void testGetAttributes(Element author) throws Exception {
        String definedName = "name";
        String undefinedName = "undefined-attribute-name";
        String defaultValue = "** Default Value **";

        String value = author.attributeValue(definedName, defaultValue);
        assertNotEquals("Defined value doesn't return specified default value", value, defaultValue);

        value = author.attributeValue(undefinedName, defaultValue);
        assertEquals("Undefined value returns specified default value", value, defaultValue);
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
