/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import static org.dom4j.io.OutputFormat.ALPHABETICALLY_ORDERED_ATTRIBUTES_COMPARATOR;

import java.io.StringWriter;
import java.util.List;

import org.dom4j.AbstractTestCase;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.w3c.dom.NamedNodeMap;

/**
 * DOCUMENT ME!
 * 
 * @author Maarten
 */
public class DOMWriterTest extends AbstractTestCase {

    public void testNamespaceBug() throws Exception {
        org.dom4j.Document doc = getDocument("/xml/namespaces.xml");
        DOMWriter writer = new DOMWriter(org.dom4j.dom.DOMDocument.class);
        org.w3c.dom.Document result = writer.write(doc);

        NamedNodeMap atts = result.getDocumentElement().getAttributes();
        assertEquals(4, atts.getLength());

        XMLWriter wr = new XMLWriter();
        wr.setOutputStream(System.out);
        wr.write((org.dom4j.Document) result);
    }

    public void testBug905745() throws Exception {
        org.dom4j.Document doc = getDocument("/xml/namespaces.xml");
        DOMWriter writer = new DOMWriter();
        org.w3c.dom.Document result = writer.write(doc);

        NamedNodeMap atts = result.getDocumentElement().getAttributes();
        org.w3c.dom.Node versionAttr = atts.getNamedItem("version");
        assertNotNull(versionAttr);
        assertNotNull(versionAttr.getLocalName());
        assertEquals("version", versionAttr.getLocalName());
        assertEquals("version", versionAttr.getNodeName());
    }

    public void testBug926752() throws Exception {
        org.dom4j.Document doc = getDocument("/xml/test/defaultNamespace.xml");
        DOMWriter writer = new DOMWriter(org.dom4j.dom.DOMDocument.class);
        org.w3c.dom.Document result = writer.write(doc);

        NamedNodeMap atts = result.getDocumentElement().getAttributes();
        assertEquals(1, atts.getLength());

        OutputFormat format = OutputFormat.createCompactFormat();
        format.setSuppressDeclaration(true);

        XMLWriter wr = new XMLWriter(format);
        StringWriter strWriter = new StringWriter();
        wr.setWriter(strWriter);
        wr.write((org.dom4j.Document) result);
        assertEquals("<a xmlns=\"dummyNamespace\"><b><c>Hello</c></b></a>",
                strWriter.toString());
    }

    public void testUntouchedAttributesOrderAsDefault() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final OutputFormat format = new OutputFormat();
        final XMLWriter writer = new XMLWriter(stringWriter, format);
        writer.write(getDocument("/xml/attributesOrder.xml"));
        writer.close();
        org.dom4j.Document doc = getDocument(stringWriter);

        assertNull(writer.getOutputFormat().getAttributesOrderComparator());

        final org.dom4j.Element rootElement = doc.getRootElement();
        assertEquals("rootElement", rootElement.getName());

        List<Attribute> atts = rootElement.attributes();
        assertEquals(1, atts.size());
        assertEquals("version", atts.get(0).getName());

        // top-level children
        List<Element> childElems = rootElement.elements();
        assertEquals(3, childElems.size());

        assertEquals("firstElem", childElems.get(0).getName());
        atts = childElems.get(0).attributes();
        assertEquals(4, atts.size());
        assertEquals("b", atts.get(0).getName());
        assertEquals("a", atts.get(1).getName());
        assertEquals("d", atts.get(2).getName());
        assertEquals("c", atts.get(3).getName());

        assertEquals("secondElem", childElems.get(1).getName());
        atts = childElems.get(1).attributes();
        assertEquals(4, atts.size());
        assertEquals("b", atts.get(0).getName());
        assertEquals("a", atts.get(1).getName());
        assertEquals("d", atts.get(2).getName());
        assertEquals("c", atts.get(3).getName());

        assertEquals("noChildren", childElems.get(2).getName());
        atts = childElems.get(2).attributes();
        assertEquals(4, atts.size());
        assertEquals("b", atts.get(0).getName());
        assertEquals("a", atts.get(1).getName());
        assertEquals("d", atts.get(2).getName());
        assertEquals("c", atts.get(3).getName());

        // sub children
        childElems = rootElement.elements().get(0).elements();
        assertEquals(1, childElems.size());
        assertEquals("nestedElem", childElems.get(0).getName());
        atts = childElems.get(0).attributes();
        assertEquals(4, atts.size());
        assertEquals("b", atts.get(0).getName());
        assertEquals("a", atts.get(1).getName());
        assertEquals("d", atts.get(2).getName());
        assertEquals("c", atts.get(3).getName());
    }

    public void testOrderingSettingForAttributes() throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final OutputFormat format = new OutputFormat();
        format.setAttributesOrderComparator(ALPHABETICALLY_ORDERED_ATTRIBUTES_COMPARATOR);
        final XMLWriter writer = new XMLWriter(stringWriter, format);
        writer.write(getDocument("/xml/attributesOrder.xml"));
        writer.close();
        org.dom4j.Document doc = getDocument(stringWriter);

        assertEquals(ALPHABETICALLY_ORDERED_ATTRIBUTES_COMPARATOR,
                     writer.getOutputFormat().getAttributesOrderComparator());

        final org.dom4j.Element rootElement = doc.getRootElement();
        assertEquals("rootElement", rootElement.getName());

        List<Attribute> atts = rootElement.attributes();
        assertEquals(1, atts.size());
        assertEquals("version", atts.get(0).getName());

        // top-level children
        List<Element> childElems = rootElement.elements();
        assertEquals(3, childElems.size());

        assertEquals("firstElem", childElems.get(0).getName());
        atts = childElems.get(0).attributes();
        assertEquals(4, atts.size());
        assertEquals("a", atts.get(0).getName());
        assertEquals("b", atts.get(1).getName());
        assertEquals("c", atts.get(2).getName());
        assertEquals("d", atts.get(3).getName());

        assertEquals("secondElem", childElems.get(1).getName());
        atts = childElems.get(1).attributes();
        assertEquals(4, atts.size());
        assertEquals("a", atts.get(0).getName());
        assertEquals("b", atts.get(1).getName());
        assertEquals("c", atts.get(2).getName());
        assertEquals("d", atts.get(3).getName());

        assertEquals("noChildren", childElems.get(2).getName());
        atts = childElems.get(2).attributes();
        assertEquals(4, atts.size());
        assertEquals("a", atts.get(0).getName());
        assertEquals("b", atts.get(1).getName());
        assertEquals("c", atts.get(2).getName());
        assertEquals("d", atts.get(3).getName());

        // sub children
        childElems = rootElement.elements().get(0).elements();
        assertEquals(1, childElems.size());
        assertEquals("nestedElem", childElems.get(0).getName());
        atts = childElems.get(0).attributes();
        assertEquals(4, atts.size());
        assertEquals("a", atts.get(0).getName());
        assertEquals("b", atts.get(1).getName());
        assertEquals("c", atts.get(2).getName());
        assertEquals("d", atts.get(3).getName());
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
