
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.DOMWriter;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class PerfDOM4JAttr {

    public static void main(String args[]) {
        Document doc;

        try {
            int numrec = 1;

            numrec = 10000;
            System.out.println("\n1000 Elements -------------------");
            doc = PerfDOM4JAttr.createDocument(numrec, 20, 1);
            PerfDOM4JAttr.createW3CDOM(doc);
            PerfDOM4JAttr.write(doc, "DOM4JAttr_" + numrec + ".xml");
            PerfDOM4JAttr.parse(numrec, 1);
            // PerfDOM4JAttr.transform(doc,"item.xslt",1);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"1\"]", 3);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"500\"]", 3);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"999\"]", 3);
            PerfDOM4JAttr
                    .xpathNodes(doc, "/ItemResultSet/Item[@guid=\"1\"]", 3);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"500\"]",
                    3);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"999\"]",
                    3);
            PerfDOM4JAttr.xpathNodes(doc, "/*/Item", 100);

            numrec = 1000;
            System.out.println("\n1000 Elements -------------------");
            doc = PerfDOM4JAttr.createDocument(numrec, 20, 1);
            PerfDOM4JAttr.createW3CDOM(doc);
            PerfDOM4JAttr.write(doc, "DOM4JAttr_" + numrec + ".xml");
            PerfDOM4JAttr.parse(numrec, 3);
            PerfDOM4JAttr.transform(doc, "item.xslt", 3);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"1\"]", 3);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"500\"]", 3);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"999\"]", 3);
            PerfDOM4JAttr
                    .xpathNodes(doc, "/ItemResultSet/Item[@guid=\"1\"]", 3);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"500\"]",
                    3);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"999\"]",
                    3);
            PerfDOM4JAttr.xpathNodes(doc, "/*/Item", 100);

            numrec = 100;
            System.out.println("\n100 Elements --------------------");
            doc = PerfDOM4JAttr.createDocument(numrec, 20, 10);
            PerfDOM4JAttr.createW3CDOM(doc);
            PerfDOM4JAttr.write(doc, "DOM4JAttr_" + numrec + ".xml");
            PerfDOM4JAttr.parse(numrec, 10);
            PerfDOM4JAttr.transform(doc, "item.xslt", 10);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"1\"]", 10);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"50\"]", 10);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"99\"]", 10);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"1\"]",
                    10);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"50\"]",
                    10);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"99\"]",
                    10);
            PerfDOM4JAttr.xpathNodes(doc, "/*/Item", 100);

            numrec = 10;
            System.out.println("\n10 Elements ---------------------");
            doc = PerfDOM4JAttr.createDocument(numrec, 20, 100);
            PerfDOM4JAttr.createW3CDOM(doc);
            PerfDOM4JAttr.write(doc, "DOM4JAttr_" + numrec + ".xml");
            PerfDOM4JAttr.parse(numrec, 100);
            PerfDOM4JAttr.transform(doc, "item.xslt", 10);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"1\"]", 100);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"5\"]", 100);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"9\"]", 100);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"1\"]",
                    100);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"5\"]",
                    100);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"9\"]",
                    100);
            PerfDOM4JAttr.xpathNodes(doc, "/*/Item", 100);

            numrec = 1;
            System.out.println("\n1 Element -----------------------");
            doc = PerfDOM4JAttr.createDocument(numrec, 20, 100);
            PerfDOM4JAttr.createW3CDOM(doc);
            PerfDOM4JAttr.write(doc, "DOM4JAttr_" + numrec + ".xml");
            PerfDOM4JAttr.parse(numrec, 100);
            PerfDOM4JAttr.transform(doc, "item.xslt", 10);
            PerfDOM4JAttr.xpath(doc, "/ItemResultSet/Item[@guid=\"1\"]", 100);
            PerfDOM4JAttr.xpathNodes(doc, "/ItemResultSet/Item[@guid=\"1\"]",
                    100);
            PerfDOM4JAttr.xpathNodes(doc, "/*/Item", 100);

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public static Document createDocument(int iNumRecs, int iNumFlds, int pp) {

        double start = System.currentTimeMillis();
        Document document = null;
        for (int kk = 0; kk < pp; kk++) {
            document = DocumentHelper.createDocument();

            Element root = document.addElement("ItemResultSet");
            for (int ii = 0; ii < iNumRecs; ii++) {

                Element Record = root.addElement("Item").addAttribute("guid",
                        "" + ii);
                for (int jj = 0; jj < iNumFlds; jj++) {
                    Record.addAttribute("Attr" + jj, "123456789");
                }

            }
        }
        double end = System.currentTimeMillis();

        System.err.println("Creation time  			:  " + (end - start) / pp);

        return document;
    }

    public static Document parse(int iNumRecs, int kk) {

        File file = new File("DOM4JAttr_" + iNumRecs + ".xml");
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

        System.err.println("Parsing time for 		:" + 1.000 * (end - start) / kk);

        return document;
    }

    public static void createW3CDOM(Document doc) {

        long start = System.currentTimeMillis();
        try {
            DOMWriter dw = new DOMWriter();
            dw.write(doc);

        } catch (Exception de) {
        }

        long end = System.currentTimeMillis();

        System.err.println("W3C Creation time for 		:" + (end - start));
    }

    public static void write(Document document, String name) throws IOException {

        long start = System.currentTimeMillis();
        // lets write to a file

        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(new FileWriter(name), format);
            writer.write(document);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();

        System.err.println("DOM4JAttr File write time 		:" + (end - start)
                + "  " + name);
    }

    public static void transform(Document xmlDoc, String xslFile, int kk) {

        System.err.println("DOM4JAttr start transform ");
        int ii = 1;
        try {
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(
                    xslFile));

            long start = System.currentTimeMillis();
            for (ii = 0; ii < kk; ii++) {
                Source source = new DocumentSource(xmlDoc);
                DocumentResult result = new DocumentResult();
                transformer.transform(source, result);

                // output the transformed document
            }
            long end = System.currentTimeMillis();

            System.err.println("DOM4JAttr transform  time 		:" + (end - start)
                    / ii);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void xpath(Document document, String xpathExp, int kk) {

        long start = System.currentTimeMillis();
        XPath xpath = document.createXPath(xpathExp);
        for (int ii = 0; ii < kk; ii++) {
            Node node = xpath.selectSingleNode(document);

            if ((node != null) & (ii == 0)) {
                String val = node.getStringValue();
                // System.out.println("xpath OK:"+val);
            }
        }

        long end = System.currentTimeMillis();
        System.err.println("DOM4JAttr xpath  time 		:" + (end - start) / kk);
    }

    public static void xpathNodes(Document document, String xpathExp, int kk) {

        long start = System.currentTimeMillis();
        XPath xpath = document.createXPath(xpathExp);

        for (int ii = 0; ii < kk; ii++) {

            try {

                List nodeList = xpath.selectNodes(document);
                if ((nodeList != null) && (nodeList.size() > 0)) {
                    Node node = (Node) nodeList.get(0);
                    if ((node != null) & (ii == 0)) {
                        String val = node.getStringValue();
                        // System.out.println("xpathNodes OK:"+val);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }

        }

        long end = System.currentTimeMillis();

        System.err.println("DOM4JAttr xpath Nodes time 		:" + 1.000
                * (end - start) / kk);
    }

}
