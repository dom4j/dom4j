/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * The SAXModifier reads, modifies and writes XML documents using SAX.
 * 
 * <p>
 * Registered {@link ElementModifier}objects can provide modifications to (part
 * of) the xml tree, while the document is still being processed. This makes it
 * possible to change large xml documents without having them in memory.
 * </p>
 * 
 * <p>
 * The modified document is written when the {@link XMLWriter}is specified.
 * </p>
 * 
 * @author Wonne Keysers (Realsoftware.be)
 * 
 * @see org.dom4j.io.SAXReader
 * @see org.dom4j.io.XMLWriter
 */
public class SAXModifier {
    private XMLWriter xmlWriter;

    private XMLReader xmlReader;

    private boolean pruneElements;

    private SAXModifyReader modifyReader;

    private HashMap modifiers = new HashMap();

    /**
     * Creates a new modifier. <br>
     * The XMLReader to parse the source will be created via the
     * org.xml.sax.driver system property or JAXP if the system property is not
     * set.
     */
    public SAXModifier() {
    }

    /**
     * Creates a new modifier. <br>
     * The XMLReader to parse the source will be created via the
     * org.xml.sax.driver system property or JAXP if the system property is not
     * set.
     * 
     * @param pruneElements
     *            Set to true when the modified document must NOT be kept in
     *            memory.
     */
    public SAXModifier(boolean pruneElements) {
        this.pruneElements = pruneElements;
    }

