/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.tree.NamespaceStack;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;

/** <p><code>SAXWriter</code> writes a DOM4J tree to a SAX ContentHandler.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SAXWriter implements XMLReader {

    protected static final String[] LEXICAL_HANDLER_NAMES = {
        "http://xml.org/sax/properties/lexical-handler",
        "http://xml.org/sax/handlers/LexicalHandler"
    };
    
    protected static String FEATURE_NAMESPACE_PREFIXES = "http://xml.org/sax/features/namespace-prefixes";
    protected static String FEATURE_NAMESPACES = "http://xml.org/sax/features/namespaces";

    /** <code>ContentHandler</code> to which SAX events are raised */
    private ContentHandler contentHandler;
    
    /** code>DTDHandler</code> fired when a document has a DTD */
    private DTDHandler dtdHandler;
    
    /** code>EntityResolver</code> fired when a document has a DTD */
    private EntityResolver entityResolver;

    private ErrorHandler errorHandler;
    
    /** code>LexicalHandler</code> fired on Entity and CDATA sections */
    private LexicalHandler lexicalHandler;

    /** code>AttributesImpl</code> used when generating the Attributes */
    private AttributesImpl attributes = new AttributesImpl();
    
    /** Stores the features */
    private Map features = new HashMap();
    
    /** Stores the properties */
    private Map properties = new HashMap();

    /** Whether namespace declarations are exported as attributes or not */
    private boolean declareNamespaceAttributes;
    
    
    public SAXWriter() {
        properties.put( FEATURE_NAMESPACE_PREFIXES, Boolean.FALSE );
        properties.put( FEATURE_NAMESPACE_PREFIXES, Boolean.TRUE );
    }

    public SAXWriter(ContentHandler contentHandler) {
        this();
        this.contentHandler = contentHandler;
    }

    public SAXWriter(
        ContentHandler contentHandler, 
        LexicalHandler lexicalHandler
    ) {
        this();
        this.contentHandler = contentHandler;
        this.lexicalHandler = lexicalHandler;
    }

    public SAXWriter(
        ContentHandler contentHandler, 
        LexicalHandler lexicalHandler,
        EntityResolver entityResolver
    ) {
        this();
        this.contentHandler = contentHandler;
        this.lexicalHandler = lexicalHandler;
        this.entityResolver = entityResolver;
    }

    
    /**
     * A polymorphic method to write any Node to this SAX stream
     */
    public void write(Node node) throws SAXException {
        int nodeType = node.getNodeType();
        switch (nodeType) {
            case Node.ELEMENT_NODE:
                write((Element) node);
                break;
            case Node.ATTRIBUTE_NODE:
                write((Attribute) node);
                break;
            case Node.TEXT_NODE:
                write(node.getText());
                break;
            case Node.CDATA_SECTION_NODE:
                write((CDATA) node);
                break;
            case Node.ENTITY_REFERENCE_NODE:
                write((Entity) node);
                break;
            case Node.PROCESSING_INSTRUCTION_NODE:
                write((ProcessingInstruction) node);
                break;
            case Node.COMMENT_NODE:
                write((Comment) node);
                break;
            case Node.DOCUMENT_NODE:
                write((Document) node);
                break;
            case Node.DOCUMENT_TYPE_NODE:
                write((DocumentType) node);
                break;
            case Node.NAMESPACE_NODE:
                // Will be output with attributes
                //write((Namespace) node);
                break;
            default:
                throw new SAXException( "Invalid node type: " + node );
        }
    }


    /** Generates SAX events for the given Document and all its content
      *
      * @param document is the Document to parse
      * @throw SAXException if there is a SAX error processing the events
      */
    public void write(Document document) throws SAXException {
        if (document != null) {       
            checkForNullHandlers();
            
            documentLocator(document);
            startDocument();            
            entityResolver(document);
            dtdHandler(document);
            
            writeContent( document, new NamespaceStack() );
            endDocument();
        }
    }
    
    
    
    /** Generates SAX events for the given Element and all its content
      *
      * @param element is the Element to parse
      * @throw SAXException if there is a SAX error processing the events
      */
    public void write( Element element ) throws SAXException {
        write( element, new NamespaceStack() );
    }
    

    /** <p>Writes the opening tag of an {@link Element},
      * including its {@link Attribute}s
      * but without its content.</p>
      *
      * @param element <code>Element</code> to output.
      */
    public void writeOpen(Element element) throws SAXException {
        startElement(element, null);
    }

    /** <p>Writes the closing tag of an {@link Element}</p>
      *
      * @param element <code>Element</code> to output.
      */
    public void writeClose(Element element) throws SAXException {
        endElement(element);
    }
    
    /** Generates SAX events for the given text
      *
      * @param text is the text to send to the SAX ContentHandler
      * @throw SAXException if there is a SAX error processing the events
      */
    public void write( String text ) throws SAXException {
        if ( text != null ) {
            char[] chars = text.toCharArray();
            contentHandler.characters( chars, 0, chars.length );
        }
    }
    
    /** Generates SAX events for the given CDATA
      *
      * @param cdata is the CDATA to parse
      * @throw SAXException if there is a SAX error processing the events
      */
    public void write( CDATA cdata ) throws SAXException {
        String text = cdata.getText();
        if ( lexicalHandler != null ) {
            lexicalHandler.startCDATA();
            write( text );
            lexicalHandler.endCDATA();
        }
        else {
            write( text );
        }
    }
    
    /** Generates SAX events for the given Comment
      *
      * @param comment is the Comment to parse
      * @throw SAXException if there is a SAX error processing the events
      */
    public void write( Comment comment ) throws SAXException {
        if ( lexicalHandler != null ) {
            String text = comment.getText();
            char[] chars = text.toCharArray();
            lexicalHandler.comment( chars, 0, chars.length );
        }
    }
    
    /** Generates SAX events for the given Entity
      *
              * @param e is the Entity to parse
      * @throw SAXException if there is a SAX error processing the events
      */
    public void write( Entity entity ) throws SAXException {
        String text = entity.getText();
        if ( lexicalHandler != null ) {
            String name = entity.getName();
            lexicalHandler.startEntity(name);
            write( text );
            lexicalHandler.endEntity(name);
        }
        else {
            write( text );
        }
    }
    
    /** Generates SAX events for the given ProcessingInstruction
      *
      * @param pi is the ProcessingInstruction to parse
      * @throw SAXException if there is a SAX error processing the events
      */
    public void write( ProcessingInstruction pi ) throws SAXException {        
        String target = pi.getTarget();
        String text = pi.getText();
        contentHandler.processingInstruction(target, text);
    }
    

    
    /** Should namespace declarations be converted to "xmlns" attributes. This property
      * defaults to <code>false</code> as per the SAX specification. 
      * This property is set via the SAX feature "http://xml.org/sax/features/namespace-prefixes"
      */ 
    public boolean isDeclareNamespaceAttributes() {
        return declareNamespaceAttributes;
    }
    
    /** Sets whether namespace declarations should be exported as "xmlns" attributes or not.
      * This property is set from the SAX feature "http://xml.org/sax/features/namespace-prefixes"
      */ 
    public void setDeclareNamespaceAttributes(boolean declareNamespaceAttributes) {
        this.declareNamespaceAttributes = declareNamespaceAttributes;
    }
    

    
    // XMLReader methods
    //-------------------------------------------------------------------------                

    /** @return the <code>ContentHandler</code> called when SAX events 
      * are raised
      */
    public ContentHandler getContentHandler() {
        return contentHandler;
    }

    /** Sets the <code>ContentHandler</code> called when SAX events 
      * are raised
      *
      * @param contentHandler is the <code>ContentHandler</code> called when SAX events 
      * are raised
      */
    public void setContentHandler(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }


    /** @return the <code>DTDHandler</code> 
      */
    public DTDHandler getDTDHandler() {
        return dtdHandler;
    }

    /** Sets the <code>DTDHandler</code>.
      */
    public void setDTDHandler(DTDHandler dtdHandler) {
        this.dtdHandler = dtdHandler;
    }

    /** @return the <code>ErrorHandler</code> 
      */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /** Sets the <code>ErrorHandler</code>.
      */
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    /** @return the <code>EntityResolver</code> used when a Document contains 
      * a DTD
      */
    public EntityResolver getEntityResolver() {
        return entityResolver;
    }

    /** Sets the <code>EntityResolver</code> .
      *
      * @param entityResolver is the <code>EntityResolver</code> 
      */
    public void setEntityResolver(EntityResolver entityResolver) {
        this.entityResolver = entityResolver;
    }

    /** @return the <code>LexicalHandler</code> used when a Document contains 
      * a DTD
      */
    public LexicalHandler getLexicalHandler() {
        return lexicalHandler;
    }

    /** Sets the <code>LexicalHandler</code> .
      *
      * @param entityResolver is the <code>LexicalHandler</code> 
      */
    public void setLexicalHandler(LexicalHandler lexicalHandler) {
        this.lexicalHandler = lexicalHandler;
    }

    
    /** Sets the <code>XMLReader</code> used to write SAX events to
      * 
      * @param xmlReader is the <code>XMLReader</code> 
      */
    public void setXMLReader(XMLReader xmlReader) {
        setContentHandler( xmlReader.getContentHandler() );
        setDTDHandler( xmlReader.getDTDHandler() );
        setEntityResolver( xmlReader.getEntityResolver() );
        setErrorHandler( xmlReader.getErrorHandler() );
    }
    
    /** Looks up the value of a feature.
      */
    public boolean getFeature(String name) 
            throws SAXNotRecognizedException, SAXNotSupportedException {
        Boolean answer = (Boolean) features.get(name);
        return answer != null && answer.booleanValue();
    }

    /** This implementation does actually use any features but just
      * stores them for later retrieval
      */
    public void setFeature(String name, boolean value) throws SAXNotRecognizedException, SAXNotSupportedException {
        if ( FEATURE_NAMESPACE_PREFIXES.equals( name ) ) {
            setDeclareNamespaceAttributes( value );
        }
        else if ( FEATURE_NAMESPACE_PREFIXES.equals( name ) ) {
            if ( ! value ) {
                throw new SAXNotSupportedException(name + ". namespace feature is always supported in dom4j." );
            }
        }
        features.put(name, (value) ? Boolean.TRUE : Boolean.FALSE );        
    }

    /** Sets the given SAX property
      */    
    public void setProperty(String name, Object value) throws SAXNotRecognizedException, SAXNotSupportedException {
        for (int i = 0; i < LEXICAL_HANDLER_NAMES.length; i++) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                setLexicalHandler((LexicalHandler) value);
                return;
            }
        }
        properties.put(name, value);
    }

    /** Gets the given SAX property
      */    
    public Object getProperty(String name) throws SAXNotRecognizedException, SAXNotSupportedException {
        for (int i = 0; i < LEXICAL_HANDLER_NAMES.length; i++) {
            if (LEXICAL_HANDLER_NAMES[i].equals(name)) {
                return getLexicalHandler();
            }
        }
        return properties.get(name);
    }

    

    
    /** This method is not supported. 
      */
    public void parse(String systemId) throws SAXNotSupportedException {
        throw new SAXNotSupportedException(
            "This XMLReader can only accept <dom4j> InputSource objects"
        );
    }

    
    /** Parses an XML document. 
      * This method can only accept DocumentInputSource inputs
      * otherwise a {@link SAXNotSupportedException} exception is thrown.
      *
      * @throws SAXNotSupportedException 
      *      if the input source is not wrapping a dom4j document
      */
    public void parse(InputSource input) throws SAXException {
        if (input instanceof DocumentInputSource) {
            DocumentInputSource documentInput = (DocumentInputSource) input;
            Document document = documentInput.getDocument();
            write( document );
        }
        else {
            throw new SAXNotSupportedException(
                "This XMLReader can only accept <dom4j> InputSource objects"
            );
        }
    }

    
    
    // Implementation methods    
    //-------------------------------------------------------------------------                
    
    protected void writeContent( Branch branch, NamespaceStack namespaceStack ) throws SAXException {
        for ( Iterator iter = branch.nodeIterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if ( object instanceof Element ) {
                write( (Element) object, namespaceStack );
            }
            else if ( object instanceof CharacterData ) { 
                if ( object instanceof Text ) {
                    Text text = (Text) object;
                    write( text.getText() );
                }
                else if ( object instanceof CDATA ) {
                    write( (CDATA) object );
                }
                else if ( object instanceof Comment ) {
                    write( (Comment) object );
                }
                else {
                    throw new SAXException( "Invalid Node in DOM4J content: " + object + " of type: " + object.getClass() );
                }
            }
            else if ( object instanceof String ) { 
                write( (String) object );
            }
            else if ( object instanceof Entity ) { 
                write( (Entity) object );
            }
            else if ( object instanceof ProcessingInstruction ) { 
                write( (ProcessingInstruction) object );
            }
            else if ( object instanceof Namespace ) { 
                // namespaceStack are written via write of element
            }
            else {
                throw new SAXException( "Invalid Node in DOM4J content: " + object );
            }
        }
    }
    
    /** The {@link Locator} is only really useful when parsing a textual
      * document as its main purpose is to identify the line and column number.
      * Since we are processing an in memory tree which will probably have
      * its line number information removed, we'll just use -1 for the line
      * and column numbers.
      */
    protected void documentLocator(Document document) throws SAXException {
        LocatorImpl locator = new LocatorImpl();
        
        String publicID = null;
        String systemID = null;
        DocumentType docType = document.getDocType();
        if (docType != null) {
            publicID = docType.getPublicID();
            systemID = docType.getSystemID();
        }
        if ( publicID != null ) {
            locator.setPublicId(publicID);
        }
        if ( systemID != null ) {
            locator.setSystemId(systemID);
        }
            
        locator.setLineNumber(-1);
        locator.setColumnNumber(-1);
        
        contentHandler.setDocumentLocator( locator );
    }

    protected void entityResolver(Document document) throws SAXException {
        if (entityResolver != null) {
            DocumentType docType = document.getDocType();
            if (docType != null) {
                String publicID = docType.getPublicID();
                String systemID = docType.getSystemID();
                
                if ( publicID != null || systemID != null ) {
                    try {
                        entityResolver.resolveEntity( publicID, systemID );
                    }
                    catch (IOException e) {
                        throw new SAXException( 
                            "Could not resolve entity publicID: " 
                            + publicID + " systemID: " + systemID, e 
                        );
                    }
                }
            }
        }
    }

    
    /** We do not yet support DTD or XML Schemas so this method does nothing
      * right now.
      */
    protected void dtdHandler(Document document) throws SAXException {
    }

    protected void startDocument() throws SAXException {
        contentHandler.startDocument();
    }

    protected void endDocument() throws SAXException {
        contentHandler.endDocument();
    }
    
    protected void write( Element element, NamespaceStack namespaceStack ) throws SAXException {
        int stackSize = namespaceStack.size();
        AttributesImpl namespaceAttributes = startPrefixMapping(element, namespaceStack);
        startElement(element, namespaceAttributes);
        writeContent(element, namespaceStack);
        endElement(element);
        endPrefixMapping(namespaceStack, stackSize);
    }
    
    /** Fires a SAX startPrefixMapping event for all the namespaceStack
      * which have just come into scope
      */
    protected AttributesImpl startPrefixMapping( Element element, NamespaceStack namespaceStack ) throws SAXException {
        AttributesImpl namespaceAttributes = null;
        List declaredNamespaces = element.declaredNamespaces();
        for ( int i = 0, size = declaredNamespaces.size(); i < size ; i++ ) {
            Namespace namespace = (Namespace) declaredNamespaces.get(i);
            if ( ! isIgnoreableNamespace( namespace, namespaceStack ) ) {
                namespaceStack.push( namespace );
                contentHandler.startPrefixMapping(
                    namespace.getPrefix(), namespace.getURI()
                );
                namespaceAttributes = addNamespaceAttribute( namespaceAttributes, namespace );
            }
        }
        return namespaceAttributes;
    }
    
    /** Fires a SAX endPrefixMapping event for all the namespaceStack which 
      * have gone out of scope
      */
    protected void endPrefixMapping( NamespaceStack namespaceStack, int stackSize ) throws SAXException {                       
        while ( namespaceStack.size() > stackSize ) {
            Namespace namespace = namespaceStack.pop();
            if ( namespace != null ) {
                contentHandler.endPrefixMapping( namespace.getPrefix() );            
            }
        }
    }
    
    
    protected void startElement( Element element, AttributesImpl namespaceAttributes ) throws SAXException {                       
        contentHandler.startElement( 
            element.getNamespaceURI(), 
            element.getName(), 
            element.getQualifiedName(), 
            createAttributes( element, namespaceAttributes )
        );
    }

    protected void endElement( Element element ) throws SAXException {        
        contentHandler.endElement( 
            element.getNamespaceURI(), 
            element.getName(), 
            element.getQualifiedName()
        );
    }

    protected Attributes createAttributes( Element element, Attributes namespaceAttributes ) throws SAXException {
        attributes.clear();
        if ( namespaceAttributes != null ) {
            attributes.setAttributes( namespaceAttributes );
        }
        
        for ( Iterator iter = element.attributeIterator(); iter.hasNext(); ) {
            Attribute attribute = (Attribute) iter.next();
            attributes.addAttribute( 
                attribute.getNamespaceURI(), 
                attribute.getName(), 
                attribute.getQualifiedName(), 
                "CDATA",
                attribute.getValue()
            );
        }
        return attributes;
    }
    
    /** If isDelcareNamespaceAttributes() is enabled then this method will add the
      * given namespace declaration to the supplied attributes object, creating one if
      * it does not exist.
      */
    protected AttributesImpl addNamespaceAttribute( AttributesImpl namespaceAttributes, Namespace namespace ) {
        if ( declareNamespaceAttributes ) {
            if ( namespaceAttributes == null ) {
                namespaceAttributes = new AttributesImpl();
            }
            String prefix = namespace.getPrefix();
            String qualifiedName = "xmlns";
            if ( prefix != null && prefix.length() > 0 ) {
                qualifiedName = "xmlns:" + prefix;
            }
            String uri = "";
            String localName = prefix;
            String type = "CDATA";
            String value = namespace.getURI();
            
            namespaceAttributes.addAttribute( uri, localName, qualifiedName, type, value );
        }
        return namespaceAttributes;
    }

    
    /** @return true if the given namespace is an ignorable namespace 
      * (such as Namespace.NO_NAMESPACE or Namespace.XML_NAMESPACE) or if the
      * namespace has already been declared in the current scope
      */
    protected boolean isIgnoreableNamespace( Namespace namespace, NamespaceStack namespaceStack ) {
        if ( namespace.equals( Namespace.NO_NAMESPACE ) || namespace.equals( Namespace.XML_NAMESPACE ) ) {
            return true;
        }
        String uri = namespace.getURI();
        if ( uri == null || uri.length() <= 0 ) {
            return true;
        }
        return namespaceStack.contains( namespace );
    }

    /** Ensures non-null content handlers?
      */
    protected void checkForNullHandlers() {
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
