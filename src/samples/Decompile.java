
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;

import org.dom4j.*;
import org.dom4j.io.BinaryReader;
import org.dom4j.io.XMLWriter;

/** Decompiles a binary format DOM4J tree then writes the XML as a text file
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class Decompile extends SAXDemo {
    
    protected String outputFileName;
    
    
    public Decompile() {
    }
        
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new Decompile(), args );
    }    
    
    public void run(String[] args) throws Exception {    
        if ( args.length < 1 ) {
            printUsage( "<Binary XML document URL> [<Output XML text file>] [<SAX XMLReader Class Name>]" );
            return;
        }

        String xmlFile = args[0];        
        outputFileName = (args.length > 1) 
            ? args[1] : null;
        
        xmlReaderClassName = (args.length > 2) 
            ? args[2] : DEFAULT_XMLREADER_CLASSNAME;

        println( "Decompiling binary XML file: " + xmlFile + 
            ((outputFileName != null ) 
                ? " into XML text file: " + outputFileName
                : " to console" )
        );
        
        parse( xmlFile );
    }
    
    protected void parse( URL url ) throws Exception {
        BinaryReader reader = new BinaryReader();
        Document document = reader.read(url);
        process(document);
    }
    
    protected void process(Document document) throws Exception {
        println( "Document is: " + document );
        println( "Root is: " + document.getRootElement() );
        
        XMLWriter writer = createXMLWriter();
        if ( outputFileName == null ) {
            writer.output(document, System.out);
        }
        else {
            BufferedWriter out = new BufferedWriter(new FileWriter(outputFileName));
            try {
                writer.output(document, out);
            }
            finally {
                try {
                    out.close();
                }
                catch (Exception e) {
                }
            }
        }
    }
}