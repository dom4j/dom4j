import java.net.URL;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.TreeException;
import org.dom4j.io.BinaryReader;
import org.dom4j.io.TreeReader;

/** Perform some DOM4J parsing peformance test cases with the binary XML format.
  * 
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class PerformanceTest2 extends PerformanceTest {
    
    public PerformanceTest2() {
    }
    
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new PerformanceTest2(), args );
    }    
    
    public void run(String[] args) throws Exception {    
        if ( args.length < 1 ) {
            printUsage( "<Binary XML document URL> [<loopCount>]" );
            return;
        }

        String xmlFile = args[0];
        
        loopCount = DEFAULT_LOOP_COUNT;
        if (args.length > 1) {
            loopCount = Integer.parseInt(args[1]);
        }        

        parse( xmlFile );
    }
    
    protected void printParser() {
        println( "Using BinaryReader" );
    }
    
    protected TreeReader createTreeReader() throws Exception {
        return new BinaryReader();
    }
}
