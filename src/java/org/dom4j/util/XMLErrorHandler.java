/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.util;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * <code>XMLErrorHandler</code> is a SAX {@link ErrorHandler}which turns the
 * SAX parsing errors into XML so that the output can be formatted using XSLT or
 * the errors can be included in a SOAP message.
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class XMLErrorHandler implements ErrorHandler {
    protected static final QName ERROR_QNAME = QName.get("error");

    protected static final QName FATALERROR_QNAME = QName.get("fatalError");

    protected static final QName WARNING_QNAME = QName.get("warning");

    /** Stores the errors that occur during a SAX parse */
    private Element errors;

    /** QName used for error elements */
    private QName errorQName = ERROR_QNAME;

    /** QName used for fatalerror elements */
    private QName fatalErrorQName = FATALERROR_QNAME;

    /** QName used for warning elements */
    private QName warningQName = WARNING_QNAME;

    public XMLErrorHandler() {
        this.errors = DocumentHelper.createElement("errors");
    }

    public XMLErrorHandler(Element errors) {
        this.errors = errors;
    }

    public void error(SAXParseException e) {
        Element element = errors.addElement(errorQName);
        addException(element, e);
    }

    public void fatalError(SAXParseException e) {
        Element element = errors.addElement(fatalErrorQName);
        addException(element, e);
    }

    public void warning(SAXParseException e) {
        Element element = errors.addElement(warningQName);
        addException(element, e);
    }

    // Properties
    // -------------------------------------------------------------------------
    public Element getErrors() {
        return errors;
    }

    public void setErrors(Element errors) {
        this.errors = errors;
    }

    // Allow the QNames used to create subelements to be changed
    public QName getErrorQName() {
        return errorQName;
    }

    public void setErrorQName(QName errorQName) {
        this.errorQName = errorQName;
    }

    public QName getFatalErrorQName() {
        return fatalErrorQName;
    }

    public void setFatalErrorQName(QName fatalErrorQName) {
        this.fatalErrorQName = fatalErrorQName;
    }

    public QName getWarningQName() {
        return warningQName;
    }

    public void setWarningQName(QName warningQName) {
        this.warningQName = warningQName;
    }

    // Implementation methods
    // -------------------------------------------------------------------------

    /**
     * Adds the given parse exception information to the given element instance
     * 
     * @param element
     *            DOCUMENT ME!
     * @param e
     *            DOCUMENT ME!
     */
    protected void addException(Element element, SAXParseException e) {
        element.addAttribute("column", Integer.toString(e.getColumnNumber()));
        element.addAttribute("line", Integer.toString(e.getLineNumber()));

        String publicID = e.getPublicId();

        if ((publicID != null) && (publicID.length() > 0)) {
            element.addAttribute("publicID", publicID);
        }

        String systemID = e.getSystemId();

        if ((systemID != null) && (systemID.length() > 0)) {
            element.addAttribute("systemID", systemID);
        }

        element.addText(e.getMessage());
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
