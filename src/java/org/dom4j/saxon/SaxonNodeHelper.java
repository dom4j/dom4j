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
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.Namespace;
import org.dom4j.tree.XPathAttribute;

/** <p><code>SaxonNodeHelper</code> contains a collection of utility methods
  * for use across Node implementations.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class SaxonNodeHelper {

    protected static NodeInfo[] emptyArray = new NodeInfo[0];
    protected static SingletonEnumeration emptyEnumeration = new SingletonEnumeration(null);
    

    public static boolean isSameNode(Node that, NodeInfo other) {
        return that == other;
    }

    public static String getSystemId(Node node) {
        Document document = node.getDocument();
        if ( document != null ) {
            DocumentType docType = document.getDocType();
            if ( docType != null ) {
                return docType.getSystemID();
            }
        }
        return "";
    }
    
    public static String getBaseURI(Node node) {
        // XXXX: no support for xml:base yet
        return getSystemId(node);
    }

    public static int getLineNumber(Node node) {
        return -1;
    }

    
    public static String getLocalName(Node node) {
        return node.getName();
    }

    public static String getDisplayName(Node node) {
        return node.getName();
    }

    
    
    public static long getSequenceNumber(Node node) {
        notSupported();
        return -1;
    }
    
    /**
    * Get name code. The name code is a coded form of the node name: two nodes
    * with the same fingerprint have the same namespace URI, the same local name,
    * and the same prefix.
    */

    public static int getNameCode(Node node) {
        notSupported();
        return -1;
    }


    /**
    * Get fingerprint. The fingerprint is a coded form of the node name: two nodes
    * with the same fingerprint have the same namespace URI and the same local name.
    * The fingerprint does not include information about the prefix.
    */

    public static int getFingerprint(Node node) {
        notSupported();
        return -1;
    }

    public static short getURICode(Node node) {
        notSupported();
        return -1;
    }
            
    public static String getAttributeValue(Node node, String uri, String localName) {
        return null;
    }
    
    public static String getAttributeValue(Node node, String name) {
        return null;
    }
    
    public static String getAttributeValue(Node node, int fingerprint) {
        return null;
    }
    
    public static boolean isDocumentElement(Node node) {
        return false;
    }
    
    public static org.w3c.dom.Element getDocumentElement(Node node) {
/*        
        Document document = getDocument();
        return ( document != null ) ? document.getRootElement() : null;
*/
        notSupported();
        return null;
    }

    public static DocumentInfo getDocumentRoot(Node node) {
        return (DocumentInfo) node.getDocument();
    }

    /**
    * Get the next node in document order
    * @param anchor: the scan stops when it reaches a node that is not a descendant of the specified
    * anchor node
    * @return the next node in the document, or null if there is no such node
    */

    public static NodeInfo getNextInDocument(Node node, NodeInfo anchor) {
        notSupported();
        return null;
    }

    /**
    * Get the previous node in document order
    * @return the previous node in the document, or null if there is no such node
    */
    public static NodeInfo getPreviousInDocument(Node node) {
        notSupported();
        return null;
    }

    public static int getNumberOfChildren(Node node) {
        return 0;
    }

    public static NodeInfo[] getAllChildNodes(Node node) {
        return emptyArray;
    }

    public static NodeEnumeration enumerateChildren(Node node) {
        return emptyEnumeration;
    }

    /**
    * Get a character string that uniquely identifies this node and that collates nodes
    * into document order
    * @return a string. 
    */

    public static String generateId(Node node) {
        notSupported();
        return null;
    }

    /**
    * Perform default action for this kind of node (built-in template rule)
    */

    public static void defaultAction(Node node, Context c) throws TransformerException {
        notSupported();
    }

    /**
    * Copy this node to a given outputter
    */

    public static void copy(Node node, Outputter out) throws TransformerException {
        notSupported();
    }

    /** Copy the string-value of this node to a given outputter
      */
    public static void copyStringValue(Node node, Outputter out) throws TransformerException {
        notSupported();
    }


    /** Called when a method has not been implemented yet 
      */
    public static void notSupported() {
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
