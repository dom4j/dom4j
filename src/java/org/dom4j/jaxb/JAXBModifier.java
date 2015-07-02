/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.jaxb;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.ElementModifier;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXModifier;
import org.dom4j.io.XMLWriter;

import org.xml.sax.InputSource;

/**
 * Reads an XML document using SAX and writes its content to the provided
 * {@link org.dom4j.io.XMLWriter}. Modifications must be provided by {@link
 * org.dom4j.jaxb.JAXBObjectModifier} objects, which are called prior to writing
 * the XML fragment they are registered for.
 * 
 * @author Wonne Keysers (Realsoftware.be)
 * 
 * @see org.dom4j.io.SAXModifier
 */
public class JAXBModifier extends JAXBSupport {
    private SAXModifier modifier;

    private XMLWriter xmlWriter;

    private boolean pruneElements;

    private OutputFormat outputFormat;

    private HashMap modifiers = new HashMap();

    /**
     * Creates a new JAXBModifier for the given JAXB context path. This is the
     * Java package where JAXB can find the generated XML classes. This package
     * MUST contain jaxb.properties!
     * 
     * @param contextPath
     *            JAXB context path to be used
     * 
     * @see javax.xml.bind.JAXBContext
     */
    public JAXBModifier(String contextPath) {
        super(contextPath);
        this.outputFormat = new OutputFormat();
    }

    /**
     * Creates a new JAXBModifier for the given JAXB context path, using the
     * given {@link java.lang.ClassLoader}. This is the Java package where JAXB
     * can find the generated XML classes. This package MUST contain
     * jaxb.properties!
     * 
     * @param contextPath
     *            JAXB context path to be used
     * @param classloader
     *            the classloader to use
     * 
     * @see javax.xml.bind.JAXBContext
     */
    public JAXBModifier(String contextPath, ClassLoader classloader) {
        super(contextPath, classloader);
        this.outputFormat = new OutputFormat();
    }

    /**
     * Creates a new JAXBModifier for the given JAXB context path. The specified
     * {@link org.dom4j.io.OutputFormat}will be used while writing the XML
     * stream.
     * 
     * @param contextPath
     *            JAXB context path to be used
     * @param outputFormat
     *            the DOM4J {@link org.dom4j.io.OutputFormat}to be used
     * 
     * @see javax.xml.bind.JAXBContext
     */
    public JAXBModifier(String contextPath, OutputFormat outputFormat) {
        super(contextPath);
        this.outputFormat = outputFormat;
    }

    /**
     * Creates a new JAXBModifier for the given JAXB context path, using the
     * specified {@link java.lang.Classloader}. The specified {@link
     * org.dom4j.io.OutputFormat} will be used while writing the XML stream.
     * 
     * @param contextPath
     *            JAXB context path to be used
     * @param classloader
     *            the class loader to be used to load JAXB
     * @param outputFormat
     *            the DOM4J {@link org.dom4j.io.OutputFormat}to be used
     * 
     * @see javax.xml.bind.JAXBContext
     */
    public JAXBModifier(String contextPath, ClassLoader classloader,
            OutputFormat outputFormat) {
        super(contextPath, classloader);
        this.outputFormat = outputFormat;
    }

    /**
     * Parses the specified {@link java.io.File}with SAX
     * 
     * @param source
     *            the file to parse
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     * @throws IOException
     *             when an error occurs while writing to the {@link
     *             org.dom4j.io.XMLWriter}
     */
    public Document modify(File source) throws DocumentException, IOException {
        return installModifier().modify(source);
    }

    /**
     * Parses the specified {@link java.io.File}with SAX, using the given
     * {@link java.nio.charset.Charset}.
     * 
     * @param source
     *            the file to parse
     * @param charset
     *            the character set to use
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     * @throws IOException
     *             when an error occurs while writing to the {@link
     *             org.dom4j.io.XMLWriter}
     */
    public Document modify(File source, Charset charset)
            throws DocumentException, IOException {
        try {
            Reader reader = new InputStreamReader(new FileInputStream(source),
                    charset);

            return installModifier().modify(reader);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        } catch (FileNotFoundException ex) {
            throw new DocumentException(ex.getMessage(), ex);
        }
    }

