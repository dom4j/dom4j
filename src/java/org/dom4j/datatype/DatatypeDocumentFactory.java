/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.datatype;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/** <p><code>DatatypeDocumentFactory</code> is a factory of XML objects which 
  * support the 
  * <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Data Types</a>
  * specification.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class DatatypeDocumentFactory extends DocumentFactory {

    
    // XXXX: I don't think interning of QNames is necessary
    private static final boolean DO_INTERN_QNAME = false;
    
    
    /** The Singleton instance */
    static transient DatatypeDocumentFactory singleton = new DatatypeDocumentFactory();
    
    private static final Namespace XSI_NAMESPACE
        = Namespace.get( "xsi", "http://www.w3.org/2001/XMLSchema-instance" );
    
    private static final QName XSI_SCHEMA_LOCATION
        = QName.get( "schemaLocation", XSI_NAMESPACE );
    
    private static final QName XSI_NO_SCHEMA_LOCATION
        = QName.get( "noNamespaceSchemaLocation", XSI_NAMESPACE );
    

    /** The builder of XML Schemas */
    private SchemaParser schemaBuilder;
    
    /** reader of XML Schemas */
    private SAXReader xmlSchemaReader = new SAXReader();

    
    /** If schemas are automatically loaded when parsing instance documents */
    private boolean autoLoadSchema = true;
    
    
    /** <p>Access to the singleton instance of this factory.</p>
      *
      * @return the default singleon instance
      */
    public static DocumentFactory getInstance() {
        return singleton;
    }
    
    public DatatypeDocumentFactory() {
        schemaBuilder = new SchemaParser(this);
    }

    
    /** Loads the given XML Schema document into this factory so
      * schema-aware Document, Elements and Attributes will be created
      * by this factory.
      *
      * @param schemaDocument is an XML Schema Document instance.
      */
    public void loadSchema(Document schemaDocument) {
        schemaBuilder.build( schemaDocument );
    }
    
    /** Registers the given <code>DatatypeElementFactory</code> for the given 
      * &lt;element&gt; schema element
      */
    public DatatypeElementFactory getElementFactory( QName elementQName ) {
        if ( DO_INTERN_QNAME ) {
            elementQName = intern( elementQName );
        }
        DocumentFactory factory = elementQName.getDocumentFactory();
        return (factory instanceof DatatypeElementFactory) 
            ? (DatatypeElementFactory) factory : null;
    }
    
        
    // DocumentFactory methods
    //-------------------------------------------------------------------------
/*    
    public Element createElement(QName qname) {
        DocumentFactory elementFactory = qname.getDocumentFactory();
        if ( elementFactory != null ) {
            return elementFactory.createElement(qname);
        }
        return super.createElement(qname);
    }
*/    
    public Attribute createAttribute(Element owner, QName qname, String value) {
        if ( autoLoadSchema && qname.equals( XSI_NO_SCHEMA_LOCATION ) ) {
            Document document = (owner != null) ? owner.getDocument() : null;
            loadSchema( document, value );
        }
        return super.createAttribute( owner, qname, value );
    }
    

    
    // Implementation methods
    //-------------------------------------------------------------------------
    protected void loadSchema( Document document, String schemaInstanceURI ) {
        try {
            EntityResolver resolver = document.getEntityResolver();
            if ( resolver == null ) {
                throw new InvalidSchemaException( "No EntityResolver available so could not resolve the schema URI: " + schemaInstanceURI );
            }
            InputSource inputSource = resolver.resolveEntity( null, schemaInstanceURI );
            if ( resolver == null ) {
                throw new InvalidSchemaException( "Could not resolve the schema URI: " + schemaInstanceURI );
            }
            Document schemaDocument = xmlSchemaReader.read( inputSource );
            loadSchema( schemaDocument );
        }
        catch (Exception e) {
            System.out.println( "Failed to load schema: " + schemaInstanceURI );
            System.out.println( "Caught: " + e );
            e.printStackTrace();
            throw new InvalidSchemaException( "Failed to load schema: " + schemaInstanceURI );
        }
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
