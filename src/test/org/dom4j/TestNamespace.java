package org.dom4j;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import junit.framework.*;
import junit.textui.TestRunner;

import org.dom4j.io.SAXReader;

/** A test harness to test the use of Namespaces.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class TestNamespace extends AbstractTestCase {

    /** Input XML file to read */
    protected static String INPUT_XML_FILE = "xml/namespaces.xml";
    
    /** Namespace to use in tests */
    protected static Namespace XSL_NAMESPACE = ContentFactory.getNamespace( 
        "xsl", "http://www.w3.org/1999/XSL/Transform" 
    );
    
    
    public TestNamespace(String name) {
        super(name);
    }

    // Test case(s)
    //-------------------------------------------------------------------------                    
    public void debugShowNamespaces() throws Exception {
        Element root = getRootElement();
        
        for ( Iterator iter = root.elementIterator(); iter.hasNext(); ) {
            Element element = (Element) iter.next();
            
            log( "Found element:    " + element );
            log( "Namespace:        " + element.getNamespace() );
            log( "Namespace prefix: " + element.getNamespacePrefix() );
            log( "Namespace URI:    " + element.getNamespaceURI() );
        }
    }
        
    public void testGetElement() throws Exception {
        Element root = getRootElement();
        
        Element firstTemplate = root.getElement( "template", XSL_NAMESPACE );
        assert( "Root element contains at least one <xsl:template/> element", firstTemplate != null );
        
        log( "Found element: " + firstTemplate );
    }
        
    public void testGetElements() throws Exception {
        Element root = getRootElement();
        
        List list = root.getElements( "template", XSL_NAMESPACE );
        assert( "Root element contains at least one <xsl:template/> element", list.size() > 0 );
        
        log( "Found elements: " + list );
    }
        
    public void testElementIterator() throws Exception {
        Element root = getRootElement();
        Iterator iter = root.elementIterator( "template", XSL_NAMESPACE );
        assert( "Root element contains at least one <xsl:template/> element", iter.hasNext() );

        do {
            Element element = (Element) iter.next();
            log( "Found element: " + element );
        }
        while ( iter.hasNext() );
    }
        
    // JUnit stuff
    //-------------------------------------------------------------------------                    
    public static void main( String[] args ) {
        TestRunner.run( suite() );
    }
    
    public static Test suite() {
        return new TestSuite( TestNamespace.class );
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void setUp() throws Exception {
        SAXReader reader = new SAXReader();
        document = reader.read( new File( INPUT_XML_FILE ) );
    }

    /** @return the root element of the document */
    protected Element getRootElement() {
        Element root = document.getRootElement();
        assert( "Document has root element", root != null );
        return root;
    }
}