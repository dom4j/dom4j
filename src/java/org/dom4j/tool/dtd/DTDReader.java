package org.dom4j.tool.dtd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.net.URL;

import org.dom4j.TreeException;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.helpers.XMLReaderFactory;


/** <p><code>DTDReader</code> reads DTD events from a SAX2 parser
  * and generates a document object model.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DTDReader {

    private static String DEFAULT_SAX_DRIVER = "org.apache.xerces.parsers.SAXParser";

    /** The classname of the SAX <code>XMLReader</code>  to use */
    private String xmlReaderClassName;
    
    /** <code>XMLReader</code> used to parse the SAX events */
    private XMLReader xmlReader;
    
    /** ErrorHandler class to use */
    private ErrorHandler errorHandler = null;
        
    
    public static void main(String[] args) throws Exception {
        if ( args.length <= 0 ) {
            System.out.println( "Usage: <xmlFileName> [xmlReaderClassName]" );
            return;
        }

        try {
            DTDReader reader = new DTDReader();        
            
            if ( args.length > 1 ) {
                reader.setXMLReaderClassName( args[1] );
            }
            DocumentDecl model = reader.read( args[0] );
            
            model.write( new PrintWriter(System.out) );
        }
        catch (Exception e) {
            System.out.println( "Exception occurred: " + e );
            e.printStackTrace ();
        }
    }
    
    public DTDReader() {
        xmlReaderClassName = System.getProperty( "org.xml.sax.driver", DEFAULT_SAX_DRIVER );
    }
    
    public DTDReader(String xmlReaderClassName) {
        this.xmlReaderClassName = xmlReaderClassName;
    }

    
    /** @return the <code>ErrorHandler</code> used by SAX
      */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /** Sets the <code>ErrorHandler</code> used by the SAX 
      * <code>XMLReader</code>.
      *
      * @param errorHandler is the <code>ErrorHandler</code> used by SAX
      */
    public void setErrorHandler(ErrorHandler errorHandler) {
        errorHandler = errorHandler;
    }

    /** @return the <code>XMLReader</code> used to parse SAX events
      */
    public XMLReader getXMLReader() throws SAXException {
        if (xmlReader == null) {
            xmlReader = createXMLReader();
        }
        return xmlReader;
    }

    /** Sets the <code>XMLReader</code> used to parse SAX events
      *
      * @param xmlReader is the <code>XMLReader</code> to parse SAX events
      */
    public void setXMLReader(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    /** Sets the class name of the <code>XMLReader</code> to be used 
      * to parse SAX events.
      *
      * @param xmlReaderClassName is the class name of the <code>XMLReader</code> 
      * to parse SAX events
      */
    public void setXMLReaderClassName(String xmlReaderClassName) throws SAXException {
        this.xmlReaderClassName = xmlReaderClassName;
    }

    /** <p>Reads a DocumentDecl from the given <code>File</code></p>
      *
      * @param file is the <code>File</code> to read from.
      * @return the newly created DocumentDecl instance
      * @throws TreeException if an error occurs during parsing.
      * @throws FileNotFoundException if the file could not be found
      */
    public DocumentDecl read(File file) throws TreeException, FileNotFoundException {
        //return read(new BufferedReader(new FileReader(file)));
        return read(file.getAbsolutePath());
    }
    
    /** <p>Reads a DocumentDecl from the given <code>URL</code> using SAX</p>
      *
      * @param url <code>URL</code> to read from.
      * @return the newly created DocumentDecl instance
      * @throws TreeException if an error occurs during parsing.
      */
    public DocumentDecl read(URL url) throws TreeException {
        String systemID = url.toExternalForm();
        return read(new InputSource(systemID));
    }
    
    /** <p>Reads a DocumentDecl from the given URI using SAX</p>
      *
      * @param systemId is the URI for the input
      * @return the newly created DocumentDecl instance
      * @throws TreeException if an error occurs during parsing.
      */
    public DocumentDecl read(String systemId) throws TreeException {
        return read(new InputSource(systemId));
    }

    /** <p>Reads a DocumentDecl from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @return the newly created DocumentDecl instance
      * @throws TreeException if an error occurs during parsing.
      */
    public DocumentDecl read(InputStream in) throws TreeException {
        return read(new InputSource(in));
    }

    /** <p>Reads a DocumentDecl from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @return the newly created DocumentDecl instance
      * @throws TreeException if an error occurs during parsing.
      */
    public DocumentDecl read(Reader reader) throws TreeException {
        return read(new InputSource(reader));
    }

    /** <p>Reads a DocumentDecl from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @param systemId is the URI for the input
      * @return the newly created DocumentDecl instance
      * @throws TreeException if an error occurs during parsing.
      */
    public DocumentDecl read(InputStream in, String systemId) throws TreeException {
        InputSource source = new InputSource(in);
        source.setSystemId(systemId);
        return read(source);
    }

    /** <p>Reads a DocumentDecl from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @param systemId is the URI for the input
      * @return the newly created DocumentDecl instance
      * @throws TreeException if an error occurs during parsing.
      */
    public DocumentDecl read(Reader reader, String SystemId) throws TreeException {
        InputSource source = new InputSource(reader);
        source.setSystemId(SystemId);
        return read(source);
    }
    
    /** <p>Reads a DocumentDecl from the given <code>InputSource</code> using SAX</p>
      *
      * @param in <code>InputSource</code> to read from.
      * @param systemId is the URI for the input
      * @return the newly created DocumentDecl instance
      * @throws TreeException if an error occurs during parsing.
      */
    public DocumentDecl read(InputSource in) throws TreeException {
        try {
            XMLReader reader = getXMLReader();

            DTDHandler handler = new DTDHandler();
            reader.setProperty(
                "http://xml.org/sax/properties/declaration-handler",
                handler
            );
        
            reader.parse(in);
            return handler.getDocumentDecl();
        } 
        catch (Exception e) {
            if (e instanceof SAXParseException) {
                SAXParseException parseException = (SAXParseException) e;
                String systemId = parseException.getSystemId();
                if ( systemId == null ) {
                    systemId = "";
                }
                String message = "Error on line " 
                    + parseException.getLineNumber()
                    + " of document "  + systemId
                    + " : " + parseException.getMessage();
                
                throw new TreeException(message, e);
            }
            else {
                throw new TreeException(e.getMessage(), e);
            }
        }
    }
    
    
    /** Factory Method to allow alternate methods of 
      * creating and configuring XMLReader objects
      */
    protected XMLReader createXMLReader() throws SAXException {
        return XMLReaderFactory.createXMLReader( xmlReaderClassName );
    }
}
