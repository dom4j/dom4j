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
import java.util.List;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.NamespaceStack;

/** <p><code>DOMReader</code> navigates a W3C DOM tree and creates
  * a DOM4J tree from it.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class DOMReader {

    /** <code>DocumentFactory</code> used to create new document objects */
    private DocumentFactory factory;

    /** stack of <code>Namespace</code> and <code>QName</code> objects */
    private NamespaceStack namespaceStack;


    public DOMReader() {
        this.factory = DocumentFactory.getInstance();
        this.namespaceStack = new NamespaceStack(factory);
    }

    public DOMReader(DocumentFactory factory) {
        this.factory = factory;
        this.namespaceStack = new NamespaceStack(factory);
    }

    /** @return the <code>DocumentFactory</code> used to create document objects
      */
    public DocumentFactory getDocumentFactory() {
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
        this.namespaceStack.setDocumentFactory(factory);
    }

    public Document read(org.w3c.dom.Document domDocument) {
        if ( domDocument instanceof Document ) {
            return (Document) domDocument;
        }
        Document document = createDocument();

        clearNamespaceStack();

        org.w3c.dom.NodeList nodeList = domDocument.getChildNodes();
        for ( int i = 0, size = nodeList.getLength(); i < size; i++ ) {
            readTree( nodeList.item(i), document );
        }
        return document;
    }


    // Implementation methods
    protected void readTree(org.w3c.dom.Node node, Branch current) {
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
                readElement(node, current);
                break;

            case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                if ( current instanceof Element ) {
                    ((Element) current).addProcessingInstruction(
                        node.getNodeName(), node.getNodeValue()
                    );
                }
                else {
                    ((Document) current).addProcessingInstruction(
                        node.getNodeName(), node.getNodeValue()
                    );
                }
                break;

            case org.w3c.dom.Node.COMMENT_NODE:
                if ( current instanceof Element ) {
                    ((Element) current).addComment( node.getNodeValue() );
                }
                else {
                    ((Document) current).addComment( node.getNodeValue() );
                }
                break;

            case org.w3c.dom.Node.DOCUMENT_TYPE_NODE:
                org.w3c.dom.DocumentType domDocType
                    = (org.w3c.dom.DocumentType) node;

                document.addDocType(
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
                        element.addEntity( node.getNodeName(), "" );
                    }
                }
                break;

            case org.w3c.dom.Node.ENTITY_NODE:
                element.addEntity(
                    node.getNodeName(),
                    node.getNodeValue()
                );
                break;

            default:
                System.out.println( "WARNING: Unknown DOM node type: " + node.getNodeType() );
        }
    }

    protected void readElement(org.w3c.dom.Node node, Branch current) {
        int previouslyDeclaredNamespaces = namespaceStack.size();

        String namespaceUri = node.getNamespaceURI();
        org.w3c.dom.NamedNodeMap attributeList = node.getAttributes();
        if ( namespaceUri == null ) {
            // test if we have an "xmlns" attribute
            org.w3c.dom.Node attribute = attributeList.getNamedItem( "xmlns" );
            if ( attribute != null ) {
                namespaceUri = attribute.getNodeValue();
            }
        }

        QName qName = namespaceStack.getQName( namespaceUri, node.getLocalName(), node.getNodeName() );
        Element element = current.addElement(qName);

        if ( attributeList != null ) {
            int size = attributeList.getLength();
            List attributes = new ArrayList(size);
            for ( int i = 0; i < size; i++ ) {
                org.w3c.dom.Node attribute = attributeList.item(i);

                // Define all namespaces first then process attributes later
                String name = attribute.getNodeName();
                if (name.startsWith("xmlns")) {
                    int index = name.indexOf( ':', 5 );
                    String uri = attribute.getNodeValue();
                    if ( namespaceUri == null || ! namespaceUri.equals( uri ) ) {
                        Namespace namespace = null;
                        if ( index > 0 ) {
                            String prefix = name.substring(index + 1);
                            namespace = namespaceStack.addNamespace( prefix, uri );
                        }
                        else {
                            namespace = namespaceStack.addNamespace( "", uri );
                        }
                        element.add( namespace );
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
                QName attributeQName = namespaceStack.getQName(
                    attribute.getNamespaceURI(),
                    attribute.getLocalName(),
                    attribute.getNodeName()
                );
                element.addAttribute( attributeQName, attribute.getNodeValue() );
            }
        }

        // Recurse on child nodes
        org.w3c.dom.NodeList children = node.getChildNodes();
        for ( int i = 0, size = children.getLength(); i < size; i++ ) {
            org.w3c.dom.Node child = children.item(i);
            readTree( child, element );
        }

        // pop namespaces from the stack
        while (namespaceStack.size() > previouslyDeclaredNamespaces) {
            namespaceStack.pop();
        }
    }

    protected Namespace getNamespace(String prefix, String uri) {
        return getDocumentFactory().createNamespace(prefix, uri);
    }

    protected Document createDocument() {
        return getDocumentFactory().createDocument();
    }

    protected void clearNamespaceStack() {
        namespaceStack.clear();
        if ( ! namespaceStack.contains( Namespace.XML_NAMESPACE ) ) {
            namespaceStack.push( Namespace.XML_NAMESPACE );
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
