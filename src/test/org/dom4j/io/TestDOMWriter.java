/*
 * TestDOMWriter.java
 * JUnit based test
 *
 * Created on 4 maart 2004, 9:50
 */

package org.dom4j.io;

import junit.framework.*;
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
    
    public static Test suite() {
        TestSuite suite = new TestSuite(TestDOMWriter.class);
        return suite;
    }
    
    // TODO add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    public void testNamespaceBug() throws Exception {
        org.dom4j.Document doc = parseDocument("xml/namespaces.xml");
        DOMWriter writer = new DOMWriter(org.dom4j.dom.DOMDocument.class);
        org.w3c.dom.Document result = writer.write(doc);
        
        NamedNodeMap atts = result.getDocumentElement().getAttributes();
        assertEquals(4, atts.getLength());
        
        XMLWriter wr = new XMLWriter();
        wr.setOutputStream(System.out);
        wr.write((org.dom4j.Document) result);
    }
    
    protected org.dom4j.Document parseDocument(String file) throws Exception {
        SAXReader reader = new SAXReader();
        return reader.read( file );
    }

}
