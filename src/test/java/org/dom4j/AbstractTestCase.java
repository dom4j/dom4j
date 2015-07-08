/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import org.apache.xalan.processor.TransformerFactoryImpl;
import org.apache.xerces.jaxp.SAXParserFactoryImpl;
import org.dom4j.io.SAXReader;
import org.dom4j.util.NodeComparator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.io.File;

/**
 * An abstract base class for some DOM4J test cases
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.24 $
 */
@Test
public abstract class AbstractTestCase {
    protected Document document;

    @BeforeSuite
    public void init() throws Exception {
        System.setProperty("javax.xml.parsers.SAXParserFactory",
                SAXParserFactoryImpl.class.getName());
        System.setProperty("javax.xml.transform.TransformerFactory",
                TransformerFactoryImpl.class.getName());
    }

    @BeforeMethod
    public void setUp() throws Exception {
        document = DocumentHelper.createDocument();

        Element root = document.addElement("root");

        Element author1 = root.addElement("author").addAttribute("name",
                "James").addAttribute("location", "UK").addText(
                "James Strachan");

        Element url1 = author1.addElement("url");
        url1.addText("http://sourceforge.net/users/jstrachan/");

        Element author2 = root.addElement("author").addAttribute("name", "Bob")
                .addAttribute("location", "Canada").addText("Bob McWhirter");

        Element url2 = author2.addElement("url");
        url2.addText("http://sourceforge.net/users/werken/");
    }

    protected void log(String text) {
        System.out.println(text);
    }

    protected Document getDocument() {
        return document;
    }

    protected Document getDocument(String path) throws Exception {
        return getDocument(path, new SAXReader());
    }

    protected Document getDocument(String path, SAXReader reader)
            throws Exception {
        return reader.read(getFile(path));
    }

    /**
     * DOCUMENT ME!
     *
     * @return the root element of the document
     */
    protected Element getRootElement() {
        Element root = document.getRootElement();
        Assert.assertNotNull(root, "Document has root element");

        return root;
    }

    protected File getFile(String path) {
        return new File(System.getProperty("user.dir"), path);
    }

    protected void assertDocumentsEqual(Document doc1, Document doc2)
            throws Exception {
        try {
            Assert.assertNotNull(doc1, "Doc1 not null");
            Assert.assertNotNull(doc2, "Doc2 not null");

            doc1.normalize();
            doc2.normalize();

            assertNodesEqual(doc1, doc2);

            NodeComparator comparator = new NodeComparator();
            Assert.assertEquals(comparator.compare(doc1, doc2), 0, "Documents are equal");
        } catch (Exception e) {
            log("Failed during comparison of: " + doc1 + " and: " + doc2);
            throw e;
        }
    }

    protected void assertNodesEqual(Document n1, Document n2) {
        // assertEquals( "Document names", n1.getName(), n2.getName() );
        assertNodesEqual(n1.getDocType(), n2.getDocType());
        assertNodesEqualContent(n1, n2);
    }

    protected void assertNodesEqual(Element n1, Element n2) {
        assertNodesEqual(n1.getQName(), n2.getQName());

        int c1 = n1.attributeCount();
        int c2 = n2.attributeCount();

        Assert.assertEquals(c1, c2, String.format("Elements have same number of attributes (%d, %d for: %s and %s", c1, c2, n1, n2));

        for (int i = 0; i < c1; i++) {
            Attribute a1 = n1.attribute(i);
            Attribute a2 = n2.attribute(a1.getQName());
            assertNodesEqual(a1, a2);
        }

        assertNodesEqualContent(n1, n2);
    }

    protected void assertNodesEqual(Attribute n1, Attribute n2) {
        assertNodesEqual(n1.getQName(), n2.getQName());

        Assert.assertEquals(n1.getValue(), n2.getValue(), String.format("Attribute values for: %s and %s", n1, n2));
    }

    protected void assertNodesEqual(QName n1, QName n2) {
        Assert.assertEquals(n1.getNamespaceURI(), n2.getNamespaceURI(), String.format("URIs equal for: %s and %s", n1.getQualifiedName(), n2.getQualifiedName()));
        Assert.assertEquals(n1.getQualifiedName(), n2.getQualifiedName(), "qualified names equal");
    }

    protected void assertNodesEqual(CharacterData t1, CharacterData t2) {
        Assert.assertEquals(t1.getText(), t2.getText(), String.format("Text equal for: %s and %s", t1, t2));
    }

    protected void assertNodesEqual(DocumentType o1, DocumentType o2) {
        if (o1 != o2) {
            Assert.assertNotNull(o1, String.format("Missing DocType: %s", o1));
            Assert.assertNotNull(o2, String.format("Missing DocType: %s", o2));
            Assert.assertEquals(o1.getName(), o2.getName(), "DocType name equal");
            Assert.assertEquals(o1.getPublicID(), o2.getPublicID(), "DocType publicID equal");
            Assert.assertEquals(o1.getSystemID(), o2.getSystemID(), "DocType systemID equal");
        }
    }

    protected void assertNodesEqual(Entity o1, Entity o2) {
        Assert.assertEquals(o1.getName(), o2.getName(), "Entity names equal");
        Assert.assertEquals(o1.getText(), o2.getText(), "Entity values equal");
    }

