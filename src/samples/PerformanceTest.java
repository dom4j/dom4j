/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

import java.net.URL;
import java.io.IOException;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.dom4j.io.DocumentReader;

/** Perform some DOM4J parsing peformance test cases.
  * 
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class PerformanceTest extends SAXDemo {
    
    /** Whether the performance of each run is printed */
    protected static boolean VERBOSE = false;
    
    /** Default number of loops */
    protected static final int DEFAULT_LOOP_COUNT = 40;
    
    /** Number of loops to perform */
    protected int loopCount = DEFAULT_LOOP_COUNT;
    
    
    /** The DocumentFactory class name to use */
    protected String documentFactoryClassName;
    
    
    public static void main(String[] args) {
        run( new PerformanceTest(), args );
    }    
    
    public PerformanceTest() {
    }
    
    public void run(String[] args) throws Exception {    
        if ( args.length < 1 ) {
            printUsage( "<XML document URL> [<Document Factory Class Name>] [<loopCount>]" );
            return;
        }

        String xmlFile = args[0];
        
        documentFactoryClassName = (args.length > 1) 
            ? args[1] : null;
            
        loopCount = DEFAULT_LOOP_COUNT;
        if (args.length > 2) {
            loopCount = Integer.parseInt(args[2]);
        }        

        parse( xmlFile );
    }
    
    /** Parses the XML document at the given <code>URL</code> 
      * a number of times and outputs the timing results
      *
      * @param url is the <code>URL</code> to read 
      */
    protected void parse( URL url ) throws Exception {
        DocumentReader reader = createDocumentReader();
                    
        println( "Parsing url:      " + url );
        println( "Looping:          " + loopCount + " time(s)" );        
        println( "Using SAX parser: " + System.getProperty( "org.xml.sax.driver", "default" ) );
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
      * @param reader is the <code>DocumentReader</code> to use for the parsing
      * @return the time taken in milliseconds
      */
    protected long timeParse(URL url, DocumentReader reader) 
        throws IOException, DocumentException {

        // Build the DOM4J Document
        long start = System.currentTimeMillis();
        
        Document document = reader.read(url);
        
        long end = System.currentTimeMillis();
        return end - start;
    }

    protected DocumentReader createDocumentReader() throws Exception {
        DocumentReader answer = new SAXReader();        
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




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
