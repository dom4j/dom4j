package org.dom4j.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
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

    protected static final List EMTPY_LIST = Collections.EMPTY_LIST;
    protected static final Iterator EMPTY_ITERATOR = EMTPY_LIST.iterator();
    
    /** Store the contents of the element as a lazily created <code>List</code> */
    private List contents;
    
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
        return null;
    }
    
    public List getAdditionalNamespaces(String defaultNamespaceURI) {
        List answer = createResultList();
        List source = contents;
        if ( source != null ) {
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
        }
        return answer;
    }
    
    
    // Processing instruction API
    
    public List getProcessingInstructions() {
        List answer = createResultList();
        List source = contents;
        if ( source != null ) {
            int size = source.size();
            for ( int i = 0; i < size; i++ ) {
                Object object = source.get(i);
                if ( object instanceof ProcessingInstruction ) {
                    answer.add( object );
                }
            }
        }
        return answer;
    }
    
    public List getProcessingInstructions(String target) {
        List answer = createResultList();
        List source = contents;
        if ( source != null ) {
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
        }
        return answer;
    }
    
    public ProcessingInstruction getProcessingInstruction(String target) {
        List source = contents;
        if ( source != null ) {
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
        if ( source != null ) {
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
        if ( source != null ) {
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
        if ( source != null ) {
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
        List answer = createResultList();
        List source = contents;
        if ( source != null ) {
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
        }
        return answer;
    }
    
    public List getElements() {
        List answer = createResultList();
        List source = contents;
        if ( source != null ) {
            int size = source.size();
            for ( int i = 0; i < size; i++ ) {
                Object object = source.get(i);
                if ( object instanceof Element ) {
                    answer.add( object );
                }
            }
        }
        return answer;
    }
    
    public Iterator elementIterator() {
        if (contents == null) {
            return EMPTY_ITERATOR;
        }
        else {
            return new ElementIterator(contents.iterator());
        }
    }
        
    public Iterator elementIterator(String name, Namespace namespace) {
        if (contents == null) {
            return EMPTY_ITERATOR;
        }
        else {
            return new ElementNameIterator(contents.iterator(), name, namespace);
        }
    }
    
    public List getContent() {
        if (contents == null) {
            contents = createContentList();
        }
        return contents;
    }
    
    public void setContent(List contents) {
        this.contents = contents;
    }
    
    public void clearContent() {
        contents = null;
    }
    
    
    // node navigation API - return content as nodes
    // such as Text etc.
    public Node getNode(int index) {
        if (contents == null) {
            return null;
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
        return (contents != null) ? contents.size() : 0;
    }
    
    public Iterator nodeIterator() {
        return (contents != null) ? contents.iterator() : EMPTY_ITERATOR;
    }

    public void addNode(Node node) {
        if (contents == null) {
            contents = createContentList();
        }
        contents.add(node);
    }

    public boolean removeNode(Node node) {
        if (contents == null) {
            return false;
        }
        return contents.remove(node);
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
    
    /** @return the ID of the given <code>Element</code>
      */
    protected String getElementID(Element element) {
        // XXX: there will be other ways of finding the ID
        // XXX: should probably have an IDResolver or something
        return element.getAttributeValue( "id" );
    }
    
}
