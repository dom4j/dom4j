/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.dom;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.tree.DefaultElement;

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/** <p><code>DOMAttribute</code> implements an XML element which 
  * supports the W3C DOM API.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DOMElement extends DefaultElement { // implements org.w3c.dom.Element {

    public DOMElement(String name) { 
        super(name);
    }

    public DOMElement(QName qname) { 
        super(qname);
    }

    public DOMElement(String name, Namespace namespace) { 
        super(name, namespace);
    }

    
    
    // org.w3c.dom.Node interface
    //-------------------------------------------------------------------------        
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
    
    //already part of API  
    //
    //public short getNodeType();
    

    // delegate common functionality to SaxonNodeHelper
    public String getNodeValue() throws DOMException {
        return DOMNodeHelper.getNodeValue(this);
    }
    
    public void setNodeValue(String nodeValue) throws DOMException {
        DOMNodeHelper.setNodeValue(this, nodeValue);
    }
        

    public org.w3c.dom.Node getParentNode() {
        return DOMNodeHelper.getParentNode(this);
    }
    
    public NodeList getChildNodes() {
        return DOMNodeHelper.getChildNodes(this);
    }

    public org.w3c.dom.Node getFirstChild() {
        return DOMNodeHelper.getFirstChild(this);
    }

    public org.w3c.dom.Node getLastChild() {
        return DOMNodeHelper.getLastChild(this);
    }

    public org.w3c.dom.Node getPreviousSibling() {
        return DOMNodeHelper.getPreviousSibling(this);
    }

    public org.w3c.dom.Node getNextSibling() {
        return DOMNodeHelper.getNextSibling(this);
    }

/*    
    public NamedNodeMap getAttributes() {
        return DOMNodeHelper.getAttributes(this);
    }
*/
    
    public Document getOwnerDocument() {
        return DOMNodeHelper.getOwnerDocument(this);
    }

    public org.w3c.dom.Node insertBefore(
        org.w3c.dom.Node newChild, 
        org.w3c.dom.Node refChild
    ) throws DOMException {
        return DOMNodeHelper.insertBefore(this, newChild, refChild);
    }

    public org.w3c.dom.Node replaceChild(
        org.w3c.dom.Node newChild, 
        org.w3c.dom.Node oldChild
    ) throws DOMException {
        return DOMNodeHelper.replaceChild(this, newChild, oldChild);
    }

    public org.w3c.dom.Node removeChild(org.w3c.dom.Node oldChild) throws DOMException {
        return DOMNodeHelper.removeChild(this, oldChild);
    }

    public org.w3c.dom.Node appendChild(org.w3c.dom.Node newChild) throws DOMException {
        return DOMNodeHelper.appendChild(this, newChild);
    }

    public boolean hasChildNodes() {
        return DOMNodeHelper.hasChildNodes(this);
    }

    public org.w3c.dom.Node cloneNode(boolean deep) {
        return DOMNodeHelper.cloneNode(this, deep);
    }

    public void normalize() {
        DOMNodeHelper.normalize(this);
    }

    public boolean isSupported(String feature, String version) {
        return DOMNodeHelper.isSupported(this, feature, version);
    }

    public boolean hasAttributes() {
        return DOMNodeHelper.hasAttributes(this);
    }
    
    
    // org.w3c.dom.Element interface
    //-------------------------------------------------------------------------            
    
    public String getTagName() {
        return getName();
    }

    //public String getAttribute(String name)

    //public void setAttribute(String name, String value) throws DOMException;

    //public void removeAttribute(String name) throws DOMException;

    public org.w3c.dom.Attr getAttributeNode(String name) {
        return DOMNodeHelper.asDOMAttr( getAttribute( name ) );
    }

    public org.w3c.dom.Attr setAttributeNode(org.w3c.dom.Attr newAttr) throws DOMException {
        Attribute attribute = getAttributeObject(newAttr);
        if ( attribute != null ) {
            attribute.setValue( newAttr.getValue() );
        }
        else {
            attribute = createAttribute( newAttr );
            add( attribute );
        }
        return DOMNodeHelper.asDOMAttr( attribute );
    }

    public org.w3c.dom.Attr removeAttributeNode(org.w3c.dom.Attr oldAttr) throws DOMException {
        Attribute attribute = getAttributeObject(oldAttr);
        if ( attribute != null ) {
            attribute.detach();
            return DOMNodeHelper.asDOMAttr( attribute );
        }
        else {
            throw new DOMException( 
                DOMException.NOT_FOUND_ERR, 
                "No such attribute" 
            );
        }
    }

    public String getAttributeNS(String namespaceURI,  String localName) {
        Attribute attribute = getAttributeObjectNS( namespaceURI, localName );
        if ( attribute != null ) {
            return attribute.getValue();
        }
        return null;
    }

    public void setAttributeNS(
        String namespaceURI, 
        String qualifiedName, 
        String value
    ) throws DOMException {
        Attribute attribute = getAttributeObjectNS( namespaceURI, qualifiedName );
        if ( attribute != null ) {
            attribute.setValue(value);
        }
        else {
            QName qname = getQName( namespaceURI, qualifiedName );
            setAttributeValue( qname, value );
        }
    }

    public void removeAttributeNS(
        String namespaceURI, 
        String localName
    ) throws DOMException {
        Attribute attribute = getAttributeObjectNS( namespaceURI, localName );
        if ( attribute != null ) {
            remove( attribute );
        }
    }

    public org.w3c.dom.Attr getAttributeNodeNS(String namespaceURI,  String localName) {
        Attribute attribute = getAttributeObjectNS( namespaceURI, localName );
        if ( attribute != null ) {
            DOMNodeHelper.asDOMAttr( attribute );
        }
        return null;
    }

    public org.w3c.dom.Attr setAttributeNodeNS(org.w3c.dom.Attr newAttr) throws DOMException {
        Attribute attribute = getAttributeObjectNS( 
            newAttr.getNamespaceURI(), newAttr.getLocalName() 
        );
        if ( attribute != null ) {
            attribute.setValue( newAttr.getValue() );
        }
        else {
            attribute = createAttribute( newAttr );
            add( attribute );
        }
        return DOMNodeHelper.asDOMAttr( attribute );
    }

    public NodeList getElementsByTagName(String name) {
        ArrayList list = new ArrayList();
        for ( int i = 0, size = getNodeCount(); i < size; i++ ) {
            Node node = getNode(i);
            if ( node instanceof Element ) {
                Element element = (Element) node;
                if ( name.equals( element.getName() ) ) {
                    list.add( element );
                }
            }
        }
        return createNodeList( list );
    }

    public NodeList getElementsByTagNameNS(
        String namespaceURI,  
        String localName
    ) {
        ArrayList list = new ArrayList();
        for ( int i = 0, size = getNodeCount(); i < size; i++ ) {
            Node node = getNode(i);
            if ( node instanceof Element ) {
                Element element = (Element) node;
                if ( namespaceURI.equals( element.getNamespaceURI() ) 
                        && localName.equals( element.getName() ) ) {
                    list.add( element );
                }
            }
        }
        return createNodeList( list );
    }

    public boolean hasAttribute(String name) {
        return getAttribute(name) != null;
    }

    public boolean hasAttributeNS(String namespaceURI, String localName) {
        return getAttributeObjectNS(namespaceURI, localName) != null;
    }

    
    // Implementation methods
    //-------------------------------------------------------------------------            
    
    protected Attribute getAttributeObject(org.w3c.dom.Attr attr) {
        return getAttribute( 
            QName.get( 
                attr.getLocalName(), 
                attr.getPrefix(), 
                attr.getNamespaceURI() 
            )
        );
    }

    protected Attribute getAttributeObjectNS(String namespaceURI,  String localName) {
        List attributes = getAttributeList();
        int size = attributes.size();
        for ( int i = 0; i < size; i++ ) {
            Attribute attribute = (Attribute) attributes.get(i);
            if ( localName.equals( attribute.getName() ) &&
                namespaceURI.equals( attribute.getNamespaceURI() ) ) {
                return attribute;
            }
        }
        return null;
    }

    protected NodeList createNodeList( final List list ) {
        return new NodeList() {
            public org.w3c.dom.Node item(int index) {
                return DOMNodeHelper.asDOMNode( (Node) list.get( index ) );
            }
            public int getLength() {
                return list.size();
            }
        };
    }

    protected Attribute createAttribute( org.w3c.dom.Attr newAttr ) {
        QName qname = null;
        String name = newAttr.getLocalName();
        String uri = newAttr.getNamespaceURI();
        if ( uri != null && uri.length() > 0 ) {
            Namespace namespace = getNamespaceForURI( uri );
            if ( namespace != null ) {
                qname = QName.get( name, namespace );
            }
        }
        if ( qname == null ) {
            qname = QName.get( name );
        }
        return new DOMAttribute( qname, newAttr.getValue() );
    }
    
    protected QName getQName( String namespaceURI, String qualifiedName ) {
        int index = qualifiedName.indexOf( ':' );
        String prefix = "";
        String localName = qualifiedName;
        if ( index >= 0 ) {
            prefix = qualifiedName.substring(0, index);
            localName = qualifiedName.substring(index+1);
        }
        return QName.get( localName, prefix, namespaceURI );
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
