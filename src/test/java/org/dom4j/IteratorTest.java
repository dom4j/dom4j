/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import junit.textui.TestRunner;

import java.util.Iterator;
import java.util.List;

/**
 * A test harness to test the Iterator API in DOM4J
 * 
 * @author <a href="mailto:jdoughty@jdoughty@cs.gmu.edu">Jonathan Doughty </a>
 * @version $Revision: 1.4 $
 */
public class IteratorTest extends AbstractTestCase {
    private static final int NUMELE = 10;

    protected Document iterDocument;

    public static void main(String[] args) {
        TestRunner.run(IteratorTest.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        iterDocument = DocumentHelper.createDocument();

        Element root = iterDocument.addElement("root");

        for (int i = 0; i < NUMELE; i++) {
            root.addElement("iterator test").addAttribute("instance",
                    Integer.toString(i));
        }
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testElementCount() throws Exception {
        Element root = iterDocument.getRootElement();
        assertTrue("Has root element", root != null);

        List elements = root.elements("iterator test");
        int elementSize = elements.size();
        assertTrue("Root has " + elementSize + " children", (elements != null)
                && (elementSize == NUMELE));
    }

    public void testPlainIteration() throws Exception {
        Element root = iterDocument.getRootElement();
        List elements = root.elements("iterator test");
        Iterator iter = root.elementIterator("iterator test");
        int elementSize = elements.size();

        int count = 0;

        for (; iter.hasNext();) {
            Element e = (Element) iter.next();
            assertEquals("instance " + e.attribute("instance").getValue()
                    + " equals " + count, e.attribute("instance").getValue(),
                    Integer.toString(count));
            count++;
        }

        assertTrue(elementSize + " elements iterated", count == elementSize);
    }

    public void testSkipAlternates() throws Exception {
        Element root = iterDocument.getRootElement();
        List elements = root.elements("iterator test");
        Iterator iter = root.elementIterator("iterator test");
        int elementSize = elements.size();
        int count = 0;

        for (; iter.hasNext();) {
            Element e = (Element) iter.next();
            assertEquals("instance " + e.attribute("instance").getValue()
                    + " equals " + (count * 2), e.attribute("instance")
                    .getValue(), Integer.toString(count * 2));
            iter.next();
            count++;
        }

        assertTrue((elementSize / 2) + " alternate elements iterated",
                count == (elementSize / 2));
    }

    public void testNoHasNext() throws Exception {
        Element root = iterDocument.getRootElement();
        List elements = root.elements("iterator test");
        Iterator iter = root.elementIterator("iterator test");
        int elementSize = elements.size();
        int count = 0;
        Element e = null;

        for (; count < elementSize;) {
            e = (Element) iter.next();
            assertEquals("instance " + e.attribute("instance").getValue()
                    + " equals " + count, e.attribute("instance").getValue(),
                    Integer.toString(count));
            System.out.println("instance " + e.attribute("instance").getValue()
                    + " equals " + count);
            count++;
        }

        try {
            e = (Element) iter.next();

            if (e != null) {
                // Real Iterators wouldn't get here
                assertTrue("no more elements,value instead is "
                        + e.attribute("instance").getValue(), e == null);
            }
        } catch (Exception exp) {
            assertTrue("Real iterators throw NoSuchElementException",
                    exp instanceof java.util.NoSuchElementException);
        }
    }

    public void testExtraHasNexts() throws Exception {
        Element root = iterDocument.getRootElement();
        List elements = root.elements("iterator test");
        Iterator iter = root.elementIterator("iterator test");
        int elementSize = elements.size();
        int count = 0;

        for (; iter.hasNext();) {
            Element e = (Element) iter.next();
            assertEquals("instance " + e.attribute("instance").getValue()
                    + " equals " + count, e.attribute("instance").getValue(),
                    Integer.toString(count));
            iter.hasNext();
            count++;
        }

        assertTrue(elementSize + " elements iterated with extra hasNexts",
                count == elementSize);
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
