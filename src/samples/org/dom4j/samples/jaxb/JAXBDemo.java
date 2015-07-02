/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: JAXBDemo.java,v 1.3 2005/01/29 14:52:58 maartenc Exp $
 */

package org.dom4j.samples.jaxb;

import java.io.File;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.jaxb.JAXBModifier;
import org.dom4j.jaxb.JAXBReader;
import org.dom4j.jaxb.JAXBWriter;
import org.dom4j.test.primer.Items;
import org.dom4j.test.primer.ObjectFactory;
import org.dom4j.test.primer.PurchaseOrder;
import org.dom4j.test.primer.PurchaseOrders;
import org.dom4j.test.primer.USAddress;

/**
 * JAXB demo
 * 
 * @author Wonne Keysers (Realsoftware.be)
 */
public class JAXBDemo {

    private File outputDir = new File("build/test/");

    public static void main(String[] args) {
        JAXBDemo demo = new JAXBDemo();
        demo.init();

        demo.demoRead();
        demo.demoReadPrune();

        demo.demoWrite();

        demo.demoModify();
        demo.demoModifyWrite();
    }

    public void init() {
        outputDir.mkdirs();
    }

    public void demoRead() {
        try {
            File inputFile = new File("xml/jaxb/primer.xml");

            JAXBReader jaxbReader = new JAXBReader("org.dom4j.test.primer");

            System.out
                    .println("Fetched PurchaseOrders using JAXBObjectHandler:");
            jaxbReader.addObjectHandler("/purchaseOrders/purchaseOrder",
                    new PurchaseOrderHandler());
            Document doc = jaxbReader.read(inputFile);

            System.out.println("Fetched PurchaseOrders using DOM4J document:");
            Iterator orderIt = doc.selectNodes("/purchaseOrders/purchaseOrder")
                    .iterator();
            while (orderIt.hasNext()) {
                Element elem = (Element) orderIt.next();

                System.out.println("Order - id:" + elem.attributeValue("id")
                        + ", date:" + elem.attributeValue("orderDate"));
            }

            System.out.println("Document:");
            System.out.println(doc.asXML());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demoReadPrune() {
        try {
            File inputFile = new File("xml/jaxb/primer.xml");

            JAXBReader jaxbReader = new JAXBReader("org.dom4j.test.primer");

            System.out
                    .println("Fetched PurchaseOrders using JAXBObjectHandler:");
            jaxbReader.setPruneElements(true);
            jaxbReader.addObjectHandler("/purchaseOrders/purchaseOrder",
                    new PurchaseOrderHandler());
            Document doc = jaxbReader.read(inputFile);

            System.out.println("Pruned document:");
            System.out.println(doc.asXML());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demoWrite() {
        try {
            ObjectFactory factory = new ObjectFactory();

            PurchaseOrders orders = factory.createPurchaseOrders();

            // Order 1
            PurchaseOrder order = factory.createPurchaseOrder();

            USAddress billTo = factory.createUSAddress();
            billTo.setCity("Cambridge");
            billTo.setCountry("US");
            billTo.setName("Robert Smith");
            billTo.setState("MA");
            billTo.setStreet("8 Oak Avenue");
            billTo.setZip(new BigDecimal(12345));
            order.setBillTo(billTo);

            USAddress shipTo = factory.createUSAddress();
            shipTo.setCity("Cambridge");
            shipTo.setCountry("US");
            shipTo.setName("Alice Smith");
            shipTo.setState("MA");
            shipTo.setStreet("123 Maple Street");
            shipTo.setZip(new BigDecimal(12345));
            order.setShipTo(shipTo);

            Calendar orderDate = Calendar.getInstance();
            orderDate.set(2004, 06, 30);
            order.setOrderDate(orderDate);

            Items items = factory.createItems();
            order.setItems(items);

            orders.getPurchaseOrder().add(order);

            // Order 2
            PurchaseOrder order2 = factory.createPurchaseOrder();

            USAddress billTo2 = factory.createUSAddress();
            billTo2.setCity("Cambridge");
            billTo2.setCountry("US");
            billTo2.setName("Robert Smith");
            billTo2.setState("MA");
            billTo2.setStreet("8 Oak Avenue");
            billTo2.setZip(new BigDecimal(12345));
            order2.setBillTo(billTo2);

            USAddress shipTo2 = factory.createUSAddress();
            shipTo2.setCity("Cambridge");
            shipTo2.setCountry("US");
            shipTo2.setName("Alice Smith");
            shipTo2.setState("MA");
            shipTo2.setStreet("123 Maple Street");
            shipTo2.setZip(new BigDecimal(12345));
            order2.setShipTo(shipTo2);

            Calendar orderDate2 = Calendar.getInstance();
            orderDate2.set(2004, 06, 30);
            order2.setOrderDate(orderDate2);

            Items items2 = factory.createItems();
            order2.setItems(items2);

            orders.getPurchaseOrder().add(order2);

            File outputFile = new File(outputDir, "jaxbWrite.xml");

            JAXBWriter jaxbWriter = new JAXBWriter("org.dom4j.test.primer",
                    OutputFormat.createPrettyPrint());
            jaxbWriter.setOutput(outputFile);

            jaxbWriter.startDocument();
            jaxbWriter.write(orders);
            jaxbWriter.endDocument();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demoModify() {
        try {
            File inputFile = new File("xml/jaxb/primer.xml");

            JAXBModifier jaxbReader = new JAXBModifier("org.dom4j.test.primer");
            jaxbReader.addObjectModifier("/purchaseOrders/purchaseOrder",
                    new PurchaseOrderDateModifier());
            Document doc = jaxbReader.modify(inputFile);

            System.out.println("Modified document:");
            System.out.println(doc.asXML());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void demoModifyWrite() {
        try {
            File inputFile = new File("xml/jaxb/primer.xml");

            File outputFile = new File(outputDir, "testModifyWrite.xml");

            JAXBModifier jaxbModifier = new JAXBModifier(
                    "org.dom4j.test.primer", OutputFormat.createPrettyPrint());
            jaxbModifier.setPruneElements(true);
            jaxbModifier.setOutput(outputFile);
            jaxbModifier.addObjectModifier("/purchaseOrders/purchaseOrder",
                    new PurchaseOrderDateModifier());
            Document doc = jaxbModifier.modify(inputFile);

            System.out.println("Pruned modified document:");
            System.out.println(doc.asXML());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * $Id: JAXBDemo.java,v 1.3 2005/01/29 14:52:58 maartenc Exp $
 */
