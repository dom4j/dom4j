/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import junit.textui.TestRunner;

import java.util.Iterator;

import org.dom4j.io.SAXReader;

/**
 * A test harness for SAXReader option setMergeAdjacentText(true)
 * 
 * @author <a href="mailto:slehmann@novell.com">Steen Lehmann </a>
 * @version $Revision$
 */
public class MergeTextTest extends AbstractTestCase {
    /** Input XML file to read */
    private static final String INPUT_XML_FILE = "/xml/test/mergetext.xml";

    public static void main(String[] args) {
        TestRunner.run(MergeTextTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testNoAdjacentText() throws Exception {
        // After reading using SAXReader with mergeAdjacentText true,
        // no two Text objects should be adjacent to each other in the
        // document.
        SAXReader reader = new SAXReader();
        reader.setMergeAdjacentText(true);

        Document document = getDocument(INPUT_XML_FILE, reader);

        checkNoAdjacent(document.getRootElement());
        log("No adjacent Text nodes in " + document.asXML());
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    private void checkNoAdjacent(Element parent) {
        // Check that no two Text nodes are adjacent in the parent's content
        Node prev = null;
        Iterator iter = parent.nodeIterator();

        while (iter.hasNext()) {
            Node n = (Node) iter.next();

            if (n instanceof Text && ((prev != null) && prev instanceof Text)) {
                fail("Node: " + n + " is text and so is its "
                        + "preceding sibling: " + prev);
            } else if (n instanceof Element) {
                checkNoAdjacent((Element) n);
            }

            prev = n;
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