    /**
     * Creates a new modifier that will the specified {@link
     * org.xml.sax.XMLReader} to parse the source.
     * 
     * @param xmlReader
     *            The XMLReader to use
     */
    public SAXModifier(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    /**
     * Creates a new modifier that will the specified {@link
     * org.xml.sax.XMLReader} to parse the source.
     * 
     * @param xmlReader
     *            The XMLReader to use
     * @param pruneElements
     *            Set to true when the modified document must NOT be kept in
     *            memory.
     */
    public SAXModifier(XMLReader xmlReader, boolean pruneElements) {
        this.xmlReader = xmlReader;
    }

    /**
     * Reads a Document from the given {@link java.io.File}and writes it to the
     * specified {@link XMLWriter}using SAX. Registered {@linkElementModifier}
     * objects are invoked on the fly.
     * 
     * @param source
     *            is the <code>File</code> to read from.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             DocumentException org.dom4j.DocumentException} if an error
     *             occurs during parsing.
     */
    public Document modify(File source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Reads a Document from the given {@link org.xml.sax.InputSource}and
     * writes it to the specified {@link XMLWriter}using SAX. Registered
     * {@link ElementModifier}objects are invoked on the fly.
     * 
     * @param source
     *            is the <code>org.xml.sax.InputSource</code> to read from.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             DocumentException org.dom4j.DocumentException} if an error
     *             occurs during parsing.
     */
    public Document modify(InputSource source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Reads a Document from the given {@link java.io.InputStream}and writes it
     * to the specified {@link XMLWriter}using SAX. Registered {@link
     * ElementModifier} objects are invoked on the fly.
     * 
     * @param source
     *            is the <code>java.io.InputStream</code> to read from.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             DocumentException org.dom4j.DocumentException} if an error
     *             occurs during parsing.
     */
    public Document modify(InputStream source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Reads a Document from the given {@link java.io.InputStream}and writes it
     * to the specified {@link XMLWriter}using SAX. Registered {@link
     * ElementModifier} objects are invoked on the fly.
     * 
     * @param source
     *            is the <code>java.io.InputStream</code> to read from.
     * @param systemId
     *            DOCUMENT ME!
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             DocumentException org.dom4j.DocumentException} if an error
     *             occurs during parsing.
     */
    public Document modify(InputStream source, String systemId)
            throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Reads a Document from the given {@link java.io.Reader}and writes it to
     * the specified {@link XMLWriter}using SAX. Registered {@link
     * ElementModifier} objects are invoked on the fly.
     * 
     * @param source
     *            is the <code>java.io.Reader</code> to read from.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             DocumentException org.dom4j.DocumentException} if an error
     *             occurs during parsing.
     */
    public Document modify(Reader source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Reads a Document from the given {@link java.io.Reader}and writes it to
     * the specified {@link XMLWriter}using SAX. Registered {@link
     * ElementModifier} objects are invoked on the fly.
     * 
     * @param source
     *            is the <code>java.io.Reader</code> to read from.
     * @param systemId
     *            DOCUMENT ME!
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             DocumentException org.dom4j.DocumentException} if an error
     *             occurs during parsing.
     */
    public Document modify(Reader source, String systemId)
            throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Reads a Document from the given {@link java.net.URL}and writes it to the
     * specified {@link XMLWriter}using SAX. Registered {@linkElementModifier}
     * objects are invoked on the fly.
     * 
     * @param source
     *            is the <code>java.net.URL</code> to read from.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             DocumentException org.dom4j.DocumentException} if an error
     *             occurs during parsing.
     */
    public Document modify(URL source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Reads a Document from the given URL or filename and writes it to the
     * specified {@link XMLWriter}using SAX. Registered {@linkElementModifier}
     * objects are invoked on the fly.
     * 
     * @param source
     *            is the URL or filename to read from.
     * 
     * @return the newly created Document instance
     * 
     * @throws DocumentException
     *             DocumentException org.dom4j.DocumentException} if an error
     *             occurs during parsing.
     */
    public Document modify(String source) throws DocumentException {
        try {
            return installModifyReader().read(source);
        } catch (SAXModifyException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Adds the {@link ElementModifier}to be called when the specified element
     * path is encounted while parsing the source.
     * 
     * @param path
     *            The element path to be handled
     * @param modifier
     *            The {@link ElementModifier}to be called by the event based
     *            processor.
     */
    public void addModifier(String path, ElementModifier modifier) {
        this.modifiers.put(path, modifier);
    }

    /**
     * Removes all registered {@link ElementModifier}instances from the event
     * based processor.
     */
    public void resetModifiers() {
        this.modifiers.clear();
        getSAXModifyReader().resetHandlers();
    }

    /**
     * Removes the {@link ElementModifier}from the event based processor, for
     * the specified element path.
     * 
     * @param path
     *            The path to remove the {@link ElementModifier}for.
     */
    public void removeModifier(String path) {
        this.modifiers.remove(path);
        getSAXModifyReader().removeHandler(path);
    }

    /**
     * Get the {@link org.dom4j.DocumentFactory}used to create the DOM4J
     * document structure
     * 
     * @return <code>DocumentFactory</code> that will be used
     */
    public DocumentFactory getDocumentFactory() {
        return getSAXModifyReader().getDocumentFactory();
    }

    /**
     * Sets the {@link org.dom4j.DocumentFactory}used to create the DOM4J
     * document tree.
     * 
     * @param factory
     *            <code>DocumentFactory</code> to be used
     */
    public void setDocumentFactory(DocumentFactory factory) {
        getSAXModifyReader().setDocumentFactory(factory);
    }

    /**
     * Returns the current {@link XMLWriter}.
     * 
     * @return XMLWriter
     */
    public XMLWriter getXMLWriter() {
        return this.xmlWriter;
    }

    /**
     * Sets the {@link XMLWriter}used to write the modified document.
     * 
     * @param writer
     *            The writer to use.
     */
    public void setXMLWriter(XMLWriter writer) {
        this.xmlWriter = writer;
    }

    /**
     * Returns true when xml elements are not kept in memory while parsing. The
     * {@link org.dom4j.Document}returned by the modify methods will be null.
     * 
     * @return Returns the pruneElements.
     */
    public boolean isPruneElements() {
        return pruneElements;
    }

    private SAXReader installModifyReader() throws DocumentException {
        try {
            SAXModifyReader reader = getSAXModifyReader();

            if (isPruneElements()) {
                modifyReader.setDispatchHandler(new PruningDispatchHandler());
            }

            reader.resetHandlers();

            Iterator modifierIt = this.modifiers.entrySet().iterator();

            while (modifierIt.hasNext()) {
                Map.Entry entry = (Map.Entry) modifierIt.next();

                SAXModifyElementHandler handler = new SAXModifyElementHandler(
                        (ElementModifier) entry.getValue());
                reader.addHandler((String) entry.getKey(), handler);
            }

            reader.setXMLWriter(getXMLWriter());
            reader.setXMLReader(getXMLReader());

            return reader;
        } catch (SAXException ex) {
            throw new DocumentException(ex.getMessage(), ex);
        }
    }

    private XMLReader getXMLReader() throws SAXException {
        if (this.xmlReader == null) {
            xmlReader = SAXHelper.createXMLReader(false);
        }

        return this.xmlReader;
    }

    private SAXModifyReader getSAXModifyReader() {
        if (modifyReader == null) {
            modifyReader = new SAXModifyReader();
        }

        return modifyReader;
    }
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
