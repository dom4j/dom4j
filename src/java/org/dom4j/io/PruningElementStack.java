package org.dom4j.io;

import java.util.ArrayList;

import org.dom4j.Element;
import org.dom4j.ElementHandler;

/** <p><code>PruningElementStack</code> is a stack of {@link Element} 
  * instances which will prune the tree when a path expression is reached. 
  * This is useful for parsing very large documents where children of the
  * root element can be processed individually rather than keeping them all
  * in memory at the same time.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class PruningElementStack extends ElementStack {

    /** ElementHandler to call when pruning occurs */
    private ElementHandler elementHandler;
 
    /** the element name path which denotes the node to remove from its parent
      * when it is complete (i.e. when it is popped from the stack).
      * The first entry in the path will be a child of the root node
      */
    private String[] path;

    /** The level at which a path match can occur. 
      * We match when we have popped the selected node so the 
      * and the lastElementIndex points to its parent so this
      * value should be path.length - 2
      */
    private int matchingElementIndex;
    
    
    
    public PruningElementStack(String[] path, ElementHandler elementHandler) {
        this.path = path;
        this.elementHandler = elementHandler;
        checkPath();
    }
    
    public PruningElementStack(String[] path, ElementHandler elementHandler, int defaultCapacity) {
        super(defaultCapacity);
        this.path = path;
        this.elementHandler = elementHandler;
        checkPath();
    }
    
    public Element popElement() {
        Element answer = super.popElement();
        
        if ( lastElementIndex == matchingElementIndex && lastElementIndex >= 0 ) {
            // we are popping the correct level in the tree
            // lets check if the path fits
            //
            // NOTE: this is an inefficient way of doing it - we could 
            // maintain a history of which parts matched?
            if ( validElement( answer, lastElementIndex + 1 ) ) {
                Element parent = null;
                for ( int i = 0; i <= lastElementIndex; i++ ) {
                    parent = stack[i];
                    if ( ! validElement( parent, i ) ) {
                        parent = null;
                        break;
                    }
                }
                if ( parent != null ) {
                    pathMatches(parent, answer);
                }
            }
        }
        return answer;
    }
    
    protected void pathMatches(Element parent, Element selectedNode) {
        //System.out.println( "Matched: " + selectedNode + " about to call handler" );
        
        elementHandler.handle( selectedNode );
        
        //System.out.println( "Pruning: removing " + selectedNode + " from parent: " + parent );
        parent.remove( selectedNode );
    }
    
    protected boolean validElement(Element element, int index) {
        String requiredName = path[index];
        String name = element.getName();
        if (requiredName == name) { 
            return true;
        }
        if (requiredName != null && name != null ) {
            return requiredName.equals( name );
        }
        return false;
    }
    
    
    private void checkPath() {
        if ( path.length < 2 ) {
            throw new RuntimeException( "Invalid path of length: " + path.length + " it must be greater than 2" );
        }
        matchingElementIndex = path.length - 2;
    }
}
