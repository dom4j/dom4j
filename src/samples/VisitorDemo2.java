
import java.net.URL;

import org.dom4j.*;
import org.dom4j.io.TreeReader;
import org.dom4j.io.SAXReader;

/** This demo uses the Visitor Pattern in DOM4J to display the effect
  * of changing the {@link DocumentFactory} used when reading a DOM4J 
  * object model from a {@link SAXReader}.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class VisitorDemo2 extends VisitorDemo {
    
    /** The DocumentFactory class name to use */
    protected String documentFactoryClassName;
    
    public VisitorDemo2() {
    }
        
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new VisitorDemo2(), args );
    }    
    
    public void run(String[] args) throws Exception {    
        if ( args.length < 1 ) {
            printUsage( "<XML document URL> [<Document Factory Class Name>] [<SAX XMLReader Class Name>]" );
            return;
        }

        String xmlFile = args[0];
        
        documentFactoryClassName = (args.length > 1) 
            ? args[1] : null;
            
        xmlReaderClassName = (args.length > 2) 
            ? args[2] : DEFAULT_XMLREADER_CLASSNAME;
        
        parse( xmlFile );
    }
    
    protected void parse( URL url ) throws Exception {
        TreeReader reader = createTreeReader();
        Document document = reader.read(url);
        process(document);
    }
    
    protected TreeReader createTreeReader() throws Exception {
        println( "Using SAX parser: " + xmlReaderClassName );
        
        TreeReader answer = new SAXReader( xmlReaderClassName );        
        if ( documentFactoryClassName != null ) {
            try {
                Class theClass = Class.forName( documentFactoryClassName );
                DocumentFactory factory = (DocumentFactory) theClass.newInstance();
                if ( factory != null ) {
                    println( "DocumentFactory:  " + factory );
                    answer.setDocumentFactory( factory );
                }
            }
            catch (Exception e) {
                println( "ERROR: Failed to create an instance of DocumentFactory: " + documentFactoryClassName );
                println( "Exception: " + e );
                e.printStackTrace();
            }
        }
        return answer;
    }
}