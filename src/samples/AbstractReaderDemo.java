
import java.net.URL;

import org.dom4j.*;
import org.dom4j.io.TreeReader;
import org.dom4j.io.SAXReader;

/** This is an abstract base class for any demo which uses a 
  * {@link TreeReader} implementation such as {@link SAXReader} and parses
  * and processes a document.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class AbstractReaderDemo extends SAXDemo {
    
    /** The DocumentFactory class name to use */
    protected String documentFactoryClassName;
    
    public AbstractReaderDemo() {
    }
        
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new AbstractReaderDemo(), args );
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
            ? args[2] : null;
        
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
        
        // allow the DocumentFactory used by the TreeReader to be configured
        DocumentFactory factory = createDocumentFactory();
        if ( factory != null ) {
            println( "DocumentFactory:  " + factory );
            answer.setDocumentFactory( factory );
        }
        return answer;
    }
    
    protected DocumentFactory createDocumentFactory() {
        if ( documentFactoryClassName != null ) {
            try {
                Class theClass = Class.forName( documentFactoryClassName );
                return (DocumentFactory) theClass.newInstance();
            }
            catch (Exception e) {
                println( "ERROR: Failed to create an instance of DocumentFactory: " + documentFactoryClassName );
                println( "Exception: " + e );
                e.printStackTrace();
            }
        }
        return null;
    }
}