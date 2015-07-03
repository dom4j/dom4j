/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import junit.textui.TestRunner;

/**
 * A test harness to test the addNode() methods on node
 * 
 * @author Philippe Ombredanne
 */
public class AddNodeTest extends AbstractTestCase {
    public static void main(String[] args) {
        TestRunner.run(AddNodeTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testDom4jAddNodeClone() {
        Document maindoc = DocumentHelper.createDocument();
        Element docroot = maindoc.addElement("document");
        Element header = docroot.addElement("header").addText("Some Text");

        Document subdoc = DocumentHelper.createDocument();
        Element docroot2 = subdoc.addElement("document");

        docroot2.add((Element) maindoc.selectSingleNode("/document/header")
                .clone());
        assertEquals(subdoc.asXML(), maindoc.asXML());
    }

    public void testDom4jAddNodeCreateCopy() {
        Document maindoc = DocumentHelper.createDocument();
        Element docroot = maindoc.addElement("document");
        Element header = docroot.addElement("header").addText("Some Text");

        Document subdoc = DocumentHelper.createDocument();
        Element docroot2 = subdoc.addElement("document");

        docroot2.add(((Element) maindoc.selectSingleNode("/document/header"))
                .createCopy());
        assertEquals(subdoc.asXML(), maindoc.asXML());
    }

    public void testDom4jAddNodeBug() {
        Document maindoc = DocumentHelper.createDocument();
        Element docroot = maindoc.addElement("document");
        Element header = docroot.addElement("header").addText("Some Text");

        Document subdoc = DocumentHelper.createDocument();
        Element subroot = subdoc.addElement("document");

        try {
            // add the header node from maindoc without clone... or createCopy
            subroot.add((Element) maindoc.selectSingleNode("/document/header"));
            fail();
        } catch (IllegalAddException e) {
            // the header already has a parent, this exception is normal
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
