/*
 * TestDOMWriter.java
 * JUnit based test
 *
 * Created on 4 maart 2004, 9:50
 */

package org.dom4j.io;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.*;
import junit.textui.TestRunner;
import org.dom4j.AbstractTestCase;
import org.w3c.dom.NamedNodeMap;

/**
 *
 * @author Maarten
 */
public class TestDOMReader extends AbstractTestCase {
    
    public TestDOMReader(java.lang.String testName) {
        super(testName);
    }
    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TestDOMReader.class);
        return suite;
    }
    
    // TODO add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    public void testBug972737() throws Exception {
        String xml = 
                "<schema targetNamespace='http://SharedTest.org/xsd' " +
                "        xmlns='http://www.w3.org/2001/XMLSchema' " +
                "        xmlns:xsd='http://www.w3.org/2001/XMLSchema'>" +
                "    <complexType name='AllStruct'>" +
                "        <all>" +
                "            <element name='arString' type='xsd:string'/>" +
                "            <element name='varInt' type='xsd:int'/>" +
                "        </all>" +
                "    </complexType>" +
                "</schema>";

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));
        
        DOMReader reader = new DOMReader();
        org.dom4j.Document dom4jDoc = reader.read(doc);
        
        List namespaces = dom4jDoc.getRootElement().declaredNamespaces();
        assertEquals(2, namespaces.size());
        
        System.out.println(dom4jDoc.asXML());
    }

}
