/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import org.apache.commons.xml.secure.SecureSAXParserFactory;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;

/**
 * <p>
 * <code>SAXHelper</code> contains some helper methods for working with SAX
 * and XMLReader objects.
 * </p>
 *
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision: 1.18 $
 */
class SAXHelper {
    protected SAXHelper() {
    }

    public static boolean setParserProperty(XMLReader reader,
            String propertyName, Object value) {
        try {
            reader.setProperty(propertyName, value);

            return true;
        } catch (SAXNotSupportedException e) {
            // ignore
        } catch (SAXNotRecognizedException e) {
            // ignore
        }

        return false;
    }

    public static boolean setParserFeature(XMLReader reader,
            String featureName, boolean value) {
        try {
            reader.setFeature(featureName, value);

            return true;
        } catch (SAXNotSupportedException e) {
            // ignore
        } catch (SAXNotRecognizedException e) {
            // ignore
        }

        return false;
    }

    /**
     * Creates a default XMLReader via JAXP.
     *
     * <p>This method internally calls {@link SecureSAXParserFactory}{@code .newInstance().newSAXParser().getXMLReader()},
     * with the requested validation and with namespace support.</p>
     *
     * <p>The returned reader is namespace-aware, reports no namespace prefixes
     * as attributes and uses a {@code Locator2} if available. Be sure to
     * configure the returned reader if this does not suit you.</p>
     *
     * <p>The reader remains secure, as long as you don't loosen any of the
     * <a href="https://commons.apache.org/proper/commons-secure-xml/threat_model.html#Reserved_Settings">reserved settings</a>
     * or install an allow-all resolver, such as the one
     * {@link EntityResolutionStrategy#ALLOW} installs.</p>
     *
     * @param validating
     *            whether the reader validates against the DTD
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
    public static XMLReader createXMLReader(boolean validating) {
        XMLReader reader = JAXPHelper.createXMLReader(validating, true);

        // configure namespace support
        SAXHelper.setParserFeature(reader, "http://xml.org/sax/features/namespace-prefixes", false);

        // use Locator2 if possible
        SAXHelper.setParserFeature(reader,"http://xml.org/sax/features/use-locator2", true);

        return reader;
    }

    protected static boolean isVerboseErrorReporting() {
        try {
            String flag = System.getProperty("org.dom4j.verbose");

            if ((flag != null) && flag.equalsIgnoreCase("true")) {
                return true;
            }
        } catch (Exception e) {
            // in case a security exception
            // happens in an applet or similar JVM
        }

        return true;
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
