/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.tree.NamespaceStack;

/** <p><code>DOMWriter</code> takes a DOM4J tree and outputs
  * it as a W3C DOM object</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DOMWriter {

    private static boolean loggedWarning = false;
    private static final String[] DEFAULT_DOM_DOCUMENT_CLASSES = {
        "org.apache.xerces.dom.DocumentImpl", // Xerces
        "org.apache.crimson.tree.XmlDocument", // Crimson
        "com.sun.xml.tree.XmlDocument", // Sun's Project X
        "oracle.xml.parser.v2.XMLDocument", // Oracle V2
        "oracle.xml.parser.XMLDocument" // Oracle V1
    };

    // the Class used to create new DOM Document instances
    private Class domDocumentClass;
    
    /** stack of <code>Namespace</code> objects */
    private NamespaceStack namespaceStack = new NamespaceStack();

     
    public DOMWriter() {
    }
    
    public DOMWriter(Class domDocumentClass) {
        this.domDocumentClass = domDocumentClass;
    }

    public Class getDomDocumentClass() throws DocumentException {
        if ( domDocumentClass == null ) {
            // lets try and find one in the classpath
            int size = DEFAULT_DOM_DOCUMENT_CLASSES.length;
            for ( int i = 0; i < size; i++ ) {
                try {
                    String name = DEFAULT_DOM_DOCUMENT_CLASSES[i];
                    domDocumentClass = Class.forName( 
                        name,
                        true,
                        DOMWriter.class.getClassLoader()
                    );
                    if ( domDocumentClass != null ) {
                        break;
                    }
                }
                catch (Exception e) {
                    // could not load class correctly
                    // lets carry on to the next one
                }
            }
        }
        return domDocumentClass;
    }
    
    /** Sets the DOM {@link org.w3c.dom.Document} implementation
      * class used by the writer when creating DOM documents.
      *
      * @param domDocumentClass is the Class implementing
      * the {@link org.w3c.dom.Document} interface
      */
    public void setDomDocumentClass(Class domDocumentClass) {
        this.domDocumentClass = domDocumentClass;
    }
    
    /** Sets the DOM {@link org.w3c.dom.Document} implementation
      * class name used by the writer when creating DOM documents.
      *
      * @param className is the name of the Class implementing
      * the {@link org.w3c.dom.Document} interface
      * @throws DocumentException if the class could not be loaded
      */
    public void setDomDocumentClassName(String className) throws DocumentException {
        try {
            this.domDocumentClass = Class.forName( 
                className,
                true,
                DOMWriter.class.getClassLoader()
            );
        }
        catch (Exception e) {
            throw new DocumentException( 
                "Could not load the DOM Document class: "  + className, e 
            );
        }
    }

    
    public org.w3c.dom.Document write(Document document) throws DocumentException {
        if ( document instanceof org.w3c.dom.Document ) {
            return (org.w3c.dom.Document) document;
        }
        resetNamespaceStack();
        org.w3c.dom.Document domDocument = createDomDocument(document);
        appendDOMTree(domDocument, domDocument, document.content());
        namespaceStack.clear();
        return domDocument;
    }
    
    public org.w3c.dom.Document write(
        Document document, 
        org.w3c.dom.DOMImplementation domImplementation
    ) throws DocumentException {
        if ( document instanceof org.w3c.dom.Document ) {
            return (org.w3c.dom.Document) document;
        }
        resetNamespaceStack();
        org.w3c.dom.Document domDocument = createDomDocument(document, domImplementation);
        appendDOMTree(domDocument, domDocument, document.content());
        namespaceStack.clear();
        return domDocument;
    }
    
    protected void appendDOMTree( 
        org.w3c.dom.Document domDocument, 
        org.w3c.dom.Node domCurrent,
        List content
    ) {
        int size = content.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = content.get(i);
            if (object instanceof Element) {
                appendDOMTree( domDocument, domCurrent, (Element) object);
            }
            else if ( object instanceof String ) {
                appendDOMTree( domDocument, domCurrent, (String) object );
            }
            else if ( object instanceof Text ) {
                Text text = (Text) object;
                appendDOMTree( domDocument, domCurrent, text.getText() );
            }
            else if ( object instanceof CDATA ) {
                appendDOMTree( domDocument, domCurrent, (CDATA) object );
            }
            else if ( object instanceof Comment ) {
                appendDOMTree( domDocument, domCurrent, (Comment) object );
            }
            else if ( object instanceof Entity ) {
                appendDOMTree( domDocument, domCurrent, (Entity) object );
            }
            else if ( object instanceof ProcessingInstruction ) {
                appendDOMTree( domDocument, domCurrent, (ProcessingInstruction) object );
            }
        }
    }
        
    protected void appendDOMTree( 
        org.w3c.dom.Document domDocument, 
        org.w3c.dom.Node domCurrent,
        Element element
    ) {        
        org.w3c.dom.Element domElement = null;
        String elementUri = element.getNamespaceURI();
        if (elementUri != null && elementUri.length() > 0 ) {            
            domElement = domDocument.createElementNS( elementUri, element.getQualifiedName() );
        }
        else {
            domElement = domDocument.createElement( element.getQualifiedName() );
        }
        
        int stackSize = namespaceStack.size();
        List declaredNamespaces = element.declaredNamespaces();
        for ( int i = 0, size = declaredNamespaces.size(); i < size ; i++ ) {
            Namespace namespace = (Namespace) declaredNamespaces.get(i);
            if ( isNamespaceDeclaration( namespace ) ) {
                namespaceStack.push( namespace );     
                writeNamespace( domElement, namespace );
            }
        }
        
        // add the attributes
        for ( int i = 0, size = element.attributeCount(); i < size ; i++ ) {
            Attribute attribute = (Attribute) element.attribute(i);
            String uri = attribute.getNamespaceURI();
            if ( uri != null && uri.length() > 0 ) {
                //writeNamespace( domElement, attribute.getNamespace() );
                domElement.setAttributeNS( uri, attribute.getQualifiedName(), attribute.getValue() );
            }
            else {
                domElement.setAttribute( attribute.getName(), attribute.getValue() );
            }
        }

        // add content
        appendDOMTree( domDocument, domElement, element.content() );
        
        domCurrent.appendChild( domElement );
        
        while ( namespaceStack.size() > stackSize ) {
            namespaceStack.pop();
        }
    }
    
    protected void appendDOMTree( 
        org.w3c.dom.Document domDocument, 
        org.w3c.dom.Node domCurrent,
        CDATA cdata
    ) {
        org.w3c.dom.CDATASection domCDATA = 
            domDocument.createCDATASection(cdata.getText());        
        domCurrent.appendChild(domCDATA);
    }
        
    protected void appendDOMTree( 
        org.w3c.dom.Document domDocument, 
        org.w3c.dom.Node domCurrent,
        Comment comment
    ) {
        org.w3c.dom.Comment domComment = 
            domDocument.createComment(comment.getText());
        domCurrent.appendChild(domComment);
    }
        
    protected void appendDOMTree( 
        org.w3c.dom.Document domDocument, 
        org.w3c.dom.Node domCurrent,
        String text
    ) {
        org.w3c.dom.Text domText = domDocument.createTextNode(text);
        domCurrent.appendChild(domText);
    }
        
    protected void appendDOMTree( 
        org.w3c.dom.Document domDocument, 
        org.w3c.dom.Node domCurrent,
        Entity entity
    ) {
        org.w3c.dom.EntityReference domEntity = 
            domDocument.createEntityReference(entity.getName());
        domCurrent.appendChild(domEntity);
    }
        
    protected void appendDOMTree( 
        org.w3c.dom.Document domDocument, 
        org.w3c.dom.Node domCurrent,
        ProcessingInstruction pi
    ) {
        org.w3c.dom.ProcessingInstruction domPI =
            domDocument.createProcessingInstruction(pi.getTarget(), pi.getText());
        domCurrent.appendChild(domPI);
    }
    
    /** @return the new local namespace set which may be different from the input
      * set if a new namespace is added to the set
      */
    protected void writeNamespace( 
        org.w3c.dom.Element domElement, 
        Namespace namespace
    ) {
        String attributeName = attributeNameForNamespace(namespace);
        //domElement.setAttributeNS("", attributeName, namespace.getURI());
        domElement.setAttribute(attributeName, namespace.getURI());
    }
    
    protected String attributeNameForNamespace(Namespace namespace) {
        String xmlns = "xmlns";
        String prefix = namespace.getPrefix();
        if ( prefix.length() > 0 ) {
            return xmlns + ":" + prefix;
        }
        return xmlns;
    }
    
    protected org.w3c.dom.Document createDomDocument(
        Document document
    ) throws DocumentException {
        // lets try JAXP first
        org.w3c.dom.Document answer = createDomDocumentViaJAXP();
        if ( answer != null ) {
            return answer;
        }
        Class theClass = getDomDocumentClass();
        try {
            return (org.w3c.dom.Document) theClass.newInstance();
        }
        catch (Exception e) {
            throw new DocumentException( 
                "Could not instantiate an instance of DOM Document wtih class: " 
                + theClass.getName(), e 
            );
        }
    }
    
    protected org.w3c.dom.Document createDomDocumentViaJAXP() throws DocumentException {
        if ( ! SAXHelper.classNameAvailable( "javax.xml.parsers.DocumentBuilderFactory" ) ) {
            // don't attempt to use JAXP if it is not in the ClassPath
            return null;
        }
        
        // try use JAXP to load the XMLReader...
        try {
            return JAXPHelper.createDocument( false, true );
        }
        catch (Throwable e) {
            if ( ! loggedWarning ) {                    
                loggedWarning = true;
                if ( SAXHelper.isVerboseErrorReporting() ) {
                    // log all exceptions as warnings and carry
                    // on as we have a default SAX parser we can use
                    System.out.println( 
                        "Warning: Caught exception attempting to use JAXP to "
                         + "create a W3C DOM document" 
                    );
                    System.out.println( "Warning: Exception was: " + e );
                    e.printStackTrace();
                }
                else {
                    System.out.println( 
                        "Warning: Error occurred using JAXP to create a DOM document." 
                    );
                }
            }
        }
        return null;
    }
    protected org.w3c.dom.Document createDomDocument(
        Document document, 
        org.w3c.dom.DOMImplementation domImplementation
    ) throws DocumentException {
        
        String namespaceURI = null;
        String qualifiedName = null;
        org.w3c.dom.DocumentType docType = null;
        return domImplementation.createDocument( 
            namespaceURI, qualifiedName, docType 
        );
    }

    protected boolean isNamespaceDeclaration( Namespace ns ) {
        if (ns != null && ns != Namespace.NO_NAMESPACE && ns != Namespace.XML_NAMESPACE) {
            String uri = ns.getURI();
            if ( uri != null && uri.length() > 0 ) {
                if ( ! namespaceStack.contains( ns ) ) {
                    return true;

                }
            }
        }
        return false;
    }
    
    protected void resetNamespaceStack() {
        namespaceStack.clear();
        namespaceStack.push( Namespace.XML_NAMESPACE );
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
