/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.datatype;

import com.sun.msv.datatype.xsd.DatatypeFactory;
import com.sun.msv.datatype.xsd.TypeIncubator;
import com.sun.msv.datatype.xsd.XSDatatype;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;
import org.dom4j.util.AttributeHelper;

import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

/**
 * <p>
 * <code>SchemaParser</code> reads an XML Schema Document.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @author Yuxin Ruan
 * @version $Revision$
 */
public class SchemaParser {
    private static final Namespace XSD_NAMESPACE = Namespace.get("xsd",
            "http://www.w3.org/2001/XMLSchema");

    // Use QNames for the elements
    private static final QName XSD_ELEMENT = QName
            .get("element", XSD_NAMESPACE);

    private static final QName XSD_ATTRIBUTE = QName.get("attribute",
            XSD_NAMESPACE);

    private static final QName XSD_SIMPLETYPE = QName.get("simpleType",
            XSD_NAMESPACE);

    private static final QName XSD_COMPLEXTYPE = QName.get("complexType",
            XSD_NAMESPACE);

    private static final QName XSD_RESTRICTION = QName.get("restriction",
            XSD_NAMESPACE);

    private static final QName XSD_SEQUENCE = QName.get("sequence",
            XSD_NAMESPACE);

    private static final QName XSD_CHOICE = QName.get("choice", XSD_NAMESPACE);

    private static final QName XSD_ALL = QName.get("all", XSD_NAMESPACE);

    private static final QName XSD_INCLUDE = QName
            .get("include", XSD_NAMESPACE);

    /** Document factory used to register Element specific factories */
    private DatatypeDocumentFactory documentFactory;

    /**
     * Cache of <code>XSDatatype</code> instances loaded or created during
     * this build
     */
    private Map dataTypeCache = new HashMap();

    /** NamedTypeResolver */
    private NamedTypeResolver namedTypeResolver;

    /** target namespace */
    private Namespace targetNamespace;

    public SchemaParser() {
        this(DatatypeDocumentFactory.singleton);
    }

    public SchemaParser(DatatypeDocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
        this.namedTypeResolver = new NamedTypeResolver(documentFactory);
    }

    /**
     * Parses the given schema document
     * 
     * @param schemaDocument
     *            is the document of the XML Schema
     */
    public void build(Document schemaDocument) {
        this.targetNamespace = null;
        internalBuild(schemaDocument);
    }

    public void build(Document schemaDocument, Namespace namespace) {
        this.targetNamespace = namespace;
        internalBuild(schemaDocument);
    }

