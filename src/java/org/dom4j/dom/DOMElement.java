/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.dom;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>
 * <code>DOMElement</code> implements an XML element which supports the W3C
 * DOM API.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.23 $
 */
public class DOMElement extends DefaultElement implements org.w3c.dom.Element {
    /** The <code>DocumentFactory</code> instance used by default */
    private static final DocumentFactory DOCUMENT_FACTORY = DOMDocumentFactory
            .getInstance();

    public DOMElement(String name) {
        super(name);
    }

    public DOMElement(QName qname) {
        super(qname);
    }

    public DOMElement(QName qname, int attributeCount) {
        super(qname, attributeCount);
    }

    public DOMElement(String name, Namespace namespace) {
        super(name, namespace);
    }

    // org.w3c.dom.Node interface
    // -------------------------------------------------------------------------
    public boolean supports(String feature, String version) {
        return DOMNodeHelper.supports(this, feature, version);
    }

    public String getNamespaceURI() {
        return getQName().getNamespaceURI();
    }

    public String getPrefix() {
        return getQName().getNamespacePrefix();
    }

    public void setPrefix(String prefix) throws DOMException {
        DOMNodeHelper.setPrefix(this, prefix);
    }

    public String getLocalName() {
        return getQName().getName();
    }

    public String getNodeName() {
        return getName();
    }

    // already part of API
    //
    // public short getNodeType();
    public String getNodeValue() throws DOMException {
        return null;
    }

    public void setNodeValue(String nodeValue) throws DOMException {
    }

    public org.w3c.dom.Node getParentNode() {
        return DOMNodeHelper.getParentNode(this);
    }

    public NodeList getChildNodes() {
        return DOMNodeHelper.createNodeList(content());
    }

    public org.w3c.dom.Node getFirstChild() {
        return DOMNodeHelper.asDOMNode(node(0));
    }

    public org.w3c.dom.Node getLastChild() {
        return DOMNodeHelper.asDOMNode(node(nodeCount() - 1));
    }

    public org.w3c.dom.Node getPreviousSibling() {
        return DOMNodeHelper.getPreviousSibling(this);
    }

    public org.w3c.dom.Node getNextSibling() {
        return DOMNodeHelper.getNextSibling(this);
    }

    public NamedNodeMap getAttributes() {
        return new DOMAttributeNodeMap(this);
    }

    public Document getOwnerDocument() {
        return DOMNodeHelper.getOwnerDocument(this);
    }

    public org.w3c.dom.Node insertBefore(org.w3c.dom.Node newChild,
            org.w3c.dom.Node refChild) throws DOMException {
        checkNewChildNode(newChild);

        return DOMNodeHelper.insertBefore(this, newChild, refChild);
    }

    public org.w3c.dom.Node replaceChild(org.w3c.dom.Node newChild,
            org.w3c.dom.Node oldChild) throws DOMException {
        checkNewChildNode(newChild);

        return DOMNodeHelper.replaceChild(this, newChild, oldChild);
    }

    public org.w3c.dom.Node removeChild(org.w3c.dom.Node oldChild)
            throws DOMException {
        return DOMNodeHelper.removeChild(this, oldChild);
    }

    public org.w3c.dom.Node appendChild(org.w3c.dom.Node newChild)
            throws DOMException {
        checkNewChildNode(newChild);

        return DOMNodeHelper.appendChild(this, newChild);
    }

    private void checkNewChildNode(org.w3c.dom.Node newChild)
            throws DOMException {
        final int nodeType = newChild.getNodeType();

        if (!((nodeType == Node.ELEMENT_NODE) || (nodeType == Node.TEXT_NODE)
                || (nodeType == Node.COMMENT_NODE)
                || (nodeType == Node.PROCESSING_INSTRUCTION_NODE)
                || (nodeType == Node.CDATA_SECTION_NODE) 
                || (nodeType == Node.ENTITY_REFERENCE_NODE))) {
            throw new DOMException(DOMException.HIERARCHY_REQUEST_ERR,
                    "Given node cannot be a child of element");
        }
    }

    public boolean hasChildNodes() {
        return nodeCount() > 0;
    }

