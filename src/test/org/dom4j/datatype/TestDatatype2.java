/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 *
 * $Id$
 */

package org.dom4j.datatype;

import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/** Test harness for XML Schema Datatypes support
  *
  * @author Yuxin Ruan
  * @version $Revision$
  */
public class TestDatatype2 extends TestCase {

    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }

    public static Test suite() {
        return new TestSuite(TestDatatype2.class);
    }

    public TestDatatype2(String name) {
        super(name);
    }

    public void setUp() {
    }

    public void tearDown() {
    }

    public void testSchema() throws Exception {
        Document schema=getSchema();
        validateDocumentWithSchema(schema);
    }

    public void testSchemaWithNamedComplexType() throws Exception {
        Document schema=getSchemaWithNamedComplexType();
        validateDocumentWithSchema(schema);
    }

    public void testSchemaWithReference() throws Exception {
        Document schema=getSchemaWithReference();
        validateDocumentWithSchema(schema);
    }

    public void testSchemaWithNamedSimpleType() throws Exception {
        Document schema=getSchemaWithNamedSimpleType();
        validateDocumentWithSchema(schema);
    }

    private void validateDocumentWithSchema(Document schema) throws Exception {
        Document doc=getSource(schema);
        Element root=doc.getRootElement();
        validateLongAttribute(root);
        validateFloatElement(root);
        validateDateElement(root);
    }

    private void validateLongAttribute(Element root) throws Exception {
        Attribute attr=root.attribute("longAttribute");
        Object attrData=attr.getData();
        validateData("testLongAttribute",attrData,new Long(123));
        System.out.println("retrieved attribute "+attrData);
    }

    private void validateFloatElement(Element root) throws Exception {
        Element elem=root.element("floatElement");
        Object elemData=elem.getData();
        validateData("testFloatElement",elemData,new Float(1.23));
        System.out.println("retrieved element:"+elemData);
    }

    private void validateDateElement(Element root) throws Exception {
        Element elem = root.element("dateElement");
        Object elemData = elem.getData();
        Calendar expected = getDate();
        
        System.out.println("retrieved element:" + elemData);
        
        // don't compare the Calendar instances, compare their strings instead!
        assertTrue(elemData instanceof Calendar);
        Calendar elemCal = (Calendar) elemData;

        DateFormat format = new SimpleDateFormat("MM/dd/yyyyZ");
        format.setTimeZone(elemCal.getTimeZone());
        String elemStr = format.format(elemCal.getTime());
        
        format.setTimeZone(expected.getTimeZone());
        String expectedStr = format.format(expected.getTime());

        assertEquals("testDateElement", expectedStr, elemStr);
    }

    private void validateData(String testName, Object retrieved, Object expected)
            throws Exception {
        Class retrievedClass=retrieved.getClass();
        Class expectedClass=expected.getClass();

        //compare class
        if (!expectedClass.equals(retrievedClass)) {
            String msg="class mismatch in "+testName+
                ":expected "+expectedClass+
                ", retrieved "+retrievedClass;
            throw new Exception(msg);
        }

        //compare value
        if (!expected.equals(retrieved)) {
            String msg="value mismatch in "+testName+
                ":expected "+expected+
                ", retrieved "+retrieved;
            throw new Exception(msg);
        }
    }

    private Document getSource(Document schema) throws Exception {
        StringBuffer buffer=new StringBuffer();
        buffer.append("<?xml version='1.0' ?>");
        buffer.append("<test xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'");
        buffer.append("         xsi:noNamespaceSchemaLocation='long.xsd'");
        buffer.append("         longAttribute='123' >");
        buffer.append("     <floatElement>1.23</floatElement>");
        buffer.append("     <dateElement>"+getDateString()+"</dateElement>");
        buffer.append("</test>");

        StringReader in=new StringReader(buffer.toString());
        DatatypeDocumentFactory docFactory=new DatatypeDocumentFactory();
        docFactory.loadSchema(schema);
        SAXReader parser=new SAXReader(docFactory);
        return parser.read(in);
    }

    private Document getSchema() throws Exception {
        StringBuffer buffer=new StringBuffer();
        buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
        buffer.append("<xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>");
        buffer.append("     <xsd:element name='test'>");
        buffer.append("         <xsd:complexType>");
        buffer.append("             <xsd:sequence>");
        buffer.append("                 <xsd:element name='floatElement' type='xsd:float' />");
        buffer.append("                 <xsd:element name='dateElement' type='xsd:date' />");
        buffer.append("             </xsd:sequence>");
        buffer.append("             <xsd:attribute name='longAttribute' type='xsd:long' />");
        buffer.append("         </xsd:complexType>");
        buffer.append("     </xsd:element>");
        buffer.append("</xsd:schema>");

        StringReader in=new StringReader(buffer.toString());
        SAXReader parser=new SAXReader();
        return parser.read(in);
    }

    private Document getSchemaWithNamedComplexType() throws Exception {
        StringBuffer buffer=new StringBuffer();
        buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
        buffer.append("<xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>");
        buffer.append("     <xsd:element name='test' type='TimePeriodType' />");

        buffer.append("     <xsd:complexType name='TimePeriodType'>");
        buffer.append("         <xsd:sequence>");
        buffer.append("             <xsd:element name='floatElement' type='xsd:float' />");
        buffer.append("             <xsd:element name='dateElement' type='xsd:date' />");
        buffer.append("         </xsd:sequence>");
        buffer.append("         <xsd:attribute name='longAttribute' type='xsd:long' />");
        buffer.append("     </xsd:complexType>");
        buffer.append("</xsd:schema>");

        StringReader in=new StringReader(buffer.toString());
        SAXReader parser=new SAXReader();
        return parser.read(in);
    }

    private Document getSchemaWithReference() throws Exception {
        StringBuffer buffer=new StringBuffer();
        buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
        buffer.append("<xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>");
        buffer.append("     <xsd:element name='test' type='TimePeriodType' />");

        buffer.append("     <xsd:complexType name='TimePeriodType'>");
        buffer.append("         <xsd:sequence>");
        buffer.append("             <xsd:element name='floatElement' type='xsd:float' />");
        buffer.append("             <xsd:element ref='dateElement' />");
        buffer.append("         </xsd:sequence>");
        buffer.append("         <xsd:attribute name='longAttribute' type='xsd:long' />");
        buffer.append("     </xsd:complexType>");

        buffer.append("     <xsd:element name='dateElement' type='xsd:date' />");
        buffer.append("</xsd:schema>");

        StringReader in=new StringReader(buffer.toString());
        SAXReader parser=new SAXReader();
        return parser.read(in);
    }

    private Document getSchemaWithNamedSimpleType() throws Exception {
        StringBuffer buffer=new StringBuffer();
        buffer.append("<?xml version='1.0' encoding='UTF-8'?>");
        buffer.append("<xsd:schema xmlns:xsd='http://www.w3.org/2001/XMLSchema'>");
        buffer.append("     <xsd:element name='test'>");
        buffer.append("         <xsd:complexType>");
        buffer.append("             <xsd:sequence>");
        buffer.append("                 <xsd:element name='floatElement' type='xsd:float' />");
        buffer.append("                 <xsd:element name='dateElement' type='dateType' />");
        buffer.append("             </xsd:sequence>");
        buffer.append("             <xsd:attribute name='longAttribute' type='xsd:long' />");
        buffer.append("         </xsd:complexType>");
        buffer.append("     </xsd:element>");

        buffer.append("     <xsd:simpleType name='dateType'>");
        buffer.append("         <xsd:restriction base='xsd:date'>");
        //buffer.append("             <whiteSpace value='collapse' />");
        buffer.append("         </xsd:restriction>");
        buffer.append("     </xsd:simpleType>");
        buffer.append("</xsd:schema>");

        StringReader in=new StringReader(buffer.toString());
        SAXReader parser=new SAXReader();
        return parser.read(in);
    }

    private static String getDateString() {
        //return dateTime in ISO8601 format
        String yyyy=Integer.toString(year);
        String mm=Integer.toString(month);
        String dd=Integer.toString(date);
        return yyyy+"-"+mm+"-"+dd+"Z";
    }

    private static Calendar getDate() {
        Calendar calendar=new GregorianCalendar();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, date);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.setTimeZone(new SimpleTimeZone(0, "XSD 'Z' timezone"));
        return calendar;
    }

    public static final int year=2001;
    public static final int month=10;
    public static final int date=31;
}




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project - 
 *    http://www.dom4j.org
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
