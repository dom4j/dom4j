package org.dom4j.datatype;

import com.sun.msv.datatype.xsd.XSDatatype;

import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;


import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.Node;

import com.sun.msv.datatype.xsd.XSDatatype;

class NamedTypeResolver {

    Map complexTypeMap=new HashMap();
    Map simpleTypeMap=new HashMap();
    Map typedElementMap=new HashMap();
    Map elementFactoryMap=new HashMap();

    DocumentFactory documentFactory;

    NamedTypeResolver(DocumentFactory documentFactory) {
        this.documentFactory=documentFactory;
    }

    void registerComplexType(QName type,DocumentFactory factory) {
        complexTypeMap.put(type,factory);
    }

    void registerSimpleType(QName type,XSDatatype datatype) {
        simpleTypeMap.put(type,datatype);
    }

    void registerTypedElement(Element element,QName type,DocumentFactory parentFactory) {
        typedElementMap.put(element,type);
        elementFactoryMap.put(element,parentFactory);
    }

    void resolveElementTypes() {
        Iterator iterator=typedElementMap.keySet().iterator();
        while (iterator.hasNext()) {
            Element element=(Element)iterator.next();
            QName elementQName=getQNameOfSchemaElement(element);
            QName type=(QName)typedElementMap.get(element);

            if (complexTypeMap.containsKey(type)) {
                DocumentFactory factory=
                    (DocumentFactory)complexTypeMap.get(type);
                elementQName.setDocumentFactory(factory);
            } else if (simpleTypeMap.containsKey(type)) {
                XSDatatype datatype=(XSDatatype)simpleTypeMap.get(type);
                DocumentFactory factory=
                        (DocumentFactory)elementFactoryMap.get(element);
                if (factory instanceof DatatypeElementFactory) {
                    ((DatatypeElementFactory)factory).setChildElementXSDatatype(elementQName,datatype);
                }
            }
        }
    }

    void resolveNamedTypes() {
        resolveElementTypes();
    }

    private QName getQNameOfSchemaElement(Element element) {
        String name=element.attributeValue("name");
        return getQName(name);
    }

    private QName getQName( String name ) {
        return documentFactory.createQName(name);
    }

}