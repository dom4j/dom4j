/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.dom;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.Namespace;

import org.w3c.dom.DOMException;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

/** <p><code>DOMNodeHelper</code> contains a collection of utility methods
  * for use across Node implementations.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DOMNodeHelper {

    public static final NodeList EMPTY_NODE_LIST = new EmptyNodeList();
    
    public static class EmptyNodeList implements NodeList {
        public org.w3c.dom.Node item(int index) {
            return null;
        }
        public int getLength() {
            return 0;
        }
    };
    
    public static void setPrefix(Node node, String prefix) throws DOMException {
        notSupported();
    }
    
    public static String getNodeValue(Node node) throws DOMException {
        return node.getText();
    }
    
    public static void setNodeValue(Node node, String nodeValue) throws DOMException {
        node.setText(nodeValue);
    }

    public static org.w3c.dom.Node getParentNode(Node node) {
        return asDOMNode( node.getParent() );
    }

    public static NodeList getChildNodes(Node node) {
        return EMPTY_NODE_LIST;
    }

    public static org.w3c.dom.Node getFirstChild(Node node) {
        return null;
    }

    public static org.w3c.dom.Node getLastChild(Node node) {
        return null;
    }

    public static org.w3c.dom.Node getPreviousSibling(Node node) {
        Element parent = node.getParent();
        if ( parent != null ) {
            int index = parent.indexOf( node );
            if ( index > 0 ) {
                Node previous = parent.getNode(index - 1);
                return asDOMNode( previous );
            }
        }
        return null;
    }

    public static org.w3c.dom.Node getNextSibling(Node node) {
        Element parent = node.getParent();
        if ( parent != null ) {
            int index = parent.indexOf( node );
            if ( index >= 0 ) {
                if ( ++index < parent.getNodeCount() ) {
                    Node next = parent.getNode(index);
                    return asDOMNode( next );
                }
            }
        }
        return null;
    }

    public static NamedNodeMap getAttributes(Node node) {
        return null;
    }

    public static org.w3c.dom.Document getOwnerDocument(Node node) {
        return asDOMDocument( node.getDocument() );
    }

    public static org.w3c.dom.Node insertBefore(
        Node node, 
        org.w3c.dom.Node newChild, 
        org.w3c.dom.Node refChild
    ) throws DOMException {
        throw new DOMException( DOMException.HIERARCHY_REQUEST_ERR, "Children not allowed for this node: " + node );
    }

    public static org.w3c.dom.Node replaceChild(
        Node node, 
        org.w3c.dom.Node newChild, 
        org.w3c.dom.Node oldChild
    ) throws DOMException {
        throw new DOMException( DOMException.HIERARCHY_REQUEST_ERR, "Children not allowed for this node: " + node );
    }

    public static org.w3c.dom.Node removeChild(
        Node node, 
        org.w3c.dom.Node oldChild
    ) throws DOMException {
        throw new DOMException( DOMException.HIERARCHY_REQUEST_ERR, "Children not allowed for this node: " + node );
    }

    public static org.w3c.dom.Node appendChild(
        Node node, 
        org.w3c.dom.Node newChild
    ) throws DOMException {
        throw new DOMException( DOMException.HIERARCHY_REQUEST_ERR, "Children not allowed for this node: " + node );
    }

    public static boolean hasChildNodes(Node node) {
        return false;
    }

    public static org.w3c.dom.Node cloneNode(Node node, boolean deep) {
        return asDOMNode( (Node) node.clone() );
    }

    public static void normalize(Node node) {
        notSupported();
    }

    public static boolean isSupported(Node node, String feature, String version) {
        return false;
    }

    public static boolean hasAttributes(Node node) {
        return false;
    }
    
    /** Called when a method has not been implemented yet 
      */
    public static void notSupported() {
        throw new UnsupportedOperationException("Not supported yet");
    }
    
    public static org.w3c.dom.Node asDOMNode(Node node) {
        if ( node instanceof org.w3c.dom.Node ) {
            return (org.w3c.dom.Node) node;
        }
        else {
            // Use DOMWriter?
            notSupported();
            return null;
        }
    }
    
    public static org.w3c.dom.Document asDOMDocument(Document document) {
        if ( document instanceof org.w3c.dom.Document ) {
            return (org.w3c.dom.Document) document;
        }
        else {
            // Use DOMWriter?
            notSupported();
            return null;
        }
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
