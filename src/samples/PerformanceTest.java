import java.net.URL;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.TreeException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.TreeReader;

/** Perform some DOM4J parsing peformance test cases.
  * 
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class PerformanceTest extends SAXDemo {
    
    /** Whether the performance of each run is printed */
    protected static boolean VERBOSE = false;
    
    /** Default number of loops */
    protected static final int DEFAULT_LOOP_COUNT = 100;
    
    /** Number of loops to perform */
    protected int loopCount = DEFAULT_LOOP_COUNT;
    
    
    /** The DocumentFactory class name to use */
    protected String documentFactoryClassName;
    
    
    public PerformanceTest() {
    }
    
    /** The program entry point.
      *
      * @param args the command line arguments
      */
    public static void main(String[] args) {
        run( new PerformanceTest(), args );
    }    
    
    public void run(String[] args) throws Exception {    
        if ( args.length < 1 ) {
            printUsage( "<XML document URL> [<Document Factory Class Name>] [<SAX XMLReader Class Name>] [<loopCount>]" );
            return;
        }

        String xmlFile = args[0];
        
        documentFactoryClassName = (args.length > 1) 
            ? args[1] : null;
            
        xmlReaderClassName = (args.length > 2) 
            ? args[2] : DEFAULT_XMLREADER_CLASSNAME;
        
        loopCount = DEFAULT_LOOP_COUNT;
        if (args.length > 3) {
            loopCount = Integer.parseInt(args[3]);
        }        

        parse( xmlFile );
    }
    
    /** Parses the XML document at the given <code>URL</code> 
      * a number of times and outputs the timing results
      *
      * @param url is the <code>URL</code> to read 
      */
    protected void parse( URL url ) throws Exception {
        TreeReader reader = createTreeReader();
                    
        println( "Parsing url:      " + url );
        println( "Looping:          " + loopCount + " time(s)" );        
        println( "DocumentFactory:  " + reader.getDocumentFactory() );
        
        if ( loopCount <= 0 ) {
            return;
        }
        
        long[] times = new long[loopCount];
        for ( int i = 0; i < loopCount; i++ ) {
            times[i] = timeParse(url, reader);
        }

        println( "Performance summary" );

        int displayCount = 4;
        if ( VERBOSE || loopCount < displayCount ) {
            displayCount = loopCount;
        }
        for ( int i = 0; i < displayCount; i++ ) {
            println( "run: " + i + " took: " + times[i] + " (ms)" );
        }        
        
        long minimum = times[0];
        for ( int i = 1; i < loopCount; i++ ) {
            long time = times[i];            
            if ( time < minimum ) {
                minimum = time;
            }
        }        
        
        println( "Minimum time of run                   : " + minimum + " (ms)" );
        
        // average ignoring first loop
        long total = 0;
        for ( int i = 0; i < loopCount; i++ ) {
            total += times[i];
        }
        
        double average = total / loopCount;
        
        println( "Average time of run                   : " + average + " (ms)" );
        
        if ( loopCount == 1 ) {
            return;
        }
        total -= times[0];
        average = total / (loopCount - 1);
        
        println( "Average (excluding first run)         : " + average + " (ms)" );
        
        if ( loopCount == 2 ) {
            return;
        }
        total -= times[1];
        average = total / (loopCount - 2);
        
        println( "Average (excluding first & second run): " + average + " (ms)" );
    }

    /** Parses the XML document at the given URL and times how long it takes.
      *
      * @param url is the <code>URL</code> to read 
      * @param reader is the <code>TreeReader</code> to use for the parsing
      * @return the time taken in milliseconds
      */
    protected long timeParse(URL url, TreeReader reader) 
        throws IOException, TreeException {

        // Build the DOM4J Document
        long start = System.currentTimeMillis();
        
        Document document = reader.read(url);
        
        long end = System.currentTimeMillis();
        return end - start;
    }

    protected void printParser() {
        println( "Using SAX parser: " + xmlReaderClassName );
        println( "DocumentFactory:  " + documentFactoryClassName );
    }
    
    protected TreeReader createTreeReader() throws Exception {
        TreeReader answer = new SAXReader( xmlReaderClassName );        
        if ( documentFactoryClassName != null ) {
            try {
                Class theClass = Class.forName( documentFactoryClassName );
                DocumentFactory factory = (DocumentFactory) theClass.newInstance();
                if ( factory != null ) {
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
