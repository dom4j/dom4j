package org.dom4j;

import java.util.Iterator;
import java.util.List;

import junit.framework.*;
import junit.textui.TestRunner;

/** An abstract base class for some DOM4J test cases
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class AbstractTestCase extends TestCase {

    protected Document document;
    
    
    public AbstractTestCase(String name) {
        super(name);
    }

    // Implementation methods
    //-------------------------------------------------------------------------                    
    protected void setUp() throws Exception {
        document = DocumentFactory.create();
        
        Element root = document.addElement( "root" );
        Element author1 = root.addElement( "author" );
        author1.setAttributeValue( "name", "James" );
        author1.setAttributeValue( "location", "UK" );
        
        Element author2 = root.addElement( "author" );
        author2.setAttributeValue( "name", "Bob" );
        author2.setAttributeValue( "location", "Canada" );
    }

    protected void log(String text) {
        System.out.println(text);
    }

}