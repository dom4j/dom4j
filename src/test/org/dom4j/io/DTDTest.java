/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import junit.framework.AssertionFailedError;

import junit.textui.TestRunner;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.AbstractTestCase;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.dtd.AttributeDecl;
import org.dom4j.dtd.ElementDecl;
import org.dom4j.dtd.ExternalEntityDecl;
import org.dom4j.dtd.InternalEntityDecl;
import org.dom4j.tree.DefaultDocumentType;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Tests the DocType functionality.
 * 
 * <p>
 * Incorporated additional test cases for optional processing of the internal
 * and external DTD subsets. The "external" and "mixed" tests both <strong>fail
 * </strong> due to a reported bug. See http://tinyurl.com/4dzyq
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class DTDTest extends AbstractTestCase {
    /**
     * Input XML file to read <code>xml/dtd/internal.xml</code>- document
     * using internal DTD subset, but no external DTD subset.
     */
    private static final String XML_INTERNAL_FILE = "xml/dtd/internal.xml";

    /**
     * Input XML file to read <code>xml/dtd/external.xml</code>- document
     * using external DTD subset, but no internal DTD subset. The external
     * entity should be locatable by either PUBLIC or SYSTEM identifier. The
     * testing harness should use an appropriate EntityResolver to locate the
     * external entity as a local resource (no internet access).
     */
    private static final String XML_EXTERNAL_FILE = "xml/dtd/external.xml";

    /**
     * Input XML file to read <code>xml/dtd/mixed.xml</code>- document using
     * both an internal and an external DTD subset. The external entity should
     * be locatable by either PUBLIC or SYSTEM identifier. The testing harness
     * should use an appropriate EntityResolver to locate the external entity as
     * a local resource (no internet access).
     */
    private static final String XML_MIXED = "xml/dtd/mixed.xml";

    /**
     * Input XML file to for {@linkEntityResolver}
     * <code>xml/dtd/sample.dtd</code>- the external entity providing the
     * external DTD subset for test cases that need one. The SYSTEM identifier
     * for this external entity is given by {@link#DTD_SYSTEM_ID}.
     */
    private static final String DTD_FILE = "xml/dtd/sample.dtd";

    /**
     * The PUBLIC identifier, which is <code>-//dom4j//DTD sample</code>, for
     * the external entity providing DTD for tests.
     */
    protected static final String DTD_PUBLICID = "-//dom4j//DTD sample";

    /**
     * The SYSTEM identifier, which is <code>sample.dtd</code>, for the
     * external entity providing DTD for tests.
     */
    protected static final String DTD_SYSTEM_ID = "sample.dtd";

    public static void main(String[] args) {
        TestRunner.run(DTDTest.class);
    }

    // Test case(s)
    // -------------------------------------------------------------------------

    /**
     * Test verifies correct identification of the internal DTD subset and
     * correct non-presence of the external DTD subset.
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void testInternalDTDSubset() throws Exception {
        /*
         * Setup the expected DocumentType.
         * 
         * @todo dom4j should expose a DefaultDocumentType constructor that
         * accepts only the elementName property. This is used when only an
         * internal DTD subset is being provided via the <!DOCTYPE foo [...]>
         * syntax, in which case there is neither a SYSTEM nor PUBLIC
         * identifier.
         */
        DocumentType expected = new DefaultDocumentType();

        expected.setElementName("greeting");

        expected.setInternalDeclarations(getInternalDeclarations());

        /*
         * Parse the test XML document and compare the expected and actual
         * DOCTYPEs.
         */
        try {
            assertSameDocumentType(expected, readDocument(
                    XML_INTERNAL_FILE, true, false).getDocType());
        } catch (AssertionFailedError ex) {
            throw ex;
        } catch (Throwable t) {
            fail("Not expecting: " + t);
        }
    }

    /**
     * Test verifies correct identification of the external DTD subset and
     * correct non-presence of the internal DTD subset.
     */
    public void testExternalDTDSubset() {
        /*
         * Setup the expected DocumentType.
         */
        DocumentType expected = new DefaultDocumentType("another-greeting",
                null, DTD_SYSTEM_ID);

        expected.setExternalDeclarations(getExternalDeclarations());

        /*
         * Parse the test XML document and compare the expected and actual
         * DOCTYPEs.
         */
        try {
            assertSameDocumentType(expected, readDocument(
                    XML_EXTERNAL_FILE, false, true).getDocType());
        } catch (AssertionFailedError ex) {
            throw ex;
        } catch (Throwable t) {
            fail("Not expecting: " + t);
        }
    }

    /**
     * Test verifies correct identification of the internal and external DTD
     * subsets.
     */
    public void testMixedDTDSubset() {
        /*
         * Setup the expected DocumentType.
         */
        DocumentType expected = new DefaultDocumentType("another-greeting",
                null, DTD_SYSTEM_ID);

        expected.setInternalDeclarations(getInternalDeclarations());

        expected.setExternalDeclarations(getExternalDeclarations());

        /*
         * Parse the test XML document and compare the expected and actual
         * DOCTYPEs.
         */
        try {
            assertSameDocumentType(expected, readDocument(XML_MIXED,
                    true, true).getDocType());
        } catch (AssertionFailedError ex) {
            throw ex;
        } catch (Throwable t) {
            fail("Not expecting: " + t);
        }
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * Test helper method returns a {@link List}of DTD declarations that
     * represents the expected internal DTD subset (for the tests that use an
     * internal DTD subset).
     * 
     * <p>
     * Note: The declarations returned by this method MUST agree those actually
     * declared in {@link #XML_INTERNAL_FILE}and {@link
     * #XML_MIXED}.
     * </p>
     * 
     * <p>
     * </p>
     * 
     * @return DOCUMENT ME!
     */
    protected List getInternalDeclarations() {
        List decls = new ArrayList();

        decls.add(new ElementDecl("greeting", "(#PCDATA)"));

        decls.add(new AttributeDecl("greeting", "foo", "ID", "#IMPLIED", null));

        decls.add(new InternalEntityDecl("%boolean", "( true | false )"));

        return decls;
    }

    /**
     * Test helper method returns a {@link List}of DTD declarations that
     * represents the expected external DTD subset (for the tests that use an
     * external DTD subset).
     * 
     * @return DOCUMENT ME!
     */
    protected List getExternalDeclarations() {
        List decls = new ArrayList();

        decls.add(new ElementDecl("another-greeting", "(#PCDATA)"));

        return decls;
    }

    /**
     * Test helper method compares the expected and actual {@link DocumentType}
     * objects, including their internal and external DTD subsets.
     * 
     * <p>
     * </p>
     * 
     * @param expected
     *            DOCUMENT ME!
     * @param actual
     *            DOCUMENT ME!
     */
    protected void assertSameDocumentType(DocumentType expected,
            DocumentType actual) {
        /*
         * Nothing expected?
         */
        if (expected == null) {
            if (actual == null) {
                return; // Nothing found.
            } else {
                fail("Not expecting DOCTYPE.");
            }
        } else {
            /*
             * Something expected.
             */
            if (actual == null) {
                fail("Expecting DOCTYPE");
            }

            log("Expected DocumentType:\n" + expected.toString());

            log("Actual DocumentType:\n" + actual.toString());

            // Check the internal DTD subset.
            assertSameDTDSubset("Internal", expected.getInternalDeclarations(),
                    actual.getInternalDeclarations());

            // Check the external DTD subset.
            assertSameDTDSubset("External", expected.getExternalDeclarations(),
                    actual.getExternalDeclarations());
        }
    }

    /**
     * Test helper method compares an expected set of DTD declarations with an
     * actual set of DTD declarations. This method should be invoked seperately
     * for the internal DTD subset and the external DTD subset. The declarations
     * must occur in their logical ordering. See <a
     * href="http://tinyurl.com/5jhd8">Lexical Handler </a> for conformance
     * criteria.
     * 
     * @param txt
     *            DOCUMENT ME!
     * @param expected
     *            DOCUMENT ME!
     * @param actual
     *            DOCUMENT ME!
     * 
     * @throws AssertionError
     *             DOCUMENT ME!
     */
    protected void assertSameDTDSubset(String txt, List expected, List actual) {
        /*
         * Nothing expected?
         */
        if (expected == null) {
            if (actual == null) {
                return; // Nothing found.
            } else {
                fail("Not expecting " + txt + " DTD subset.");
            }
        } else {
            /*
             * Something expected.
             */
            if (actual == null) {
                fail("Expecting " + txt + " DTD subset.");
            }

            /*
             * Correct #of declarations found?
             */
            assertEquals(txt + " DTD subset has correct #of declarations"
                    + ": expected=[" + expected.toString() + "]" + ", actual=["
                    + actual.toString() + "]", expected.size(), actual.size());

            /*
             * Check order, type, and values of each declaration. Serialization
             * tests are done separately.
             */
            Iterator itr1 = expected.iterator();

            Iterator itr2 = actual.iterator();

            while (itr1.hasNext()) {
                Object obj1 = itr1.next();

                Object obj2 = itr2.next();

                assertEquals(txt + " DTD subset: Same type of declaration",
                        obj1.getClass().getName(), obj2.getClass().getName());

                if (obj1 instanceof AttributeDecl) {
                    assertSameDecl((AttributeDecl) obj1, (AttributeDecl) obj2);
                } else if (obj1 instanceof ElementDecl) {
                    assertSameDecl((ElementDecl) obj1, (ElementDecl) obj2);
                } else if (obj1 instanceof InternalEntityDecl) {
                    assertSameDecl((InternalEntityDecl) obj1,
                            (InternalEntityDecl) obj2);
                } else if (obj1 instanceof ExternalEntityDecl) {
                    assertSameDecl((ExternalEntityDecl) obj1,
                            (ExternalEntityDecl) obj2);
                } else {
                    throw new AssertionError("Unexpected declaration type: "
                            + obj1.getClass());
                }
            }
        }
    }

    /**
     * Test helper method compares an expected and an actual {@link
     * AttributeDecl}.
     * 
     * @param expected
     *            DOCUMENT ME!
     * @param actual
     *            DOCUMENT ME!
     */
    public void assertSameDecl(AttributeDecl expected, AttributeDecl actual) {
        assertEquals("attributeName is correct", expected.getAttributeName(),
                actual.getAttributeName());

        assertEquals("elementName is correct", expected.getElementName(),
                actual.getElementName());

        assertEquals("type is correct", expected.getType(), actual.getType());

        assertEquals("value is not correct", expected.getValue(), actual
                .getValue());

        assertEquals("valueDefault is correct", expected.getValueDefault(),
                actual.getValueDefault());

        assertEquals("toString() is correct", expected.toString(), actual
                .toString());
    }

    /**
     * Test helper method compares an expected and an actual {@link
     * ElementDecl}.
     * 
     * @param expected
     *            DOCUMENT ME!
     * @param actual
     *            DOCUMENT ME!
     */
    protected void assertSameDecl(ElementDecl expected, ElementDecl actual) {
        assertEquals("name is correct", expected.getName(), actual.getName());

        assertEquals("model is not correct", expected.getModel(), actual
                .getModel());

        assertEquals("toString() is correct", expected.toString(), actual
                .toString());
    }

    /**
     * Test helper method compares an expected and an actual {@link
     * InternalEntityDecl}.
     * 
     * @param expected
     *            DOCUMENT ME!
     * @param actual
     *            DOCUMENT ME!
     */
    protected void assertSameDecl(InternalEntityDecl expected,
            InternalEntityDecl actual) {
        assertEquals("name is correct", expected.getName(), actual.getName());

        assertEquals("value is not correct", expected.getValue(), actual
                .getValue());

        assertEquals("toString() is correct", expected.toString(), actual
                .toString());
    }

    /**
     * Test helper method compares an expected and an actual {@link
     * ExternalEntityDecl}.
     * 
     * @param expected
     *            DOCUMENT ME!
     * @param actual
     *            DOCUMENT ME!
     */
    protected void assertSameDecl(ExternalEntityDecl expected,
            ExternalEntityDecl actual) {
        assertEquals("name is correct", expected.getName(), actual.getName());

        assertEquals("publicID is correct", expected.getPublicID(), actual
                .getPublicID());

        assertEquals("systemID is correct", expected.getSystemID(), actual
                .getSystemID());

        assertEquals("toString() is correct", expected.toString(), actual
                .toString());
    }

    /**
     * Helper method reads a local resource and parses it as an XML document.
     * The internal and external DTD subsets are optionally retained by the
     * parser and exposed via the {@link DocumentType}object on the returned
     * {@link Document}. The parser is configured with an {@link
     * EntityResolver}that knows how to find the local resource identified by
     * {@link #DTD_FILE}whose SYSTEM identifier is given by {@link
     * #DTD_SYSTEM_ID}.
     * 
     * @param resourceName
     *            DOCUMENT ME!
     * @param includeInternal
     *            DOCUMENT ME!
     * @param includeExternal
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     * 
     * @throws Exception
     *             DOCUMENT ME!
     */
    protected Document readDocument(String resourceName,
            boolean includeInternal, boolean includeExternal) throws Exception {
        SAXReader reader = new SAXReader();

        reader.setIncludeInternalDTDDeclarations(includeInternal);

        reader.setIncludeExternalDTDDeclarations(includeExternal);

        reader.setEntityResolver(new MyEntityResolver(DTD_FILE,
                DTD_PUBLICID, DTD_SYSTEM_ID));

        return getDocument(resourceName, reader);
    }

    /**
     * Provides a resolver for the local test DTD resource.
     */
    protected static class MyEntityResolver implements EntityResolver {
        private String resourceName;

        private String pubId;

        private String sysId;

        public MyEntityResolver(String localResourceName, String publicId,
                String systemId) {
            resourceName = localResourceName;

            sysId = systemId;
        }

        public InputSource resolveEntity(String publicId, String systemId)
                throws SAXException, IOException {
            if (pubId != null) {
                if (pubId.equals(publicId)) {
                    return new InputSource(getInputStream(resourceName));
                }
            }

            if (sysId.equals(systemId)) {
                return new InputSource(getInputStream(resourceName));
            } else {
                return null;
            }
        }

        /**
         * Returns an {@link InputStream}that will read from the indicated
         * local resource.
         * 
         * @param localResourceName
         *            DOCUMENT ME!
         * 
         * @return DOCUMENT ME!
         * 
         * @throws IOException
         *             DOCUMENT ME!
         */
        protected InputStream getInputStream(String localResourceName)
                throws IOException {
            InputStream is = new FileInputStream(localResourceName);

            return is;
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
