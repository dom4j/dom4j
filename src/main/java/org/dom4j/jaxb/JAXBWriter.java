/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.jaxb;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import jakarta.xml.bind.JAXBException;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import org.xml.sax.SAXException;

/**
 * Writes {@link jakarta.xml.bind.Element}objects to an XML stream. {@link
 * jakarta.xml.bind.Element} instances can be created using the ObjectFactory that
 * is generated by the JAXB compiler.
 *
 * @author Wonne Keysers (Realsoftware.be)
 *
 * @see org.dom4j.io.XMLWriter
 * @see jakarta.xml.bind.JAXBContext
 */
public class JAXBWriter extends JAXBSupport {
    private XMLWriter xmlWriter;

    private OutputFormat outputFormat;

    /**
     * Creates a new JAXBWriter for the given JAXB context path. This is the
     * Java package where JAXB can find the generated XML classes. This package
     * MUST contain jaxb.properties!
     *
     * @param contextPath
     *            JAXB context path to be used
     *
     * @see jakarta.xml.bind.JAXBContext
     */
    public JAXBWriter(String contextPath) {
        super(contextPath);
        outputFormat = new OutputFormat();
    }

    /**
     * Creates a new JAXBWriter for the given JAXB context path. The specied
     * {@link org.dom4j.io.OutputFormat}will be used for writing the XML
     * stream.
     *
     * @param contextPath
     *            JAXB context path to be used
     * @param outputFormat
     *            the DOM4J {@link org.dom4j.io.OutputFormat}to be used
     *
     * @see jakarta.xml.bind.JAXBContext
     */
    public JAXBWriter(String contextPath, OutputFormat outputFormat) {
        super(contextPath);
        this.outputFormat = outputFormat;
    }

    /**
     * Creates a new JAXBWriter for the given JAXB context path, using the
     * specified {@link java.lang.ClassLoader}. (This is the Java package where
     * JAXB can find the generated XML classes. This package MUST contain
     * jaxb.properties!)
     *
     * @param contextPath
     *            JAXB context path to be used
     * @param classloader
     *            the classloader to be used for loading JAXB
     *
     * @see jakarta.xml.bind.JAXBContext
     */
    public JAXBWriter(String contextPath, ClassLoader classloader) {
        super(contextPath, classloader);
    }

    /**
     * Creates a new JAXBWriter for the given JAXB context path, using the
     * specified {@link java.lang.ClassLoader}. The specied {@link
     * org.dom4j.io.OutputFormat} will be used while writing the XML stream.
     *
     * @param contextPath
     *            JAXB context path to be used
     * @param classloader
     *            the class loader to be used to load JAXB
     * @param outputFormat
     *            the DOM4J {@link org.dom4j.io.OutputFormat}to be used
     *
     * @see jakarta.xml.bind.JAXBContext
     */
    public JAXBWriter(String contextPath, ClassLoader classloader,
            OutputFormat outputFormat) {
        super(contextPath, classloader);
        this.outputFormat = outputFormat;
    }

    /**
     * Returns the OutputFormat that will be used when writing the XML stream.
     *
     * @return Returns the output format.
     */
    public OutputFormat getOutputFormat() {
        return outputFormat;
    }

    /**
     * Defines to write the resulting output to the specified {@link
     * java.io.File}.
     *
     * @param file
     *            file to write to
     *
     * @throws IOException
     *             when the file cannot be found
     */
    public void setOutput(File file) throws IOException {
        getWriter().setOutputStream(new FileOutputStream(file));
    }

    /**
     * Defines to write the resulting output to the specified {@link
     * java.io.OutputStream}
     *
     * @param outputStream
     *            outputStream to write to.
     *
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void setOutput(OutputStream outputStream) throws IOException {
        getWriter().setOutputStream(outputStream);
    }

    /**
     * Defines to write the resulting output to the specified {@link Writer}.
     *
     * @param writer
     *            writer to write to
     *
     * @throws IOException DOCUMENT ME!
     */
    public void setOutput(Writer writer) throws IOException {
        getWriter().setWriter(writer);
    }

