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
    
    public void testDeclaredNamespaces() throws Exception {
        String xml = "<a xmlns:ns1=\"uri1\">" +
                     "    <ns1:b/>" +
                     "    <ns2:c xmlns:ns2=\"uri2\"/>" +
                     "</a>";
        Document doc = DocumentHelper.parseText(xml);
        
        Element a = doc.getRootElement();
        List ns = a.declaredNamespaces();
        assertEquals(1, ns.size());
        assertSame(a.getNamespaceForPrefix("ns1"), ns.get(0));
        
        Element b = a.element("b");
        ns = b.declaredNamespaces();
        assertEquals(0, ns.size());
        
        Element c = a.element("c");
        ns = c.declaredNamespaces();
        assertEquals(1, ns.size());
        assertSame(c.getNamespaceForPrefix("ns2"), ns.get(0));
    }
    
    public void testAdditionalNamespaces() throws Exception {
        String xml = "<a xmlns:ns1=\"uri1\">" +
                     "    <ns1:b/>" +
                     "    <ns2:c xmlns:ns2=\"uri2\"/>" +
                     "</a>";
        Document doc = DocumentHelper.parseText(xml);
        
        Element a = doc.getRootElement();
        List ns = a.additionalNamespaces();
        assertEquals(1, ns.size());
        assertSame(a.getNamespaceForPrefix("ns1"), ns.get(0));
        
        Element b = a.element("b");
        ns = b.additionalNamespaces();
        assertEquals(0, ns.size());
        
        Element c = a.element("c");
        ns = c.additionalNamespaces();
        assertEquals(0, ns.size());
    }
}
