/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
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
 * @since <pre>01/05/2005</pre>
 * @version 1.0
 */
public class SimpleSingletonTest extends TestCase
{
    public SimpleSingletonTest(String name)
    {
        super(name);
    }

    private static SingletonStrategy singleton;
    private static Object reference;

    public void setUp() throws Exception
    {
        super.setUp();
        if (singleton==null) {
          singleton=new PerThreadSingleton();
          singleton.setSingletonClassName(HashMap.class.getName());
        }
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testFirstInstance() throws Exception
    {
        Map map = (Map)singleton.instance();
        String expected=null;
        String actual = (String)map.get("Test");
        assertEquals("testInstance",expected,actual);

        expected="new value";
        map.put("Test",expected);

        map = (Map)singleton.instance();
        reference=map;
        actual = (String)map.get("Test");
        assertEquals("testFirstInstance",expected,actual);
    }

    public void testSecondInstance() throws Exception
    {
        Map map = (Map)singleton.instance();
        assertEquals("testSecondInstance reference",reference,map);
        String actual = (String)map.get("Test");
        String expected = "new value";
        assertEquals("testInstance",expected,actual);
    }

    public static Test suite()
    {
        TestSuite suite = new TestSuite();
        suite.addTest(TestSuite.createTest(SimpleSingletonTest.class,"testFirstInstance"));
        suite.addTest(TestSuite.createTest(SimpleSingletonTest.class,"testSecondInstance"));
        return suite;
    }
}
