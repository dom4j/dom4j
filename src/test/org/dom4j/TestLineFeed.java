/*
 * TestLineFeed.java
 * JUnit based test
 *
 * Created on 25 maart 2004, 15:00
 */

package org.dom4j;

import java.io.StringWriter;
import junit.framework.*;
import junit.textui.TestRunner;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class TestLineFeed extends AbstractTestCase {
    
    private static final String ATT_TEXT = "Hello&#xa;There&#xa;&lt;&gt;&amp;";
    private static final String TEXT = "Hello\nThere\n&lt;&gt;&amp;";
    private static final String EXPECTED_TEXT = "Hello\nThere\n<>&";
    private static final String EXPECTED_ATT_TEXT = "Hello There <>&";

    public TestLineFeed(String name) {
        super(name);
    }

    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestLineFeed.class );
    }
    
    public void testElement() throws Exception {
        Document doc = DocumentHelper.parseText("<elem>" + TEXT + "</elem>");
        Element elem = doc.getRootElement();
        assertEquals(EXPECTED_TEXT, elem.getText());
    }


    public void testAttribute() throws Exception {
        Document doc = DocumentHelper.parseText("<elem attr=\"" + TEXT + "\"/>");
        Element elem = doc.getRootElement();
        //System.out.println(elem.attributeValue("attr"));
        assertEquals(EXPECTED_ATT_TEXT, elem.attributeValue("attr"));
        
        doc = DocumentHelper.parseText("<elem attr=\"" + ATT_TEXT + "\"/>");
        elem = doc.getRootElement();
        //System.out.println(elem.attributeValue("attr"));
        assertEquals(EXPECTED_TEXT, elem.attributeValue("attr"));
    }


    public void testCDATA() throws Exception {
        Document doc = DocumentHelper.parseText("<elem><![CDATA[" + EXPECTED_TEXT + "]]></elem>");
        Element elem = doc.getRootElement();
        assertEquals(EXPECTED_TEXT, elem.getText());
    }
    
    
    public void testXmlWriter() throws Exception {
        Element elem = DocumentHelper.createElement("elem");
        Document doc = DocumentHelper.createDocument(elem);
        elem.addCDATA(EXPECTED_TEXT);
        StringWriter sw = new StringWriter();
        XMLWriter xWriter = new XMLWriter(sw, OutputFormat.createPrettyPrint());
        xWriter.write(doc);
        xWriter.close();
        String xmlString = sw.toString();
        doc = DocumentHelper.parseText(xmlString);
        elem = doc.getRootElement();
        assertEquals(EXPECTED_TEXT, elem.getText());
    }
}
