/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.rule;

import junit.textui.TestRunner;

import org.dom4j.AbstractTestCase;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.xpath.DefaultXPath;

/**
 * A test harness to test the use of the Stylesheet and the XSLT rule engine.
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class StylesheetTest extends AbstractTestCase {
    protected String[] templates = {
            "/",
            "*",
            "root",
            "author",
            "@name",
            "root/author",
            "author[@location='UK']",
            "root/author[@location='UK']",
            "root//author[@location='UK']"};

    protected String[] templates2 = {"/", "title", "para", "*"};

    protected Stylesheet stylesheet;

    public static void main(String[] args) {
        TestRunner.run(StylesheetTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testRules() throws Exception {
        for (int i = 0, size = templates.length; i < size; i++) {
            addTemplate(templates[i]);
        }

        log("");
        log("........................................");
        log("");
        log("Running stylesheet");

        stylesheet.run(document);

        log("Finished");
    }

    public void testLittleDoc() throws Exception {
        for (int i = 0, size = templates2.length; i < size; i++) {
            addTemplate(templates2[i]);
        }
        Document doc = getDocument("/xml/test/littledoc.xml");

        stylesheet = new Stylesheet();
        stylesheet.setValueOfAction(new Action() {
            public void run(Node node) {
                log("Default ValueOf action on node: " + node);
                log("........................................");
            }
        });

        stylesheet.run(doc);
    }

    public void testFireRuleForNode() throws Exception {
        final StringBuffer b = new StringBuffer();

        final Stylesheet s = new Stylesheet();
        Pattern pattern = DocumentHelper.createPattern("url");
        Action action = new Action() {
            public void run(Node node) throws Exception {
                b.append("url");
                s.applyTemplates(node);
            }
        };

        Rule r = new Rule(pattern, action);
        s.addRule(r);

        s.applyTemplates(document, new DefaultXPath("root/author/url"));

        assertEquals("Check url is processed twice", "urlurl", b.toString());
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void setUp() throws Exception {
        super.setUp();

        stylesheet = new Stylesheet();
        stylesheet.setValueOfAction(new Action() {
            public void run(Node node) {
                log("Default ValueOf action on node: " + node);
                log("........................................");
            }
        });
    }

    protected void addTemplate(final String match) {
        log("Adding template match: " + match);

        Pattern pattern = DocumentHelper.createPattern(match);

        log("Pattern: " + pattern);
        log("........................................");

        Action action = new Action() {
            public void run(Node node) throws Exception {
                log("Matched pattern: " + match);
                log("Node: " + node.asXML());
                log("........................................");

                // apply any child templates
                stylesheet.applyTemplates(node);
            }
        };

        Rule rule = new Rule(pattern, action);
        stylesheet.addRule(rule);
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
