/*
 * TestExternalEntityDecl.java
 * JUnit based test
 *
 * Created on 1 maart 2004, 22:13
 */

package org.dom4j.dtd;

import junit.framework.*;

/**
 *
 * @author Maarten
 */
public class TestExternalEntityDecl extends TestCase {
    
    public TestExternalEntityDecl(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(TestExternalEntityDecl.class);
        return suite;
    }
    
    // TODO add test methods here, they have to start with 'test' name.
    // for example:
    // public void testHello() {}
    public void testToString() {
        ExternalEntityDecl decl1 = new ExternalEntityDecl("name", null, "systemID");
        ExternalEntityDecl decl2 = new ExternalEntityDecl("%name", null, "systemID");
        
        assertEquals("<!ENTITY name SYSTEM \"systemID\" >", decl1.toString());
        assertEquals("<!ENTITY % name SYSTEM \"systemID\" >", decl2.toString());
    }

    
}
