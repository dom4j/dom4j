/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.saxon;

import java.util.Enumeration;

import javax.xml.transform.TransformerException;

import com.icl.saxon.Context;
import com.icl.saxon.expr.NodeEnumeration;
import com.icl.saxon.expr.SingletonEnumeration;
import com.icl.saxon.om.AttributeInfo;
import com.icl.saxon.om.DocumentInfo;
import com.icl.saxon.om.NodeInfo;
import com.icl.saxon.output.Outputter;

import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.Namespace;
import org.dom4j.tree.XPathAttribute;

/** <p><code>SaxonAttribute</code> implements a doubly linked attribute which 
  * supports the SAXON tree API.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class SaxonAttribute extends XPathAttribute implements AttributeInfo {

    protected static NodeInfo[] emptyArray = new NodeInfo[0];
    protected static SingletonEnumeration emptyEnumeration = new SingletonEnumeration(null);
    

    
    public SaxonAttribute(QName qname,String value) { 
        super(qname, value);
    }
    
    public SaxonAttribute(Element parent, QName qname, String value) { 
        super(parent, qname, value);
    }

    
    // NodeInfo interface
    //-------------------------------------------------------------------------        
    public boolean isSameNode(NodeInfo other) {
        return this == other;
    }

    public String getSystemId() {
        Document document = getDocument();
        if ( document != null ) {
            DocumentType docType = document.getDocType();
            if ( docType != null ) {
                return docType.getSystemID();
            }
        }
        return "";
    }
    
    public String getBaseURI() {
        // XXXX: no support for xml:base yet
        return getSystemId();
    }

    public int getLineNumber() {
        return -1;
    }

    
    public String getLocalName() {
        return getName();
    }

    public String getPrefix() {
        return getQName().getNamespacePrefix();
    }

    public String getURI() {
        return getQName().getNamespaceURI();
    }
    
    public String getDisplayName() {
        return getName();
    }

    
    
    public long getSequenceNumber() {
        notSupported();
        return -1;
    }
    
    /**
    * Get name code. The name code is a coded form of the node name: two nodes
    * with the same fingerprint have the same namespace URI, the same local name,
    * and the same prefix.
    */

    public int getNameCode() {
        notSupported();
        return -1;
    }


    /**
    * Get fingerprint. The fingerprint is a coded form of the node name: two nodes
    * with the same fingerprint have the same namespace URI and the same local name.
    * The fingerprint does not include information about the prefix.
    */

    public int getFingerprint() {
        notSupported();
        return -1;
    }

    public short getURICode() {
        notSupported();
        return -1;
    }
        
/*
    already part of API
 
    public String getValue();

    public String getNodeName();
 *
    public String getPath(); 
 */
    
    public String getAttributeValue(String uri, String localName) {
        return null;
    }
    
    public String getAttributeValue(String name) {
        return null;
    }
    
    public String getAttributeValue(int fingerprint) {
        return null;
    }
    
    public boolean isDocumentElement() {
        return false;
    }
    
    public org.w3c.dom.Element getDocumentElement() {
/*        
        Document document = getDocument();
        return ( document != null ) ? document.getRootElement() : null;
*/
        notSupported();
        return null;
    }

    public DocumentInfo getDocumentRoot() {
        return (DocumentInfo) getDocument();
    }

    /**
    * Get the next node in document order
    * @param anchor: the scan stops when it reaches a node that is not a descendant of the specified
    * anchor node
    * @return the next node in the document, or null if there is no such node
    */

    public NodeInfo getNextInDocument(NodeInfo anchor) {
        notSupported();
        return null;
    }

    /**
    * Get the previous node in document order
    * @return the previous node in the document, or null if there is no such node
    */
    public NodeInfo getPreviousInDocument() {
        notSupported();
        return null;
    }

    public int getNumberOfChildren() {
        return 0;
    }

    public NodeInfo[] getAllChildNodes() {
        return emptyArray;
    }

    public NodeEnumeration enumerateChildren() {
        return emptyEnumeration;
    }

    /**
    * Get a character string that uniquely identifies this node and that collates nodes
    * into document order
    * @return a string. 
    */

    public String generateId() {
        notSupported();
        return null;
    }

    /**
    * Perform default action for this kind of node (built-in template rule)
    */

    public void defaultAction(Context c) throws TransformerException {
        notSupported();
    }

    /**
    * Copy this node to a given outputter
    */

    public void copy(Outputter out) throws TransformerException {
        notSupported();
    }

    /**
    * Copy the string-value of this node to a given outputter
    */

    public void copyStringValue(Outputter out) throws TransformerException {
        notSupported();
    }


    
    protected void notSupported() {
        throw new UnsupportedOperationException("Not supported yet");
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
