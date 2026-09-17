/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.xml.secure.SecureDocumentBuilderFactory;
import org.apache.commons.xml.secure.SecureSAXParserFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * <code>JAXPHelper</code> contains some helper methods for working with JAXP.
 *
 * <p>Every factory comes from
 * <a href="https://commons.apache.org/proper/commons-secure-xml/">Apache Commons Secure XML</a>:
 * the parsers it creates never fetch an external resource unless an
 * {@link EntityResolver} opts it in, and they bound entity expansion.
 * Its
 * <a href="https://commons.apache.org/proper/commons-secure-xml/threat_model.html">threat model</a>
 * lists the settings a caller may still change.</p>
 *
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.7 $
 */
class JAXPHelper {
    protected JAXPHelper() {
    }

    /**
     * Creates a SAX2 {@link XMLReader} through {@link SecureSAXParserFactory}.
     *
     * @param validating
     *            whether the reader validates against the DTD, which an
     *            {@link EntityResolver} must then provide
     * @param namespaceAware
     *            whether the reader reports namespaces
     *
     * @return a new reader
     *
     * @throws IllegalStateException
     *             if a required secure setting cannot be applied to the
     *             implementation, or the implementation cannot provide a
     *             reader
     * @throws javax.xml.parsers.FactoryConfigurationError
     *             if no implementation is available or it cannot be
     *             instantiated
     */
    public static XMLReader createXMLReader(boolean validating,
            boolean namespaceAware) {
        SAXParserFactory factory = SecureSAXParserFactory.newInstance();
        factory.setValidating(validating);
        factory.setNamespaceAware(namespaceAware);

        try {
            SAXParser parser = factory.newSAXParser();

            return parser.getXMLReader();
        } catch (ParserConfigurationException | SAXException e) {
            // Current JAXP implementations fail eagerly while the factory is configured, so these
            // checked exceptions are not thrown in practice.
            throw new IllegalStateException("Couldn't create SAX reader", e);
        }
    }

    /**
     * Creates an empty W3C DOM document through
     * {@link SecureDocumentBuilderFactory}.
     *
     * @param validating
     *            whether the builder validates against the DTD
     * @param namespaceAware
     *            whether the builder supports namespaces
     *
     * @return a new, empty document
     *
     * @throws IllegalStateException
     *             if a required secure setting cannot be applied to the
     *             implementation, or the implementation cannot provide a
     *             document builder
     * @throws javax.xml.parsers.FactoryConfigurationError
     *             if no implementation is available or it cannot be
     *             instantiated
     */
    public static org.w3c.dom.Document createDocument(boolean validating,
            boolean namespaceAware) {
        DocumentBuilderFactory factory = SecureDocumentBuilderFactory.newInstance();
        factory.setValidating(validating);
        factory.setNamespaceAware(namespaceAware);

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();

            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            // Current JAXP implementations fail eagerly while the factory is configured, so this
            // checked exception is not thrown in practice.
            throw new IllegalStateException("Couldn't create DOM document", e);
        }
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
