/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.schema;

import com.sun.msv.datatype.xsd.DatatypeFactory;
import com.sun.msv.datatype.xsd.TypeIncubator;
import com.sun.msv.datatype.xsd.XSDatatype;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.util.AttributeHelper;

import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;

/** <p><code>SchemaBuilder</code> reads an XML Schema Document.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class SchemaBuilder {
    
    private static final Namespace XSD_NAMESPACE = Namespace.get( "xsd", "http://www.w3.org/2001/XMLSchema" );
    
    private static final QName XSD_ELEMENT = QName.get( "element", XSD_NAMESPACE );
    private static final QName XSD_ATTRIBUTE = QName.get( "attribute", XSD_NAMESPACE );
    private static final QName XSD_SIMPLETYPE = QName.get( "simpleType", XSD_NAMESPACE );
    private static final QName XSD_COMPLEXTYPE = QName.get( "complexType", XSD_NAMESPACE );
    private static final QName XSD_RESTRICTION = QName.get( "restriction", XSD_NAMESPACE );
    private static final QName XSD_BASE = QName.get( "base", XSD_NAMESPACE );
    private static final QName XSD_VALUE = QName.get( "value", XSD_NAMESPACE );
    private static final QName XSD_FIXED = QName.get( "fixed", XSD_NAMESPACE );
    private static final QName XSD_NAME = QName.get( "name", XSD_NAMESPACE );
    private static final QName XSD_TYPE = QName.get( "type", XSD_NAMESPACE );
    
    
    /** Document factory used to register Element specific factories*/
    private SchemaDocumentFactory documentFactory;
    
    /** Cache of <code>XSDatatype</code> instances loaded or created during this build */
    private Map dataTypeCache = new HashMap();
    
    
    public SchemaBuilder() {
        this.documentFactory = SchemaDocumentFactory.singleton;
    }
    
    public SchemaBuilder(SchemaDocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }
    
    
    /** Parses the given schema document 
      *
      * @param schemaDocument is the document of the XML Schema
      */
    public void build( Document schemaDocument ) {
        Element root = schemaDocument.getRootElement();
        if ( root != null ) {
            Iterator iter = root.elementIterator( XSD_ELEMENT );
            while ( iter.hasNext() ) {
                onSchemaElement( (Element) iter.next() );
            }
        }
    }
    
    
    // Implementation methods
    //-------------------------------------------------------------------------
    
    /** processes an XML Schema &lt;element&gt; tag 
      */
    protected void onSchemaElement( Element xsdElement ) {
        String name = xsdElement.attributeValue( XSD_NAME );
        String type = xsdElement.attributeValue( XSD_TYPE );
        QName qname = getQName( name );
        
        if ( type != null ) {
            // register type with this element name
        }
        SchemaElementFactory elementFactory 
            = getSchemaElementFactory( qname );
        
        Element schemaComplexType = xsdElement.element( XSD_COMPLEXTYPE );
        if ( schemaComplexType != null ) {
            onSchemaComplexType( schemaComplexType, elementFactory );
        }
        Iterator iter = xsdElement.elementIterator( XSD_ATTRIBUTE );
        if ( iter.hasNext() ) {
            do {
                onSchemaAttribute( 
                    xsdElement, 
                    elementFactory,
                    (Element) iter.next() 
                );
            }
            while ( iter.hasNext() );
        }
    }
    
    /** processes an XML Schema &lt;complexTypegt; tag 
      */
    protected void onSchemaComplexType( Element schemaComplexType, SchemaElementFactory elementFactory ) {
        Iterator iter = schemaComplexType.elementIterator( XSD_ATTRIBUTE );
        while ( iter.hasNext() ) {
            Element xsdAttribute = (Element) iter.next();
            String name = xsdAttribute.attributeValue( XSD_NAME );
            QName qname = getQName( name );
            
            XSDatatype dataType = dataTypeForXsdAttribute( xsdAttribute );
            if ( dataType != null ) {
                // register the XSDatatype for the given Attribute 
                elementFactory.setChildElementXSDatatype( qname, dataType );
            }
            else {
                String type = xsdAttribute.attributeValue( XSD_TYPE );
                System.out.println( "Warning: Couldn't find XSDatatype for type: " + type + " attribute: " + name );
            }
        }
    }
    
    /** processes an XML Schema &lt;attribute&gt; tag 
      */
    protected void onSchemaAttribute( 
        Element xsdElement, 
        SchemaElementFactory elementFactory, 
        Element xsdAttribute 
    ) {
        String name = xsdAttribute.attributeValue( XSD_NAME );
        QName qname = getQName( name );
        XSDatatype dataType = dataTypeForXsdAttribute( xsdAttribute );
        if ( dataType != null ) {
            // register the XSDatatype for the given Attribute 
            elementFactory.setAttributeXSDatatype( qname, dataType );
        }
        else {
            String type = xsdAttribute.attributeValue( XSD_TYPE );
            System.out.println( "Warning: Couldn't find XSDatatype for type: " + type + " attribute: " + name );
        }
    }
    
    /** processes an XML Schema &lt;attribute&gt; tag 
      */
    protected XSDatatype dataTypeForXsdAttribute( Element xsdAttribute ) {
        String type = xsdAttribute.attributeValue( XSD_TYPE );
        XSDatatype dataType = null;
        if ( type != null ) {
            dataType = getTypeByName( type );
        }
        else {
            // must parse the <simpleType> element
            Element xsdSimpleType = xsdAttribute.element( XSD_SIMPLETYPE );
            if ( xsdSimpleType == null ) {
                String name = xsdAttribute.attributeValue( XSD_NAME );
                throw new InvalidSchemaException( 
                    "The attribute: " + name + " has no type attribute and does not contain a <simpleType/> element" 
                );
            }
            dataType = loadXSDatatypeFromSimpleType( xsdSimpleType );            
        }
        return dataType;
    }
    
    /** Loads a XSDatatype object from a <simpleType> attribute schema element */
    protected XSDatatype loadXSDatatypeFromSimpleType( Element xsdSimpleType ) {
        Element xsdRestriction = xsdSimpleType.element( XSD_RESTRICTION );
        if ( xsdRestriction != null ) {
            String base = xsdRestriction.attributeValue( XSD_BASE );
            if ( base != null ) {                
                XSDatatype baseType = getTypeByName( base );
                if ( baseType == null ) {
                    onSchemaError( 
                        "Invalid base type: " + base 
                        + " when trying to build restriction: " + xsdRestriction
                    );
                }
                else {
                    return deriveSimpleType( baseType, xsdRestriction );
                }
            }
            else {
                // simpleType and base are mutually exclusive and you
                // must have one within a <restriction> tag
                Element xsdSubType = xsdSimpleType.element( XSD_SIMPLETYPE );
                if ( xsdSubType == null ) {
                    onSchemaError(
                        "The simpleType element: "+  xsdSimpleType 
                        + " must contain a base attribute or simpleType element" 
                    );
                }
                else {
                    return loadXSDatatypeFromSimpleType( xsdSubType );
                }
            }
        }
        else {
            onSchemaError( 
                "No <restriction>. Could not create XSDatatype for simpleType: " 
                + xsdSimpleType 
            );
        }
        return null;
    }

    /** Derives a new type from a base type and a set of restrictions */
    protected XSDatatype deriveSimpleType( XSDatatype baseType, Element xsdRestriction ) {
        TypeIncubator incubator = new TypeIncubator(baseType);
        ValidationContext context = null;
        
        try {
            for ( Iterator iter = xsdRestriction.elementIterator(); iter.hasNext(); ) {
                Element element = (Element) iter.next();
                String name = element.getName();
                String value = element.attributeValue( XSD_VALUE );
                boolean fixed = AttributeHelper.booleanValue( element, XSD_FIXED );
                
                // add facet
                incubator.add( name, value, fixed, context );
            }
            // derive a new type by those facets
            String newTypeName = null;
            return incubator.derive( newTypeName );
        }
        catch (DatatypeException e) {
            onSchemaError( 
                "Invalid restriction: " + e.getMessage()
                + " when trying to build restriction: " + xsdRestriction
            );
            return null;
        }
    }
    
    /** @return the <code>SchemaElementFactory</code> for the given
      * element QName, creating one if it does not already exist
      */
    protected SchemaElementFactory getSchemaElementFactory( QName elementQName ) {
        SchemaElementFactory factory = documentFactory.getElementFactory( elementQName );               
        if ( factory == null ) {
            factory = new SchemaElementFactory( elementQName );
            elementQName.setDocumentFactory(factory);
        }
        return factory;
    }
    
    protected XSDatatype getTypeByName( String type ) {
        XSDatatype dataType = (XSDatatype) dataTypeCache.get( type );
        if ( dataType == null ) {
            dataType = DatatypeFactory.getTypeByName( type );
            if ( dataType == null ) {
                // maybe a prefix is being used
                int idx = type.indexOf(':');
                if (idx >= 0 ) {
                    String localName = type.substring(idx + 1);
                    dataType = DatatypeFactory.getTypeByName( localName );
                }
            }
            // store in cache for later
            dataTypeCache.put( type, dataType );
        }
        return dataType;
    }
    
    protected QName getQName( String name ) {
        return documentFactory.createQName(name);
    }
    
    /** Called when there is a problem with the schema and the builder cannot
      * handle the XML Schema Data Types correctly 
      */
    protected void onSchemaError( String message ) {
        // Some users may wish to disable exception throwing
        // and instead use some kind of listener for errors and continue
        //System.out.println( "WARNING: " + message );
        
        throw new InvalidSchemaException( message );
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
