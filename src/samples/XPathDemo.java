
import java.util.List;

import org.dom4j.*;

/** A sample program to demonstrate the use of XPath expressions.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XPathDemo extends SAXDemo {
    
    protected String xpath = "*";
    
    
    public XPathDemo() {
    }
        
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new XPathDemo(), args );
    }    
    
    public void run(String[] args) throws Exception {    
        if ( args.length < 2 ) {
            printUsage( "<XML document URL> <XPath expression> [<SAX XMLReader Class Name>]" );
            return;
        }

        String xmlFile = args[0];
        xpath = args[1];
        String xmlReaderClassName = (args.length > 2) 
            ? args[2] : DEFAULT_XMLREADER_CLASSNAME;
        
        parse( xmlFile, xmlReaderClassName );
    }
    
    protected void process(Document document) throws Exception {
        println( "Evaluating XPath: " + xpath );
        
        List list = document.selectNodes( xpath );
        
        println( "Found: " + list.size() + " node(s)" );
        println( "List:  " + list );
    }
}