/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
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

/**
 * <p>
 * <code>DatatypeDocumentFactory</code> is a factory of XML objects which
 * support the <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Data Types
 * </a> specification.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class DatatypeDocumentFactory extends DocumentFactory {
    // XXXX: I don't think interning of QNames is necessary
    private static final boolean DO_INTERN_QNAME = false;

    /** The Singleton instance */
    protected static transient DatatypeDocumentFactory singleton 
            = new DatatypeDocumentFactory();

    private static final Namespace XSI_NAMESPACE = Namespace.get("xsi",
            "http://www.w3.org/2001/XMLSchema-instance");

    private static final QName XSI_SCHEMA_LOCATION = QName.get(
            "schemaLocation", XSI_NAMESPACE);

    private static final QName XSI_NO_SCHEMA_LOCATION = QName.get(
            "noNamespaceSchemaLocation", XSI_NAMESPACE);

    /** The builder of XML Schemas */
    private SchemaParser schemaBuilder;

    /** reader of XML Schemas */
    private SAXReader xmlSchemaReader = new SAXReader();

    /** If schemas are automatically loaded when parsing instance documents */
    private boolean autoLoadSchema = true;

    public DatatypeDocumentFactory() {
        schemaBuilder = new SchemaParser(this);
    }

    /**
     * <p>
     * Access to the singleton instance of this factory.
     * </p>
     * 
     * @return the default singleon instance
     */
    public static DocumentFactory getInstance() {
        return singleton;
    }

    /**
     * Loads the given XML Schema document into this factory so schema-aware
     * Document, Elements and Attributes will be created by this factory.
     * 
     * @param schemaDocument
     *            is an XML Schema Document instance.
     */
    public void loadSchema(Document schemaDocument) {
        schemaBuilder.build(schemaDocument);
    }

    public void loadSchema(Document schemaDocument, Namespace targetNamespace) {
        schemaBuilder.build(schemaDocument, targetNamespace);
    }

    /**
     * Registers the given <code>DatatypeElementFactory</code> for the given
     * &lt;element&gt; schema element
     * 
     * @param elementQName
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public DatatypeElementFactory getElementFactory(QName elementQName) {
        DatatypeElementFactory result = null;
        
        if (DO_INTERN_QNAME) {
            elementQName = intern(elementQName);
        }

        DocumentFactory factory = elementQName.getDocumentFactory();
        if (factory instanceof DatatypeElementFactory) {
            result = (DatatypeElementFactory) factory;
        }
        
        return result;
    }

    // DocumentFactory methods
    // -------------------------------------------------------------------------
    public Attribute createAttribute(Element owner, QName qname, String value) {
        if (autoLoadSchema && qname.equals(XSI_NO_SCHEMA_LOCATION)) {
            Document document = (owner != null) ? owner.getDocument() : null;
            loadSchema(document, value);
        } else if (autoLoadSchema && qname.equals(XSI_SCHEMA_LOCATION)) {
            Document document = (owner != null) ? owner.getDocument() : null;
            String uri = value.substring(0, value.indexOf(' '));
            Namespace namespace = owner.getNamespaceForURI(uri);
            loadSchema(document, value.substring(value.indexOf(' ') + 1),
                    namespace);
        }

        return super.createAttribute(owner, qname, value);
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void loadSchema(Document document, String schemaInstanceURI) {
        try {
            EntityResolver resolver = document.getEntityResolver();

            if (resolver == null) {
                String msg = "No EntityResolver available for resolving URI: ";
                throw new InvalidSchemaException(msg + schemaInstanceURI);
            }

            InputSource inputSource = resolver.resolveEntity(null,
                    schemaInstanceURI);

            if (resolver == null) {
                throw new InvalidSchemaException("Could not resolve the URI: "
                        + schemaInstanceURI);
            }

            Document schemaDocument = xmlSchemaReader.read(inputSource);
            loadSchema(schemaDocument);
        } catch (Exception e) {
            System.out.println("Failed to load schema: " + schemaInstanceURI);
            System.out.println("Caught: " + e);
            e.printStackTrace();
            throw new InvalidSchemaException("Failed to load schema: "
                    + schemaInstanceURI);
        }
    }

    protected void loadSchema(Document document, String schemaInstanceURI,
            Namespace namespace) {
        try {
            EntityResolver resolver = document.getEntityResolver();

            if (resolver == null) {
                String msg = "No EntityResolver available for resolving URI: ";
                throw new InvalidSchemaException(msg + schemaInstanceURI);
            }

            InputSource inputSource = resolver.resolveEntity(null,
                    schemaInstanceURI);

            if (resolver == null) {
                throw new InvalidSchemaException("Could not resolve the URI: "
                        + schemaInstanceURI);
            }

            Document schemaDocument = xmlSchemaReader.read(inputSource);
            loadSchema(schemaDocument, namespace);
        } catch (Exception e) {
            System.out.println("Failed to load schema: " + schemaInstanceURI);
            System.out.println("Caught: " + e);
            e.printStackTrace();
            throw new InvalidSchemaException("Failed to load schema: "
                    + schemaInstanceURI);
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
