/*
 * TestInternalEntityDecl.java
 * JUnit based test
 *
 * Created on 1 maart 2004, 22:06
 */

package org.dom4j.dtd;

import junit.framework.*;

/**
 *
 * @author Maarten
 */
public class TestInternalEntityDecl extends TestCase {
    
    public TestInternalEntityDecl(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(TestInternalEntityDecl.class);
        return suite;
    }
    
    // TODO add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    public void testToString() {
        InternalEntityDecl decl1 = new InternalEntityDecl("name", "value");
        InternalEntityDecl decl2 = new InternalEntityDecl("%name", "value");
        
        assertEquals("<!ENTITY name \"value\">", decl1.toString());
        assertEquals("<!ENTITY % name \"value\">", decl2.toString());
    }
    
}
