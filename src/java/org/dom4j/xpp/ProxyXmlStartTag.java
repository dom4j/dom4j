/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.xpp;

import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.tree.AbstractElement;
import org.gjt.xpp.XmlPullParserException;
import org.gjt.xpp.XmlStartTag;

/** <p><code>ProxyXmlStartTag</code> implements the XPP XmlSmartTag
  * interface while creating a dom4j Element underneath.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class ProxyXmlStartTag implements XmlStartTag {

    /** The element being constructed */
    private Element element;
    
    /** The factory used to create new elements */
    private DocumentFactory factory = DocumentFactory.getInstance();

    
    public ProxyXmlStartTag() { 
    }
    
    public ProxyXmlStartTag(Element element) { 
        this.element = element;
    }

    // XmlStartTag interface 
    //-------------------------------------------------------------------------                        
    public void resetStartTag() {
        this.element = null;
    }
    
    public int getAttributeCount() {
        return (element != null) ? element.attributeCount() : 0;
    }
    
    public String getAttributeNamespaceUri(int index) {
        if (element != null ) {
            Attribute attribute = element.attribute(index);
            if ( attribute != null ) {
                return attribute.getNamespaceURI();
            }
        }
        return null;
    }
    
    public String getAttributeLocalName(int index) {
        if (element != null ) {
            Attribute attribute = element.attribute(index);
            if ( attribute != null ) {
                return attribute.getName();
            }
        }
        return null;
    }
    
    public String getAttributePrefix(int index) {
        if (element != null ) {
            Attribute attribute = element.attribute(index);
            if ( attribute != null ) {
                String prefix = attribute.getNamespacePrefix();
                if ( prefix != null && prefix.length() > 0 ) {
                    return prefix;
                }
            }
        }
        return null;
    }
    
    public String getAttributeRawName(int index) {
        if (element != null ) {
            Attribute attribute = element.attribute(index);
            if ( attribute != null ) {
                return attribute.getQualifiedName();
            }
        }
        return null;
    }
    
    public String getAttributeValue(int index) {
        if (element != null ) {
            Attribute attribute = element.attribute(index);
            if ( attribute != null ) {
                return attribute.getValue();
            }
        }
        return null;
    }
    
    public String getAttributeValueFromRawName(String rawName) {
        if (element != null ) {
            for ( Iterator iter = element.attributeIterator(); iter.hasNext(); ) {
                Attribute attribute = (Attribute) iter.next();
                if ( rawName.equals( attribute.getQualifiedName() ) ) {
                    return attribute.getValue();
                }
            }
        }
        return null;
    }
    
    public String getAttributeValueFromName(String namespaceURI, String localName) {
        if (element != null ) {
            for ( Iterator iter = element.attributeIterator(); iter.hasNext(); ) {
                Attribute attribute = (Attribute) iter.next();
                if ( namespaceURI.equals( attribute.getNamespaceURI() ) && localName.equals( attribute.getName() ) ) {
                    return attribute.getValue();
                }
            }
        }
        return null;
    }
    
    public boolean isAttributeNamespaceDeclaration(int index) {
        if (element != null ) {
            Attribute attribute = element.attribute(index);
            if ( attribute != null ) {
                return "xmlns".equals( attribute.getNamespacePrefix() );
            }
        }
        return false;
    }
    
    
    /** parameters modeled after SAX2 attribute approach */
    public void addAttribute(String namespaceURI, String localName, String rawName, String value) throws XmlPullParserException {
        QName qname = QName.get( rawName, namespaceURI );
        element.addAttribute( qname, value );
    }
    
    
    public void addAttribute(String namespaceURI, String localName, String rawName, String value, boolean isNamespaceDeclaration) throws XmlPullParserException {
        if ( isNamespaceDeclaration ) {
            String prefix = "";
            int idx = rawName.indexOf( ':' );
            if ( idx > 0 ) {
                prefix = rawName.substring( 0, idx );
            }
            element.addNamespace( prefix, namespaceURI );
        }
        else {
            QName qname = QName.get( rawName, namespaceURI );
            element.addAttribute( qname, value );
        }
    }
    
    public void ensureAttributesCapacity(int minCapacity) throws XmlPullParserException {
        if ( element instanceof AbstractElement ) {
            AbstractElement elementImpl = (AbstractElement) element;
            elementImpl.ensureAttributesCapacity(minCapacity);
        }
    }
    
    /** remove all atribute */
    public void removeAtttributes() throws XmlPullParserException {
        if ( element != null ) {
            element.setAttributes( new ArrayList() );
            // ##### FIXME
            // adding this method would be nice...
            // element.clearAttributes();
        }
    }
    

    public String getLocalName() {
        return element.getName();
    }
    
    public String getNamespaceUri() {
        return element.getNamespaceURI();
    }
    
    public String getPrefix() {
        return element.getNamespacePrefix();
    }
    
    
    public String getRawName() {
        return element.getQualifiedName();
    }
    
    public void modifyTag(String namespaceURI, String localName, String rawName) {
        this.element = factory.createElement( rawName, namespaceURI );
    }
    
    public void resetTag() {
        this.element = null;
    }
    
    // Properties
    //-------------------------------------------------------------------------                        
    public DocumentFactory getDocumentFactory() {
        return factory;
    }
    
    public void setDocumentFactory(DocumentFactory factory) {
        this.factory = factory;
    }
    
    public Element getElement() {
        return element;
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
