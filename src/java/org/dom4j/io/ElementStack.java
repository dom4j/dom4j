package org.dom4j.io;

import java.util.ArrayList;

import org.dom4j.Element;

/** <p><code>ElementStack</code> is used internally inside the 
  * {@link SAXContentHandler} to maintain a stack of {@link Element} 
  * instances. 
  * It opens an integration possibility allowing derivations to prune the tree
  * when a node is complete.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class ElementStack {

    /** stack of <code>Element</code> objects */
    protected Element[] stack;
    
    /** index of the item at the top of the stack or -1 if the stack is empty */
    protected int lastElementIndex = -1;

    
    public ElementStack() {
        this(50);
    }
    
    public ElementStack(int defaultCapacity) {
        stack = new Element[defaultCapacity];
    }
    
    /** Peeks at the top element on the stack without changing the contents
      * of the stack.
      * 
      * @return the current element on the stack 
      */
    public void clear() {
        lastElementIndex = -1;
    }
    
    /** Peeks at the top element on the stack without changing the contents
      * of the stack.
      * 
      * @return the current element on the stack 
      */
    public Element peekElement() {
        if ( lastElementIndex < 0 ) { 
            return null;
        }
        return stack[ lastElementIndex ];
    }
    
    /** Pops the element off the stack
      *
      * @return the element that has just been popped off the stack 
      */
    public Element popElement() {
        if ( lastElementIndex < 0 ) { 
            return null;
        }
        return stack[ lastElementIndex-- ];
    }
    
    /** Pushes a new element onto the stack
      * 
      * @return pushes the new element onto the stack and add it to its parent
      * if there is one.
      */
    public void pushElement(Element element) {
        int length = stack.length;
        if ( ++lastElementIndex >= length ) {
            reallocate( length * 2 );
        }
        stack[lastElementIndex] = element;
    }
    
    /** Reallocates the stack to the given size
      */
    protected void reallocate( int size ) {
        Element[] oldStack = stack;
        stack = new Element[ size ];
        System.arraycopy( oldStack, 0, stack, 0, oldStack.length );
    }
}
