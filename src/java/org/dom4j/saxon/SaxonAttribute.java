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
import org.dom4j.dom.DOMAttribute;

/** <p><code>SaxonAttribute</code> implements a doubly linked attribute which 
  * supports the SAXON tree API.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class SaxonAttribute extends DOMAttribute implements AttributeInfo {

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
    public String getLocalName() {
        return getQName().getName();
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

    public String getNodeName() {
        return getName();
    }
    
/*  already part of API
 
    public String getValue();
    public String getNodeName();
    public String getPath(); 
 */
    

    // delegate common functionality to SaxonNodeHelper
    
    public boolean isSameNode(NodeInfo other) {
        return SaxonNodeHelper.isSameNode(this, other);
    }

    public String getSystemId() {
        return SaxonNodeHelper.getSystemId(this);
    }
    
    public String getBaseURI() {
        return SaxonNodeHelper.getBaseURI(this);
    }

    public int getLineNumber() {
        return SaxonNodeHelper.getLineNumber(this);
    }
    
    public long getSequenceNumber() {
        return SaxonNodeHelper.getSequenceNumber(this);
    }
    
    public int getNameCode() {
        return SaxonNodeHelper.getNameCode(this);
    }

    public int getFingerprint() {
        return SaxonNodeHelper.getFingerprint(this);
    }

    public short getURICode() {
        return SaxonNodeHelper.getURICode(this);
    }
        
    public String getAttributeValue(String uri, String localName) {
        return SaxonNodeHelper.getAttributeValue(this, uri, localName);
    }
    
    public String getAttributeValue(String name) {
        return SaxonNodeHelper.getAttributeValue(this, name);
    }
    
    public String getAttributeValue(int fingerprint) {
        return SaxonNodeHelper.getAttributeValue(this, fingerprint);
    }
    
    public boolean isDocumentElement() {
        return SaxonNodeHelper.isDocumentElement(this);
    }
    
    public org.w3c.dom.Element getDocumentElement() {
        return SaxonNodeHelper.getDocumentElement(this);
    }

    public DocumentInfo getDocumentRoot() {
        return SaxonNodeHelper.getDocumentRoot(this);
    }

    public NodeInfo getNextInDocument(NodeInfo anchor) {
        return SaxonNodeHelper.getNextInDocument(this, anchor);
    }

    public NodeInfo getPreviousInDocument() {
        return SaxonNodeHelper.getPreviousInDocument(this);
    }

    public int getNumberOfChildren() {
        return SaxonNodeHelper.getNumberOfChildren(this);
    }

    public NodeInfo[] getAllChildNodes() {
        return SaxonNodeHelper.getAllChildNodes(this);
    }

    public NodeEnumeration enumerateChildren() {
        return SaxonNodeHelper.enumerateChildren(this);
    }

    public String generateId() {
        return SaxonNodeHelper.generateId(this);
    }

    public void defaultAction(Context c) throws TransformerException {
        SaxonNodeHelper.defaultAction(this, c);
    }

    public void copy(Outputter out) throws TransformerException {
        SaxonNodeHelper.copy(this, out);
    }
    
    public void copyStringValue(Outputter out) throws TransformerException {
        SaxonNodeHelper.copyStringValue(this, out);
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