    private synchronized void internalBuild(Document schemaDocument) {
        Element root = schemaDocument.getRootElement();

        if (root != null) {
            // handle schema includes
            Iterator includeIter = root.elementIterator(XSD_INCLUDE);

            while (includeIter.hasNext()) {
                Element includeElement = (Element) includeIter.next();
                String inclSchemaInstanceURI = includeElement
                        .attributeValue("schemaLocation");
                EntityResolver resolver = schemaDocument.getEntityResolver();

                try {
                    if (resolver == null) {
                        String msg = "No EntityResolver available";
                        throw new InvalidSchemaException(msg);
                    }

                    InputSource inputSource = resolver.resolveEntity(null,
                            inclSchemaInstanceURI);

                    if (inputSource == null) {
                        String msg = "Could not resolve the schema URI: "
                                + inclSchemaInstanceURI;
                        throw new InvalidSchemaException(msg);
                    }

                    SAXReader reader = new SAXReader();
                    Document inclSchemaDocument = reader.read(inputSource);
                    build(inclSchemaDocument);
                } catch (Exception e) {
                    System.out.println("Failed to load schema: "
                            + inclSchemaInstanceURI);
                    System.out.println("Caught: " + e);
                    e.printStackTrace();
                    throw new InvalidSchemaException("Failed to load schema: "
                            + inclSchemaInstanceURI);
                }
            }

            // handle elements
            Iterator iter = root.elementIterator(XSD_ELEMENT);

            while (iter.hasNext()) {
                onDatatypeElement((Element) iter.next(), documentFactory);
            }

            // handle named simple types
            iter = root.elementIterator(XSD_SIMPLETYPE);

            while (iter.hasNext()) {
                onNamedSchemaSimpleType((Element) iter.next());
            }

            // hanlde named complex types
            iter = root.elementIterator(XSD_COMPLEXTYPE);

            while (iter.hasNext()) {
                onNamedSchemaComplexType((Element) iter.next());
            }

            namedTypeResolver.resolveNamedTypes();
        }
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * processes an XML Schema &lt;element&gt; tag
     * 
     * @param xsdElement
     *            DOCUMENT ME!
     * @param parentFactory
     *            DOCUMENT ME!
     */
    private void onDatatypeElement(Element xsdElement,
            DocumentFactory parentFactory) {
        String name = xsdElement.attributeValue("name");
        String type = xsdElement.attributeValue("type");
        QName qname = getQName(name);

        DatatypeElementFactory factory = getDatatypeElementFactory(qname);

        if (type != null) {
            // register type with this element name
            XSDatatype dataType = getTypeByName(type);

            if (dataType != null) {
                factory.setChildElementXSDatatype(qname, dataType);
            } else {
                QName typeQName = getQName(type);
                namedTypeResolver.registerTypedElement(xsdElement, typeQName,
                        parentFactory);
            }

            return;
        }

        // handle element types derrived from simpleTypes
        Element xsdSimpleType = xsdElement.element(XSD_SIMPLETYPE);

        if (xsdSimpleType != null) {
            XSDatatype dataType = loadXSDatatypeFromSimpleType(xsdSimpleType);

            if (dataType != null) {
                factory.setChildElementXSDatatype(qname, dataType);
            }
        }

        Element schemaComplexType = xsdElement.element(XSD_COMPLEXTYPE);

        if (schemaComplexType != null) {
            onSchemaComplexType(schemaComplexType, factory);
        }

        Iterator iter = xsdElement.elementIterator(XSD_ATTRIBUTE);

        if (iter.hasNext()) {
            do {
                onDatatypeAttribute(xsdElement, factory, (Element) iter
                        .next());
            } while (iter.hasNext());
        }
    }

    /**
     * processes an named XML Schema &lt;complexTypegt; tag
     * 
     * @param schemaComplexType
     *            DOCUMENT ME!
     */
    private void onNamedSchemaComplexType(Element schemaComplexType) {
        Attribute nameAttr = schemaComplexType.attribute("name");

        if (nameAttr == null) {
            return;
        }

        String name = nameAttr.getText();
        QName qname = getQName(name);

        DatatypeElementFactory factory = getDatatypeElementFactory(qname);

        onSchemaComplexType(schemaComplexType, factory);
        namedTypeResolver.registerComplexType(qname, factory);
    }

    /**
     * processes an XML Schema &lt;complexTypegt; tag
     * 
     * @param schemaComplexType
     *            DOCUMENT ME!
     * @param elementFactory
     *            DOCUMENT ME!
     */
    private void onSchemaComplexType(Element schemaComplexType,
            DatatypeElementFactory elementFactory) {
        Iterator iter = schemaComplexType.elementIterator(XSD_ATTRIBUTE);

        while (iter.hasNext()) {
            Element xsdAttribute = (Element) iter.next();
            String name = xsdAttribute.attributeValue("name");
            QName qname = getQName(name);

            XSDatatype dataType = dataTypeForXsdAttribute(xsdAttribute);

            if (dataType != null) {
                // register the XSDatatype for the given Attribute
                // #### should both these be done?
                // elementFactory.setChildElementXSDatatype( qname, dataType );
                elementFactory.setAttributeXSDatatype(qname, dataType);
            }
        }

        // handle sequence definition
        Element schemaSequence = schemaComplexType.element(XSD_SEQUENCE);

        if (schemaSequence != null) {
            onChildElements(schemaSequence, elementFactory);
        }

        // handle choice definition
        Element schemaChoice = schemaComplexType.element(XSD_CHOICE);

        if (schemaChoice != null) {
            onChildElements(schemaChoice, elementFactory);
        }

        // handle all definition
        Element schemaAll = schemaComplexType.element(XSD_ALL);

        if (schemaAll != null) {
            onChildElements(schemaAll, elementFactory);
        }
    }

    private void onChildElements(Element element, DatatypeElementFactory fact) {
        Iterator iter = element.elementIterator(XSD_ELEMENT);

        while (iter.hasNext()) {
            Element xsdElement = (Element) iter.next();
            onDatatypeElement(xsdElement, fact);
        }
    }

    /**
     * processes an XML Schema &lt;attribute&gt; tag
     * 
     * @param xsdElement
     *            DOCUMENT ME!
     * @param elementFactory
     *            DOCUMENT ME!
     * @param xsdAttribute
     *            DOCUMENT ME!
     */
    private void onDatatypeAttribute(Element xsdElement,
            DatatypeElementFactory elementFactory, Element xsdAttribute) {
        String name = xsdAttribute.attributeValue("name");
        QName qname = getQName(name);
        XSDatatype dataType = dataTypeForXsdAttribute(xsdAttribute);

        if (dataType != null) {
            // register the XSDatatype for the given Attribute
            elementFactory.setAttributeXSDatatype(qname, dataType);
        } else {
            String type = xsdAttribute.attributeValue("type");
            System.out.println("Warning: Couldn't find XSDatatype for type: "
                    + type + " attribute: " + name);
        }
    }

    /**
     * processes an XML Schema &lt;attribute&gt; tag
     * 
     * @param xsdAttribute
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws InvalidSchemaException
     *             DOCUMENT ME!
     */
    private XSDatatype dataTypeForXsdAttribute(Element xsdAttribute) {
        String type = xsdAttribute.attributeValue("type");
        XSDatatype dataType = null;

        if (type != null) {
            dataType = getTypeByName(type);
        } else {
            // must parse the <simpleType> element
            Element xsdSimpleType = xsdAttribute.element(XSD_SIMPLETYPE);

            if (xsdSimpleType == null) {
                String name = xsdAttribute.attributeValue("name");
                String msg = "The attribute: " + name
                        + " has no type attribute and does not contain a "
                        + "<simpleType/> element";
                throw new InvalidSchemaException(msg);
            }

            dataType = loadXSDatatypeFromSimpleType(xsdSimpleType);
        }

        return dataType;
    }

    /**
     * processes an named XML Schema &lt;simpleTypegt; tag
     * 
     * @param schemaSimpleType
     *            DOCUMENT ME!
     */
    private void onNamedSchemaSimpleType(Element schemaSimpleType) {
        Attribute nameAttr = schemaSimpleType.attribute("name");

        if (nameAttr == null) {
            return;
        }

        String name = nameAttr.getText();
        QName qname = getQName(name);
        XSDatatype datatype = loadXSDatatypeFromSimpleType(schemaSimpleType);
        namedTypeResolver.registerSimpleType(qname, datatype);
    }

    /**
     * Loads a XSDatatype object from a &lt;simpleType&gt; attribute schema
     * element
     * 
     * @param xsdSimpleType
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    private XSDatatype loadXSDatatypeFromSimpleType(Element xsdSimpleType) {
        Element xsdRestriction = xsdSimpleType.element(XSD_RESTRICTION);

        if (xsdRestriction != null) {
            String base = xsdRestriction.attributeValue("base");

            if (base != null) {
                XSDatatype baseType = getTypeByName(base);

                if (baseType == null) {
                    onSchemaError("Invalid base type: " + base
                            + " when trying to build restriction: "
                            + xsdRestriction);
                } else {
                    return deriveSimpleType(baseType, xsdRestriction);
                }
            } else {
                // simpleType and base are mutually exclusive and you
                // must have one within a <restriction> tag
                Element xsdSubType = xsdSimpleType.element(XSD_SIMPLETYPE);

                if (xsdSubType == null) {
                    String msg = "The simpleType element: " + xsdSimpleType
                            + " must contain a base attribute or simpleType"
                            + " element";
                    onSchemaError(msg);
                } else {
                    return loadXSDatatypeFromSimpleType(xsdSubType);
                }
            }
        } else {
            onSchemaError("No <restriction>. Could not create XSDatatype for"
                    + " simpleType: " + xsdSimpleType);
        }

        return null;
    }

    /**
     * Derives a new type from a base type and a set of restrictions
     * 
     * @param baseType
     *            DOCUMENT ME!
     * @param xsdRestriction
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    private XSDatatype deriveSimpleType(XSDatatype baseType,
            Element xsdRestriction) {
        TypeIncubator incubator = new TypeIncubator(baseType);
        ValidationContext context = null;

        try {
            for (Iterator iter = xsdRestriction.elementIterator(); iter
                    .hasNext();) {
                Element element = (Element) iter.next();
                String name = element.getName();
                String value = element.attributeValue("value");
                boolean fixed = AttributeHelper.booleanValue(element, "fixed");

                // add facet
                incubator.addFacet(name, value, fixed, context);
            }

            // derive a new type by those facets
            String newTypeName = null;

            return incubator.derive("", newTypeName);
        } catch (DatatypeException e) {
            onSchemaError("Invalid restriction: " + e.getMessage()
                    + " when trying to build restriction: " + xsdRestriction);

            return null;
        }
    }

    /**
     * DOCUMENT ME!
     * 
     * @param name
     *            The name of the element
     * 
     * @return the <code>DatatypeElementFactory</code> for the given element
     *         QName, creating one if it does not already exist
     */
    private DatatypeElementFactory getDatatypeElementFactory(QName name) {
        DatatypeElementFactory factory = documentFactory
                .getElementFactory(name);

        if (factory == null) {
            factory = new DatatypeElementFactory(name);
            name.setDocumentFactory(factory);
        }

        return factory;
    }

    private XSDatatype getTypeByName(String type) {
        XSDatatype dataType = (XSDatatype) dataTypeCache.get(type);

        if (dataType == null) {
            // first check to see if it is a built-in type
            // maybe a prefix is being used
            int idx = type.indexOf(':');

            if (idx >= 0) {
                String localName = type.substring(idx + 1);

                try {
                    dataType = DatatypeFactory.getTypeByName(localName);
                } catch (DatatypeException e) {
                }
            }

            if (dataType == null) {
                try {
                    dataType = DatatypeFactory.getTypeByName(type);
                } catch (DatatypeException e) {
                }
            }

            if (dataType == null) {
                // it's no built-in type, maybe it's a type we defined ourself
                QName typeQName = getQName(type);
                dataType = (XSDatatype) namedTypeResolver.simpleTypeMap
                        .get(typeQName);
            }

            if (dataType != null) {
                // store in cache for later
                dataTypeCache.put(type, dataType);
            }
        }

        return dataType;
    }

    private QName getQName(String name) {
        if (targetNamespace == null) {
            return documentFactory.createQName(name);
        } else {
            return documentFactory.createQName(name, targetNamespace);
        }
    }

    /**
     * Called when there is a problem with the schema and the builder cannot
     * handle the XML Schema Data Types correctly
     * 
     * @param message
     *            DOCUMENT ME!
     * 
     * @throws InvalidSchemaException
     *             DOCUMENT ME!
     */
    private void onSchemaError(String message) {
        // Some users may wish to disable exception throwing
        // and instead use some kind of listener for errors and continue
        // System.out.println( "WARNING: " + message );
        throw new InvalidSchemaException(message);
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
