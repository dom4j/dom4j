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
import junit.extensions.RepeatedTest;

import java.util.HashMap;
import java.util.Map;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

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
public class PerThreadSingletonTest extends TestCase {
    public PerThreadSingletonTest(String name) {
        super(name);
    }

    private static SingletonStrategy singleton;

    private static ThreadLocal reference = new ThreadLocal();

    public void setUp() throws Exception {
        super.setUp();
        synchronized (PerThreadSingletonTest.class) {
            if (singleton == null) {
                singleton = new PerThreadSingleton();
                singleton.setSingletonClassName(HashMap.class.getName());
            }
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void testInstance() throws Exception {
        String tid = Thread.currentThread().getName();
        Map map = (Map) singleton.instance();

        String expected = "new value";
        if (!map.containsKey(tid) && reference.get() != null) {
            System.out.println("tid=" + tid + " map=" + map);
            System.out.println("reference=" + reference);
            System.out.println("singleton=" + singleton);
            fail("created singleton more than once");
        } else {
            map.put(tid, expected);
            reference.set(map);
        }

        String actual = (String) map.get(tid);
        // System.out.println("tid="+tid+ " map="+map);
        assertEquals("testInstance", expected, actual);

        map = (Map) singleton.instance();
        expected = "new value";
        actual = (String) map.get(tid);
        // System.out.println("tid="+tid+ " map="+map);
        // System.out.println("reference="+reference);
        // System.out.println("singleton="+singleton);
        assertEquals("testInstance", expected, actual);
        assertEquals("testInstance reference", reference.get(), map);

    }

    /**
     * Assembles and returns a test suite.
     * 
     * @return The suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(makeRepeatedLoadTest(5, 100, "testInstance"));
        return suite;
    }

    /**
     * JUnit method to exercise test via threads and loops
     * 
     * @param users
     *            Number of users to simulate (i.e. Threads).
     * @param iterations
     *            Number of iterations per user ( repeat the test x times).
     * @param testMethod
     *            method to execute (testXXX).
     * 
     * @return A Junit test
     */
    protected static Test makeRepeatedLoadTest(int users, int iterations,
            String testMethod) {
        long maxElapsedTime = 1200 + (1000 * users * iterations);

        Test testCase = new PerThreadSingletonTest(testMethod);

        Test repeatedTest = new RepeatedTest(testCase, iterations);
        Test loadTest = new LoadTest(repeatedTest, users);
        Test timedTest = new TimedTest(loadTest, maxElapsedTime);

        return timedTest;
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

