/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.IllegalAddException;
import org.dom4j.Node;
import org.dom4j.ProcessingInstruction;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.Visitor;
import org.dom4j.io.XMLWriter;

/** <p><code>AbstractDocument</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractDocument extends AbstractBranch implements Document {
    
    public AbstractDocument() { 
    }
 
    public short getNodeType() {
        return DOCUMENT_NODE;
    }

    public String getPath(Element context) {
        return "/";
    }
    
    public String getUniquePath(Element context) {
        return "/";
    }
    
    public Document getDocument() {
        return this;
    }
    
    public String getStringValue() {
        Element root = getRootElement();
        return ( root != null ) ? root.getStringValue() : "";
    }
    
    public String asXML() {
        try {
            StringWriter out = new StringWriter();
            XMLWriter writer = new XMLWriter( out, outputFormat );
            writer.write(this);
            return out.toString();
        } 
        catch (IOException e) {
            throw new RuntimeException("Wierd IOException while generating textual representation: " + e.getMessage());
        }
    }

    public void write(Writer out) throws IOException {
        XMLWriter writer = new XMLWriter( out, outputFormat );
        writer.write(this);
    }
        
    /** <p><code>accept</code> method is the <code>Visitor Pattern</code> method.
      * </p>
      *
      * @param visitor <code>Visitor</code> is the visitor.
      */
    public void accept(Visitor visitor) {
        visitor.visit(this);

        DocumentType docType = getDocType();
        if ( docType != null ) {
            visitor.visit( docType );
        }
        
        // visit content
        List content = content();
        if (content != null) {
            for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
                Object object = iter.next();
                if (object instanceof String) {
                    Text text = getDocumentFactory().createText((String) object);
                    visitor.visit(text);
                } 
                else {
                    Node node = (Node) object;
                    node.accept(visitor);
                }
            }            
        }
    }
    
    public String toString() {
        return super.toString() + " [Document: name " + getName() + "]";
    }
    
    public void normalize() {
        Element element = getRootElement();
        if ( element != null ) {
            element.normalize();
        }
    }
    
    public Document addComment(String comment) {
        Comment node = getDocumentFactory().createComment( comment );
        add( node );
        return this;
    }
       
    public Document addProcessingInstruction(String target, String data) {
        ProcessingInstruction node = getDocumentFactory().createProcessingInstruction( target, data );
        add( node );
        return this;
    }
    
    public Document addProcessingInstruction(String target, Map data) {
        ProcessingInstruction node = getDocumentFactory().createProcessingInstruction( target, data );
        add( node );
        return this;
    }
    
    public Element addElement(String name) {
        checkAddElementAllowed();
        Element node = super.addElement(name);
        rootElementAdded(node);
        return node;
    }
    
    public Element addElement(String qualifiedName, String namespaceURI) {
        checkAddElementAllowed();
        Element node = super.addElement(qualifiedName, namespaceURI);
        rootElementAdded(node);
        return node;
    }
    
    public Element addElement(QName qName) {
        checkAddElementAllowed();
        Element node = super.addElement(qName);
        rootElementAdded(node);
        return node;
    }

    public void setRootElement(Element rootElement) {
        clearContent();
        if ( rootElement != null ) {
            super.add(rootElement);
            rootElementAdded(rootElement);
        }
    }

    public void add(Element element) {
        checkAddElementAllowed();
        super.add(element);
        rootElementAdded(element);
    }
    
    public boolean remove(Element element) {
        boolean answer = super.remove(element);        
        Element root = getRootElement();
        if ( root != null && answer ) {
            setRootElement(null);
        }
        element.setDocument(null);
        return answer;
    }
        
    public Node asXPathResult(Element parent) {
        return this;
    }
    
    
    
    
    protected void childAdded(Node node) {
        if (node != null ) {
            node.setDocument(this);
        }
    }
    
    protected void childRemoved(Node node) {
        if ( node != null ) {
            node.setDocument(null);
        }
    }     

    protected void checkAddElementAllowed() {
        Element root = getRootElement();
        if ( root != null ) {
            throw new IllegalAddException(  
                this, 
                root, 
                "Cannot add another element to this Document as it already has "
                + " a root element of: " + root.getQualifiedName()
            );
        }
    }

    /** Called to set the root element variable */
    protected abstract void rootElementAdded(Element rootElement);
    
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
