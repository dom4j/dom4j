package org.dom4j;

import org.testng.annotations.Test;

/**
 * @author Filip Jirsák
 */
public class AllowedCharsTest {
    @Test
    public void localName() {
        QName.get("element");
        QName.get(":element");
        QName.get("elem:ent");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void localNameFail() {
        QName.get("!element");
    }

    @Test
    public void qname() {
        QName.get("element", "http://example.com/namespace");
        QName.get("ns:element", "http://example.com/namespace");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void qnameFail1() {
        QName.get("ns:elem:ent", "http://example.com/namespace");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void qnameFail2() {
        QName.get(":nselement", "http://example.com/namespace");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createElementLT() {
        DocumentHelper.createElement("element<name");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createElementGT() {
        DocumentHelper.createElement("element>name");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void createElementAmpersand() {
        DocumentHelper.createElement("element&name");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addElement() {
        Element root = DocumentHelper.createElement("root");
        root.addElement("element>name");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addElementQualified() {
        Element root = DocumentHelper.createElement("root");
        root.addElement("element>name", "http://example.com/namespace");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addElementQualifiedPrefix() {
        Element root = DocumentHelper.createElement("root");
        root.addElement("ns:element>name", "http://example.com/namespace");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void addElementPrefix() {
        Element root = DocumentHelper.createElement("root");
        root.addElement("ns>:element", "http://example.com/namespace");
    }

    //TODO It is illegal to create element or attribute with namespace prefix and empty namespace IRI.
    //See https://www.w3.org/TR/2006/REC-xml-names11-20060816/#scoping
}
