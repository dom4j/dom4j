/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;

import org.dom4j.Document;
import org.dom4j.Node;

import org.xml.sax.InputSource;
import org.xml.sax.XMLFilter;
import org.xml.sax.XMLReader;

/** <p><code>DocumentSource</code> implements a JAXP {@link Source}
  * for a {@link Document}.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DocumentSource extends SAXSource {

    /** The XMLReader to use */
    private SAXWriter saxWriter = new SAXWriter();

    
    /** Creates a JAXP {@link Source} for the given 
      * {@link Node}.
      */
    public DocumentSource(Node node) {
        setDocument(node.getDocument());
    }

    /** Creates a JAXP {@link Source} for the given 
      * {@link Document}.
      */
    public DocumentSource(Document document) {
        setDocument(document);
    }


    // Properties
    //-------------------------------------------------------------------------                

    /** @return the document which is being used as the JAXP {@link Source}
      */
    public Document getDocument() {
        DocumentInputSource documentInputSource 
            = (DocumentInputSource) getInputSource();
        return documentInputSource.getDocument();
    }

    /** Sets the document used as the JAXP {@link Source}
      */
    public void setDocument(Document document) {
        super.setInputSource( new DocumentInputSource(document) );
    }


    // Overloaded methods
    //-------------------------------------------------------------------------                

    /** @return the XMLReader to be used for the JAXP {@link Source}.
     */
    public XMLReader getXMLReader() {
        return saxWriter;
    }

    /** This method is not supported as this source is always a 
      * {@link Document} instance.
      *
      * @throws UnsupportedOperationException as this method is unsupported
      */
    public void setInputSource(InputSource inputSource) 
            throws UnsupportedOperationException {
        if ( inputSource instanceof DocumentInputSource ) {
            super.setInputSource( (DocumentInputSource) inputSource );
        }
        else {
            throw new UnsupportedOperationException();
        }
    }

    /** Sets the XMLReader used for the JAXP {@link Source}.
      */
    public void setXMLReader(XMLReader reader)
            throws UnsupportedOperationException {
/*
        if (reader instanceof XMLFilter) {
            XMLFilter filter = (XMLFilter) reader;
            while (filter.getParent() instanceof XMLFilter) {
                filter = (XMLFilter) filter.getParent();
            }
            // install filter in SAXWriter....
            saxWriter.setParent(xmlReader);
        }
*/        
        throw new UnsupportedOperationException();
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
