/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import java.io.IOException;

import org.dom4j.Document;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * <p>
 * <code>SAXValidator</code> validates an XML document by writing the document
 * to a text buffer and parsing it with a validating SAX parser. This could be
 * implemented much more efficiently by validating against the dom4j object
 * model directly but at least allows the reuse of existing SAX based validating
 * parsers.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class SAXValidator {
    /** <code>XMLReader</code> used to parse the SAX events */
    private XMLReader xmlReader;

    /** ErrorHandler class to use */
    private ErrorHandler errorHandler;

    public SAXValidator() {
    }

    public SAXValidator(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    /**
     * Validates the given <code>Document</code> by writing it to a validating
     * SAX Parser.
     * 
     * @param document
     *            is the Document to validate
     * 
     * @throws SAXException
     *             if a validation error occurs
     * @throws RuntimeException
     *             DOCUMENT ME!
     */
    public void validate(Document document) throws SAXException {
        if (document != null) {
            XMLReader reader = getXMLReader();

            if (errorHandler != null) {
                reader.setErrorHandler(errorHandler);
            }

            try {
                reader.parse(new DocumentInputSource(document));
            } catch (IOException e) {
                throw new RuntimeException("Caught and exception that should "
                        + "never happen: " + e);
            }
        }
    }

    // Properties
    // -------------------------------------------------------------------------

    /**
     * DOCUMENT ME!
     * 
     * @return the <code>XMLReader</code> used to parse SAX events
     * 
     * @throws SAXException
     *             DOCUMENT ME!
     */
    public XMLReader getXMLReader() throws SAXException {
        if (xmlReader == null) {
            xmlReader = createXMLReader();
            configureReader();
        }

        return xmlReader;
    }

    /**
     * Sets the <code>XMLReader</code> used to parse SAX events
     * 
     * @param reader
     *            is the <code>XMLReader</code> to parse SAX events
     * 
     * @throws SAXException
     *             DOCUMENT ME!
     */
    public void setXMLReader(XMLReader reader) throws SAXException {
        this.xmlReader = reader;
        configureReader();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the <code>ErrorHandler</code> used by SAX
     */
    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    /**
     * Sets the <code>ErrorHandler</code> used by the SAX
     * <code>XMLReader</code>.
     * 
     * @param errorHandler
     *            is the <code>ErrorHandler</code> used by SAX
     */
    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * Factory Method to allow alternate methods of creating and configuring
     * XMLReader objects
     * 
     * @return DOCUMENT ME!
     * 
     * @throws SAXException
     *             DOCUMENT ME!
     */
    protected XMLReader createXMLReader() throws SAXException {
        return SAXHelper.createXMLReader(true);
    }

    /**
     * Configures the XMLReader before use
     * 
     * @throws SAXException
     *             DOCUMENT ME!
     */
    protected void configureReader() throws SAXException {
        ContentHandler handler = xmlReader.getContentHandler();

        if (handler == null) {
            xmlReader.setContentHandler(new DefaultHandler());
        }

        // configure validation support
        xmlReader.setFeature("http://xml.org/sax/features/validation", true);

        // configure namespace support
        xmlReader.setFeature("http://xml.org/sax/features/namespaces", true);
        xmlReader.setFeature("http://xml.org/sax/features/namespace-prefixes",
                false);
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