    protected void assertNodesEqual(ProcessingInstruction n1, ProcessingInstruction n2) {
        Assert.assertEquals(n1.getTarget(), n2.getTarget(), "PI targets equal");
        Assert.assertEquals(n1.getText(), n2.getText(), "PI text equal");
    }

    protected void assertNodesEqual(Namespace n1, Namespace n2) {
        Assert.assertEquals(n1.getPrefix(), n2.getPrefix(), "Namespace prefixes not equal");
        Assert.assertEquals(n1.getURI(), n2.getURI(), "Namespace URIs not equal");
    }

    protected void assertNodesEqualContent(Branch b1, Branch b2) {
        int c1 = b1.nodeCount();
        int c2 = b2.nodeCount();

        if (c1 != c2) {
            log("Content of: " + b1);
            log("is: " + b1.content());
            log("Content of: " + b2);
            log("is: " + b2.content());
        }

        Assert.assertEquals(c1, c2, String.format("Branches have same number of children (%d, %d for: %s and %s", c1, c2, b1, b2));

        for (int i = 0; i < c1; i++) {
            Node n1 = b1.node(i);
            Node n2 = b2.node(i);
            assertNodesEqual(n1, n2);
        }
    }

    protected void assertNodesEqual(Node n1, Node n2) {
        int nodeType1 = n1.getNodeType();
        int nodeType2 = n2.getNodeType();
        Assert.assertEquals(nodeType1, nodeType2, "Nodes are of same type");

        switch (nodeType1) {
            case Node.ELEMENT_NODE:
                assertNodesEqual((Element) n1, (Element) n2);

                break;

            case Node.DOCUMENT_NODE:
                assertNodesEqual((Document) n1, (Document) n2);

                break;

            case Node.ATTRIBUTE_NODE:
                assertNodesEqual((Attribute) n1, (Attribute) n2);

                break;

            case Node.TEXT_NODE:
                assertNodesEqual((Text) n1, (Text) n2);

                break;

            case Node.CDATA_SECTION_NODE:
                assertNodesEqual((CDATA) n1, (CDATA) n2);

                break;

            case Node.ENTITY_REFERENCE_NODE:
                assertNodesEqual((Entity) n1, (Entity) n2);

                break;

            case Node.PROCESSING_INSTRUCTION_NODE:
                assertNodesEqual((ProcessingInstruction) n1,
                        (ProcessingInstruction) n2);

                break;

            case Node.COMMENT_NODE:
                assertNodesEqual((Comment) n1, (Comment) n2);

                break;

            case Node.DOCUMENT_TYPE_NODE:
                assertNodesEqual((DocumentType) n1, (DocumentType) n2);

                break;

            case Node.NAMESPACE_NODE:
                assertNodesEqual((Namespace) n1, (Namespace) n2);

                break;

            default:
                Assert.fail(String.format("Invalid node types. node1: %s and node2: %s", n1, n2));
        }
    }

    protected void assertTrue(boolean condition) {
        Assert.assertTrue(condition);
    }

    protected void assertTrue(String message, boolean condition) {
        Assert.assertTrue(condition, message);
    }

    protected void assertFalse(boolean condition) {
        Assert.assertFalse(condition);
    }

    protected void assertFalse(String message, boolean condition) {
        Assert.assertFalse(condition, message);
    }

    protected void assertNull(Object object) {
        Assert.assertNull(object);
    }

    protected void assertNull(String message, Object object) {
        Assert.assertNull(object, message);
    }

    protected void assertNotNull(Object object) {
        Assert.assertNotNull(object);
    }

    protected void assertNotNull(String message, Object object) {
        Assert.assertNotNull(object, message);
    }

    protected void assertEquals(int expected, int actual) {
        Assert.assertEquals(actual, expected);
    }

    protected void assertEquals(String message, int expected, int actual) {
        Assert.assertEquals(actual, expected, message);
    }

    protected void assertEquals(String expected, String actual) {
        Assert.assertEquals(actual, expected);
    }

    protected void assertEquals(String message, String expected, String actual) {
        Assert.assertEquals(actual, expected, message);
    }

    protected void assertEquals(Object expected, Object actual) {
        Assert.assertEquals(actual, expected);
    }

    protected void assertNotEquals(Object expected, Object actual) {
        Assert.assertNotEquals(actual, expected);
    }

    protected void assertNotEquals(String message, Object expected, Object actual) {
        Assert.assertNotEquals(actual, expected, message);
    }

    protected void assertEquals(String message, Object expected, Object actual) {
        Assert.assertEquals(actual, expected, message);
    }

    protected void assertSame(Object expected, Object actual) {
        Assert.assertSame(actual, expected);
    }

    protected void assertSame(String message, Object expected, Object actual) {
        Assert.assertSame(actual, expected, message);
    }

    protected void assertNotSame(Object expected, Object actual) {
        Assert.assertNotSame(actual, expected);
    }

    protected void assertNotSame(String message, Object expected, Object actual) {
        Assert.assertNotSame(actual, expected, message);
    }

    protected void fail() {
        Assert.fail();
    }

    protected void fail(String message) {
        Assert.fail(message);
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
