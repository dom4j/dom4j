/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 *
 * $Id$
 */

package org.dom4j;

import com.clarkware.junitperf.LoadTest;
import com.clarkware.junitperf.TimedTest;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.extensions.RepeatedTest;
import junit.framework.Test;
import junit.framework.TestSuite;


/** A test harness to test the dom4j package in a threaded environment
  *
  * @author <a href="mailto:ddlucas@lse.com">David Lucas</a>
  * @version $Revision$
  */
public class TestThreading extends AbstractTestCase {
    public TestThreading(String name) {
        super(name);
    }

    final static boolean debug=true;

    final static java.lang.ThreadLocal formatterCache = new java.lang.ThreadLocal();
    final static String seperator=" - ";
    final static FieldPosition FIELD_ZERO = new FieldPosition(0);

    private final static void preformat(StringBuffer strBuf, String context) {
        long now = System.currentTimeMillis();
        Date currentTime = new Date(now);
        SimpleDateFormat formatter = (SimpleDateFormat) formatterCache.get();
        if (formatter == null) {
            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS zzz");
            formatterCache.set(formatter);
        }
        strBuf.append("[");
        formatter.format(currentTime, strBuf, FIELD_ZERO);
        strBuf.append(" (").append(now).append(") ]");

        strBuf.append(seperator);
        strBuf.append(getThreadId());
        strBuf.append(seperator);
        strBuf.append(context);
        strBuf.append(seperator);
    }

    private static final String getThreadId() {
      String tid = Thread.currentThread().getName();
      return tid;
    }

    public static final void debug(String context, String msg ) {
      if (debug) {
        StringBuffer sb = new StringBuffer();
        preformat(sb,context);
        sb.append(msg);
        System.out.println(sb.toString());
      }
    }

    /**
     * static initializer
     */
    static {
    }

    /**
     *  The JUnit setup method
     */
    protected void setUp() { }


    /**
     *  The teardown method for JUnit
     */
    protected void tearDown() { }


    /**
     * This test combines many different types of operations on DOM4J in a
     * threaded environment.  If a problem occurs with threading, the tests will
     * fail.  This was used to help isolate an internal threading issue.
     * Unfortunately it may not always create the condition necessary to break
     * un-thread-safe code.  This is due to the nature of the machine, JVM, and
     * application and if the conditions are right.  Typically the problems of
     * multithreading occur due to an unprotected HashMap or ArrayList in a class
     * being used by more than one thread.  Also, most developers think that
     * their class or object instance will only be used by one thread.  But if
     * a factory or singleton caches a class or instance, it can quickly become
     * an unsafe environment.  Hence this test to assist in locating threading
     * issues.
     */
    public void testCombo() {
      int loop = 10;
      try {
        debug("testCombo", "beginning test...");

        long begin = System.currentTimeMillis();
        String value=null;
        String expected=null;
        String xml=null;
        Document doc=null;
        Element root=null;
        Element item=null;
        Element newItem=null;
        QName qn = null;
        Namespace ns = null;
        long now=0;

        xml = "<ROOT xmlns:t0=\"http://www.lse.com/t0\" >" +
                "  <ctx><type>Context</type></ctx>" +
                "  <A><B><C><D>This is a TEST</D></C></B></A>" +
                "  <t0:Signon><A>xyz</A><t0:Cust>customer</t0:Cust></t0:Signon>" +
                "</ROOT>";


        for (int i = 0; i < loop; i++) {
            doc = DocumentHelper.parseText(xml);

            root = doc.getRootElement();
            ns = Namespace.get("t0","http://www.lse.com/t0");
            qn = QName.get("Signon",ns);
            item = root.element(qn);
            value = item.asXML();
            expected="<t0:Signon xmlns:t0=\"http://www.lse.com/t0\"><A>xyz</A><t0:Cust>customer</t0:Cust></t0:Signon>";
            assertEquals("test t0:Signon ",expected,value);

            qn = root.getQName("Test");
            newItem = DocumentHelper.createElement(qn);
            now=System.currentTimeMillis();
            newItem.setText(String.valueOf(now));
            root.add(newItem);

            qn = root.getQName("Test2");
            newItem = DocumentHelper.createElement(qn);
            now=System.currentTimeMillis();
            newItem.setText(String.valueOf(now));
            root.add(newItem);

            item=root.element(qn);
            item.detach();
            item.setQName(qn);
            root.add(item);
            value = item.asXML();
            expected="<Test2>"+now+"</Test2>";
            assertEquals("test Test2 ",expected,value);

            qn = root.getQName("Test3");
            newItem = DocumentHelper.createElement(qn);
            now=System.currentTimeMillis();
            newItem.setText(String.valueOf(now));
            root.add(newItem);

            item=root.element(qn);
            item.detach();
            item.setQName(qn);
            root.add(item);
            value = item.asXML();
            expected="<Test3>"+now+"</Test3>";
            assertEquals("test Test3 ",expected,value);

            qn = item.getQName("Test4");
            newItem = DocumentHelper.createElement(qn);
            now=System.currentTimeMillis();
            newItem.setText(String.valueOf(now));
            root.add(newItem);

            item=root.element(qn);
            item.detach();
            item.setQName(qn);
            root.add(item);
            value = item.asXML();
            expected="<Test4>"+now+"</Test4>";
            assertEquals("test Test4 ",expected,value);
        }
        double duration = System.currentTimeMillis()-begin;
        double avg = duration / loop;
        debug("testCombo",
              "*** duration="+duration+" , loop="+loop+" , avg="+avg);
      }
      catch (Exception e) {
        e.printStackTrace();
        assertTrue("Exception in test: "+e.getMessage(),false);
      }
    }


