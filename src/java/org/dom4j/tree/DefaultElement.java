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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.IllegalAddException;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

/** <p><code>DefaultElement</code> is the default DOM4J default implementation
  * of an XML element.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultElement extends AbstractElement {

    protected static final List EMPTY_LIST = Collections.EMPTY_LIST;
    protected static final Iterator EMPTY_ITERATOR = EMPTY_LIST.iterator();
    
    /** The parent of this node */
    private Element parent;

    /** The document of this node */
    private Document document;

    /** The <code>QName</code> for this element */
    private QName qname;
    
    /** Store the contents of the element as a lazily created <code>List</code> */
    private List contents;
    
    /** Cache the first Text node to delay creating the content list for 
      * elements with no other content other than a single text node */
    private Node firstNode;
    
    /** Lazily constructes list of attributes */
    private List attributes;

    
    
    public DefaultElement(String name) { 
        this.qname = QName.get(name);
    }

    public DefaultElement(QName qname) { 
        this.qname = qname;
    }

    public DefaultElement(String name, Namespace namespace) { 
        this.qname = QName.get(name, namespace);
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public Document getDocument() {
        if ( document != null ) {
            return document;
        }
        if ( parent != null ) {
            return parent.getDocument();
        }
        return null;
    }
    
    public void setDocument(Document document) {
        this.document = document;
    }
    
    public boolean supportsParent() {
        return true;
    }

    public QName getQName() {
        return qname;
    }
    
    
    public String getText() {
        if ( contents == null ) {
            if ( firstNode != null ) {
                if ( firstNode instanceof CharacterData ) {
                    return firstNode.getText();
                }
            }
            return "";
        }
        else {
            return super.getText();
        }
    }
    
    public Namespace getNamespaceForPrefix(String prefix) {
        if ( prefix == null || prefix.length() <= 0 ) {
            return Namespace.NO_NAMESPACE;
        }
        else if ( prefix.equals( getNamespacePrefix() ) ) {
            return getNamespace();
        }
        else if ( prefix.equals( "xml" ) ) {
            return Namespace.XML_NAMESPACE;
        }
        else {
            List source = contents;
            if ( source != null ) {
                int size = source.size();
                for ( int i = 0; i < size; i++ ) {
                    Object object = source.get(i);
                    if ( object instanceof Namespace ) {
                        Namespace namespace = (Namespace) object;
                        if ( prefix.equals( namespace.getPrefix() ) ) {
                            return namespace;
                        }
                    }
                }
            }
            else 
            if ( firstNode instanceof Namespace ) {
                Namespace namespace = (Namespace) firstNode;
                if ( prefix.equals( namespace.getPrefix() ) ) {
                    return namespace;
                }
            }
            Element parent = getParent();
            if ( parent != null ) {
                return parent.getNamespaceForPrefix(prefix);
            }
        }
        return null;
    }
   
    public Namespace getNamespaceForURI(String uri) {
        if ( uri == null || uri.length() <= 0 ) {
            return Namespace.NO_NAMESPACE;
        }
        else if ( uri.equals( getNamespaceURI() ) ) {
            return getNamespace();
        }
        else {
            List source = contents;
            if ( source != null ) {
                int size = source.size();
                for ( int i = 0; i < size; i++ ) {
                    Object object = source.get(i);
                    if ( object instanceof Namespace ) {
                        Namespace namespace = (Namespace) object;
                        if ( uri.equals( namespace.getURI() ) ) {
                            return namespace;
                        }
                    }
                }
            }
            else 
            if ( firstNode instanceof Namespace ) {
                Namespace namespace = (Namespace) firstNode;
                if ( uri.equals( namespace.getURI() ) ) {
                    return namespace;
                }
            }
            Element parent = getParent();
            if ( parent != null ) {
                return parent.getNamespaceForURI(uri);
            }
            return null;
        }
    }
    
    public List getDeclaredNamespaces() {
        BackedList answer = createResultList();
        if ( getNamespaceURI().length() > 0 ) {
            answer.addLocal( getNamespace() );
        }
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Namespace ) {
                answer.addLocal( firstNode );
            }
        }
        else {
            int size = source.size();
            for ( int i = 0; i < size; i++ ) {
                Object object = source.get(i);
                if ( object instanceof Namespace ) {
                    answer.addLocal( object );
                }
            }
        }
        return answer;
    }
    
    public List getAdditionalNamespaces() {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Namespace ) {
                Namespace namespace = (Namespace) firstNode;
                return createSingleResultList( namespace );
            }
            else {
                return createEmptyList();
            }
        }
        BackedList answer = createResultList();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof Namespace ) {
                Namespace namespace = (Namespace) object;
                answer.addLocal( namespace );
            }
        }
        return answer;
    }
    
    public List getAdditionalNamespaces(String defaultNamespaceURI) {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Namespace ) {
                Namespace namespace = (Namespace) firstNode;
                if ( ! defaultNamespaceURI.equals( namespace.getURI() ) ) {
                    return createSingleResultList( namespace );
                }
            }
            else {
                return createEmptyList();
            }
        }
        BackedList answer = createResultList();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof Namespace ) {
                Namespace namespace = (Namespace) object;
                if ( ! defaultNamespaceURI.equals( namespace.getURI() ) ) {
                    answer.addLocal( namespace );
                }
            }
        }
        return answer;
    }
    
    
    // Processing instruction API
    
    public List getProcessingInstructions() {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof ProcessingInstruction ) {
                return createSingleResultList( firstNode );
            }
            return createEmptyList();
        }
        BackedList answer = createResultList();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof ProcessingInstruction ) {
                answer.addLocal( object );
            }
        }
        return answer;
    }
    
    public List getProcessingInstructions(String target) {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof ProcessingInstruction ) {
                ProcessingInstruction pi = (ProcessingInstruction) firstNode;
                if ( target.equals( pi.getName() ) ) {                  
                    return createSingleResultList( pi );
                }
            }
            return createEmptyList();
        }
        BackedList answer = createResultList();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof ProcessingInstruction ) {
                ProcessingInstruction pi = (ProcessingInstruction) object;
                if ( target.equals( pi.getName() ) ) {                  
                    answer.addLocal( pi );
                }
            }
        }
        return answer;
    }
    
    public ProcessingInstruction getProcessingInstruction(String target) {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof ProcessingInstruction ) {
                ProcessingInstruction pi = (ProcessingInstruction) firstNode;
                if ( target.equals( pi.getName() ) ) {                  
                    return pi;
                }
            }
        }
        else {
            int size = source.size();
            for ( int i = 0; i < size; i++ ) {
                Object object = source.get(i);
                if ( object instanceof ProcessingInstruction ) {
                    ProcessingInstruction pi = (ProcessingInstruction) object;
                    if ( target.equals( pi.getName() ) ) {                  
                        return pi;
                    }
                }
            }
        }
        return null;
    }
    
    public boolean removeProcessingInstruction(String target) {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof ProcessingInstruction ) {
                ProcessingInstruction pi = (ProcessingInstruction) firstNode;
                if ( target.equals( pi.getName() ) ) {                  
                    firstNode = null;
                    return true;
                }
            }
        }
        else {
            for ( Iterator iter = source.iterator(); iter.hasNext(); ) {
                Object object = iter.next();
                if ( object instanceof ProcessingInstruction ) {
                    ProcessingInstruction pi = (ProcessingInstruction) object;
                    if ( target.equals( pi.getName() ) ) {                  
                        iter.remove();
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public Element getElementByID(String elementID) {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Element ) {
                Element element = (Element) firstNode;
                String id = getElementID(element);
                if ( id != null && id.equals( elementID ) ) {
                    return element;
                }
            }
        }
        else {
            int size = source.size();
            for ( int i = 0; i < size; i++ ) {
                Object object = source.get(i);
                if ( object instanceof Element ) {
                    Element element = (Element) object;
                    String id = getElementID(element);
                    if ( id != null && id.equals( elementID ) ) {
                        return element;
                    }
                }
            }
        }
        return null;
    }
    
    public Element getElement(String name, Namespace namespace) {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Element ) {
                Element element = (Element) firstNode;
                String uri = namespace.getURI();
                if ( name.equals( element.getName() )
                    && uri.equals( element.getNamespaceURI() ) ) {
                    return element;
                }
            }
        }
        else {
            String uri = namespace.getURI();
            int size = source.size();
            for ( int i = 0; i < size; i++ ) {
                Object object = source.get(i);
                if ( object instanceof Element ) {
                    Element element = (Element) object;
                    if ( name.equals( element.getName() )
                        && uri.equals( element.getNamespaceURI() ) ) {
                        return element;
                    }
                }
            }
        }
        return null;
    }
    
    public List getElements(String name, Namespace namespace) {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Element ) {
                Element element = (Element) firstNode;
                String uri = namespace.getURI();
                if ( name.equals( element.getName() )
                    && uri.equals( element.getNamespaceURI() ) ) {
                    return createSingleResultList( element );
                }
            }
            return createEmptyList();
        }
        BackedList answer = createResultList();
        String uri = namespace.getURI();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof Element ) {
                Element element = (Element) object;
                if ( name.equals( element.getName() )
                    && uri.equals( element.getNamespaceURI() ) ) {
                    answer.addLocal( element );
                }
            }
        }
        return answer;
    }
    
    public List getElements() {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Element ) {
                Element element = (Element) firstNode;
                return createSingleResultList( element );
            }
            return createEmptyList();
        }
        BackedList answer = createResultList();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof Element ) {
                answer.addLocal( object );
            }
        }
        return answer;
    }
    
    public Iterator elementIterator() {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Element ) {
                Element element = (Element) firstNode;
                return createSingleIterator( element );
            }
            return EMPTY_ITERATOR;
        }
        else {
            return new ElementIterator(source.iterator());
        }
    }
        
    public Iterator elementIterator(String name, Namespace namespace) {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Element ) {
                Element element = (Element) firstNode;
                String uri = namespace.getURI();
                if ( name.equals( element.getName() )
                    && uri.equals( element.getNamespaceURI() ) ) {
                    return createSingleIterator( element );
                }
            }
            return EMPTY_ITERATOR;
        }
        else {
            return new ElementNameIterator(source.iterator(), name, namespace);
        }
    }
    
    public List getContent() {
        List backingList = getContentList();
        return new BackedList(this, backingList, backingList);
    }
    
    public void setContent(List contents) {
        this.contents = contents;
    }
    
    public void clearContent() {
        contents = null;
        firstNode = null;
    }
    
    
    // node navigation API - return content as nodes
    // such as Text etc.
    public Node getNode(int index) {
        if (contents == null) {
            return firstNode;
        }
        Object object = contents.get(index);
        if (object instanceof Node) {
            return (Node) object;
        }
        if (object instanceof String) {
            return new DefaultText((String) object);
        }
        return null;
    }
    
    public int getNodeCount() {
        if ( contents == null ) {
            return ( firstNode != null ) ? 1 : 0;
        }
        return contents.size();
    }
    
    public Iterator nodeIterator() {
        if ( contents == null ) {
            if ( firstNode != null ) {
                return createSingleIterator( firstNode );
            }
        }
        else {
            if (contents != null) {
                return contents.iterator();
            }
        }
        return EMPTY_ITERATOR;
    }

    
    public List getAttributes() {
        if ( attributes == null ) {
            attributes = createAttributeList();
        }
        return attributes;
    }
    
    public void setAttributes(List attributes) {
        this.attributes = attributes;
    }
    
    public Attribute getAttribute(String name, Namespace namespace) {
        if ( attributes != null ) {
            String uri = namespace.getURI();
            int size = attributes.size();
            for ( int i = 0; i < size; i++ ) {
                Attribute attribute = (Attribute) attributes.get(i);
                if ( name.equals( attribute.getName() )
                    && uri.equals( attribute.getNamespaceURI() ) ) {
                    childRemoved(attribute);
                    return attribute;
                }
            }
        }
        return null;
    }

    public boolean removeAttribute(String name, Namespace namespace) {
        if ( attributes != null ) {
            String uri = namespace.getURI();
            for ( Iterator iter = attributes.iterator(); iter.hasNext(); ) {
                Attribute attribute = (Attribute) iter.next();
                if ( name.equals( attribute.getName() )
                    && uri.equals( attribute.getNamespaceURI() ) ) {
                    iter.remove();
                    childRemoved(attribute);
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean remove(Attribute attribute) {
        if ( attributes == null ) {
            return false;
        }
        boolean answer = attributes.remove(attribute);
        if ( answer ) {
            childRemoved(attribute);
        }
        return answer;
    }
    

    /** A Factory Method pattern which lazily creates 
      * a List implementation used to store attributes
      */
    protected List createAttributeList() {
        return new ArrayList();
    }
    
    
    protected void addNode(Node node) {
        if (node.getParent() != null) {
            // XXX: could clone here
            String message = "The Node already has an existing parent of \"" 
                + node.getParent().getQualifiedName() + "\"";
            throw new IllegalAddException(this, node, message);
        }
        if (contents == null) {
            if ( firstNode == null ) {
                firstNode = node;
            }
            else {
                contents = createContentList();
                contents.add( firstNode );
                contents.add( node );
            }
        }
        else {
            contents.add(node);
        }        
        childAdded(node);
    }

    protected boolean removeNode(Node node) {
        boolean answer = false;
        if (contents == null) {
            if ( firstNode == node ) {
                firstNode = null;
                answer = true;
            }
        }
        else {
            answer = contents.remove(node);
        }
        if (answer) {
            childRemoved(node);
        }
        return answer;
    }

    // Implementation methods
    
    
    /** A Factory Method pattern which lazily creates 
      * a List implementation used to store content
      */
    protected List createContentList() {
        return new ArrayList();
    }
    
    /** A Factory Method pattern which creates 
      * a BackedList implementation used to store results of 
      * a filtered content query such as 
      * {@link #getProcessingInstructions} or
      * {@link #getElements} which changes are reflected in the content
      */
    protected BackedList createResultList() {
        return new BackedList( this, getContentList() );
    }
    
    /** A Factory Method pattern which creates 
      * a BackedList implementation which contains a single result
      */
    protected List createSingleResultList( Object result ) {
        BackedList list = new BackedList( this, getContentList(), 1 );
        list.addLocal( result );
        return list;
    }
    
    /** A Factory Method pattern which lazily creates an empty
      * a BackedList implementation
      */
    protected List createEmptyList() {
        return new BackedList( this, getContentList(), 0 );
    }
    
    protected List getContentList() {
        if ( contents == null ) {
            contents = createContentList();
            if ( firstNode != null ) {
                contents.add( firstNode );
            }
        }
        return contents;
    }

    protected Iterator createSingleIterator( Object result ) {
        return new SingleIterator( result );
    }
    
    /** @return the ID of the given <code>Element</code>
      */
    protected String getElementID(Element element) {
        // XXX: there will be other ways of finding the ID
        // XXX: should probably have an IDResolver or something
        return element.getAttributeValue( "id" );
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
