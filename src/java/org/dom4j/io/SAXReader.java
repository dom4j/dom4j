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
import java.net.URL;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.ElementHandler;
import org.dom4j.DocumentException;
import org.dom4j.io.aelfred.SAXDriver;

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

    /** ElementHandler to call when each <code>Element</code> is complete */
    private ElementHandler elementHandler;
 
    /** ElementHandler to call before pruning occurs */
    private ElementHandler pruningElementHandler;
 
    /** ErrorHandler class to use */
    private ErrorHandler errorHandler;
 
    /** The pruning path to use if any */
    private String pruningPath;
 
    
    
    
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
      * @throws FileNotFoundException if the file could not be found
      */
    public Document read(File file) throws DocumentException, FileNotFoundException {
        Document document = read(new BufferedReader(new FileReader(file)));
        //Document document = read(file.getAbsolutePath());
        document.setName( file.getAbsolutePath() );
        return document;
    }
    
    /** <p>Reads a Document from the given <code>URL</code> using SAX</p>
      *
      * @param url <code>URL</code> to read from.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(URL url) throws DocumentException {
        String systemID = url.toExternalForm();
        Document document = read(new InputSource(systemID));
        document.setName( url.toString() );
        return document;
    }
    
    /** <p>Reads a Document from the given URI using SAX</p>
      *
      * @param systemId is the URI for the input
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      */
    public Document read(String systemId) throws DocumentException {
        return read(new InputSource(systemId));
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

            SAXContentHandler contentHandler = createContentHandler();
            reader.setContentHandler(contentHandler);

            configureReader(reader, contentHandler);
        
            reader.parse(in);
            return contentHandler.getDocument();
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

    /** Returns the <code>ElementHandler</code> which is called as 
      * <code>Element</code> objects are built.
      *
      * @return the handler of elements 
      */
    public ElementHandler getElementHandler() {
        return elementHandler;
    }

    /** Sets the <code>ElementHandler</code> which is called as 
      * <code>Element</code> objects are built.
      *
      * @param elementHandler is the handler of elements 
      */
    public void setElementHandler(ElementHandler elementHandler) {
        this.elementHandler = elementHandler;
    }

    /** Returns the <code>ElementHandler</code> called when pruning 
      * large documents.
      *
      * @return the handler of elements which are about to be pruned
      */
    public ElementHandler getPruningElementHandler() {
        return pruningElementHandler;
    }

    /** Sets the <code>ElementHandler</code> called when pruning 
      * large documents.
      *
      * @param elementHandler is the handler of elements to use when pruning
      */
    public void getPruningElementHandler(ElementHandler pruningElementHandler) {
        this.pruningElementHandler = pruningElementHandler;
    }

    /** Returns the pruning patch used when parsing SAX events. The pruning path
      * allows large documents to be parsed.
      *
      * @return the pruningPath to use when reading documents
      */
    public String getPruningPath() {
        return pruningPath;
    }

    /** Sets the pruning patch used when parsing SAX events. The pruning path
      * allows large documents to be parsed.
      *
      * @param pruningPath is the pruningPath to use
      */
    public void setPruningPath(String pruningPath) {
        this.pruningPath = pruningPath;
    }

    /** Sets the pruning patch used when parsing SAX events. The pruning path
      * allows large documents to be parsed. It should contain the '/' character
      * to seperate paths such as "A/B/C".
      *
      * @param pruningPath is the path expression for the nodes to call the handler and to prune 
      */
    public void setPruningMode(String pruningPath, ElementHandler pruningElementHandler) {
        this.pruningPath = pruningPath;
        this.pruningElementHandler = pruningElementHandler;
    }

    
    // Implementation methods    
    //-------------------------------------------------------------------------                
    
    protected void configureReader(XMLReader reader, DefaultHandler contentHandler) throws DocumentException {                
        // configure lexical handling
        setParserProperty(
            reader,
            "http://xml.org/sax/handlers/LexicalHandler", 
            contentHandler
        );
        // try alternate property just in case
        setParserProperty(
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
    protected SAXContentHandler createContentHandler() {
        if ( pruningPath != null ) {
            // NOTE: should handle full XPath patterns sometime
            String[] path = tokenizePath( pruningPath );
            ElementStack stack = new PruningElementStack( path, getPruningElementHandler() );
            return new SAXContentHandler( getDocumentFactory(), getElementHandler(), stack );
        }
        else {
            return new SAXContentHandler( getDocumentFactory(), getElementHandler() );
        }
    }
    
    protected String[] tokenizePath(String path) {
        ArrayList list = new ArrayList();
        if ( path.charAt(0) == '/' ) {
            path = path.substring(1);
        }
        for ( StringTokenizer enum = new StringTokenizer( path, "/" ); enum.hasMoreTokens(); ) {
            list.add( enum.nextToken() );
        }
        String[] answer = new String[ list.size() ];
        list.toArray( answer );
        return answer;
    }

    /** Factory Method to allow alternate methods of 
      * creating and configuring XMLReader objects
      */
    protected XMLReader createXMLReader() throws SAXException {
        String className = System.getProperty( "org.xml.sax.driver" );
        if (className == null || className.trim().length() <= 0) {
            XMLReader reader = createXMLReaderViaJAXP();
            if ( reader != null ) {
                return reader;
            }
            return new SAXDriver();
        }
        return XMLReaderFactory.createXMLReader();
    }

    /** This method attempts to use JAXP to locate the  
      * SAX2 XMLReader implementation.  
      * This method uses reflection to avoid being dependent directly
      * on the JAXP classes.
      */
    protected XMLReader createXMLReaderViaJAXP() {
        Class factoryClass = null;
        try {
            factoryClass = Class.forName("javax.xml.parsers.SAXParserFactory");
        }
        catch (Exception e) {
            // JAXP is not loaded so continue
        }
        if (factoryClass == null) {
            return null;
        }
        try {            
            Method newParserInstanceMethod 
                = factoryClass.getMethod("newInstance", null);            
            Object factory = newParserInstanceMethod.invoke(null, null);
            if ( factory != null ) {
                // set validating mode
                Class[] setValidatePrototype = { boolean.class };
                Method setValidatingMethod = factoryClass.getMethod(
                    "setValidating", setValidatePrototype
                );
                Object[] setValidaingArgs = { 
                    (isValidating()) ? Boolean.TRUE : Boolean.FALSE 
                };
                setValidatingMethod.invoke( factory, setValidaingArgs );

                // create JAXP SAXParser
                Method newSAXParserMethod 
                    = factoryClass.getMethod("newSAXParser", null);
                Object jaxpParser  = newSAXParserMethod.invoke(factory, null);
                if ( jaxpParser != null ) {
                    Class parserClass = jaxpParser.getClass();
                    Method getXMLReaderMethod 
                        = parserClass.getMethod("getXMLReader", null);

                    return (XMLReader) getXMLReaderMethod.invoke(jaxpParser, null);
                }
            }
        }
        catch (Throwable e) {
            if ( isVerboseErrorReporting() ) {
                // log all exceptions as warnings and carry
                // on as we have a default SAX parser we can use
                System.out.println( 
                    "Warning: Caught exception attempting to use JAXP to "
                     + "load a SAX XMLReader " 
                );

                // extract the real exception if its wrapped in 
                // a reflection exception wrapper
                if ( e instanceof InvocationTargetException ) {
                    InvocationTargetException ie = (InvocationTargetException) e;
                    e = ie.getTargetException();
                }
                System.out.println( "Warning: Exception was: " + e );
                System.out.println( 
                    "Warning: I will print the stack trace then carry on "
                     + "using the default SAX parser" 
                 );
                e.printStackTrace();
            }
            else {
                System.out.println( 
                    "Info: Could not use JAXP to load a SAXParser. Will use Aelfred instead" 
                );
            }
        }
        return null;
    }
    
    protected boolean isVerboseErrorReporting() {
        try {
            String flag = System.getProperty( "org.dom4j.verbose" );
            if ( flag != null && flag.equalsIgnoreCase( "true" ) ) {
                return true;
            }
        }
        catch (Exception e) {
            // in case a security exception
            // happens in an applet or similar JVM
        }
        return false;
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
