/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 */

package org.dom4j.tree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.dom4j.AbstractTestCase;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.IllegalAddException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/** A test harness to test the addAttribute() methods on attributes
  *
  * @author <a href="mailto:maartenc@users.sourceforge.net">Maarten Coene</a>
  */
public class TestDefaultDocument extends AbstractTestCase {

    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestDefaultDocument.class );
    }
    
    public TestDefaultDocument(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------
    public void testDoubleRootElement() {
        Document document = DocumentFactory.getInstance().createDocument();
        document.addElement("root");
        
        Element root = DocumentFactory.getInstance().createElement("anotherRoot");
        try {
            document.add(root);
            fail();
        } catch (IllegalAddException e) {
            String msg = e.getMessage();
            assertTrue(msg.indexOf(root.toString()) != -1);
        }
    }
    
    public void testBug799656() throws Exception {
        Document document = DocumentFactory.getInstance().createDocument();
        Element el = document.addElement("root");
        el.setText("text with an \u00FC in it");  // u00FC is umlaut
        
        System.out.println(document.asXML());
        
        DocumentHelper.parseText(document.asXML());
    }
    
    public void testEncoding() throws Exception {
        Document document = DocumentFactory.getInstance().createDocument();
        Element el = document.addElement("root");
        el.setText("text with an \u00FC in it");  // u00FC is umlaut
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        OutputFormat of = OutputFormat.createPrettyPrint();
        of.setEncoding("koi8-r");
        XMLWriter writer = new XMLWriter(out, of);
        writer.write(document);
        
        String result = out.toString();
        
        System.out.println(result);
        
        DocumentHelper.parseText(result);
    }
    
}
