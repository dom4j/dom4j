/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import junit.textui.TestRunner;

import java.util.List;

/**
 * A test harness to test the copy() methods on Element
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class CopyTest extends AbstractTestCase {
    public static void main(String[] args) {
        TestRunner.run(CopyTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testRoot() throws Exception {
        document.setName("doc1");

        Element root = document.getRootElement();
        List authors = root.elements("author");

        assertTrue("Should be at least 2 authors", authors.size() == 2);

        Element author1 = (Element) authors.get(0);
        Element author2 = (Element) authors.get(1);

        testCopy(root);
        testCopy(author1);
        testCopy(author2);
    }

    protected void testCopy(Element element) throws Exception {
        assertTrue("Not null", element != null);

        int attributeCount = element.attributeCount();
        int nodeCount = element.nodeCount();

        Element copy = element.createCopy();

        assertEquals("Node size not equal after copy", element.nodeCount(),
                nodeCount);
        assertTrue("Same attribute size after copy",
                element.attributeCount() == attributeCount);

        assertTrue("Copy has same node size", copy.nodeCount() == nodeCount);
        assertTrue("Copy has same attribute size",
                copy.attributeCount() == attributeCount);

        for (int i = 0; i < attributeCount; i++) {
            Attribute attr1 = element.attribute(i);
            Attribute attr2 = copy.attribute(i);

            assertTrue("Attribute: " + i + " name is equal", attr1.getName()
                    .equals(attr2.getName()));
            assertTrue("Attribute: " + i + " value is equal", attr1.getValue()
                    .equals(attr2.getValue()));
        }

        for (int i = 0; i < nodeCount; i++) {
            Node node1 = element.node(i);
            Node node2 = copy.node(i);

            assertTrue("Node: " + i + " type is equal",
                    node1.getNodeType() == node2.getNodeType());
            assertTrue("Node: " + i + " value is equal", node1.getText()
                    .equals(node2.getText()));
        }
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