    /**
     * Parses the specified {@link org.xml.sax.InputSource}with SAX.
     * 
     * @param source
     *            the input source to parse
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     * @throws IOException
     *             when an error occurs while writing to the {@link
     *             org.dom4j.io.XMLWriter}
     */
    public Document modify(InputSource source) throws DocumentException,
            IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the specified {@link java.io.InputStream}with SAX.
     * 
     * @param source
     *            the inputstream to parse
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     * @throws IOException
     *             when an error occurs while writing to the {@link
     *             org.dom4j.io.XMLWriter}
     */
    public Document modify(InputStream source) throws DocumentException,
            IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the specified {@link java.io.InputStream}with SAX.
     * 
     * @param source
     *            the inputstream to parse
     * @param systemId
     *            the URI of the given inputstream
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     * @throws IOException
     *             when an error occurs while writing to the {@link
     *             org.dom4j.io.XMLWriter}
     */
    public Document modify(InputStream source, String systemId)
            throws DocumentException, IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the specified {@link java.io.Reader}with SAX.
     * 
     * @param r
     *            the reader to use for parsing
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     * @throws IOException
     *             when an error occurs while writing to the {@link
     *             org.dom4j.io.XMLWriter}
     */
    public Document modify(Reader r) throws DocumentException, IOException {
        try {
            return installModifier().modify(r);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the specified {@link java.io.Reader}with SAX.
     * 
     * @param source
     *            the reader to parse
     * @param systemId
     *            the URI of the given reader
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     * @throws IOException
     *             when an error occurs while writing to the {@link
     *             org.dom4j.io.XMLWriter}
     */
    public Document modify(Reader source, String systemId)
            throws DocumentException, IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the the given URL or filename.
     * 
     * @param url
     *            the URL or filename to parse
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     * @throws IOException
     *             when an error occurs while writing to the {@link
     *             org.dom4j.io.XMLWriter}
     */
    public Document modify(String url) throws DocumentException, IOException {
        try {
            return installModifier().modify(url);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the the given URL.
     * 
     * @param source
     *            the URL to parse
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     * @throws IOException
     *             when an error occurs while writing to the {@link
     *             org.dom4j.io.XMLWriter}
     */
    public Document modify(URL source) throws DocumentException, IOException {
        try {
            return installModifier().modify(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Sets the Output to write the (modified) xml document to.
     * 
     * @param file
     *            the {@link java.io.File}to write to
     * 
     * @throws IOException
     *             when the file cannot be found or when the outputformat
     */
    public void setOutput(File file) throws IOException {
        createXMLWriter().setOutputStream(new FileOutputStream(file));
    }

    /**
     * Sets the Output to write the (modified) xml document to.
     * 
     * @param outputStream
     *            the {@link java.io.OutputStream}to write to
     * 
     * @throws IOException
     *             when an error occurs
     */
    public void setOutput(OutputStream outputStream) throws IOException {
        createXMLWriter().setOutputStream(outputStream);
    }

    /**
     * Sets the Output to write the (modified) xml document to.
     * 
     * @param writer
     *            the {@link java.io.Writer}to write to
     * 
     * @throws IOException
     *             when an error occurs
     */
    public void setOutput(Writer writer) throws IOException {
        createXMLWriter().setWriter(writer);
    }

    /**
     * Adds the {@link JAXBObjectModifier}to be called when the specified xml
     * path is encounted while parsing the source.
     * 
     * @param path
     *            the element path to listen for
     * @param mod
     *            the modifier to register
     */
    public void addObjectModifier(String path, JAXBObjectModifier mod) {
        modifiers.put(path, mod);
    }

    /**
     * Removes the {@link JAXBObjectModifier}from the event based processor,
     * for the specified element path.
     * 
     * @param path
     *            the xml path to remove the modifier for
     */
    public void removeObjectModifier(String path) {
        modifiers.remove(path);
        getModifier().removeModifier(path);
    }

    /**
     * Removes all registered {@link JAXBObjectModifier}instances from the
     * event based processor.
     */
    public void resetObjectModifiers() {
        modifiers.clear();
        getModifier().resetModifiers();
    }

    /**
     * Returns true when the modified {@link org.dom4j.Document}is not kept in
     * memory.
     * 
     * @return Returns true if elements are pruned.
     */
    public boolean isPruneElements() {
        return pruneElements;
    }

    /**
     * Define whether the modified {@link org.dom4j.Document}must only be
     * written to the output and pruned from the DOM4J tree.
     * 
     * @param pruneElements
     *            When true, elements will not be kept in memory
     */
    public void setPruneElements(boolean pruneElements) {
        this.pruneElements = pruneElements;
    }

    private SAXModifier installModifier() throws IOException {
        modifier = new SAXModifier(isPruneElements());

        modifier.resetModifiers();

        Iterator modifierIt = modifiers.entrySet().iterator();

        while (modifierIt.hasNext()) {
            Map.Entry entry = (Map.Entry) modifierIt.next();
            ElementModifier mod = new JAXBElementModifier(this,
                    (JAXBObjectModifier) entry.getValue());
            getModifier().addModifier((String) entry.getKey(), mod);
        }

        modifier.setXMLWriter(getXMLWriter());

        return modifier;
    }

    private SAXModifier getModifier() {
        if (this.modifier == null) {
            modifier = new SAXModifier(isPruneElements());
        }

        return modifier;
    }

    private XMLWriter getXMLWriter() {
        return xmlWriter;
    }

    private XMLWriter createXMLWriter() throws IOException {
        if (this.xmlWriter == null) {
            xmlWriter = new XMLWriter(outputFormat);
        }

        return xmlWriter;
    }

    private class JAXBElementModifier implements ElementModifier {
        private JAXBModifier jaxbModifier;

        private JAXBObjectModifier objectModifier;

        public JAXBElementModifier(JAXBModifier jaxbModifier,
                JAXBObjectModifier objectModifier) {
            this.jaxbModifier = jaxbModifier;
            this.objectModifier = objectModifier;
        }

        public org.dom4j.Element modifyElement(org.dom4j.Element element)
                throws Exception {
            javax.xml.bind.Element originalObject = jaxbModifier
                    .unmarshal(element);
            javax.xml.bind.Element modifiedObject = objectModifier
                    .modifyObject(originalObject);

            return jaxbModifier.marshal(modifiedObject);
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
