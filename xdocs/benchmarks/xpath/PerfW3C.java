
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.apache.xpath.XPathAPI;

import org.dom4j.io.SAXReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PerfW3C {

    public static void main(String args[]) {
        Document doc;
        System.err.println("W3C createDocument:");

        int numrec;
        long start = 0;
        long end = 0;

        numrec = 1000;
        System.out.println("\n1000 Elements ---------------------------------");
        doc = PerfW3C.createDocument(numrec, 20, 1);
        PerfW3C.write(doc, "w3c_" + numrec + ".xml");
        PerfW3C.parse(numrec, 1);
        PerfW3C.transform(doc, "item.xslt", 1);
        PerfW3C.xpath(doc, "/*/*/Attr1x1", 1);
        PerfW3C.xpath(doc, "/*/*/Attr1x500", 1);
        PerfW3C.xpath(doc, "/*/*/Attr1x999", 1);
        PerfW3C.xpathNodes(doc, "/*/*/Attr1x1", 1);
        PerfW3C.xpathNodes(doc, "/*/*/Attr1x500", 1);
        PerfW3C.xpathNodes(doc, "/*/*/Attr1x999", 1);
        PerfW3C.xpathNodes(doc, "/*/Item", 1);

        numrec = 100;
        System.out.println("\n100 Elements ----------------------------------");
        doc = PerfW3C.createDocument(numrec, 20, 1);
        PerfW3C.write(doc, "w3c_" + numrec + ".xml");
        PerfW3C.transform(doc, "item.xslt", 10);
        PerfW3C.parse(numrec, 10);
        PerfW3C.xpath(doc, "/*/*/Attr0x1", 10);
        PerfW3C.xpath(doc, "/*/*/Attr0x50", 10);
        PerfW3C.xpath(doc, "/*/*/Attr0x99", 10);
        PerfW3C.xpathNodes(doc, "/*/*/Attr0x0", 10);
        PerfW3C.xpathNodes(doc, "/*/*/Attr1x50", 10);
        PerfW3C.xpathNodes(doc, "/*/*/Attr1x99", 10);
        PerfW3C.xpathNodes(doc, "/*/Item", 10);

        numrec = 10;
        System.out.println("\n10 Elements -----------------------------------");
        doc = PerfW3C.createDocument(numrec, 20, 10);
        PerfW3C.write(doc, "w3c_" + numrec + ".xml");
        PerfW3C.parse(numrec, 50);
        PerfW3C.transform(doc, "item.xslt", 10);
        PerfW3C.xpath(doc, "/*/*/Attr5", 100);
        PerfW3C.xpathNodes(doc, "/*/*/Attr1x5", 100);
        PerfW3C.xpathNodes(doc, "/*/Item", 100);

        numrec = 1;
        System.out.println("\n1 Elements ------------------------------------");
        doc = PerfW3C.createDocument(numrec, 20, 10);
        PerfW3C.write(doc, "w3c_" + numrec + ".xml");
        PerfW3C.parse(numrec, 100);
        PerfW3C.transform(doc, "item.xslt", 10);
        PerfW3C.xpath(doc, "/*/*/Attr1x0", 100);
        PerfW3C.xpathNodes(doc, "/*/*/Attr1x0", 100);
        PerfW3C.xpathNodes(doc, "/*/Item", 100);

    }

    public static Document createDocument(int iNumRecs, int iNumFlds, int pp) {

        double start = System.currentTimeMillis();
        Document document = null;
        for (int kk = 0; kk < pp; kk++) {
            document = new DocumentImpl();

            Element root = document.createElement("ItemResultSet"); // Create
                                                                    // Root
                                                                    // Element
            document.appendChild(root);

            for (int ii = 0; ii < iNumRecs; ii++) {

                Element Record = document.createElement("Item");
                root.appendChild(Record);
                for (int jj = 0; jj < iNumFlds; jj++) {
                    /*
                     * AttrImpl a =
                     * (AttrImpl)document.createAttribute("Attr"+jj);
                     * a.setNodeValue("123456789"); Record.setAttributeNode(a);
                     */
                    Element field = document.createElement("Attr" + jj + "x"
                            + ii);
                    field.appendChild(document.createTextNode("123456789"));
                    Record.appendChild(field);

                }
            }
        }

        double end = System.currentTimeMillis();

        System.err.println("Creation time  		  :" + (end - start) / pp);

        return document;
    }

    public static void write(Document document, String name) {

        long start = System.currentTimeMillis();
        // lets write to a file

        OutputFormat format = new OutputFormat(document); // Serialize DOM
        format.setIndent(2);
        format.setLineSeparator(System.getProperty("line.separator"));
        format.setLineWidth(80);
        try {

            FileWriter writer = new FileWriter(name);
            BufferedWriter buf = new BufferedWriter(writer);
            XMLSerializer FileSerial = new XMLSerializer(writer, format);
            FileSerial.asDOMSerializer(); // As a DOM Serializer
            FileSerial.serialize(document);

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }

        long end = System.currentTimeMillis();

        System.err.println("W3C File write time :" + (end - start) + "  "
                + name);
    }

    public static Document parse(int iNumRecs, int kk) {

        File file = new File("dom4j_" + iNumRecs + ".xml");
        double start = System.currentTimeMillis();
        Document document = null;

        for (int pp = 0; pp < kk; pp++) {
            try {
                SAXReader SAXrd = new SAXReader();
                SAXrd.read(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        double end = System.currentTimeMillis();

        // System.err.println("DOM4J createDocument:" + "Num Rec. = " + iNumRecs
        // + " Num. Fld.=" + iNumFlds);
        System.err.println("Parsing time for 			:" + iNumRecs + "  "
                + (end - start) / kk);

        return document;
    }

    public static void transform(Document xmlDoc, String xslFile, int kk) {
        int ii = 1;
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(
                    xslFile));

            long start = System.currentTimeMillis();
            for (ii = 0; ii < kk; ii++) {
                DOMSource source = new DOMSource(xmlDoc);
                DOMResult result = new DOMResult();
                transformer.transform(source, result);
            }
            long end = System.currentTimeMillis();

            System.err.println("W3C transform  time :" + (end - start) / ii);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void xpath(Document document, String xpathExp, int pp) {

        long start = System.currentTimeMillis();

        for (int ii = 0; ii < pp; ii++) {

            try {

                Node node = XPathAPI.selectSingleNode(document, xpathExp);
                if ((node != null) & (ii == 0)) {
                    String val = node.getNodeName();
                    // System.out.println(val);
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        long end = System.currentTimeMillis();

        System.err.println("W3C xpath  time :" + 1.000 * (end - start) / pp);
    }

    public static void xpathNodes(Document document, String xpathExp, int pp) {

        long start = System.currentTimeMillis();

        for (int ii = 0; ii < pp; ii++) {

            try {

                NodeList nodeList = XPathAPI.selectNodeList(document, xpathExp);
                if ((nodeList != null) && (nodeList.getLength() > 0)) {
                    Node node = nodeList.item(0);
                    if ((node != null) & (ii == 0)) {
                        String val = node.getNodeName();
                        // System.out.println(val);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        long end = System.currentTimeMillis();

        System.err.println("W3C xpathNodes  time :" + 1.000 * (end - start)
                / pp);
    }

}
