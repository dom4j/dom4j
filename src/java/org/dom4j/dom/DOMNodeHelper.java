/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.dom;

import java.util.List;

import org.dom4j.Branch;
import org.dom4j.CharacterData;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Node;
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
    }
    
    
    // Node API
    //-------------------------------------------------------------------------        
    public static boolean supports(Node node, String feature, String version) {
        return false;
    }

    public static String getNamespaceURI(Node node) {
        return null;
    }

    public static String getPrefix(Node node) {
        return null;
    }
    
    public static String getLocalName(Node node) {
        return null;
    }
    
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
                Node previous = parent.node(index - 1);
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
                if ( ++index < parent.nodeCount() ) {
                    Node next = parent.node(index);
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
        if ( node instanceof Branch ) {
            Branch branch = (Branch) node;
            List list = branch.content();
            int index = list.indexOf(refChild);
            if ( index < 0 ) {
                branch.add((Node) newChild);
            }
            else {
                list.add(index, newChild);
            }
            return newChild;
        }
        else {
            throw new DOMException( DOMException.HIERARCHY_REQUEST_ERR, "Children not allowed for this node: " + node );
        }
    }

    public static org.w3c.dom.Node replaceChild(
        Node node, 
        org.w3c.dom.Node newChild, 
        org.w3c.dom.Node oldChild
    ) throws DOMException {
        if ( node instanceof Branch ) {
            Branch branch = (Branch) node;
            List list = branch.content();
            int index = list.indexOf(oldChild);
            if ( index < 0 ) {
                throw new DOMException( DOMException.NOT_FOUND_ERR, "Tried to replace a non existing child for node: " + node );
            }
            list.set(index, newChild);
            return oldChild;
        }
        else {        
            throw new DOMException( DOMException.HIERARCHY_REQUEST_ERR, "Children not allowed for this node: " + node );
        }
    }

    public static org.w3c.dom.Node removeChild(
        Node node, 
        org.w3c.dom.Node oldChild
    ) throws DOMException {
        if ( node instanceof Branch ) {
            Branch branch = (Branch) node;
            branch.remove((Node) oldChild);
            return oldChild;
        }
        throw new DOMException( DOMException.HIERARCHY_REQUEST_ERR, "Children not allowed for this node: " + node );
    }

    public static org.w3c.dom.Node appendChild(
        Node node, 
        org.w3c.dom.Node newChild
    ) throws DOMException {
        if ( node instanceof Branch ) {
            Branch branch = (Branch) node;
            branch.add( (Node) newChild );
            return newChild;
        }
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


    // CharacterData API
    //-------------------------------------------------------------------------        
    
    public static String getData(CharacterData charData) throws DOMException {
        return charData.getText();
    }
    
    public static void setData(CharacterData charData, String data) throws DOMException {
        charData.setText(data);
    }

    public static int getLength(CharacterData charData) {
        String text = charData.getText();
        return ( text != null ) ? text.length() : 0;
    }

    public static String substringData(
        CharacterData charData, int offset, int count
    ) throws DOMException {
        String text = charData.getText();
        int length = ( text != null ) ? text.length() : 0;
        if ( offset < 0 || offset >= length ) {
            throw new DOMException( 
                DOMException.INDEX_SIZE_ERR, 
                "No text at offset: " + offset
            );
        }
        return text.substring( offset, offset + count );
    }

    public static void appendData(
        CharacterData charData, String arg
    ) throws DOMException {
        if ( charData.isReadOnly() ) {
            throw new DOMException( 
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                "CharacterData node is read only: " + charData 
            );
        }
        else {
            String text = charData.getText();
            if ( text == null ) {
                charData.setText( text );
            }
            else {
                charData.setText( text + arg );
            }
        }
    }

    public static void insertData(CharacterData charData, int offset, String arg) throws DOMException {
        if ( charData.isReadOnly() ) {
            throw new DOMException( 
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                "CharacterData node is read only: " + charData 
            );
        }
        else {
            String text = charData.getText();
            if ( text == null ) {
                charData.setText( arg );
            }
            else {
                int length = text.length();
                if ( offset < 0 || offset >= length ) {
                    throw new DOMException( 
                        DOMException.INDEX_SIZE_ERR, 
                        "No text at offset: " + offset
                    );
                }
                else {
                    StringBuffer buffer = new StringBuffer( text );
                    buffer.insert( offset, arg );
                    charData.setText( buffer.toString() );
                }
            }
        }
    }

    public static void deleteData(CharacterData charData, int offset, int count) throws DOMException {
        if ( charData.isReadOnly() ) {
            throw new DOMException( 
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                "CharacterData node is read only: " + charData 
            );
        }
        else {
            String text = charData.getText();
            if ( text != null ) {
                int length = text.length();
                if ( offset < 0 || offset >= length ) {
                    throw new DOMException( 
                        DOMException.INDEX_SIZE_ERR, 
                        "No text at offset: " + offset
                    );
                }
                else {
                    StringBuffer buffer = new StringBuffer( text );
                    buffer.delete( offset, offset + count );
                    charData.setText( buffer.toString() );
                }
            }
        }
    }

    public static void replaceData(
        CharacterData charData, int offset, int count, String arg
    ) throws DOMException {
        if ( charData.isReadOnly() ) {
            throw new DOMException( 
                DOMException.NO_MODIFICATION_ALLOWED_ERR,
                "CharacterData node is read only: " + charData 
            );
        }
        else {
            String text = charData.getText();
            if ( text != null ) {
                int length = text.length();
                if ( offset < 0 || offset >= length ) {
                    throw new DOMException( 
                        DOMException.INDEX_SIZE_ERR, 
                        "No text at offset: " + offset
                    );
                }
                else {
                    StringBuffer buffer = new StringBuffer( text );
                    buffer.replace( offset, offset + count, arg );
                    charData.setText( buffer.toString() );
                }
            }
        }
    }


    // Branch API
    //-------------------------------------------------------------------------        
    
    public static void appendElementsByTagName(
        List list, Branch parent, String name
    ) {
        for ( int i = 0, size = parent.nodeCount(); i < size; i++ ) {
            Node node = parent.node(i);
            if ( node instanceof Element ) {
                Element element = (Element) node;
                if ( name.equals( element.getName() ) ) {
                    list.add( element );
                }
                appendElementsByTagName(list, element, name);
            }
        }
    }

    public static void appendElementsByTagNameNS(
        List list, Branch parent, String namespaceURI, String localName
    ) {
        for ( int i = 0, size = parent.nodeCount(); i < size; i++ ) {
            Node node = parent.node(i);
            if ( node instanceof Element ) {
                Element element = (Element) node;
                if ( namespaceURI.equals( element.getNamespaceURI() ) 
                        && localName.equals( element.getName() ) ) {
                    list.add( element );
                }
                appendElementsByTagNameNS(list, element, namespaceURI, localName);
            }
        }
    }

    
    // Helper methods
    //-------------------------------------------------------------------------        
    
    public static NodeList createNodeList( final List list ) {
        return new NodeList() {
            public org.w3c.dom.Node item(int index) {
                return DOMNodeHelper.asDOMNode( (Node) list.get( index ) );
            }
            public int getLength() {
                return list.size();
            }
        };
    }

    public static org.w3c.dom.Node asDOMNode(Node node) {
        if ( node instanceof org.w3c.dom.Node ) {
            return (org.w3c.dom.Node) node;
        }
        else {
            // Use DOMWriter?
            System.out.println( "Cannot convert: " + node + " into a W3C DOM Node");
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
    
    public static org.w3c.dom.DocumentType asDOMDocumentType(DocumentType documentType) {
        if ( documentType instanceof org.w3c.dom.DocumentType ) {
            return (org.w3c.dom.DocumentType) documentType;
        }
        else {
            // Use DOMWriter?
            notSupported();
            return null;
        }
    }

    public static org.w3c.dom.Text asDOMText(CharacterData text) {
        if ( text instanceof org.w3c.dom.Text ) {
            return (org.w3c.dom.Text) text;
        }
        else {
            // Use DOMWriter?
            notSupported();
            return null;
        }
    }

    public static org.w3c.dom.Element asDOMElement(Node element) {
        if ( element instanceof org.w3c.dom.Element ) {
            return (org.w3c.dom.Element) element;
        }
        else {
            // Use DOMWriter?
            notSupported();
            return null;
        }
    }

    public static org.w3c.dom.Attr asDOMAttr(Node attribute) {
        if ( attribute instanceof org.w3c.dom.Attr ) {
            return (org.w3c.dom.Attr) attribute;
        }
        else {
            // Use DOMWriter?
            notSupported();
            return null;
        }
    }

    /** Called when a method has not been implemented yet 
      */
    public static void notSupported() {
        throw new DOMException( DOMException.NOT_SUPPORTED_ERR, "Not supported yet");
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
