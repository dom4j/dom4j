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
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.DocumentType;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.LocatorImpl;

/** <p><code>SAXWriter</code> writes a DOM4J tree to a SAX ContentHandler.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SAXWriter {

    /** <code>ContentHandler</code> to which SAX events are raised */
    private ContentHandler contentHandler;
    
    /** code>EntityResolver</code> fired when a document has a DTD */
    private EntityResolver entityResolver;

    /** code>LexicalHandler</code> fired on Entity and CDATA sections */
    private LexicalHandler lexicalHandler;

    /** code>AttributesImpl</code> used when generating the Attributes */
    private AttributesImpl attributes = new AttributesImpl();
    
    
    
    public SAXWriter() {
    }

    public SAXWriter(ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
    }

    public SAXWriter(
        ContentHandler contentHandler, 
        EntityResolver entityResolver, 
        LexicalHandler lexicalHandler
    ) {
        this.contentHandler = contentHandler;
        this.entityResolver = entityResolver;
        this.lexicalHandler = lexicalHandler;
    }

    
    
    // Properties
    
    /** @return the <code>ContentHandler</code> called when SAX events 
      * are raised
      */
    public ContentHandler getContentHandler() throws SAXException {
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
        entityResolver = entityResolver;
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
        lexicalHandler = lexicalHandler;
    }

    
    

    /** Generates SAX events for the given Document and all its content
      *
      * @param document is the Document to parse
      * @throw SAXException if there is a SAX error processing the events
      */
    public void write(Document document) throws SAXException {
        if (document != null) {       
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
    
    /** Generates SAX events for the given text
      *
      * @param text is the text to send to the SAX ContentHandler
      * @throw SAXException if there is a SAX error processing the events
      */
    public void write( String text ) throws SAXException {
        char[] chars = text.toCharArray();
        contentHandler.characters( chars, 0, chars.length );
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
    

    
    // Implementation methods    
    
    protected void writeContent( Branch branch, NamespaceStack namespaces ) throws SAXException {
        for ( Iterator iter = branch.nodeIterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if ( object instanceof Element ) {
                write( (Element) object, namespaces );
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
                    throw new SAXException( "Invalid Node in DOM4J content: " + object );
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
                // namespaces are written via write of element
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
        
        DocumentType docType = document.getDocType();
        if (docType != null) {
            locator.setPublicId( docType.getPublicID() );
            locator.setSystemId( docType.getSystemID() );
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
    
    protected void write( Element element, NamespaceStack namespaces ) throws SAXException {
        int stackSize = namespaces.size();
        startPrefixMapping(element, namespaces);
        startElement(element);
        writeContent(element, namespaces);
        endElement(element);
        endPrefixMapping(namespaces, stackSize);
    }
    
    /** Fires a SAX startPrefixMapping event for all the namespaces
      * which have just come into scope
      */
    protected void startPrefixMapping( Element element, NamespaceStack namespaces ) throws SAXException {
        List declaredNamespaces = element.getDeclaredNamespaces();
        for ( int i = 0, size = declaredNamespaces.size(); i < size ; i++ ) {
            Namespace namespace = (Namespace) declaredNamespaces.get(i);
            if ( ! isIgnoreableNamespace( namespace, namespaces ) ) {
                namespaces.push( namespace );
                contentHandler.startPrefixMapping(
                    namespace.getPrefix(), namespace.getURI()
                );
            }
        }
    }
    
    /** Fires a SAX endPrefixMapping event for all the namespaces which 
      * have gone out of scope
      */
    protected void endPrefixMapping( NamespaceStack namespaces, int stackSize ) throws SAXException {                       
        while ( namespaces.size() > stackSize ) {
            Namespace namespace = namespaces.pop();
            if ( namespace != null ) {
                contentHandler.endPrefixMapping( namespace.getPrefix() );            
            }
        }
    }
    
    
    protected void startElement( Element element ) throws SAXException {                       
        contentHandler.startElement( 
            element.getNamespaceURI(), 
            element.getName(), 
            element.getQualifiedName(), 
            createAttributes( element )
        );
    }

    protected void endElement( Element element ) throws SAXException {        
        contentHandler.endElement( 
            element.getNamespaceURI(), 
            element.getName(), 
            element.getQualifiedName()
        );
    }

    protected Attributes createAttributes( Element element ) throws SAXException {
        attributes.clear();
        
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
    
    /** @return true if the given namespace is an ignorable namespace 
      * (such as Namespace.NO_NAMESPACE or Namespace.XML_NAMESPACE) or if the
      * namespace has already been declared in the current scope
      */
    protected boolean isIgnoreableNamespace( Namespace namespace, NamespaceStack namespaces ) {
        if ( namespace.equals( Namespace.NO_NAMESPACE ) || namespace.equals( Namespace.XML_NAMESPACE ) ) {
            return true;
        }
        return namespaces.containsPrefix( namespace.getPrefix() );
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
