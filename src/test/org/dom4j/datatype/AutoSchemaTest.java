/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.datatype;

import junit.textui.TestRunner;

import java.math.BigInteger;
import java.util.Calendar;

import org.dom4j.DocumentFactory;
import org.dom4j.io.SAXReader;

/**
 * Test harness for the XML Schema Data Type integration. These tests use
 * auto-loading of the XML Schema document
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class AutoSchemaTest extends AbstractDataTypeTestCase {
    public static void main(String[] args) {
        TestRunner.run(AutoSchemaTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------
    public void testIntAttribute() throws Exception {
        testNodes("//person/@x", Integer.class);
    }

    public void testIntElement() throws Exception {
        testNodes("//person/salary", Integer.class);
    }

    public void testString() throws Exception {
        testNodes("//person/note", String.class);
    }

    public void testDate() throws Exception {
        testNodes("//person/@d", Calendar.class);
    }

    public void testDateTime() throws Exception {
        testNodes("//person/@dt", Calendar.class);
    }

    public void testInteger() throws Exception {
        testNodes("//person/@age", BigInteger.class);
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void setUp() throws Exception {
        super.setUp();

        DocumentFactory factory = loadDocumentFactory();
        SAXReader reader = new SAXReader(factory);
        document = getDocument(getDocumentURI(), reader);
    }

    protected String getDocumentURI() {
        return "/xml/test/schema/personal-schema.xml";
    }

    protected DocumentFactory loadDocumentFactory() throws Exception {
        return DatatypeDocumentFactory.getInstance();
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
