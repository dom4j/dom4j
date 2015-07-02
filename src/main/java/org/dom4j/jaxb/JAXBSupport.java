/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.jaxb;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.dom.DOMDocument;

/**
 * DOCUMENT ME!
 * 
 * @author Wonne Keysers (Realsoftware.be)
 */
abstract class JAXBSupport {
    private String contextPath;

    private ClassLoader classloader;

    private JAXBContext jaxbContext;

    private Marshaller marshaller;

    private Unmarshaller unmarshaller;

    public JAXBSupport(String contextPath) {
        this.contextPath = contextPath;
    }

    public JAXBSupport(String contextPath, ClassLoader classloader) {
        this.contextPath = contextPath;
        this.classloader = classloader;
    }

    /**
     * Marshals the given {@link javax.xml.bind.Element}in to its DOM4J
     * counterpart.
     * 
     * @param element
     *            JAXB Element to be marshalled
     * 
     * @return the marshalled DOM4J {@link org.dom4j.Element}
     * 
     * @throws JAXBException
     *             when an error occurs
     */
    protected org.dom4j.Element marshal(javax.xml.bind.Element element)
            throws JAXBException {
        DOMDocument doc = new DOMDocument();
        getMarshaller().marshal(element, doc);

        return doc.getRootElement();
    }

    /**
     * Unmarshalls the specified DOM4J {@link org.dom4j.Element}into a {@link
     * javax.xml.bind.Element}
     * 
     * @param element
     *            the DOM4J element to unmarshall
     * 
     * @return the unmarshalled JAXB object
     * 
     * @throws JAXBException
     *             when an error occurs
     */
    protected javax.xml.bind.Element unmarshal(org.dom4j.Element element)
            throws JAXBException {
        Source source = new StreamSource(new StringReader(element.asXML()));

        return (javax.xml.bind.Element) getUnmarshaller().unmarshal(source);
    }

    private Marshaller getMarshaller() throws JAXBException {
        if (marshaller == null) {
            marshaller = getContext().createMarshaller();
        }

        return marshaller;
    }

    private Unmarshaller getUnmarshaller() throws JAXBException {
        if (unmarshaller == null) {
            unmarshaller = getContext().createUnmarshaller();
        }

        return unmarshaller;
    }

    private JAXBContext getContext() throws JAXBException {
        if (jaxbContext == null) {
            if (classloader == null) {
                jaxbContext = JAXBContext.newInstance(contextPath);
            } else {
                jaxbContext = JAXBContext.newInstance(contextPath, classloader);
            }
        }

        return jaxbContext;
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
