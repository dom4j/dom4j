/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.ElementHandler;
import org.dom4j.DocumentException;

import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
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
  * <p>The actual SAX parser that is used by this class is configurable 
  * so you can use your favourite SAX parser if you wish. DOM4J comes 
  * configured with its own SAX parser so you do not need to worry about 
  * configuring the SAX parser.</p>
  *
  * <p>To explicitly configure the SAX parser that is used via Java code you
  * can use a constructor or use the 
  * {@link #setXMLReader(XMLReader)} or
  * {@link #setXMLReaderClassName(String)} methods.</p>
  *
  * <p>If the parser is not specified explicitly then the standard SAX 
  * policy of using the <code>org.xml.sax.driver</code> system property is 
  * used to determine the implementation class of {@link XMLReader}.</p>
  *  
  * <p>If the <code>org.xml.sax.driver</code> system property is not defined 
  * then JAXP is used via reflection (so that DOM4J is not explicitly dependent 
  * on the JAXP classes) to load the JAXP configured SAXParser. 
  * If there is any error creating a JAXP SAXParser an informational message is 
  * output and  then the default (Aelfred) SAX parser is used instead.</p>
  *
  * <p>If you are trying to use JAXP to explicitly set your SAX parser 
  * and are experiencing problems, you can turn on verbose error reporting 
  * by defining the system property <code>org.dom4j.verbose</code> to be "true"
  * which will output a more detailed description of why JAXP could not find a 
  * SAX parser</p>
  *
  * <p>
  * For more information on JAXP please go to 
  * <a href="http://java.sun.com/xml/">Sun's Java &amp; XML site</a></p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SAXReader {

    /** <code>DocumentFactory</code> used to create new document objects */
    private DocumentFactory factory;
    
    /** <code>XMLReader</code> used to parse the SAX events */
    private XMLReader xmlReader;
    
    /** Whether validation should occur */
    private boolean validating;
    
    /** DispatchHandler to call when each <code>Element</code> is encountered */
    private DispatchHandler dispatchHandler;
 
    /** ErrorHandler class to use */
    private ErrorHandler errorHandler;
 
    
    
    
    public SAXReader() {
    }

    public SAXReader(boolean validating) {
        this.validating = validating;
    }

    public SAXReader(DocumentFactory factory) {
        this.factory = factory;
    }

    public SAXReader(DocumentFactory factory, boolean validating) {
        this.factory = factory;
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
        if (xmlReaderClassName != null) {
            this.xmlReader = XMLReaderFactory.createXMLReader(xmlReaderClassName);
        }
    }
    
    public SAXReader(String xmlReaderClassName, boolean validating) throws SAXException {
        if (xmlReaderClassName != null) {
            this.xmlReader = XMLReaderFactory.createXMLReader(xmlReaderClassName);
        }
        this.validating = validating;
    }

    
    
        
    /** <p>Reads a Document from the given <code>File</code></p>
      *
      * @param file is the <code>File</code> to read from.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      * @throws MalformedURLException if a URL could not be made for the given File
      */
    public Document read(File file) throws DocumentException, MalformedURLException {
        return read( file.toURL() );
    }
    
    /** <p>Reads a Document from the given <code>URL</code> using SAX</p>
      *
      * @param url <code>URL</code> to read from.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(URL url) throws DocumentException {
        String systemID = url.toExternalForm();
        return read(new InputSource(systemID));
    }
    
    /** <p>Reads a Document from the given URL or filename using SAX.</p>
      *
      * <p>
      * If the systemId contains a <code>':'</code> character then it is
      * assumed to be a URL otherwise its assumed to be a file name.
      * If you want finer grained control over this mechansim then please
      * explicitly pass in either a {@link URL} or a {@link File} instance
      * instead of a {@link String} to denote the source of the document.
      * </p>
      *
      * @param systemId is a URL for a document or a file name.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      * @throws MalformedURLException if a URL could not be made for the given File
      */
    public Document read(String systemId) throws DocumentException, MalformedURLException {
        if ( systemId.indexOf( ':' ) >= 0 ) {
            // lets assume its a URL
            return read(new InputSource(systemId));
        }
        else {
            // lets assume that we are given a file name
            return read( new File(systemId) );
        }
    }

    /** <p>Reads a Document from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(InputStream in) throws DocumentException {
        return read(new InputSource(in));
    }

    /** <p>Reads a Document from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(Reader reader) throws DocumentException {
        return read(new InputSource(reader));
    }

    /** <p>Reads a Document from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @param systemId is the URI for the input
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(InputStream in, String systemId) throws DocumentException {
        InputSource source = new InputSource(in);
        source.setSystemId(systemId);
        return read(source);
    }

    /** <p>Reads a Document from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @param systemId is the URI for the input
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(Reader reader, String SystemId) throws DocumentException {
        InputSource source = new InputSource(reader);
        source.setSystemId(SystemId);
        return read(source);
    }
    
    /** <p>Reads a Document from the given <code>InputSource</code> using SAX</p>
      *
      * @param in <code>InputSource</code> to read from.
      * @param systemId is the URI for the input
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(InputSource in) throws DocumentException {
        try {
            XMLReader reader = getXMLReader();

            XMLReader xmlReader = getXMLReader();
            EntityResolver entityResolver = xmlReader.getEntityResolver();
            if ( entityResolver == null ) {
                entityResolver = createDefaultEntityResolver( in.getSystemId() );
            }
            
            SAXContentHandler contentHandler = createContentHandler(reader);
            contentHandler.setEntityResolver( entityResolver );
            contentHandler.setInputSource( in );
            reader.setContentHandler(contentHandler);

            configureReader(reader, contentHandler);
        
            reader.parse(in);
            return contentHandler.getDocument();
        } 
        catch (Exception e) {
            if (e instanceof SAXParseException) {
                e.printStackTrace();
                SAXParseException parseException = (SAXParseException) e;
                String systemId = parseException.getSystemId();
                if ( systemId == null ) {
                    systemId = "";
                }
                String message = "Error on line " 
                    + parseException.getLineNumber()
                    + " of document "  + systemId
                    + " : " + parseException.getMessage();
                
                throw new DocumentException(message, e);
            }
            else {
                throw new DocumentException(e.getMessage(), e);
            }
        }
    }
    

    
    // Properties
    //-------------------------------------------------------------------------                
    
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
    
    /** @return the <code>DocumentFactory</code> used to create document objects
      */
    public DocumentFactory getDocumentFactory() {
        if (factory == null) {
            factory = DocumentFactory.getInstance();
        }
        return factory;
    }

    /** <p>This sets the <code>DocumentFactory</code> used to create new documents.
      * This method allows the building of custom DOM4J tree objects to be implemented
      * easily using a custom derivation of {@link DocumentFactory}</p>
      *
      * @param factory <code>DocumentFactory</code> used to create DOM4J objects
      */
    public void setDocumentFactory(DocumentFactory factory) {
        this.factory = factory;
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
        this.errorHandler = errorHandler;
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

    
    /** Adds the <code>ElementHandler</code> to be called when the 
      * specified path is encounted.
      *
      * @param path is the path to be handled
      * @param handler is the <code>ElementHandler</code> to be called
      * by the event based processor.
      */
    public void addHandler(String path, ElementHandler handler) {
        getDispatchHandler().addHandler(path, handler);   
    }
    
    /** Removes the <code>ElementHandler</code> from the event based
      * processor, for the specified path.
      *
      * @param path is the path to remove the <code>ElementHandler</code> for.
      */
    public void removeHandler(String path) {
        getDispatchHandler().removeHandler(path);   
    }
    
    /** When multiple <code>ElementHandler</code> instances have been 
      * registered, this will set a default <code>ElementHandler</code>
      * to be called for any path which does <b>NOT</b> have a handler
      * registered.
      * @param handler is the <code>ElementHandler</code> to be called
      * by the event based processor.
      */
    public void setDefaultHandler(ElementHandler handler) {
        getDispatchHandler().setDefaultHandler(handler);   
    }
    
    // Implementation methods    
    //-------------------------------------------------------------------------                    
    protected DispatchHandler getDispatchHandler() {
        if (dispatchHandler == null) {
            dispatchHandler = new DispatchHandler();
        }
        return dispatchHandler;   
    }
    
    protected void setDispatchHandler(DispatchHandler dispatchHandler) {
        this.dispatchHandler = dispatchHandler;
    }
    
    /** Factory Method to allow alternate methods of 
      * creating and configuring XMLReader objects
      */
    protected XMLReader createXMLReader() throws SAXException {
        return SAXHelper.createXMLReader( isValidating() );
    }
    
    /** Configures the XMLReader before use */
    protected void configureReader(XMLReader reader, DefaultHandler contentHandler) throws DocumentException {                
        // configure lexical handling
        SAXHelper.setParserProperty(
            reader,
            "http://xml.org/sax/handlers/LexicalHandler", 
            contentHandler
        );
        // try alternate property just in case
        SAXHelper.setParserProperty(
            reader,
            "http://xml.org/sax/properties/lexical-handler", 
            contentHandler
        );
        
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
                throw new DocumentException( 
                    "Validation not supported for XMLReader: " + reader, 
                    e
                );
            }

        }
    }
        
    /** Factory Method to allow user derived SAXContentHandler objects to be used
      */
    protected SAXContentHandler createContentHandler(XMLReader reader) {
        return new SAXContentHandler( 
            getDocumentFactory(), dispatchHandler 
        );
    }

    protected EntityResolver createDefaultEntityResolver( String documentSystemId ) {
        String prefix = null;
        if ( documentSystemId != null && documentSystemId.length() > 0 ) {
            int idx = documentSystemId.lastIndexOf( '/' );
            if ( idx > 0 ) {
                prefix = documentSystemId.substring(0, idx+1);
                
            }
        }
        return new SAXEntityResolver(prefix);
    }
    
    protected static class SAXEntityResolver implements EntityResolver, Serializable {
        String uriPrefix;
        
        public SAXEntityResolver(String uriPrefix) {
            this.uriPrefix = uriPrefix;
        }
        
        public InputSource resolveEntity(String publicId, String systemId) {            
            // try create a relative URI reader...
            if ( systemId != null && systemId.length() > 0 ) {
                if ( uriPrefix != null ) {
                    systemId = uriPrefix + systemId;
                }                    
            }
            return new InputSource(systemId);
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
