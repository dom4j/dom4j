/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
   
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.DocumentType;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;

/** <p><code>DOMReader</code> navigates a W3C DOM tree and creates
  * a DOM4J tree from it.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DOMReader {

    /** <code>DocumentFactory</code> used to create new document objects */
    private DocumentFactory factory;
    
    /** Stores the default namespace prefix map */
    private HashMap defaultNamespaces;
    
    
    public DOMReader() {
    }
    
    public DOMReader(DocumentFactory factory) {
        this.factory = factory;
    }
    
    /** @return the <code>DocumentFactory</code> used to create document objects
      */
    public DocumentFactory getDocumentFactory() {
        if (factory == null) {
            factory = DocumentFactory.getInstance();
        }
        return factory;
    }

    /** <p>This sets the <code>DocumentFactory</code> used to create new documents.
      * This method allows the building of custom DOM4J tree objects to be implemented
      * easily using a custom derivation of {@link DocumentFactory}</p>
      *
      * @param factory <code>DocumentFactory</code> used to create DOM4J objects
      */
    public void setDocumentFactory(DocumentFactory factory) {
        this.factory = factory;
    }

    public Document read(org.w3c.dom.Document domDocument) {
        if ( domDocument instanceof Document ) {
            return (Document) domDocument;
        }
        Document document = createDocument();
        org.w3c.dom.NodeList nodeList = domDocument.getChildNodes();
        for ( int i = 0, size = nodeList.getLength(); i < size; i++ ) {
            readTree( nodeList.item(i), document, getDefaultNamespaces() );
        }
        return document;
    }
    
    
    // Implementation methods
    protected void readTree(org.w3c.dom.Node node, Branch current, HashMap declaredNamespaces) {    
        Element element = null;
        Document document = null;
        if ( current instanceof Element ) {
            element = (Element) current;
        }
        else {
            document = (Document) current;
        }
        switch (node.getNodeType()) {
            case org.w3c.dom.Node.ELEMENT_NODE:
                readElement(node, current, declaredNamespaces);
                break;

            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                current.addProcessingInstruction(
                    node.getNodeName(), node.getNodeValue()
                );
                break;

            case org.w3c.dom.Node.COMMENT_NODE:
                current.addComment( node.getNodeValue() );
                break;

            case org.w3c.dom.Node.DOCUMENT_TYPE_NODE:
                org.w3c.dom.DocumentType domDocType 
                    = (org.w3c.dom.DocumentType) node;
                
                document.setDocType( 
                    domDocType.getName(), 
                    domDocType.getPublicId(), 
                    domDocType.getSystemId() 
                );
                break;

            case org.w3c.dom.Node.TEXT_NODE:
                element.addText( node.getNodeValue() );
                break;

            case org.w3c.dom.Node.CDATA_SECTION_NODE:
                element.addCDATA( node.getNodeValue() );
                break;


            case org.w3c.dom.Node.ENTITY_REFERENCE_NODE: {
                // is there a better way to get the value of an entity?
                    org.w3c.dom.Node firstChild = node.getFirstChild();
                    if ( firstChild != null ) {
                        element.addEntity(
                            node.getNodeName(), 
                            firstChild.getNodeValue()
                        );
                    }
                    else {
                        element.addEntity( node.getNodeName() );
                    }
                }
                break;

            case org.w3c.dom.Node.ENTITY_NODE:
                element.addEntity(
                    node.getNodeName(), 
                    node.getNodeValue()
                );
                break;
        }
    }
    
    protected void readElement(org.w3c.dom.Node node, Branch current, HashMap declaredNamespaces) {
        Element element = null;
        String qualifiedName = node.getNodeName();
        String namespaceUri = node.getNamespaceURI();
        if ( namespaceUri != null ) {
            String prefix = "";
            String localName = qualifiedName;
            int index = qualifiedName.indexOf( ':' );
            if ( index >= 0 ) {
                prefix = qualifiedName.substring(0, index);
                localName = qualifiedName.substring(index + 1);
            }
            QName qName = factory.createQName(localName, prefix, namespaceUri);
            element = current.addElement(qName);
        }
        else {
            element = current.addElement(qualifiedName);
        }

        HashMap localNamespaces = declaredNamespaces;
        org.w3c.dom.NamedNodeMap attributeList = node.getAttributes();
        if ( attributeList != null ) {
            int size = attributeList.getLength();
            List attributes = new ArrayList(size);
            for ( int i = 0; i < size; i++ ) {
                org.w3c.dom.Node attribute = attributeList.item(i);

                // Define all namespaces first then process attributes later
                String name = attribute.getNodeName();
                if (name.startsWith("xmlns")) {                        
                    int index = name.indexOf( ':', 5 );
                    if ( index > 0 ) {
                        String uri = attribute.getNodeValue();
                        if ( namespaceUri == null || ! namespaceUri.equals( uri ) ) {
                            String prefix = name.substring(index + 1);
                            Namespace namespace = element.addNamespace( prefix, uri );
                            if ( localNamespaces == declaredNamespaces ) {
                                localNamespaces = (HashMap) declaredNamespaces.clone();
                            }
                            localNamespaces.put(prefix, namespace);
                        }
                    }
                } 
                else {
                    attributes.add( attribute );
                }
            }
            
            // now add the attributes, the namespaces should be available
            size = attributes.size();
            for ( int i = 0; i < size; i++ ) {
                org.w3c.dom.Node attribute = (org.w3c.dom.Node) attributes.get(i);                
                String prefix = attribute.getPrefix();
                String value = attribute.getNodeValue();
                if ( prefix != null && prefix.length() > 0 ) {
                    Namespace namespace = (Namespace) localNamespaces.get(prefix);
                    element.setAttributeValue( factory.createQName( attribute.getLocalName(), namespace ), value  );
                }
                else {
                    element.setAttributeValue( attribute.getNodeName(), value );
                }
            }
        }

        // Recurse on child nodes
        org.w3c.dom.NodeList children = node.getChildNodes();
        for ( int i = 0, size = children.getLength(); i < size; i++ ) {
            org.w3c.dom.Node child = children.item(i);
            readTree( child, element, localNamespaces );
        }
    }
    
    protected Namespace getNamespace(String prefix, String uri) {
        return getDocumentFactory().createNamespace(prefix, uri);
    }
    
    protected Document createDocument() {
        return getDocumentFactory().createDocument();
    }
    
    protected HashMap getDefaultNamespaces() {
        if ( defaultNamespaces == null ) {
            defaultNamespaces = new HashMap();
            defaultNamespaces.put( "xml", Namespace.XML_NAMESPACE );
        }
        return defaultNamespaces;        
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
