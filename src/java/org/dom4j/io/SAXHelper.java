/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.dom4j.io.aelfred.SAXDriver;

import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/** <p><code>SAXHelper</code> contains some helper methods for working with 
  * SAX and XMLReader objects.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
class SAXHelper {

    public static boolean setParserProperty(XMLReader reader, String propertyName, Object value) {    
        try {
            reader.setProperty(propertyName, value);
            return true;
        } 
        catch (SAXNotSupportedException e) {
        } 
        catch (SAXNotRecognizedException e) {
        }
        return false;
    }

    /** Creats a default XMLReader via the org.xml.sax.driver system property 
      * or JAXP if the system property is not set.
      */
    public static XMLReader createXMLReader(boolean validating) throws SAXException {
        String className = null;
        try {
            className = System.getProperty( "org.xml.sax.driver" );
        }
        catch (Exception e) {
        }
        if (className == null || className.trim().length() <= 0) {
            XMLReader reader = createXMLReaderViaJAXP( validating );
            if ( reader != null ) {
                return reader;
            }
            return new SAXDriver();
        }
        return XMLReaderFactory.createXMLReader();
    }

    /** This method attempts to use JAXP to locate the  
      * SAX2 XMLReader implementation.  
      * This method uses reflection to avoid being dependent directly
      * on the JAXP classes.
      */
    protected static XMLReader createXMLReaderViaJAXP(boolean validating) {
        try {
            Class factoryClass = Class.forName("javax.xml.parsers.SAXParserFactory");
            // try use JAXP to load the XMLReader...
            try {
                return JAXPHelper.createXMLReader( validating );
            }
            catch (Exception e) {
                if ( isVerboseErrorReporting() ) {
                    // log all exceptions as warnings and carry
                    // on as we have a default SAX parser we can use
                    System.out.println( 
                        "Warning: Caught exception attempting to use JAXP to "
                         + "load a SAX XMLReader " 
                    );
                    System.out.println( "Warning: Exception was: " + e );
                    System.out.println( 
                        "Warning: I will print the stack trace then carry on "
                         + "using the default SAX parser" 
                     );
                    e.printStackTrace();
                }
                else {
                    System.out.println( 
                        "Info: Could not use JAXP to load a SAXParser. Will use Aelfred instead" 
                    );
                }
            }
        }
        catch (Exception e) {
            // JAXP is not loaded so continue, its probably not in the CLASSPATH
        }
        return null;
    }
    
    protected static boolean isVerboseErrorReporting() {
        try {
            String flag = System.getProperty( "org.dom4j.verbose" );
            if ( flag != null && flag.equalsIgnoreCase( "true" ) ) {
                return true;
            }
        }
        catch (Exception e) {
            // in case a security exception
            // happens in an applet or similar JVM
        }
        return false;
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
