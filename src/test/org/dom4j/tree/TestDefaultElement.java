/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.util.List;
import junit.framework.*;
import junit.textui.TestRunner;
import org.dom4j.*;

/**
 *
 * @author Maarten Coene
 */
public class TestDefaultElement extends AbstractTestCase {

    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestDefaultElement.class );
    }
    
    public TestDefaultElement(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------
    public void testBug894878() {
        Element foo = DocumentFactory.getInstance().createElement("foo");
        foo.addText("bla").addAttribute("foo", "bar");
        assertEquals("<foo foo=\"bar\">bla</foo>", foo.asXML());
        
        foo = DocumentFactory.getInstance().createElement("foo");
        foo.addAttribute("foo", "bar").addText("bla");
        assertEquals("<foo foo=\"bar\">bla</foo>", foo.asXML());
    }
    
    public void testGetNamespacesForURI() throws Exception {
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
        Document doc = DocumentHelper.parseText(xml);
        Element schema = doc.getRootElement();
        List namespaces = schema.getNamespacesForURI("http://www.w3.org/2001/XMLSchema");
        
        assertNotNull(namespaces);
        assertEquals(2, namespaces.size());
    }
}
