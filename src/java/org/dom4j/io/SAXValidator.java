/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import org.dom4j.Document;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/** <p><code>SAXValidator</code> writes a DOM4J tree to a SAX XMLReader to
  * validate that it conforms to a DTD or XML Schema.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SAXValidator {

    /** <code>SAXWriter</code> used to write SAX events to a validating SAX parser */
    private SAXWriter saxWriter = new SAXWriter();
    
    /** <code>XMLReader</code> used to parse the SAX events */
    private XMLReader xmlReader;
    
    
    
    public SAXValidator() {
    }

    public SAXValidator(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }


    
    /** Validates the given <code>Document</code> by writing it to a 
      * validating SAX Parser.
      *
      * @param document is the Document to validate
      * @throw SAXException if a validation error occurs
      */
    public void validate(Document document) throws SAXException {
        if (document != null) {       
            configureReader();
            saxWriter.setXMLReader( xmlReader );
            saxWriter.write( document );
        }
    }
    
    
    // Properties
    //-------------------------------------------------------------------------                
    
    /** @return the <code>XMLReader</code> used to parse SAX events
      */
    public XMLReader getXMLReader() throws SAXException {
        if (xmlReader == null) {
            xmlReader = createXMLReader();
            configureReader();
        }
        return xmlReader;
    }

    /** Sets the <code>XMLReader</code> used to parse SAX events
      *
      * @param xmlReader is the <code>XMLReader</code> to parse SAX events
      */
    public void setXMLReader(XMLReader xmlReader) {
        this.xmlReader = xmlReader;
    }

    
    // Implementation methods
    //-------------------------------------------------------------------------                
    /** Factory Method to allow alternate methods of 
      * creating and configuring XMLReader objects
      */
    protected XMLReader createXMLReader() throws SAXException {
        return SAXHelper.createXMLReader( true );
    }
    
    /** Configures the XMLReader before use */
    protected void configureReader() throws SAXException {                
        // configure validation support
        xmlReader.setFeature(
            "http://xml.org/sax/features/validation", 
            true
        );

        // configure namespace support
        xmlReader.setFeature(
            "http://xml.org/sax/features/namespaces", 
            true
        );
        xmlReader.setFeature(
            "http://xml.org/sax/features/namespace-prefixes", 
            false
        );
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
