/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import junit.textui.TestRunner;

import java.io.StringReader;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.io.DOMReader;

import org.xml.sax.InputSource;

/**
 * Test the use of namespaces
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class NamespacesTest extends AbstractTestCase {
    public static void main(String[] args) {
        TestRunner.run(NamespacesTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testNamespaces() throws Exception {
        testNamespaces(document);
        testNamespaces(saxRoundTrip(document));
        testNamespaces(domRoundTrip(document));
    }

    public void testNamespaces(Document document) throws Exception {
        Document doc2 = (Document) document.clone();

        Element root = doc2.getRootElement();
        assertNamespace(root.getNamespace(), "",
                "http://www.w3.org/2001/XMLSchema");
        assertEquals("xmlns=\"http://www.w3.org/2001/XMLSchema\"", root
                .getNamespace().asXML());
        assertEquals("namespace::*[name()='']", root.getNamespace().getPath());
        assertEquals("namespace::*[name()='']", root.getNamespace()
                .getUniquePath());

        List additionalNS = root.additionalNamespaces();
        assertTrue("at least one additional namespace", (additionalNS != null)
                && (additionalNS.size() > 0));

        Namespace ns = (Namespace) additionalNS.get(0);
        assertNamespace(ns, "t", "http://www.w3.org/namespace/");
        assertEquals("xmlns:t=\"http://www.w3.org/namespace/\"", ns.asXML());
        assertEquals("namespace::t", ns.getPath());
        assertEquals("namespace::t", ns.getUniquePath());

        Node node = root.node(0);
        assertTrue("First node is a namespace", node instanceof Namespace);

        // now lets try change the namespace
        root.remove(ns);
        root.addNamespace("t", "myNewURI");

        additionalNS = root.additionalNamespaces();
        assertTrue("at least one additional namespace", (additionalNS != null)
                && (additionalNS.size() > 0));

        ns = (Namespace) additionalNS.get(0);
        assertNamespace(ns, "t", "myNewURI");

        // lets test the list is backed
        additionalNS.remove(0);
        additionalNS.add(Namespace.get("t", "myNewURI-2"));

        additionalNS = root.additionalNamespaces();
        assertTrue("at least one additional namespace", (additionalNS != null)
                && (additionalNS.size() > 0));

        ns = (Namespace) additionalNS.get(0);
        assertNamespace(ns, "t", "myNewURI-2");

        additionalNS.clear();
        root.addNamespace("t", "myNewURI");

        additionalNS = root.additionalNamespaces();
        assertTrue("at least one additional namespace", (additionalNS != null)
                && (additionalNS.size() > 0));

        ns = (Namespace) additionalNS.get(0);
        assertNamespace(ns, "t", "myNewURI");

        log("Namespaces: " + additionalNS);
        log("XML is now");
        log(root.asXML());
    }

    public void testNamespaceForPrefix() throws Exception {
        testNamespaceForPrefix(document);
        testNamespaceForPrefix(saxRoundTrip(document));
        testNamespaceForPrefix(domRoundTrip(document));
    }

    public void testNamespaceForPrefix(Document document) throws Exception {
        Element root = document.getRootElement();
        Namespace ns = root.getNamespaceForPrefix("t");

        assertNamespace(ns, "t", "http://www.w3.org/namespace/");

        Element element = (Element) root.elements().get(0);
        Namespace ns2 = element.getNamespaceForPrefix("t");

        assertNamespace(ns2, "t", "http://www.w3.org/namespace/");
        assertTrue("Same namespace instance returned", ns == ns2);

        log("found: " + ns.asXML());
    }

    public void testNamespaceForDefaultPrefix() throws Exception {
        Document document = getDocument("/xml/test/defaultNamespace.xml");

        testNamespaceForDefaultPrefix(document);
        testNamespaceForDefaultPrefix(saxRoundTrip(document));
        testNamespaceForDefaultPrefix(domRoundTrip(document));
    }

    public void testNamespaceForDefaultPrefix(Document document)
            throws Exception {
        List list = document.selectNodes("//*");

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Element element = (Element) iter.next();
            Namespace ns = element.getNamespaceForPrefix("");
            assertNamespace(ns, "", "dummyNamespace");
            ns = element.getNamespaceForPrefix(null);
            assertNamespace(ns, "", "dummyNamespace");
            log("found: " + ns.asXML());
        }
    }

    public void testAttributeDefaultPrefix() throws Exception {
        Document document = getDocument("/xml/test/soap3.xml");

        testAttributeDefaultPrefix(document);
        testAttributeDefaultPrefix(saxRoundTrip(document));
        testAttributeDefaultPrefix(domRoundTrip(document));
    }

    public void testAttributeDefaultPrefix(Document document) throws Exception {
        List list = document.selectNodes("//@*[local-name()='actor']");

        assertTrue("Matched at least one 'actor' attribute", list.size() > 0);

        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Attribute attribute = (Attribute) iter.next();

            log("found: " + attribute.asXML());

            Element element = attribute.getParent();
            assertTrue("Attribute has a parent", element != null);

            Namespace ns = element.getNamespaceForPrefix("");

            String uri = "http://schemas.xmlsoap.org/soap/envelope/";
            assertNamespace(ns, "", uri);

            Namespace ns2 = attribute.getNamespace();

            // Note that namespaces do not inherit the default namespace!
            assertNamespace(ns2, "", "");
        }
    }

    public void testNamespaceForURI() throws Exception {
        testNamespaceForURI(document);
        testNamespaceForURI(saxRoundTrip(document));
        testNamespaceForURI(domRoundTrip(document));
    }

    public void testNamespaceForURI(Document document) throws Exception {
        Element root = document.getRootElement();

        Namespace ns = root.getNamespaceForURI("http://www.w3.org/namespace/");

        assertNamespace(ns, "t", "http://www.w3.org/namespace/");

        Element element = (Element) root.elements().get(0);
        Namespace ns2 = element
                .getNamespaceForURI("http://www.w3.org/namespace/");

        assertNamespace(ns2, "t", "http://www.w3.org/namespace/");

        assertTrue("Same namespace instance returned", ns == ns2);

        log("found: " + ns.asXML());
    }

    public void testRedeclareNamespaces() throws Exception {
        Document document = getDocument("/xml/test/soap2.xml");
        testRedeclareNamespaces(document);
        testRedeclareNamespaces(saxRoundTrip(document));
        testRedeclareNamespaces(domRoundTrip(document));
    }

    public void testRedeclareNamespaces(Document document) throws Exception {
        String uri = "http://schemas.xmlsoap.org/soap/envelope/";
        assertNamespaces(document.selectNodes("//*[local-name()='Envelope']"),
                "SOAP-ENV", uri);
        assertNamespaces(document.selectNodes("//*[local-name()='Body']"),
                "SOAP-ENV", uri);
        assertNamespaces(document.selectNodes("//*[local-name()='bar']"), "a",
                "barURI");
        assertNamespaces(document.selectNodes("//*[local-name()='newBar']"),
                "a", "newBarURI");
        assertNamespaces(document.selectNodes("//*[local-name()='foo']"), "",
                "fooURI");
        assertNamespaces(document.selectNodes("//*[local-name()='newFoo']"),
                "", "newFooURI");
    }

    public void testDefaultNamespaceIssue() throws Exception {
        Document document = getDocument("/xml/test/defaultNamespaceIssue.xsd");
        testDefaultNamespaceIssue(document);
        testDefaultNamespaceIssue(saxRoundTrip(document));
        testDefaultNamespaceIssue(domRoundTrip(document));
    }

    public void testDefaultNamespaceIssue(Document document) throws Exception {
        // When writing documents using a default namespace with XMLWriter
        // a redeclaration of the default namespace to "" was dropped in the
        // output. Test that
        // <xsd:schema><xsd:element><xsd:annotation><xsd:documentation><text>
        // is in no namespace.
        String expr 
            = "/xsd:schema/xsd:element/xsd:annotation/xsd:documentation/text";
        assertNotNull("default namespace redeclaration", (Element) document
                .selectSingleNode(expr));

        // The test document has a default namespace declaration on the root
        // element ("schema"), but the element itself is not in the default
        // namespace. Test that declaredNamespaces on the root element also
        // returns the default namespace declaration.
        Iterator iter = document.getRootElement().declaredNamespaces()
                .iterator();

        while (iter.hasNext()) {
            Namespace ns = (Namespace) iter.next();

            if ("urn:wapforum:devicesheet".equals(ns.getURI())
                    && "".equals(ns.getPrefix())) {
                return;
            }
        }

        fail("Default namespace declaration not present on root element");
    }

    public void testDefaultNamespace() throws Exception {
        Document doc = DocumentHelper.createDocument();
        Element processDef = doc.addElement("process-definition",
                "http://jbpm.org/statedefinition-2.0-beta3");
        Element startState = processDef.addElement("start-state");
        startState.addAttribute("name", "start");

        Element transition = startState.addElement("transition");
        transition.addAttribute("to", "first");

        assertEquals("http://jbpm.org/statedefinition-2.0-beta3", startState
                .getNamespace().getURI());
        assertEquals("", startState.getNamespace().getPrefix());

        System.out.println(doc.asXML());
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void setUp() throws Exception {
        super.setUp();
        document = getDocument("/xml/test/test_schema.xml");
    }

    protected Document saxRoundTrip(Document document) throws Exception {
        return DocumentHelper.parseText(document.asXML());
    }

    protected Document domRoundTrip(Document document) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document domDocument = builder.parse(new InputSource(
                new StringReader(document.asXML())));

        // now lets read it back as a DOM4J object
        DOMReader domReader = new DOMReader();

        return domReader.read(domDocument);
    }

    protected void assertNamespaces(List elements, String prefix, String uri)
            throws Exception {
        for (Iterator iter = elements.iterator(); iter.hasNext();) {
            Element element = (Element) iter.next();
            assertNamespace(element.getNamespace(), prefix, uri);
        }
    }

    protected void assertNamespace(Namespace ns, String prefix, String uri)
            throws Exception {
        assertEquals("namespace prefix", prefix, ns.getPrefix());
        assertEquals("namespace URI", uri, ns.getURI());
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
