package org.dom4j;

import java.util.Iterator;
import java.util.List;

import junit.framework.*;
import junit.textui.TestRunner;

/** A test harness to test XPath expression evaluation in DOM4J
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TestXPath extends AbstractTestCase {

    protected static boolean VERBOSE = true;
    
    protected static String[] paths = {
        "root",
        "text()",
        "//author",
        "//author[@location='UK']",
        "//@location"
    };
    
    
    public TestXPath(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void testXPaths() throws Exception {        
        int size = paths.length;
        for ( int i = 0; i < size; i++ ) {
            testXPath( paths[i] );
        }
    }
        
    // JUnit stuff
    //-------------------------------------------------------------------------                    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestXPath.class );
    }
    
    protected void testXPath(String xpath) {
        List list = document.selectNodes(xpath);
        
        System.out.println( "Searched path: " + xpath + " found: " + list.size() + " result(s)" );
        
        if ( VERBOSE ) {
            System.out.println( list );
        }
    }

}