    /**
     * Start a document by writing the initial XML declaration to the output.
     * This must be done prior to writing any other elements.
     *
     * @throws IOException
     *             if an error occured while writing the output
     * @throws SAXException
     *             thrown by the underlying SAX driver
     */
    public void startDocument() throws IOException, SAXException {
        getWriter().startDocument();
    }

    /**
     * Stop writing the document to the output. This must be done when all other
     * elements are finished.
     *
     * @throws IOException
     *             if an error occured while writing the output
     * @throws SAXException
     *             thrown by the underlying SAX driver
     */
    public void endDocument() throws IOException, SAXException {
        getWriter().endDocument();
    }

    /**
     * Writes the specified {@link jakarta.xml.bind.Element}to the document.
     * {@link jakarta.xml.bind.Element}instances can be created using the
     * ObjectFactory that is generated by the JAXB compiler.
     *
     * @param jaxbObject DOCUMENT ME!
     *
     * @throws IOException
     *             if an error occured while writing the output
     * @throws JAXBException
     *             when an error occured while marshalling the jaxbObject
     */
    public void write(jakarta.xml.bind.Element jaxbObject) throws IOException,
            JAXBException {
        getWriter().write(marshal(jaxbObject));
    }

    /**
     * Writes the closing tag of the specified {@link jakarta.xml.bind.Element}to
     * the document. This method can be used for writing {@link
     * jakarta.xml.bind.Element} instances can be created using the ObjectFactory
     * that is generated by the JAXB compiler.
     *
     * @param jaxbObject
     *            the JAXB element to write
     *
     * @throws IOException
     *             if an error occured while writing the output
     * @throws JAXBException
     *             when an error occured while marshalling the jaxbObject
     */
    public void writeClose(jakarta.xml.bind.Element jaxbObject)
            throws IOException, JAXBException {
        getWriter().writeClose(marshal(jaxbObject));
    }

    /**
     * Writes the opening tag of the specified {@link jakarta.xml.bind.Element}to
     * the document. {@link jakarta.xml.bind.Element}instances can be created
     * using the ObjectFactory that is generated by the JAXB compiler.
     *
     * @param jaxbObject
     *            the JAXB element to write
     *
     * @throws IOException
     *             if an error occured while writing the output
     * @throws JAXBException
     *             when an error occured while marshalling the jaxbObject
     */
    public void writeOpen(jakarta.xml.bind.Element jaxbObject)
            throws IOException, JAXBException {
        getWriter().writeOpen(marshal(jaxbObject));
    }

    /**
     * Writes the specified {@link org.dom4j.Element}to the document.
     *
     * @param element
     *            the {@link org.dom4j.Element}to write
     *
     * @throws IOException
     *             if an error occured while writing the output
     */
    public void writeElement(Element element) throws IOException {
        getWriter().write(element);
    }

    /**
     * Writes the closing tag of the specified {@link org.dom4j.Element}to the
     * document.
     *
     * @param element
     *            the {@link org.dom4j.Element}to write
     *
     * @throws IOException
     *             if an error occured while writing the output
     */
    public void writeCloseElement(Element element) throws IOException {
        getWriter().writeClose(element);
    }

    /**
     * Writes the opening tag of the specified {@link org.dom4j.Element}to the
     * document.
     *
     * @param element
     *            the {@link org.dom4j.Element}to write
     *
     * @throws IOException
     *             if an error occured while writing the output
     */
    public void writeOpenElement(Element element) throws IOException {
        getWriter().writeOpen(element);
    }

    private XMLWriter getWriter() throws IOException {
        if (xmlWriter == null) {
            if (this.outputFormat != null) {
                xmlWriter = new XMLWriter(outputFormat);
            } else {
                xmlWriter = new XMLWriter();
            }
        }

        return xmlWriter;
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
