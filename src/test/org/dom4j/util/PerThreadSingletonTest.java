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
 * @since <pre>01/05/2005</pre>
 * @version 1.0
 */
public class PerThreadSingletonTest extends TestCase
{
    public PerThreadSingletonTest(String name)
    {
        super(name);
    }

    private static SingletonStrategy singleton;
    private static ThreadLocal reference = new ThreadLocal();

    public void setUp() throws Exception
    {
        super.setUp();
        synchronized(PerThreadSingletonTest.class) {
            if (singleton==null) {
              singleton=new PerThreadSingleton();
              singleton.setSingletonClassName(HashMap.class.getName());
            }
        }
    }

    public void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testInstance() throws Exception
    {
        String tid = Thread.currentThread().getName();
        Map map = (Map)singleton.instance();

        String expected="new value";
        if (map.containsKey(tid)==false && reference.get()!=null) {
            System.out.println("tid="+tid+ " map="+map);
            System.out.println("reference="+reference);
            System.out.println("singleton="+singleton);
            fail("created singleton more than once");
        }
        else {
            map.put(tid,expected);
            reference.set(map);
        }

        String actual = (String)map.get(tid);
//        System.out.println("tid="+tid+ " map="+map);
        assertEquals("testInstance",expected,actual);

        map = (Map)singleton.instance();
        expected="new value";
        actual = (String)map.get(tid);
//        System.out.println("tid="+tid+ " map="+map);
//        System.out.println("reference="+reference);
//        System.out.println("singleton="+singleton);
        assertEquals("testInstance",expected,actual);
        assertEquals("testInstance reference",reference.get(),map);

    }

    /**
     * Assembles and returns a test suite.
     *
     * @return The  suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(makeRepeatedLoadTest(5, 100, "testInstance"));
        return suite;
    }

    /**
     * JUnit method to exercise test via threads and loops
     *
     * @param users Number of users to simulate (i.e. Threads).
     * @param iterations Number of iterations per user ( repeat the test x
     *        times).
     * @param testMethod method to execute (testXXX).
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
