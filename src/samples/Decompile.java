/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


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
            printUsage( "<Binary XML document URL> [<Output XML text file>]" );
            return;
        }

        String xmlFile = args[0];        
        outputFileName = (args.length > 1) 
            ? args[1] : null;
        
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
