/*
 * TestDOMWriter.java
 * JUnit based test
 *
 * Created on 4 maart 2004, 9:50
 */

package org.dom4j.io;

import java.io.StringWriter;
import java.net.URL;
import junit.framework.*;
import junit.textui.TestRunner;
import org.dom4j.AbstractTestCase;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author Maarten
 */
public class TestDOMWriter extends AbstractTestCase {
    
    public TestDOMWriter(java.lang.String testName) {
        super(testName);
    }
    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TestDOMWriter.class);
        return suite;
    }
    
    // TODO add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    public void testNamespaceBug() throws Exception {
        org.dom4j.Document doc = parseDocument("/xml/namespaces.xml");
        DOMWriter writer = new DOMWriter(org.dom4j.dom.DOMDocument.class);
        org.w3c.dom.Document result = writer.write(doc);
        
        NamedNodeMap atts = result.getDocumentElement().getAttributes();
        assertEquals(4, atts.getLength());
        
        XMLWriter wr = new XMLWriter();
        wr.setOutputStream(System.out);
        wr.write((org.dom4j.Document) result);
    }
    
    public void testBug905745() throws Exception {
        org.dom4j.Document doc = parseDocument("/xml/namespaces.xml");
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
        org.dom4j.Document doc = parseDocument("/xml/test/defaultNamespace.xml");
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
        assertEquals("<a xmlns=\"dummyNamespace\"><b><c>Hello</c></b></a>", strWriter.toString());
    }

    protected org.dom4j.Document parseDocument(String file) throws Exception {
        URL url = getClass().getResource(file);
        SAXReader reader = new SAXReader();
        return reader.read(url);
    }

}
