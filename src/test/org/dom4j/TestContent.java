package org.dom4j;

import java.util.Iterator;
import java.util.List;

import junit.framework.*;
import junit.textui.TestRunner;

/** A test harness to test the content API in DOM4J
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TestContent extends AbstractTestCase {

    public TestContent(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testContent() throws Exception {
        Element root = document.getRootElement();
        assert( "Has root element", root != null );
        
        List authors = root.getElements( "author" );
        assert( "Root has children", authors != null && authors.size() == 2 );
        
        Element author1 = (Element) authors.get(0);
        Element author2 = (Element) authors.get(1);
        
        assert( "Author1 is James", author1.getAttributeValue( "name" ).equals( "James" ) );
        assert( "Author2 is Bob", author2.getAttributeValue( "name" ).equals( "Bob" ) );
        
        testGetAttributes(author1);
        testGetAttributes(author2);
    }
        
    public void testGetContents() throws Exception {
        Element root = document.getRootElement();
        assert( "Has root element", root != null );
        
        List content = root.getContent();
        assert( "Root has content", content != null && content.size() >= 2 );

        boolean iterated = false;
        for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            assert( "Content object is a node", object instanceof Node );
            iterated = true;
        }
        
        assert( "Iteration completed", iterated );
    }
    
    public void testGetNode() throws Exception {
        Element root = document.getRootElement();
        assert( "Has root element", root != null );
        
        int count = root.getNodeCount();
        assert( "Root has correct node count", count == 2 );
        
        boolean iterated = false;
        for ( int i = 0; i < count; i++ ) {
            Node node = root.getNode(i);
            assert( "Valid node returned from getNode()", node != null );
            iterated = true;
        }
        
        assert( "Iteration completed", iterated );
    }
        
    public void testGetXPathNode() throws Exception {
        Element root = document.getRootElement();
        assert( "Has root element", root != null );
        
        int count = root.getXPathNodeCount();
        assert( "Root has correct node count", count == 2 );
        
        boolean iterated = false;
        for ( int i = 0; i < count; i++ ) {
            Node node = root.getXPathNode(i);
            assert( "Valid node returned from getNode()", node != null );
            assert( "Node supports the parent relationship", node.supportsParent() );
            iterated = true;
        }
        
        assert( "Iteration completed", iterated );
    }
        
    // JUnit stuff
    //-------------------------------------------------------------------------                    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestContent.class );
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void testGetAttributes(Element author) throws Exception {
        
        String definedName = "name";
        String undefinedName = "undefined-attribute-name";
        String defaultValue = "** Default Value **";
        
        String value = author.getAttributeValue( definedName, defaultValue );
        assert( "Defined value doesn't return specified default value", value != defaultValue );
        
        value = author.getAttributeValue( undefinedName, defaultValue );        
        assert( "Undefined value returns specified default value", value == defaultValue );
    }
    
}