package org.dom4j;

import java.util.Iterator;
import java.util.List;

import junit.framework.*;
import junit.textui.TestRunner;

/** A test harness to test the parent relationship and use of the
  * {@link Node#asXPathNode} method.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TestParent extends AbstractTestCase {
    
    public TestParent(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testParentRelationship() throws Exception {        
        testParentRelationship( document.getRootElement() );
    }
        
    // JUnit stuff
    //-------------------------------------------------------------------------                    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestParent.class );
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void testParentRelationship( Element parent, List content ) {
        for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            if ( object instanceof Element ) {
                testParentRelationship( (Element) object );
            }
            testXPathNode( parent, (Node) object );
        }
    }
    
    protected void testParentRelationship( Element element ) {
        testParentRelationship( element, element.getAttributes() );
        testParentRelationship( element, element.getContent() );
    }    
    
    
    protected void testXPathNode( Element parent, Node node ) {
        if ( node.supportsParent() ) {
            assert( "getParent() returns parent", node.getParent() == parent );
        }
        else {
            // lets create an XPath node
            Node xpathNode = node.asXPathNode( parent );
            assert( "XPath Node supports parent", xpathNode.supportsParent() );
            assert( "getParent() returns parent", xpathNode.getParent() == parent );
        }
    }    
}