    /**
     * This test isolates QNameCache in a multithreaded environment.
     */
    public void testQNameCache() {
      int loop = 100;
      try {
        debug("testQNameCache", "beginning test...");

        long begin = System.currentTimeMillis();
        String value=null;
        String expected=null;
        String xml=null;
        Document doc=null;
        Element root=null;
        Element item=null;
        Element newItem=null;
        QName qn = null;
        Namespace ns = null;
        long now=0;

        xml = "<ROOT xmlns:t0=\"http://www.lse.com/t0\" >" +
                "  <ctx><type>Context</type></ctx>" +
                "  <A><B><C><D>This is a TEST</D></C></B></A>" +
                "  <t0:Signon><A>xyz</A><t0:Cust>customer</t0:Cust></t0:Signon>" +
                "</ROOT>";


        for (int i = 0; i < loop; i++) {
            doc = DocumentHelper.parseText(xml);
            root = doc.getRootElement();

            qn=DocumentHelper.createQName("test");
            value = fetchValue(qn);
            expected="<test/>";
            assertEquals("test test ",expected,value);

            //creat it again
            qn=DocumentHelper.createQName("test");
            value = fetchValue(qn);
            expected="<test/>";
            assertEquals("test test again ",expected,value);

            qn=root.getQName("t0:Signon");
            value= fetchValue(qn);
            expected="<t0:Signon xmlns:t0=\"http://www.lse.com/t0\"/>";
            assertEquals("test t0:Signon ",expected,value);

        }
        double duration = System.currentTimeMillis()-begin;
        double avg = duration / loop;
        debug("testQNameCache",
              "*** duration="+duration+" , loop="+loop+" , avg="+avg);
      }
      catch (Exception e) {
        e.printStackTrace();
        assertTrue("Exception in test: "+e.getMessage(),false);
      }
    }


    /**
     * This method creates a value that can be expected during a test
     * @param qn
     * @return
     */
    public String fetchValue(QName qn) {
      String value=null;

      StringBuffer sb = new StringBuffer();
      sb.append("<");
      String prefix=qn.getNamespacePrefix();
      if (prefix!=null && prefix.length()>0) {
        sb.append(prefix).append(":");
      }
      sb.append(qn.getName());
      String uri=qn.getNamespaceURI();
      if (uri!=null && uri.length()>0) {
        sb.append(" xmlns");
        if (prefix!=null && prefix.length()>0){
          sb.append(":").append(prefix);
        }
        sb.append("=\"").append(uri).append("\"");
      }
      sb.append("/>");

      value=sb.toString();
      return value;
    }

    /**
     *  Assembles and returns a test suite.
     *
     *@return    The  suite
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(makeRepeatedLoadTest(5, 10, "testCombo"));
        suite.addTest(makeRepeatedLoadTest(5, 10, "testQNameCache"));
        return suite;
    }



    /**
     *  JUnit method to exercise test via threads and loops
     *
     *@param  users       Number of users to simulate (i.e. Threads).
     *@param  iterations  Number of iterations per user ( repeat the test x times).
     *@param  method      method to execute (testXXX).
     *@return             A Junit test
     */
    protected static Test makeRepeatedLoadTest(int users, int iterations, String testMethod) {
        long maxElapsedTime = 120000 + (1000 * users * iterations);

        Test testCase = new TestThreading(testMethod);

        Test repeatedTest = new RepeatedTest(testCase, iterations);
        Test loadTest = new LoadTest(repeatedTest, users);
        Test timedTest = new TimedTest(loadTest, maxElapsedTime);

        return timedTest;
    }

    /**
     *  The main program for the DOM4JThreadTest class
     *
     *@param  args  The command line arguments
     */
    public static void main(String[] args) {
        String[] testCaseName = {TestThreading.class.getName()};
        junit.textui.TestRunner.main(testCaseName);
    }


}




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
