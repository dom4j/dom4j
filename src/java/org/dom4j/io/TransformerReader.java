/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;


import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;

import org.xml.sax.InputSource;

/** <p><code>TransformerReader</code> is a Facade over the TRaX API making 
  * it a little easier to create Transformer instances.</p>
  *
  * <p>
  * For more information on JAXP please go to 
  * <a href="http://java.sun.com/xml/">Sun's Java &amp; XML site</a></p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TransformerReader {

    /** <code>TransformerFactory</code> used to create new Transformer objects */
    private TransformerFactory factory;
           
    
    public TransformerReader() {
    }

    public TransformerReader(TransformerFactory factory) {
        this.factory = factory;
    }


        
    /** <p>Reads a Transformer from the given <code>File</code></p>
      *
      * @param file is the <code>File</code> to read from.
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      * @throws FileNotFoundException if the file could not be found
      */
    public Transformer read(File file) throws TransformerConfigurationException, TransformerFactoryConfigurationError, FileNotFoundException {
        return read(new StreamSource( file ) );
    }
    
    /** <p>Reads a Transformer from the given <code>URL</code></p>
      *
      * @param url <code>URL</code> to read from.
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(URL url) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        String systemID = url.toExternalForm();
        return read(new StreamSource(systemID));
    }
    
    /** <p>Reads a Transformer from the given URI</p>
      *
      * @param systemID is the URI for the input
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(String systemID) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return read(new StreamSource(systemID));
    }

    /** <p>Reads a Transformer from the given <code>Document</code></p>
      *
      * @param document is the dom4j <code>Document</code> to read the stylesheet from.
      * @param systemID is the URI for the input
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(Document document) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return read( new DocumentSource( document ) );
    }
    
    /** <p>Reads a Transformer from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(InputStream in) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return read(new StreamSource(in));
    }

    /** <p>Reads a Transformer from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(Reader reader) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return read(new StreamSource(reader));
    }

    /** <p>Reads a Transformer from the given stream using SAX</p>
      *
      * @param in <code>InputStream</code> to read from.
      * @param systemID is the URI for the input
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(InputStream in, String systemID) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return read( new StreamSource( in, systemID ) );
    }

    /** <p>Reads a Transformer from the given <code>Reader</code> using SAX</p>
      *
      * @param reader is the reader for the input
      * @param systemID is the URI for the input
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(Reader reader, String systemID) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return read( new StreamSource( reader, systemID ) );
    }
    
    /** <p>Reads a Transformer from the given <code>InputSource</code> using SAX</p>
      *
      * @param in <code>InputSource</code> to read from.
      * @param systemID is the URI for the input
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(InputSource inputSource) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return read( new SAXSource( inputSource ) );
    }
    
    /** <p>Reads a Transformer from the given W3C <code>Node</code></p>
      *
      * @param in <code>InputSource</code> to read from.
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(org.w3c.dom.Node node) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return read( new DOMSource( node ) );
    }
    
    /** <p>Reads a Transformer from the given W3C <code>Node</code></p>
      *
      * @param in <code>InputSource</code> to read from.
      * @param systemID is the URI for the input
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(org.w3c.dom.Node node, String systemID) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return read( new DOMSource( node, systemID ) );
    }
    
    /** <p>Reads a Transformer from the given <code>InputSource</code> using SAX</p>
      *
      * @param source is the TRaX <code>Source</code> to read from.
      * @return the newly created Transformer instance
      * @throws TransformerConfigurationException if the Transformer could not 
      *     be configured from the source
      * @throws TransformerFactoryConfigurationError if a TRaX implmentation 
      *     is not available or cannot be instantiated.
      */
    public Transformer read(Source source) throws TransformerConfigurationException, TransformerFactoryConfigurationError {
        return getTransformerFactory().newTransformer(source);
    }
    

    // Properties
    //-------------------------------------------------------------------------                
    
    /** @return the <code>TransformerFactory</code> used to create transformer objects
      */
    public TransformerFactory getTransformerFactory() throws TransformerConfigurationException {
        if (factory == null) {
            factory = createTransformerFactory();
        }
        return factory;
    }
    
    /** This sets the <code>TransformerFactory</code> used to create new transformers.
      *
      * @param factory <code>TransformerFactory</code> used to create Transformer objects
      */
    public void setTransformerFactory(TransformerFactory factory) {
        this.factory = factory;
    }

    
    
    // Implementation methods    
    //-------------------------------------------------------------------------                
    
    /** A factory method to create new <code>TransformerFactory</code> instances
      * @return a newly created <code>TransformerFactory</code> instance
      */
    protected TransformerFactory createTransformerFactory() throws TransformerConfigurationException {
        return TransformerFactory.newInstance();
    }

}




/*
 * Redistribution and use of this software and associated transformeration
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this transformer.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the transformeration and/or other
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
