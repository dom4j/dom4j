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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.io.SAXReader;

import org.xml.sax.InputSource;

/**
 * Reads an XML document and creates a DOM4J tree from SAX parsing events.
 * {@link JAXBObjectHandler}objects can be registered to automatically receive
 * unmarshalled XML fragments. Registered {@linkorg.dom4j.ElementHandler}
 * implementations are notified when a certain element path is encountered
 * 
 * @author Wonne Keysers (Realsoftware.be)
 * 
 * @see org.dom4j.io.SAXReader
 * @see javax.xml.bind.JAXBContext
 */
public class JAXBReader extends JAXBSupport {
    private SAXReader reader;

    private boolean pruneElements;

    /**
     * Creates a new JAXBReader for the given JAXB context path. This is the
     * Java package where JAXB can find the generated XML classes. This package
     * MUST contain jaxb.properties!
     * 
     * @param contextPath
     *            context path to be used
     * 
     * @see javax.xml.bind.JAXBContext
     */
    public JAXBReader(String contextPath) {
        super(contextPath);
    }

    /**
     * Creates a new JAXBReader for the given JAXB context path, using the
     * specified {@link java.lang.Classloader}. This is the Java package where
     * JAXB can find the generated XML classes. This package MUST contain
     * jaxb.properties!
     * 
     * @param contextPath
     *            to be used
     * @param classloader
     *            to be used
     * 
     * @see javax.xml.bind.JAXBContext
     */
    public JAXBReader(String contextPath, ClassLoader classloader) {
        super(contextPath, classloader);
    }

    /**
     * Parses the specified {@link java.io.File}
     * 
     * @param source
     *            the file to parse
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     */
    public Document read(File source) throws DocumentException {
        return getReader().read(source);
    }

    /**
     * Parses the specified {@link java.io.File}, using the given {@link
     * java.nio.charset.Charset}.
     * 
     * @param file
     *            the file to parse
     * @param charset
     *            the charset to be used
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     */
    public Document read(File file, Charset charset) throws DocumentException {
        try {
            Reader xmlReader = new InputStreamReader(new FileInputStream(file),
                    charset);

            return getReader().read(xmlReader);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        } catch (FileNotFoundException ex) {
            throw new DocumentException(ex.getMessage(), ex);
        }
    }

    /**
     * Parses the specified {@link org.xml.sax.InputSource}
     * 
     * @param source
     *            the source to parse
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     */
    public Document read(InputSource source) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the specified {@link java.io.InputStream}
     * 
     * @param source
     *            the input stream to parse
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     */
    public Document read(InputStream source) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the specified {@link java.io.InputStream}
     * 
     * @param source
     *            the input stream to parse
     * @param systemId
     *            is the URI for the input
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     */
    public Document read(InputStream source, String systemId)
            throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the specified {@link java.io.Reader}
     * 
     * @param source
     *            the input reader to use
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     */
    public Document read(Reader source) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the specified {@link java.io.Reader}
     * 
     * @param source
     *            the input reader to parse
     * @param systemId
     *            is the URI for the input
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     */
    public Document read(Reader source, String systemId)
            throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Parses the the given URL or filename.
     * 
     * @param source
     *            the location to parse
     * 
     * @return the resulting DOM4J document
     * 
     * @throws DocumentException
     *             when an error occurs while parsing
     */
    public Document read(String source) throws DocumentException {
        try {
            return getReader().read(source);
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
     */
    public Document read(URL source) throws DocumentException {
        try {
            return getReader().read(source);
        } catch (JAXBRuntimeException ex) {
            Throwable cause = ex.getCause();
            throw new DocumentException(cause.getMessage(), cause);
        }
    }

    /**
     * Registers a {@link JAXBObjectHandler}that will be supplied with the
     * unmarshalled representation of the xml fragment whenever the specified
     * path is encounted.
     * 
     * @param path
     *            the path to listen for
     * @param handler
     *            the handler to be notified
     */
    public void addObjectHandler(String path, JAXBObjectHandler handler) {
        ElementHandler eHandler = new UnmarshalElementHandler(this, handler);
        getReader().addHandler(path, eHandler);
    }

    /**
     * Removes the {@link JAXBObjectHandler}from the event based processor, for
     * the specified element path.
     * 
     * @param path
     *            The path to remove the {@link JAXBObjectHandler}for
     */
    public void removeObjectHandler(String path) {
        getReader().removeHandler(path);
    }

    /**
     * Adds the <code>ElementHandler</code> to be called when the specified
     * path is encounted.
     * 
     * @param path
     *            is the path to be handled
     * @param handler
     *            is the <code>ElementHandler</code> to be called by the event
     *            based processor.
     */
    public void addHandler(String path, ElementHandler handler) {
        getReader().addHandler(path, handler);
    }

    /**
     * Removes the <code>ElementHandler</code> from the event based processor,
     * for the specified path.
     * 
     * @param path
     *            is the path to remove the <code>ElementHandler</code> for.
     */
    public void removeHandler(String path) {
        getReader().removeHandler(path);
    }

    /**
     * Removes all registered {@link JAXBObjectHandler}and {@link
     * org.dom4j.ElementHandler} instances from the event based processor.
     */
    public void resetHandlers() {
        getReader().resetHandlers();
    }

    /**
     * When 'true', the DOM4J document will not be kept in memory while parsing.
     * 
     * @return Returns the pruneElements.
     */
    public boolean isPruneElements() {
        return pruneElements;
    }

    /**
     * Set to true when DOM4J elements must immediately be pruned from the tree.
     * The {@link Document}will not be available afterwards!
     * 
     * @param pruneElements
     */
    public void setPruneElements(boolean pruneElements) {
        this.pruneElements = pruneElements;

        if (pruneElements) {
            getReader().setDefaultHandler(new PruningElementHandler());
        }
    }

    private SAXReader getReader() {
        if (reader == null) {
            reader = new SAXReader();
        }

        return reader;
    }

    private class UnmarshalElementHandler implements ElementHandler {
        private JAXBReader jaxbReader;

        private JAXBObjectHandler handler;

        public UnmarshalElementHandler(JAXBReader documentReader,
                JAXBObjectHandler handler) {
            this.jaxbReader = documentReader;
            this.handler = handler;
        }

        public void onStart(ElementPath elementPath) {
        }

        public void onEnd(ElementPath elementPath) {
            try {
                org.dom4j.Element elem = elementPath.getCurrent();

                javax.xml.bind.Element jaxbObject 
                        = (javax.xml.bind.Element) jaxbReader.unmarshal(elem);

                if (jaxbReader.isPruneElements()) {
                    elem.detach();
                }

                handler.handleObject(jaxbObject);
            } catch (Exception ex) {
                throw new JAXBRuntimeException(ex);
            }
        }
    }

    private class PruningElementHandler implements ElementHandler {
        public PruningElementHandler() {
        }

        public void onStart(ElementPath parm1) {
        }

        public void onEnd(ElementPath elementPath) {
            Element elem = elementPath.getCurrent();
            elem.detach();
            elem = null;
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
