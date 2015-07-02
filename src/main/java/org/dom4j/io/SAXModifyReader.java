/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import org.dom4j.DocumentFactory;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * The SAXModifier parses, updates and writes an XML document. <br>
 * The input that is parsed is directly written to the specified output, unless
 * the current xml element has an associated ElementHandler. <br>
 * The {@link org.dom4j.ElementHandler}objects make it possible to update the
 * document on the fly, without having read tje complete document.
 * 
 * @author Wonne Keysers (Realsoftware.be)
 * 
 * @see org.dom4j.io.SAXReader
 * @see org.dom4j.io.XMLWriters
 */
class SAXModifyReader extends SAXReader {
    private XMLWriter xmlWriter;

    private boolean pruneElements;

    public SAXModifyReader() {
    }

    public SAXModifyReader(boolean validating) {
        super(validating);
    }

    public SAXModifyReader(DocumentFactory factory) {
        super(factory);
    }

    public SAXModifyReader(DocumentFactory factory, boolean validating) {
        super(factory, validating);
    }

    public SAXModifyReader(XMLReader xmlReader) {
        super(xmlReader);
    }

    public SAXModifyReader(XMLReader xmlReader, boolean validating) {
        super(xmlReader, validating);
    }

    public SAXModifyReader(String xmlReaderClassName) throws SAXException {
        super(xmlReaderClassName);
    }

    public SAXModifyReader(String xmlReaderClassName, boolean validating)
            throws SAXException {
        super(xmlReaderClassName, validating);
    }

    public void setXMLWriter(XMLWriter writer) {
        this.xmlWriter = writer;
    }

    public boolean isPruneElements() {
        return pruneElements;
    }

    public void setPruneElements(boolean pruneElements) {
        this.pruneElements = pruneElements;
    }

    protected SAXContentHandler createContentHandler(XMLReader reader) {
        SAXModifyContentHandler handler = new SAXModifyContentHandler(
                getDocumentFactory(), getDispatchHandler());

        handler.setXMLWriter(xmlWriter);

        return handler;
    }

    protected XMLWriter getXMLWriter() {
        return this.xmlWriter;
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
