/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

/** <p><code>DefaultContentModel</code> is a default implementation of 
  * <code>ContentModel</code>.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultContentModel extends AbstractContentModel {

    protected static final List EMPTY_LIST = Collections.EMPTY_LIST;
    protected static final Iterator EMPTY_ITERATOR = EMPTY_LIST.iterator();
    
    /** Store the contents of the element as a lazily created <code>List</code> */
    private List contents;
    
    /** Cache the first Text node to delay creating the content list for 
      * elements with no other content other than a single text node */
    private Node firstNode;
    
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
        return null;
    }
   
    public Namespace getNamespaceForURI(String uri) {
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
        return null;
    }
    
    public List getAdditionalNamespaces(String defaultNamespaceURI) {
        List source = contents;
        if ( source == null ) {
            if ( firstNode instanceof Namespace ) {
                List answer = createResultList();
                Namespace namespace = (Namespace) firstNode;
                if ( ! defaultNamespaceURI.equals( namespace.getURI() ) ) {
                    return createSingleResultList( namespace );
                }
            }
            else {
                return EMPTY_LIST;
            }
        }
        List answer = createResultList();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof Namespace ) {
                Namespace namespace = (Namespace) object;
                if ( ! defaultNamespaceURI.equals( namespace.getURI() ) ) {
                    answer.add( namespace );
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
            return EMPTY_LIST;
        }
        List answer = createResultList();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof ProcessingInstruction ) {
                answer.add( object );
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
            return EMPTY_LIST;
        }
        List answer = createResultList();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof ProcessingInstruction ) {
                ProcessingInstruction pi = (ProcessingInstruction) object;
                if ( target.equals( pi.getName() ) ) {                  
                    answer.add( pi );
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
            return EMPTY_LIST;
        }
        List answer = createResultList();
        String uri = namespace.getURI();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof Element ) {
                Element element = (Element) object;
                if ( name.equals( element.getName() )
                    && uri.equals( element.getNamespaceURI() ) ) {
                    answer.add( element );
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
            return EMPTY_LIST;
        }
        List answer = createResultList();
        int size = source.size();
        for ( int i = 0; i < size; i++ ) {
            Object object = source.get(i);
            if ( object instanceof Element ) {
                answer.add( object );
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
        if (contents == null) {
            contents = createContentList();
            if ( firstNode != null ) {
                contents.add( firstNode );
            }
        }
        return contents;
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

    public void addNode(Node node) {
        if (contents == null) {
            if ( firstNode == null ) {
                firstNode = node;
                return;
            }
            else {
                contents = createContentList();
                contents.add( firstNode );
                contents.add(node);
            }
        }
        else {
            contents.add(node);
        }
    }

    public boolean removeNode(Node node) {
        if (contents == null) {
            if ( firstNode == node ) {
                firstNode = null;
                return true;
            }
            return false;
        }
        else {
            return contents.remove(node);
        }
    }

    // Implementation methods
    
    
    /** A Factory Method pattern which lazily creates 
      * a List implementation used to store content
      */
    protected List createContentList() {
        return new ArrayList();
    }
    
    /** A Factory Method pattern which lazily creates 
      * a List implementation used to store results of 
      * a filtered content query such as 
      * {@link #getProcessingInstructions} or
      * {@link #getElements}
      */
    protected List createResultList() {
        return new ArrayList();
    }
    
    /** A Factory Method pattern which lazily creates 
      * a List implementation which contains a single result
      */
    protected List createSingleResultList( Object result ) {
        ArrayList list = new ArrayList(1);
        list.add( result );
        return list;
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
