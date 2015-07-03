/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.util;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.Map;

/**
 * PerThreadSingleton Tester.
 * 
 * @author ddlucas
 * @since
 * 
 * <pre>
 * 01 / 05 / 2005
 * </pre>
 * 
 * @version 1.0
 */
public class SimpleSingletonTest extends TestCase {
    public SimpleSingletonTest(String name) {
        super(name);
    }

    private static SingletonStrategy singleton;

    private static Object reference;

    public void setUp() throws Exception {
        super.setUp();
        if (singleton == null) {
            singleton = new PerThreadSingleton();
            singleton.setSingletonClassName(HashMap.class.getName());
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFirstInstance() throws Exception {
        Map map = (Map) singleton.instance();
        String expected = null;
        String actual = (String) map.get("Test");
        assertEquals("testInstance", expected, actual);

        expected = "new value";
        map.put("Test", expected);

        map = (Map) singleton.instance();
        reference = map;
        actual = (String) map.get("Test");
        assertEquals("testFirstInstance", expected, actual);
    }

    public void testSecondInstance() throws Exception {
        Map map = (Map) singleton.instance();
        assertEquals("testSecondInstance reference", reference, map);
        String actual = (String) map.get("Test");
        String expected = "new value";
        assertEquals("testInstance", expected, actual);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(TestSuite.createTest(SimpleSingletonTest.class,
                "testFirstInstance"));
        suite.addTest(TestSuite.createTest(SimpleSingletonTest.class,
                "testSecondInstance"));
        return suite;
    }
}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */

