package org.dom4j.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.TreeException;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/** <p><code>SAXReader</code> creates a DOM4J tree from SAX parsing events.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SAXReader extends TreeReader {

    /** <code>XMLReader</code> used to parse the SAX events */
    private XMLReader xmlReader;
    
    /** Whether validation should occur */
    private boolean validating;

    /** ErrorHandler class to use */
    private ErrorHandler errorHandler = null;
 
    
    
    
    public SAXReader() {
    }

    public SAXReader(boolean validating) {
        this.validating = validating;
    }

    public SAXReader(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    public SAXReader(XMLReader xmlReader, boolean validating) {
        this.xmlReader = xmlReader;
        this.validating = validating;
    }

    public SAXReader(String xmlReaderClassName) throws SAXException {
        this.xmlReader = XMLReaderFactory.createXMLReader(xmlReaderClassName);
    }
    
    public SAXReader(String xmlReaderClassName, boolean validating) throws SAXException {
        this.xmlReader = XMLReaderFactory.createXMLReader(xmlReaderClassName);
        this.validating = validating;
    }
    
    /** @return the validation mode, true if validating will be done 
      * otherwise false.
      */
    public boolean isValidating() {
        return validating;
    }
    
    /** Sets the validation mode.
      *
      * @param validating indicates whether or not validation should occur.
      */
    public void setValidation(boolean validating) {
        this.validating = validating;
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
        setXMLReader( XMLReaderFactory.createXMLReader(xmlReaderClassName) );
    }

    /** <p>Reads a Document from the given <code>File</code></p>
      *
      * @param file is the <code>File</code> to read from.
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      * @throws FileNotFoundException if the file could not be found
      */
    public Document read(File file) throws TreeException, FileNotFoundException {
        Document document = read(new BufferedReader(new FileReader(file)));
        document.setName( file.getAbsolutePath() );
        return document;
    }
    
    /** <p>Reads a Document from the given <code>URL</code> using SAX</p>
      *
      * @param url <code>URL</code> to read from.
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public Document read(URL url) throws TreeException {
        String systemID = url.toExternalForm();
        Document document = read(new InputSource(systemID));
        document.setName( url.toString() );
        return document;
    }
    
    /** <p>Reads a Document from the given URI using SAX</p>
      *
      * @param systemId is the URI for the input
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public Document read(String systemId) throws TreeException {
        return read(new InputSource(systemId));
    }

    /** <p>Reads a Document from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public Document read(InputStream in) throws TreeException {
        return read(new InputSource(in));
    }

    /** <p>Reads a Document from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public Document read(Reader reader) throws TreeException {
        return read(new InputSource(reader));
    }

    /** <p>Reads a Document from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @param systemId is the URI for the input
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public Document read(InputStream in, String systemId) throws TreeException {
        InputSource source = new InputSource(in);
        source.setSystemId(systemId);
        return read(source);
    }

    /** <p>Reads a Document from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @param systemId is the URI for the input
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public Document read(Reader reader, String SystemId) throws TreeException {
        InputSource source = new InputSource(reader);
        source.setSystemId(SystemId);
        return read(source);
    }
    
    /** <p>Reads a Document from the given <code>InputSource</code> using SAX</p>
      *
      * @param in <code>InputSource</code> to read from.
      * @param systemId is the URI for the input
      * @return the newly created Document instance
      * @throws TreeException if an error occurs during parsing.
      */
    public Document read(InputSource in) throws TreeException {
        try {
            XMLReader reader = getXMLReader();

            Document document = createDocument();
            DefaultHandler contentHandler = createContentHandler(document);
            reader.setContentHandler(contentHandler);

            configureReader(reader, contentHandler);
        
            reader.parse(in);
            return document;
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
    
    
    // Implementation methods
    
    
    protected void configureReader(XMLReader reader, DefaultHandler contentHandler) throws TreeException {                
        // configure lexical handling
        boolean lexicalReporting = setParserProperty(
            reader,
            "http://xml.org/sax/handlers/LexicalHandler", 
            contentHandler
        );
        if ( ! lexicalReporting ) {
            // try alternate property            
            setParserProperty(
                reader,
                "http://xml.org/sax/properties/lexical-handler", 
                contentHandler
            );
        }
        
        try {
            // configure namespace support
            reader.setFeature(
                "http://xml.org/sax/features/namespaces", 
                true
            );
            reader.setFeature(
                "http://xml.org/sax/features/namespace-prefixes", 
                false
            );

            // configure validation support
            reader.setFeature(
                "http://xml.org/sax/features/validation", 
                isValidating()
            );
            if (errorHandler != null) {
                 reader.setErrorHandler(errorHandler);
            }
            else {
                 reader.setErrorHandler(contentHandler);
            }
        } 
        catch (Exception e) {
            if (isValidating()) {
                throw new TreeException( 
                    "Validation not supported for XMLReader: " + reader, 
                    e
                );
            }

        }
    }
        
    protected boolean setParserProperty(XMLReader reader, String propertyName, ContentHandler contentHandler) {    
        try {
            reader.setProperty(propertyName, contentHandler);
            return true;
        } 
        catch (SAXNotSupportedException e) {
        } 
        catch (SAXNotRecognizedException e) {
        }
        return false;
    }

    /** Factory Method to allow user derived SAXContentHandler objects to be used
      */
    protected DefaultHandler createContentHandler( Document document ) {
        return new SAXContentHandler(document);
    }
    
    /** Factory Method to allow alternate methods of 
      * creating and configuring XMLReader objects
      */
    protected XMLReader createXMLReader() throws SAXException {
        return XMLReaderFactory.createXMLReader();
    }


}
