package org.dom4j;

/** <p><code>IllegalAddNodeException</code> is thrown when a node
  * is added incorrectly to an <code>{@link Element}</code></p>
  *
  * @version $Revision$
  */
public class IllegalAddNodeException extends IllegalArgumentException {

    public IllegalAddNodeException(String reason) {
        super(reason);
    }
    
    public IllegalAddNodeException(Element parent, Node node, String reason) {
        super( "The node \"" + node.toString() 
            + "\" could not be added to the element \"" 
            + parent.getQualifiedName() + "\" because: " + reason 
        );
    }
    
    public IllegalAddNodeException(Branch parent, Node node, String reason) {
        super( "The node \"" + node.toString() 
            + "\" could not be added to the branch \"" 
            + parent.getName() + "\" because: " + reason 
        );
    }
}
