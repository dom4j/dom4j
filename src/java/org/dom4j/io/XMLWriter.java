/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
   
import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.DocumentType;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.DTDHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**<p><code>XMLWriter</code> takes a DOM4J tree and formats it to a
  * stream as XML.  
  * It can also take SAX events too so can be used by SAX clients as this object 
  * implements the {@link ContentHandler} and {@link LexicalHandler} interfaces.
  * as well. This formatter performs typical document
  * formatting.  The XML declaration and processing instructions are
  * always on their own lines. An {@link OutputFormat} object can be
  * used to define how whitespace is handled when printing and allows various
  * configuration options, such as to allow suppression of the XML declaration,
  * the encoding declaration or whether empty documents are collapsed.</p>
  *
  * <p> There are <code>write(...)</code> methods to print any of the
  * standard DOM4J classes, including <code>Document</code> and
  * <code>Element</code>, to either a <code>Writer</code> or an
  * <code>OutputStream</code>.  Warning: using your own
  * <code>Writer</code> may cause the writer's preferred character
  * encoding to be ignored.  If you use encodings other than UTF8, we
  * recommend using the method that takes an OutputStream instead.
  * </p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XMLWriter implements ContentHandler, LexicalHandler {

    protected static final OutputFormat DEFAULT_FORMAT = new OutputFormat();

    /** Stores the last type of node written so algorithms can refer to the 
      * previous node type */
    protected int lastOutputNodeType;

    /** The Writer used to output to */
    protected Writer writer;
    
    /** The Stack of namespaces written so far */
    private NamespaceStack namespaces = new NamespaceStack();
    
    /** The format used by this writer */
    private OutputFormat format;
    /** The initial number of indentations (so you can print a whole
        document indented, if you like) **/
    private int indentLevel = 0;

    

    public XMLWriter(Writer writer) {
        this( writer, DEFAULT_FORMAT );
    }
    
    public XMLWriter(Writer writer, OutputFormat format) {
        this.writer = writer;
        this.format = format;
    }
    
    public XMLWriter() throws UnsupportedEncodingException {
        this( DEFAULT_FORMAT );
    }

    public XMLWriter(OutputStream out) throws UnsupportedEncodingException {
        this( out, DEFAULT_FORMAT );
    }
    
    public XMLWriter(OutputStream out, OutputFormat format) throws UnsupportedEncodingException {
        this.format = format;
        this.writer = createWriter(out, format.getEncoding());
    }
    
    public XMLWriter(OutputFormat format) throws UnsupportedEncodingException {
        this.format = format;
        this.writer = createWriter( System.out, format.getEncoding() );
    }

    
    public void setWriter(Writer writer) {
        this.writer = writer;
    }
    
    public void setOutputStream(OutputStream out) throws UnsupportedEncodingException {
        this.writer = createWriter(out, format.getEncoding());
    }
    

    /** Set the initial indentation level.  This can be used to output
      * a document (or, more likely, an element) starting at a given
      * indent level, so it's not always flush against the left margin.
      * Default: 0
      *
      * @param indentLevel the number of indents to start with
      */
    public void setIndentLevel(int indentLevel) {
        this.indentLevel = indentLevel;
    }
    
    
    /** Flushes the underlying Writer */
    public void flush() throws IOException {
        writer.flush();
    }
    
    /** Closes the underlying Writer */
    public void close() throws IOException {
        writer.close();
    }

    /** Writes the new line text to the underlying Writer */
    public void println() throws IOException {
        writer.write( format.getLineSeparator() );
    }

    /** Writes the given {@link Attribute}.
      *
      * @param attribute <code>Attribute</code> to output.
      */
    public void write(Attribute attribute) throws IOException {        
        writer.write(" ");
        writer.write(attribute.getQualifiedName());
        writer.write("=");

        writer.write("\"");
        writer.write(escapeAttributeEntities(attribute.getValue()));
        writer.write("\"");
        lastOutputNodeType = Node.ATTRIBUTE_NODE;
    }

    
    /** <p>This will print the <code>Document</code> to the current Writer.</p>
     *
     * <p> Warning: using your own Writer may cause the writer's
     * preferred character encoding to be ignored.  If you use
     * encodings other than UTF8, we recommend using the method that
     * takes an OutputStream instead.  </p>
     *
     * <p>Note: as with all Writers, you may need to flush() yours
     * after this method returns.</p>
     *
     * @param doc <code>Document</code> to format.
     * @throws <code>IOException</code> - if there's any problem writing.
     **/
    public void write(Document doc) throws IOException {
        writeDeclaration();
        
        if (doc.getDocType() != null) {
            indent();
            write(doc.getDocType());
        }

        for ( int i = 0, size = doc.nodeCount(); i < size; i++ ) {
            Node node = doc.node(i);
            write( node );
        }
        writePrintln();
        flush();
    }

    /** <p>Writes the <code>{@link Element}</code>, including
      * its <code>{@link Attribute}</code>s, and its value, and all
      * its content (child nodes) to the current Writer.</p>
      *
      * @param element <code>Element</code> to output.
      */
    public void write(Element element) throws IOException {
        int size = element.nodeCount();
        
        writePrintln();
        indent();
        
        writer.write("<");
        writer.write(element.getQualifiedName());
        
        int previouslyDeclaredNamespaces = namespaces.size();
        Namespace ns = element.getNamespace();
        if (ns != null && ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE) {
            String prefix = ns.getPrefix();        
            if ( ! namespaces.containsPrefix( prefix ) ) {
                namespaces.push(ns);
                write(ns);
            }
        }

        // Print out additional namespace declarations
        List additionalNamespaces = element.additionalNamespaces();
        if (additionalNamespaces != null) {
            for (int i=0; i<additionalNamespaces.size(); i++) {
                Namespace additional = (Namespace)additionalNamespaces.get(i);
                namespaces.push(additional);
                write(additional);
            }
        }

        writeAttributes(element);

        lastOutputNodeType = Node.ELEMENT_NODE;
        
        if ( size <= 0 ) {
            writeEmptyElementClose(element);
        }
        else {
            if ( element.isTextOnly() ) {
                String text = format.isTrimText() ? element.getTextTrim() : element.getText();
                if (text == null || text.length() <= 0 ) {
                    writeEmptyElementClose(element);
                }
                else {
                    // we know it's not null or empty from above
                    writer.write(">");
                    writer.write( text );
                    writeClose(element);
                }
            }
            else {
                // we know it's not null or empty from above
                writer.write(">");        
                ++indentLevel;
                
                for ( int i = 0; i < size; i++ ) {
                    Node node = element.node(i);
                    write(node);
                }
                --indentLevel;                

                writePrintln();
                indent();

                writeClose(element);
            }
        }

        // remove declared namespaces from stack
        while (namespaces.size() > previouslyDeclaredNamespaces) {
            namespaces.pop();
        }
        
        lastOutputNodeType = Node.ELEMENT_NODE;
    }
    
    /** Writes the given {@link CDATA}.
      *
      * @param cdata <code>CDATA</code> to output.
      */
    public void write(CDATA cdata) throws IOException {
        writer.write(cdata.asXML());
        lastOutputNodeType = Node.CDATA_SECTION_NODE;
    }
    
    /** Writes the given {@link Comment}.
      *
      * @param comment <code>Comment</code> to output.
      */
    public void write(Comment comment) throws IOException {        
        if (format.isNewlines()) {
            if ( lastOutputNodeType != Node.COMMENT_NODE ) {
                println();
            }
            indent();
        }
        writer.write(comment.asXML());
        writePrintln();

        lastOutputNodeType = Node.COMMENT_NODE;
    }
    
    /** Writes the given {@link DocumentType}.
      *
      * @param docType <code>DocumentType</code> to output.
      */
    public void write(DocumentType docType) throws IOException {
        if (docType != null) {
            String publicID = docType.getPublicID();
            String systemID = docType.getSystemID();
            boolean hasPublic = false;

            writer.write("<!DOCTYPE ");
            writer.write(docType.getElementName());
            if ((publicID != null) && (!publicID.equals(""))) {
                writer.write(" PUBLIC \"");
                writer.write(publicID);
                writer.write("\"");
                hasPublic = true;
            }
            if ((systemID != null) && (!systemID.equals(""))) {
                if (!hasPublic) {
                    writer.write(" SYSTEM");
                }
                writer.write(" \"");
                writer.write(systemID);
                writer.write("\"");
            }
            writer.write(">");
            writePrintln();
        }
    }

    
    /** Writes the given {@link Entity}.
      *
      * @param entity <code>Entity</code> to output.
      */
    public void write(Entity entity) throws IOException {
        writer.write(entity.asXML());
        lastOutputNodeType = Node.ENTITY_REFERENCE_NODE;
    }
    

    /** Writes the given {@link Namespace}.
      *
      * @param namespace <code>Namespace</code> to output.
      */
    public void write(Namespace namespace) throws IOException {
        if ( namespace != null ) {
            String prefix = namespace.getPrefix();
            if (prefix != null && prefix.length() > 0) {
                writer.write(" xmlns");
                if (!prefix.equals("")) {
                    writer.write(":");
                    writer.write(prefix);
                }
                writer.write("=\"");
                writer.write(namespace.getURI());
                writer.write("\"");
            }
        }
    }

    /** Writes the given {@link ProcessingInstruction}.
      *
      * @param processingInstruction <code>ProcessingInstruction</code> to output.
      */
    public void write(ProcessingInstruction processingInstruction) throws IOException {
        //indent();
        writer.write(processingInstruction.asXML());
        writePrintln();
        lastOutputNodeType = Node.PROCESSING_INSTRUCTION_NODE;
    }
    
    /** <p>Print out a {@link String}, Perfoms
      * the necessary entity escaping and whitespace stripping.</p>
      *
      * @param text is the text to output
      */
    public void write(String text) throws IOException {
        boolean trimText = format.isTrimText();
        if ( trimText ) {
            text = text.trim();
        }
        if ( text.length() > 0 ) {
            if (lastOutputNodeType == Node.ELEMENT_NODE) {
                String padText = getPadText();
                if ( padText != null ) {
                    writer.write(padText);
                }
            }
            text = escapeElementEntities(text);
            if (trimText) {
                StringTokenizer tokenizer = new StringTokenizer(text);
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();
                    writer.write(token);
                    if (tokenizer.hasMoreTokens()) {
                        writer.write(" ");
                    }
                }
            } 
            else {                    
                writer.write(text);
            }
            lastOutputNodeType = Node.TEXT_NODE;
        }
    }


    /** Writes the given {@link Text}.
      *
      * @param text <code>Text</code> to output.
      */
    public void write(Text text) throws IOException {
        write(text.getText());
    }
    
    /** Writes the given {@link Node}.
      *
      * @param node <code>Node</code> to output.
      */
    public void write(Node node) throws IOException {
        int nodeType = node.getNodeType();
        switch (nodeType) {
            case Node.ELEMENT_NODE:
                write((Element) node);
                break;
            case Node.ATTRIBUTE_NODE:
                write((Attribute) node);
                break;
            case Node.TEXT_NODE:
                write((Text) node);
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
                write((Namespace) node);
                break;
            default:
                throw new IOException( "Invalid node type: " + node );
        }
    }
    
    /** Writes the given object which should be a String, a Node or a List
      * of Nodes.
      *
      * @param object is the object to output.
      */
    public void write(Object object) throws IOException {
        if (object instanceof Node) {
            write((Node) object);
        }
        else if (object instanceof String) {
            write((String) object);
        }
        else if (object instanceof List) {
            List list = (List) object;
            for ( int i = 0, size = list.size(); i < size; i++ ) {
                write( list.get(i) );
            }
        }
        else if (object != null) {
            throw new IOException( "Invalid object: " + object );
        }
    }
    
    
    /** <p>Writes the opening tag of an {@link Element}, 
      * including its {@link Attribute}s
      * but without its content.</p>
      *
      * @param element <code>Element</code> to output.
      */
    public void writeOpen(Element element) throws IOException {
        writer.write("<");
        writer.write( element.getQualifiedName() );
        writeAttributes(element);
        writer.write(">");
    }
    
    /** <p>Writes the closing tag of an {@link Element}</p>
      *
      * @param element <code>Element</code> to output.
      */
    public void writeClose(Element element) throws IOException {
        writeClose( element.getQualifiedName() );
    }

    
    // ContentHandler interface
    //-------------------------------------------------------------------------
    public void setDocumentLocator(Locator locator) {
    }
    
    public void startDocument() throws SAXException {
        try {
            writeDeclaration();
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    
    public void endDocument() throws SAXException {
    }
    
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
    }
    
    public void endPrefixMapping(String prefix) throws SAXException {
    }
    
    public void startElement(String namespaceURI, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            indent();
            writer.write("<");
            writer.write(qName);
            write( attributes );
            writer.write(">");
            writePrintln();
            ++indentLevel;
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            --indentLevel;
            indent();
            // XXXX: need to determine this using a stack and checking for
            // content / children
            boolean hadContent = true;
            if ( hadContent ) {
                writeClose(qName);
            }
            else {
                writeEmptyElementClose(qName);
            }
            writePrintln();
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            writer.write(ch, start, length);
        }
        catch (IOException e) {
            handleException(e);
        }
    }

    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        try {
            writer.write(ch, start, length);
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    public void skippedEntity(String name) throws SAXException {
    }
    
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            indent();
            writer.write("<?");
            writer.write(target);
            writer.write(" ");
            writer.write(data);
            writer.write("?>");
            writePrintln();
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    

    // DTDHandler interface
    //-------------------------------------------------------------------------
    public void notationDecl(String name, String publicID, String systemID) throws SAXException {
        write(name, publicID, systemID);
    }
    
    public void unparsedEntityDecl(String name, String publicID, String systemID, String notationName) throws SAXException {
    }
    

    // LexicalHandler interface
    //-------------------------------------------------------------------------
    public void startDTD(String name, String publicID, String systemID) throws SAXException {
        write(name, publicID, systemID);
    }
    
    public void endDTD() throws SAXException {
    }
    
    public void startCDATA() throws SAXException {
        try {
            writer.write( "<![CDATA[" );
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    public void endCDATA() throws SAXException {
        try {
            writer.write( "]]>" );
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    public void startEntity(String name) throws SAXException {
        try {
            writer.write( "&" );
            writer.write( name );
            writer.write( ";" );
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    public void endEntity(String name) throws SAXException {
    }
    
    public void comment(char[] ch, int start, int length) throws SAXException {
        try {
            writer.write( "<!--" );
            writer.write(ch, start, length);
            writer.write( "-->" );
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    
    
    // Implementation methods
    //-------------------------------------------------------------------------
    
    /** Writes the attributes of the given element
      *
      */
    protected void writeAttributes( Element element ) throws IOException {

        // I do not yet handle the case where the same prefix maps to
        // two different URIs. For attributes on the same element
        // this is illegal; but as yet we don't throw an exception
        // if someone tries to do this
        Set prefixes = new HashSet();

        for ( int i = 0, size = element.attributeCount(); i < size; i++ ) {
            Attribute attribute = element.attribute(i);
            Namespace ns = attribute.getNamespace();
            if (ns != null && ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE) {
                String prefix = ns.getPrefix();           
                String uri = namespaces.getURI(prefix);
                if (!ns.getURI().equals(uri)) { // output a new namespace declaration
                    write(ns);
                    namespaces.push(ns);
                }
            }
            
            writer.write(" ");
            writer.write(attribute.getQualifiedName());
            writer.write("=");

            writer.write("\"");
            writer.write(escapeAttributeEntities(attribute.getValue()));
            writer.write("\"");
        }
    }

    protected void write(Attributes attributes) throws IOException {
        for (int i = 0, size = attributes.getLength(); i < size; i++) {
            write( attributes, i );
        }
    }

    protected void write(Attributes attributes, int index) throws IOException {       
        writer.write(" ");
        writer.write(attributes.getQName(index));
        writer.write("=");

        writer.write("\"");
        writer.write(escapeAttributeEntities(attributes.getValue(index)));
        writer.write("\"");
    }

    
    
    protected void indent() throws IOException {
        String indent = format.getIndent();
        if (indent != null && !indent.equals("")) {
            for (int i = 0; i < indentLevel; i++) {
                writer.write(indent);
            }
        }
    }
    
    /**
     * <p>
     * This will print a new line only if the newlines flag was set to true
     * </p>
     *
     * @param out <code>Writer</code> to write to
     */
    protected void writePrintln() throws IOException  {
        if (format.isNewlines()) {
            writer.write( format.getLineSeparator() );
        }
    }

    /**
     * Get an OutputStreamWriter, use preferred encoding.
     */
    protected Writer createWriter(OutputStream outStream, String encoding) throws UnsupportedEncodingException {
        Writer writer = new OutputStreamWriter(
            new BufferedOutputStream(outStream), encoding
        );
        return writer;
    }

    /**
     * <p>
     * This will write the declaration to the given Writer.
     *   Assumes XML version 1.0 since we don't directly know.
     * </p>
     */
    protected void writeDeclaration() throws IOException {
        String encoding = format.getEncoding();
        
        // Only print of declaration is not suppressed
        if (! format.isSuppressDeclaration()) {
            // Assume 1.0 version
            if (encoding.equals("UTF8")) {
                writer.write("<?xml version=\"1.0\"");
                if (!format.isOmitEncoding()) {
                    writer.write(" encoding=\"UTF-8\"");
                }
                writer.write("?>");
            } else {
                writer.write("<?xml version=\"1.0\"");
                if (! format.isOmitEncoding()) {
                    writer.write(" encoding=\"" + encoding + "\"");
                }
                writer.write("?>");
            }
            println();
        }        
    }    

    protected void write(String name, String publicID, String systemID) throws SAXException {
        try {
            boolean hasPublic = false;
            writer.write("<!DOCTYPE ");
            writer.write(name);
            if ((publicID != null) && (!publicID.equals(""))) {
                writer.write(" PUBLIC \"");
                writer.write(publicID);
                writer.write("\"");
                hasPublic = true;
            }
            if ((systemID != null) && (!systemID.equals(""))) {
                if (!hasPublic) {
                    writer.write(" SYSTEM");
                }
                writer.write(" \"");
                writer.write(systemID);
                writer.write("\"");
            }
            writer.write(">");
            writePrintln();
        }
        catch (IOException e) {
            handleException(e);
        }
    }
    
    /** Writes the empty close of an {@link Element}.
      *
      * @param element <code>Element</code> to close.
      */
    protected void writeEmptyElementClose(Element element) throws IOException {
        writeEmptyElementClose( element.getQualifiedName() );
    }
    
    
    protected void writeClose(String qualifiedName) throws IOException {
        writer.write("</");
        writer.write(qualifiedName);
        writer.write(">");
    }

    protected void writeEmptyElementClose(String qualifiedName) throws IOException {
        // Simply close up
        if (! isExpandEmptyElements()) {
            writer.write("/>");
        } else {
            writer.write("></");
            writer.write(qualifiedName);
            writer.write(">");
        }
    }

    protected boolean isExpandEmptyElements() {
        return format.isExpandEmptyElements();
    }

    /**
     * <p>
     * This will take the five pre-defined entities in XML 1.0 and
     *   convert their character representation to the appropriate
     *   entity reference, suitable for XML attributes.
     * </p>
     *
     * @param st <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    protected String escapeAttributeEntities(String st) {
        StringBuffer buff = new StringBuffer();
        char[] block = st.toCharArray();
        String stEntity = null;
        int i, last;

        for (i=0, last=0; i < block.length; i++) {
            switch(block[i]) {
                case '<' :
                    stEntity = "&lt;";
                    break;
                case '>' :
                    stEntity = "&gt;";
                    break;
                case '\'' :
                    stEntity = "&apos;";
                    break;
                case '\"' :
                    stEntity = "&quot;";
                    break;
                case '&' :
                    stEntity = "&amp;";
                    break;
                default :
                    /* no-op */ ;
            }
            if (stEntity != null) {
                buff.append(block, last, i - last);
                buff.append(stEntity);
                stEntity = null;
                last = i + 1;
            }
        }
        if(last < block.length) {
            buff.append(block, last, i - last);
        }

        return buff.toString();
    }



    /**
     * <p>
     * This will take the three pre-defined entities in XML 1.0
     *   (used specifically in XML elements) and
     *   convert their character representation to the appropriate
     *   entity reference, suitable for XML element.
     * </p>
     *
     * @param st <code>String</code> input to escape.
     * @return <code>String</code> with escaped content.
     */
    protected String escapeElementEntities(String st) {
        StringBuffer buff = new StringBuffer();
        char[] block = st.toCharArray();
        String stEntity = null;
        int i, last;

        for (i=0, last=0; i < block.length; i++) {
            switch(block[i]) {
                case '<' :
                    stEntity = "&lt;";
                    break;
                case '>' :
                    stEntity = "&gt;";
                    break;
                case '&' :
                    stEntity = "&amp;";
                    break;
                default :
                    /* no-op */ ;
            }
            if (stEntity != null) {
                buff.append(block, last, i - last);
                buff.append(stEntity);
                stEntity = null;
                last = i + 1;
            }
        }
        if(last < block.length) {
            buff.append(block, last, i - last);
        }

        return buff.toString();
    }

    protected void handleException(IOException e) throws SAXException {
        throw new SAXException(e);
    }
    
    protected String getPadText() {
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
