/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


import java.net.URL;

import org.dom4j.*;
import org.dom4j.io.DocumentReader;
import org.dom4j.io.SAXReader;

/** This is an abstract base class for any demo which uses a 
  * {@link DocumentReader} implementation such as {@link SAXReader} and parses
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
            printUsage( "<XML document URL> [<Document Factory Class Name>]" );
            return;
        }

        String xmlFile = args[0];
        
        documentFactoryClassName = (args.length > 1) 
            ? args[1] : null;

        parse( xmlFile );
    }
    
    protected void parse( URL url ) throws Exception {
        DocumentReader reader = createDocumentReader();
        Document document = reader.read(url);
        process(document);
    }
    
    protected DocumentReader createDocumentReader() throws Exception {
        println( "Using SAX parser: " + System.getProperty( "org.xml.sax.driver", "default" ) );
        
        DocumentReader answer = new SAXReader();        
        
        // allow the DocumentFactory used by the DocumentReader to be configured
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
