/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/**
 * <p>
 * <code>SAXHelper</code> contains some helper methods for working with SAX
 * and XMLReader objects.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
class SAXHelper {
    private static boolean loggedWarning = true;

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
     * Creats a default XMLReader via the org.xml.sax.driver system property or
     * JAXP if the system property is not set.
     * 
     * @param validating
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws SAXException
     *             DOCUMENT ME!
     */
    public static XMLReader createXMLReader(boolean validating)
            throws SAXException {
        XMLReader reader = null;

        if (reader == null) {
            reader = createXMLReaderViaJAXP(validating, true);
        }

        if (reader == null) {
            try {
                reader = XMLReaderFactory.createXMLReader();
            } catch (Exception e) {
                if (isVerboseErrorReporting()) {
                    // log all exceptions as warnings and carry
                    // on as we have a default SAX parser we can use
                    System.out.println("Warning: Caught exception attempting "
                            + "to use SAX to load a SAX XMLReader ");
                    System.out.println("Warning: Exception was: " + e);
                    System.out
                            .println("Warning: I will print the stack trace "
                                    + "then carry on using the default "
                                    + "SAX parser");
                    e.printStackTrace();
                }

                throw new SAXException(e);
            }
        }

        if (reader == null) {
            throw new SAXException("Couldn't create SAX reader");
        }

        return reader;
    }

    /**
     * This method attempts to use JAXP to locate the SAX2 XMLReader
     * implementation. This method uses reflection to avoid being dependent
     * directly on the JAXP classes.
     * 
     * @param validating
     *            DOCUMENT ME!
     * @param namespaceAware
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    protected static XMLReader createXMLReaderViaJAXP(boolean validating,
            boolean namespaceAware) {
        // try use JAXP to load the XMLReader...
        try {
            return JAXPHelper.createXMLReader(validating, namespaceAware);
        } catch (Throwable e) {
            if (!loggedWarning) {
                loggedWarning = true;

                if (isVerboseErrorReporting()) {
                    // log all exceptions as warnings and carry
                    // on as we have a default SAX parser we can use
                    System.out.println("Warning: Caught exception attempting "
                            + "to use JAXP to load a SAX XMLReader");
                    System.out.println("Warning: Exception was: " + e);
                    e.printStackTrace();
                }
            }
        }

        return null;
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
