
import org.dom4j.*;
import org.dom4j.io.HTMLWriter;
import org.dom4j.io.XMLWriter;

/** A demonstration of the use of <code>HTMLWriter</code> to display
  * XHTML documents to HTML aware browsers.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class HTMLWriterDemo extends SAXDemo {
    
    public HTMLWriterDemo() {
    }
    
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new HTMLWriterDemo(), args );
    }    
    
    /** A Factory Method to create an <code>XMLWriter</code>
      * instance allowing derived classes to change this behaviour
      */
    protected XMLWriter createXMLWriter() {
        return new HTMLWriter();
    }
    
}