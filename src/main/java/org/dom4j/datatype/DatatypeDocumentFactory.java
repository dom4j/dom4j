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
import org.xml.sax.SAXException;
import org.xml.sax.ext.EntityResolver2;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * <p>
 * <code>DatatypeDocumentFactory</code> is a factory of XML objects which
 * support the <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Data Types
 * </a> specification.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.9 $
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
        loadSchema(readSchema(document, schemaInstanceURI));
    }

    protected void loadSchema(Document document, String schemaInstanceURI,
            Namespace namespace) {
        loadSchema(readSchema(document, schemaInstanceURI), namespace);
    }

    /**
     * Reads the schema referenced by a document.
     * <p>
     * The schema is obtained through the {@link EntityResolver} of the
     * document, which must provide it.
     *
     * @param document the document that references the schema
     * @param schemaInstanceURI the schema location as written in the document
     * @return the schema document
     * @throws InvalidSchemaException if the schema cannot be read
     */
    private Document readSchema(Document document, String schemaInstanceURI) {
        try {
            EntityResolver resolver = document.getEntityResolver();

            if (resolver == null) {
                String msg = "No EntityResolver available for resolving URI: ";
                throw new InvalidSchemaException(msg + schemaInstanceURI);
            }

            InputSource inputSource = resolveSchema(resolver,
                    document.getName(), schemaInstanceURI);

            if (inputSource == null) {
                throw new InvalidSchemaException("Could not resolve the URI: "
                        + schemaInstanceURI);
            }

            return xmlSchemaReader.read(inputSource);
        } catch (Exception e) {
            System.out.println("Failed to load schema: " + schemaInstanceURI);
            System.out.println("Caught: " + e);
            e.printStackTrace();

            InvalidSchemaException exception = new InvalidSchemaException(
                    "Failed to load schema: " + schemaInstanceURI
                    + " (the EntityResolver of the document must provide"
                    + " it, see SAXReader.setEntityResolutionStrategy)");
            exception.initCause(e);
            throw exception;
        }
    }

    /**
     * Resolves a schema location through an entity resolver.
     * <p>
     * An {@link EntityResolver2} receives the location as written, together
     * with the location of the document as base URI. A plain
     * {@link EntityResolver} receives the location resolved against the
     * document, since it expects an absolute system identifier.
     *
     * @param resolver the resolver of the document
     * @param baseURI the location of the document, or null if unknown. The
     *                name of a document read by {@link SAXReader} is its
     *                system identifier, which the parser has made absolute.
     * @param schemaInstanceURI the schema location as written in the document
     * @return the resolved schema, or null if the resolver does not provide it
     * @throws SAXException if the resolver fails
     * @throws IOException if the resolver fails to open the schema
     */
    private static InputSource resolveSchema(EntityResolver resolver,
            String baseURI, String schemaInstanceURI) throws SAXException,
            IOException {
        if (resolver instanceof EntityResolver2) {
            return ((EntityResolver2) resolver).resolveEntity(null, null,
                    baseURI, schemaInstanceURI);
        }

        String systemId = schemaInstanceURI;
        if (baseURI != null && !baseURI.isEmpty()) {
            try {
                systemId = new URI(baseURI).resolve(schemaInstanceURI).toString();
            } catch (URISyntaxException | IllegalArgumentException e) {
                // fall through
            }
        }

        return resolver.resolveEntity(null, systemId);
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
