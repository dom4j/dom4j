
import java.net.URL;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

/** This sample parses a big document using the pruning option of the 
  * {@link SAXReader}.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class LargeDocumentDemo extends SAXDemo implements ElementHandler {
    
    protected String pruningPath;
    
    public LargeDocumentDemo() {
    }
        
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new LargeDocumentDemo(), args );
    }    
    
    public void run(String[] args) throws Exception {    
        if ( args.length < 2 ) {
            printUsage( "<XML document URL> <pruningPath>" );
            return;
        }

        String xmlFile = args[0];
        pruningPath = args[1];
        
        parse( xmlFile );
    }
    
    // ElementHandler interface     
    public void handle(Element element) {
        println( "Called during parsing with element: " + element 
            + " with: " + element.getContent().size() + " content node(s)" 
        );
    }
    
    protected void parse( URL url ) throws Exception {
        SAXReader reader = new SAXReader();        

        println( "Parsing document:   " + url );
        println( "Using Pruning Path: " + pruningPath );
        
        // enable pruning to call me back as each Element is complete
        reader.setPruningMode( pruningPath, this );
        
        Document documemnt = reader.read(url);
        
        // the document will be complete but have the prunePath elements pruned
    }
}