    public org.w3c.dom.Node cloneNode(boolean deep) {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public boolean isSupported(String feature, String version) {
        return DOMNodeHelper.isSupported(this, feature, version);
    }

    public boolean hasAttributes() {
        return DOMNodeHelper.hasAttributes(this);
    }

    // org.w3c.dom.Element interface
    // -------------------------------------------------------------------------
    public String getTagName() {
        return getName();
    }

    public String getAttribute(String name) {
        String answer = attributeValue(name);

        return (answer != null) ? answer : "";
    }

    public void setAttribute(String name, String value) throws DOMException {
        addAttribute(name, value);
    }

    public void removeAttribute(String name) throws DOMException {
        Attribute attribute = attribute(name);

        if (attribute != null) {
            remove(attribute);
        }
    }

    public org.w3c.dom.Attr getAttributeNode(String name) {
        return DOMNodeHelper.asDOMAttr(attribute(name));
    }

    public org.w3c.dom.Attr setAttributeNode(org.w3c.dom.Attr newAttr)
            throws DOMException {
        if (this.isReadOnly()) {
            throw new DOMException(DOMException.NO_MODIFICATION_ALLOWED_ERR,
                    "No modification allowed");
        }

        Attribute attribute = attribute(newAttr);

        if (attribute != newAttr) {
            if (newAttr.getOwnerElement() != null) {
                throw new DOMException(DOMException.INUSE_ATTRIBUTE_ERR,
                        "Attribute is already in use");
            }

            Attribute newAttribute = createAttribute(newAttr);

            if (attribute != null) {
                attribute.detach();
            }

            add(newAttribute);
        }

        return DOMNodeHelper.asDOMAttr(attribute);
    }

    public org.w3c.dom.Attr removeAttributeNode(org.w3c.dom.Attr oldAttr)
            throws DOMException {
        Attribute attribute = attribute(oldAttr);

        if (attribute != null) {
            attribute.detach();

            return DOMNodeHelper.asDOMAttr(attribute);
        } else {
            throw new DOMException(DOMException.NOT_FOUND_ERR,
                    "No such attribute");
        }
    }

    public String getAttributeNS(String namespaceURI, String localName) {
        Attribute attribute = attribute(namespaceURI, localName);

        if (attribute != null) {
            String answer = attribute.getValue();

            if (answer != null) {
                return answer;
            }
        }

        return "";
    }

    public void setAttributeNS(String namespaceURI, String qualifiedName,
            String value) throws DOMException {
        Attribute attribute = attribute(namespaceURI, qualifiedName);

        if (attribute != null) {
            attribute.setValue(value);
        } else {
            QName qname = getQName(namespaceURI, qualifiedName);
            addAttribute(qname, value);
        }
    }

    public void removeAttributeNS(String namespaceURI, String localName)
            throws DOMException {
        Attribute attribute = attribute(namespaceURI, localName);

        if (attribute != null) {
            remove(attribute);
        }
    }

    public org.w3c.dom.Attr getAttributeNodeNS(String namespaceURI,
            String localName) {
        Attribute attribute = attribute(namespaceURI, localName);

        if (attribute != null) {
            DOMNodeHelper.asDOMAttr(attribute);
        }

        return null;
    }

    public org.w3c.dom.Attr setAttributeNodeNS(org.w3c.dom.Attr newAttr)
            throws DOMException {
        Attribute attribute = attribute(newAttr.getNamespaceURI(), newAttr
                .getLocalName());

        if (attribute != null) {
            attribute.setValue(newAttr.getValue());
        } else {
            attribute = createAttribute(newAttr);
            add(attribute);
        }

        return DOMNodeHelper.asDOMAttr(attribute);
    }

    public NodeList getElementsByTagName(String name) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagName(list, this, name);

        return DOMNodeHelper.createNodeList(list);
    }

    public NodeList getElementsByTagNameNS(String namespace, String lName) {
        ArrayList list = new ArrayList();
        DOMNodeHelper.appendElementsByTagNameNS(list, this, namespace, lName);

        return DOMNodeHelper.createNodeList(list);
    }

    public boolean hasAttribute(String name) {
        return attribute(name) != null;
    }

    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return attribute(namespaceURI, localName) != null;
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected DocumentFactory getDocumentFactory() {
        DocumentFactory factory = getQName().getDocumentFactory();

        return (factory != null) ? factory : DOCUMENT_FACTORY;
    }

    protected Attribute attribute(org.w3c.dom.Attr attr) {
        return attribute(DOCUMENT_FACTORY.createQName(attr.getLocalName(), attr
                .getPrefix(), attr.getNamespaceURI()));
    }

    protected Attribute attribute(String namespaceURI, String localName) {
        List attributes = attributeList();
        int size = attributes.size();

        for (int i = 0; i < size; i++) {
            Attribute attribute = (Attribute) attributes.get(i);

            if (localName.equals(attribute.getName())
                    && (((namespaceURI == null || namespaceURI.length() == 0)
                          && ((attribute.getNamespaceURI() == null) 
                              || (attribute.getNamespaceURI().length() == 0)))
                              || ((namespaceURI != null) && namespaceURI
                                      .equals(attribute.getNamespaceURI())))) {
                return attribute;
            }
        }

        return null;
    }

    protected Attribute createAttribute(org.w3c.dom.Attr newAttr) {
        QName qname = null;
        String name = newAttr.getLocalName();

        if (name != null) {
            String prefix = newAttr.getPrefix();
            String uri = newAttr.getNamespaceURI();
            qname = getDocumentFactory().createQName(name, prefix, uri);
        } else {
            name = newAttr.getName();
            qname = getDocumentFactory().createQName(name);
        }

        return new DOMAttribute(qname, newAttr.getValue());
    }

    protected QName getQName(String namespace, String qualifiedName) {
        int index = qualifiedName.indexOf(':');
        String prefix = "";
        String localName = qualifiedName;

        if (index >= 0) {
            prefix = qualifiedName.substring(0, index);
            localName = qualifiedName.substring(index + 1);
        }

        return getDocumentFactory().createQName(localName, prefix, namespace);
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
