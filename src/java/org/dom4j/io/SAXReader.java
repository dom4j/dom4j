/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;
import org.dom4j.ElementHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;
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

    /** The entity resolver */
    private EntityResolver entityResolver;
    
    /** Should element & attribute names and namespace URIs be interned? */
    private boolean stringInternEnabled = true;
    
    /** Should internal DTD declarations be expanded into a List in the DTD */
    private boolean includeInternalDTDDeclarations = false;
    
    /** Should external DTD declarations be expanded into a List in the DTD */
    private boolean includeExternalDTDDeclarations = false;
    
    /** Whether adjacent text nodes should be merged */
    private boolean mergeAdjacentText = false;    
    
    /** Holds value of property stripWhitespaceText. */
    private boolean stripWhitespaceText = false;
    
    /** Should we ignore comments */
    private boolean ignoreComments = false;
    
    
    //private boolean includeExternalGeneralEntities = false;
    //private boolean includeExternalParameterEntities = false;
    
    /** The SAX filter used to filter SAX events */
    private XMLFilter xmlFilter;
    
    
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


    
    /** Allows a SAX property to be set on the underlying SAX parser.
      * This can be useful to set parser-specific properties
      * such as the location of schema or DTD resources. 
      * Though use this method with caution as it has the possibility
      * of breaking the standard behaviour.
      * An alternative to calling this method is to correctly configure an 
      * XMLReader object instance and call the {@link #setXMLReader(XMLReader)} method
      *
      * @name is the SAX property name
      * @value is the value of the SAX property
      * @throws SAXException if the XMLReader could not be created or
      * the property could not be changed. 
      */
    public void setProperty(String name, Object value) throws SAXException {
        getXMLReader().setProperty(name, value);
    }
    
    
    /** Sets a SAX feature on the underlying SAX parser.
      * This can be useful to set parser-specific features. 
      * Though use this method with caution as it has the possibility
      * of breaking the standard behaviour.
      * An alternative to calling this method is to correctly configure an 
      * XMLReader object instance and call the {@link #setXMLReader(XMLReader)} method
      *
      * @name is the SAX feature name
      * @value is the value of the SAX feature
      * @throws SAXException if the XMLReader could not be created or
      * the feature could not be changed. 
      */
    public void setFeature(String name, boolean value) throws SAXException {
        getXMLReader().setFeature(name, value);
    }
    
        
    /** <p>Reads a Document from the given <code>File</code></p>
      *
      * @param file is the <code>File</code> to read from.
      * @return the newly created Document instance
      * @throws DocumentException if an error occurs during parsing.
      * @throws MalformedURLException if a URL could not be made for the given File
      */
    public Document read(File file) throws DocumentException, MalformedURLException {
        try {
            /*
             * We cannot convert the file to an URL because if the filename
             * contains '#' characters, there will be problems with the 
             * URL in the InputSource (because a URL like 
             * http://myhost.com/index#anchor is treated the same as
             * http://myhost.com/index)
             * Thanks to Christian Oetterli
             */
            return read( new InputSource(new FileReader(file)) );
        } catch (FileNotFoundException e) {
            throw new MalformedURLException(e.getMessage());
        }
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
            XMLReader xmlReader = getXMLReader();
            
            xmlReader = installXMLFilter(xmlReader);
            
            EntityResolver entityResolver = xmlReader.getEntityResolver();
            if ( entityResolver == null ) {
                entityResolver = this.entityResolver;
                if ( entityResolver == null ) {
                    entityResolver = createDefaultEntityResolver( in.getSystemId() );
                }
                xmlReader.setEntityResolver( entityResolver );
            }
            else {
                if ( this.entityResolver != null ) {
                    xmlReader.setEntityResolver( this.entityResolver );
                }
            }
            
            SAXContentHandler contentHandler = createContentHandler(xmlReader);
            contentHandler.setEntityResolver( entityResolver );
            contentHandler.setInputSource( in );
            contentHandler.setIncludeInternalDTDDeclarations( isIncludeInternalDTDDeclarations() );
            contentHandler.setIncludeExternalDTDDeclarations( isIncludeExternalDTDDeclarations() );
            contentHandler.setMergeAdjacentText( isMergeAdjacentText() );
            contentHandler.setStripWhitespaceText( isStripWhitespaceText() );
            contentHandler.setIgnoreComments( isIgnoreComments() );
            xmlReader.setContentHandler(contentHandler);

            configureReader(xmlReader, contentHandler);
        
            xmlReader.parse(in);
            return contentHandler.getDocument();
        } 
        catch (Exception e) {
            if (e instanceof SAXParseException) {
                //e.printStackTrace();
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
    
    /** @return whether internal DTD declarations should be expanded into the DocumentType
      * object or not. 
      */
    public boolean isIncludeInternalDTDDeclarations() {
        return includeInternalDTDDeclarations;
    }
    
    /** Sets whether internal DTD declarations should be expanded into the DocumentType
      * object or not.
      *
      * @param includeInternalDTDDeclarations whether or not DTD declarations should be expanded
      * and included into the DocumentType object.
      */
    public void setIncludeInternalDTDDeclarations(boolean includeInternalDTDDeclarations) {
        this.includeInternalDTDDeclarations = includeInternalDTDDeclarations;
    }
    
    /** @return whether external DTD declarations should be expanded into the DocumentType
      * object or not. 
      */
    public boolean isIncludeExternalDTDDeclarations() {
        return includeExternalDTDDeclarations;
    }
    
    /** Sets whether DTD external declarations should be expanded into the DocumentType
      * object or not.
      *
      * @param includeInternalDTDDeclarations whether or not DTD declarations should be expanded
      * and included into the DocumentType object.
      */
    public void setIncludeExternalDTDDeclarations(boolean includeExternalDTDDeclarations) {
        this.includeExternalDTDDeclarations = includeExternalDTDDeclarations;
    }
    
    /** Sets whether String interning
      * is enabled or disabled for element & attribute names and namespace URIs.
      * This proprety is enabled by default.
      */
    public boolean isStringInternEnabled() {
        return stringInternEnabled;
    }
    
    /** Sets whether String interning 
      * is enabled or disabled for element & attribute names and namespace URIs
      */
    public void setStringInternEnabled(boolean stringInternEnabled) {
        this.stringInternEnabled = stringInternEnabled;
    }
    
    /** Returns whether adjacent text nodes should be merged together.
      * @return Value of property mergeAdjacentText.
      */
    public boolean isMergeAdjacentText() {
        return mergeAdjacentText;
    }
    
    /** Sets whether or not adjacent text nodes should be merged
      * together when parsing.
      * @param mergeAdjacentText New value of property mergeAdjacentText.
      */
    public void setMergeAdjacentText(boolean mergeAdjacentText) {
        this.mergeAdjacentText = mergeAdjacentText;
    }
           
    /** Sets whether whitespace between element start and end tags should be ignored
      * 
      * @return Value of property stripWhitespaceText.
      */
    public boolean isStripWhitespaceText() {
        return stripWhitespaceText;
    }
    
    /** Sets whether whitespace between element start and end tags should be ignored.
      *
      * @param stripWhitespaceText New value of property stripWhitespaceText.
      */
    public void setStripWhitespaceText(boolean stripWhitespaceText) {
        this.stripWhitespaceText = stripWhitespaceText;
    }
    
    /**
     * Returns whether we should ignore comments or not.
     * @return boolean
     */
    public boolean isIgnoreComments() {
        return ignoreComments;
    }

    /**
     * Sets whether we should ignore comments or not.
     * @param ignoreComments whether we should ignore comments or not.
     */
    public void setIgnoreComments(boolean ignoreComments) {
        this.ignoreComments = ignoreComments;
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

    /** Returns the current entity resolver used to resolve entities
     */
    public EntityResolver getEntityResolver() {
        return entityResolver;
    }
    
    /** Sets the entity resolver used to resolve entities. 
     */
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
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
    
    
    /** Returns the SAX filter being used to filter SAX events.
      *
      * @return the SAX filter being used or null if no SAX filter is installed
      */
    public XMLFilter getXMLFilter() {
        return xmlFilter;
    }
    
    /** Sets the SAX filter to be used when filtering SAX events
      *
      * @param xmlFilter is the SAX filter to use or null to disable filtering
      */
    public void setXMLFilter(XMLFilter xmlFilter) {
        this.xmlFilter = xmlFilter;
    }
    
    // Implementation methods    
    //-------------------------------------------------------------------------    
    
    /** Installs any XMLFilter objects required to allow the SAX event stream
      * to be filtered and preprocessed before it gets to dom4j.
      *
      * @return the new XMLFilter if applicable or the original XMLReader if no
      * filter is being used.
      */
    protected XMLReader installXMLFilter(XMLReader xmlReader) {
        XMLFilter xmlFilter = getXMLFilter();
        if ( xmlFilter != null ) {
            // find the root XMLFilter
            XMLFilter root = xmlFilter;
            while (true) {
                XMLReader parent = root.getParent();
                if ( parent instanceof XMLFilter ) {
                    root = (XMLFilter) parent;
                }
                else {
                    break;
                }
            }
            root.setParent(xmlReader);
            return xmlFilter;
        }
        return xmlReader;
    }

    
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

        // register the DeclHandler 
        if ( includeInternalDTDDeclarations ) {
            SAXHelper.setParserProperty(
                reader,
                "http://xml.org/sax/properties/declaration-handler", 
                contentHandler
            );
        }
    
        // configure namespace support
        SAXHelper.setParserFeature(
            reader,
            "http://xml.org/sax/features/namespaces", 
            true
        );

        SAXHelper.setParserFeature(
            reader,
            "http://xml.org/sax/features/namespace-prefixes", 
            false
        );

        // string interning
        SAXHelper.setParserFeature(
            reader,
            "http://xml.org/sax/features/string-interning", 
            isStringInternEnabled()
        );
        
        // external entites
/*        
        SAXHelper.setParserFeature(
            reader,
            "http://xml.org/sax/properties/external-general-entities",
            includeExternalGeneralEntities
        );
        SAXHelper.setParserFeature(
            reader,
            "http://xml.org/sax/properties/external-parameter-entities",
            includeExternalParameterEntities
        );
*/        
        try {
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
                if ( uriPrefix != null && systemId.indexOf( ':' ) <= 0 ) {
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
