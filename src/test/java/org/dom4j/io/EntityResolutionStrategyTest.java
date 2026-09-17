/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import java.io.StringReader;

import org.dom4j.AbstractTestCase;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.testng.Assert;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * Tests the {@link EntityResolutionStrategy} of {@link SAXReader} against a
 * document that references an external entity stored next to it.
 */
public class EntityResolutionStrategyTest extends AbstractTestCase {
    private static final String XML_FILE = "/xml/entity/external.xml";

    /** Document whose DTD, stored next to it, declares its element and entity. */
    private static final String VALIDATED_XML_FILE = "/xml/entity/validated.xml";

    public void testIgnoreIsTheDefaultWhenNotValidating() {
        Assert.assertEquals(EntityResolutionStrategy.getDefault(false),
                EntityResolutionStrategy.IGNORE);
        Assert.assertEquals(new SAXReader().getEntityResolutionStrategy(),
                EntityResolutionStrategy.IGNORE);
        Assert.assertEquals(new SAXReader(false).getEntityResolutionStrategy(),
                EntityResolutionStrategy.IGNORE);
    }

    public void testDenyIsTheDefaultWhenValidating() {
        Assert.assertEquals(EntityResolutionStrategy.getDefault(true),
                EntityResolutionStrategy.DENY);
        Assert.assertEquals(new SAXReader(true).getEntityResolutionStrategy(),
                EntityResolutionStrategy.DENY);
        Assert.assertEquals(
                new SAXReader((XMLReader) null, true).getEntityResolutionStrategy(),
                EntityResolutionStrategy.DENY);
    }

    public void testValidatingReaderDeniesByDefault() throws Exception {
        try {
            readGreeting(new SAXReader(true), VALIDATED_XML_FILE);
            Assert.fail("a validating reader should deny the external DTD");
        } catch (DocumentException e) {
            Assert.assertTrue(e.getMessage().contains("greeting.dtd"),
                    e.getMessage());
            Assert.assertTrue(e.getMessage().contains("DENY"),
                    e.getMessage());
        }
    }

    public void testValidatingReaderWithAllow() throws Exception {
        SAXReader reader = new SAXReader(true);
        reader.setEntityResolutionStrategy(EntityResolutionStrategy.ALLOW);

        Assert.assertEquals(readGreeting(reader, VALIDATED_XML_FILE),
                "Hello, world!");
    }

    public void testExplicitStrategyOverridesTheValidatingDefault() {
        SAXReader reader = new SAXReader(true);
        reader.setEntityResolutionStrategy(EntityResolutionStrategy.IGNORE);
        Assert.assertEquals(reader.getEntityResolutionStrategy(),
                EntityResolutionStrategy.IGNORE);
    }

    public void testIgnore() throws Exception {
        SAXReader reader = new SAXReader();
        reader.setEntityResolutionStrategy(EntityResolutionStrategy.IGNORE);

        Assert.assertEquals(readGreeting(reader), "Hello, !");
    }

    public void testAllow() throws Exception {
        SAXReader reader = new SAXReader();
        reader.setEntityResolutionStrategy(EntityResolutionStrategy.ALLOW);

        Assert.assertEquals(readGreeting(reader), "Hello, world!");
    }

    public void testDeny() throws Exception {
        SAXReader reader = new SAXReader();
        reader.setEntityResolutionStrategy(EntityResolutionStrategy.DENY);

        try {
            readGreeting(reader);
            Assert.fail("DENY should have failed the parse");
        } catch (DocumentException e) {
            Assert.assertTrue(e.getMessage().contains("name.txt"),
                    e.getMessage());
        }
    }

    public void testCreateDefaultIsEquivalentToConstructor() throws Exception {
        Assert.assertEquals(readGreeting(SAXReader.createDefault()), "Hello, !");
    }

    public void testExplicitResolverWinsOverStrategy() throws Exception {
        SAXReader reader = new SAXReader();
        reader.setEntityResolutionStrategy(EntityResolutionStrategy.DENY);
        reader.setEntityResolver((publicId, systemId) -> new InputSource(
                new StringReader("there")));

        Assert.assertEquals(readGreeting(reader), "Hello, there!");
    }

    public void testStrategyAppliesToEveryRead() throws Exception {
        SAXReader reader = new SAXReader();
        Assert.assertEquals(readGreeting(reader), "Hello, !");

        reader.setEntityResolutionStrategy(EntityResolutionStrategy.ALLOW);
        Assert.assertEquals(readGreeting(reader), "Hello, world!");
        Assert.assertNull(reader.getEntityResolver());
    }

    public void testNullStrategyIsRejected() {
        try {
            new SAXReader().setEntityResolutionStrategy(null);
            Assert.fail("null strategy should be rejected");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    public void testDefaultFromSystemProperty() {
        String property = EntityResolutionStrategy.SYSTEM_PROPERTY;
        String old = System.getProperty(property);

        try {
            System.setProperty(property, "allow");
            Assert.assertEquals(EntityResolutionStrategy.getDefault(false),
                    EntityResolutionStrategy.ALLOW);
            Assert.assertEquals(EntityResolutionStrategy.getDefault(true),
                    EntityResolutionStrategy.ALLOW);
            Assert.assertEquals(new SAXReader().getEntityResolutionStrategy(),
                    EntityResolutionStrategy.ALLOW);
            Assert.assertEquals(new SAXReader(true).getEntityResolutionStrategy(),
                    EntityResolutionStrategy.ALLOW);

            System.setProperty(property, " Deny ");
            Assert.assertEquals(EntityResolutionStrategy.getDefault(false),
                    EntityResolutionStrategy.DENY);

            System.setProperty(property, "not a strategy");
            Assert.assertEquals(EntityResolutionStrategy.getDefault(false),
                    EntityResolutionStrategy.IGNORE);
            Assert.assertEquals(EntityResolutionStrategy.getDefault(true),
                    EntityResolutionStrategy.DENY);
        } finally {
            if (old == null) {
                System.clearProperty(property);
            } else {
                System.setProperty(property, old);
            }
        }
    }

    private String readGreeting(SAXReader reader) throws Exception {
        return readGreeting(reader, XML_FILE);
    }

    private String readGreeting(SAXReader reader, String path)
            throws Exception {
        Document document = getDocument(path, reader);

        return document.getRootElement().getText();
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
