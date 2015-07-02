/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.dom;

import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.util.SingletonStrategy;
import org.w3c.dom.DOMException;

/**
 * <p>
 * <code>DOMDocumentFactory</code> is a factory of DOM4J objects which
 * implement the W3C DOM API.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.21 $
 */
public class DOMDocumentFactory extends DocumentFactory implements
        org.w3c.dom.DOMImplementation {

    /** The Singleton instance */
    private static SingletonStrategy singleton = null;

    static {
        try {
            String defaultSingletonClass = "org.dom4j.util.SimpleSingleton";
            Class clazz = null;
            try {
                String singletonClass = defaultSingletonClass;
                singletonClass = System.getProperty(
                        "org.dom4j.dom.DOMDocumentFactory.singleton.strategy",
                        singletonClass);
                clazz = Class.forName(singletonClass);
            } catch (Exception exc1) {
                try {
                    String singletonClass = defaultSingletonClass;
                    clazz = Class.forName(singletonClass);
                } catch (Exception exc2) {
                }
            }
            singleton = (SingletonStrategy) clazz.newInstance();
            singleton.setSingletonClassName(DOMDocumentFactory.class.getName());
        } catch (Exception exc3) {
        }
    }

    /**
     * <p>
     * Access to the singleton instance of this factory.
     * </p>
     * 
     * @return the default singleon instance
     */
    public static DocumentFactory getInstance() {
        DOMDocumentFactory fact = (DOMDocumentFactory) singleton.instance();
        return fact;
    }

    // Factory methods
    public Document createDocument() {
        DOMDocument answer = new DOMDocument();
        answer.setDocumentFactory(this);

        return answer;
    }

    public DocumentType createDocType(String name, String publicId,
            String systemId) {
        return new DOMDocumentType(name, publicId, systemId);
    }

    public Element createElement(QName qname) {
        return new DOMElement(qname);
    }

    public Element createElement(QName qname, int attributeCount) {
        return new DOMElement(qname, attributeCount);
    }

    public Attribute createAttribute(Element owner, QName qname, String value) {
        return new DOMAttribute(qname, value);
    }

    public CDATA createCDATA(String text) {
        return new DOMCDATA(text);
    }

    public Comment createComment(String text) {
        return new DOMComment(text);
    }

    public Text createText(String text) {
        return new DOMText(text);
    }

    public Entity createEntity(String name) {
        return new DOMEntityReference(name);
    }

    public Entity createEntity(String name, String text) {
        return new DOMEntityReference(name, text);
    }

    public Namespace createNamespace(String prefix, String uri) {
        return new DOMNamespace(prefix, uri);
    }

    public ProcessingInstruction createProcessingInstruction(String target,
            String data) {
        return new DOMProcessingInstruction(target, data);
    }

    public ProcessingInstruction createProcessingInstruction(String target,
            Map data) {
        return new DOMProcessingInstruction(target, data);
    }

    // org.w3c.dom.DOMImplementation interface
    public boolean hasFeature(String feat, String version) {
        if ("XML".equalsIgnoreCase(feat) || "Core".equalsIgnoreCase(feat)) {
            return ((version == null) || (version.length() == 0)
                    || "1.0".equals(version) || "2.0".equals(version));
        }

        return false;
    }

    public org.w3c.dom.DocumentType createDocumentType(String qualifiedName,
            String publicId, String systemId) throws DOMException {
        return new DOMDocumentType(qualifiedName, publicId, systemId);
    }

    public org.w3c.dom.Document createDocument(String namespaceURI,
            String qualifiedName, org.w3c.dom.DocumentType docType)
            throws org.w3c.dom.DOMException {
        DOMDocument document;

        if (docType != null) {
            DOMDocumentType documentType = asDocumentType(docType);
            document = new DOMDocument(documentType);
        } else {
            document = new DOMDocument();
        }

        document.addElement(createQName(qualifiedName, namespaceURI));

        return document;
    }

    // Implementation methods
    protected DOMDocumentType asDocumentType(org.w3c.dom.DocumentType docType) {
        if (docType instanceof DOMDocumentType) {
            return (DOMDocumentType) docType;
        } else {
            return new DOMDocumentType(docType.getName(),
                    docType.getPublicId(), docType.getSystemId());
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