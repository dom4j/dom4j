/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.tree;

import java.util.List;

/**
 * <p>
 * <code>DefaultDocumentType</code> is the DOM4J default implementation of an
 * XML document type.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class DefaultDocumentType extends AbstractDocumentType {
    /** The root element name of the document typ */
    protected String elementName;

    /** Holds value of property publicID. */
    private String publicID;

    /** Holds value of property systemID. */
    private String systemID;

    /** The internal DTD declarations */
    private List internalDeclarations;

    /** The external DTD declarations */
    private List externalDeclarations;

    public DefaultDocumentType() {
    }

    /**
     * <p>
     * This will create a new <code>DocumentType</code> with a reference to
     * the external DTD
     * </p>
     * 
     * @param elementName
     *            is the root element name of the document type
     * @param systemID
     *            is the system ID of the external DTD
     */
    public DefaultDocumentType(String elementName, String systemID) {
        this.elementName = elementName;
        this.systemID = systemID;
    }

    /**
     * <p>
     * This will create a new <code>DocumentType</code> with a reference to
     * the external DTD
     * </p>
     * 
     * @param elementName
     *            is the root element name of the document type
     * @param publicID
     *            is the public ID of the DTD
     * @param systemID
     *            is the system ID of the DTD
     */
    public DefaultDocumentType(String elementName, String publicID,
            String systemID) {
        this.elementName = elementName;
        this.publicID = publicID;
        this.systemID = systemID;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the public ID of the document type
     */
    public String getPublicID() {
        return publicID;
    }

    /**
     * Sets the public ID of the document type
     * 
     * @param publicID
     *            DOCUMENT ME!
     */
    public void setPublicID(String publicID) {
        this.publicID = publicID;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the system ID of the document type
     */
    public String getSystemID() {
        return systemID;
    }

    /**
     * Sets the system ID of the document type
     * 
     * @param systemID
     *            DOCUMENT ME!
     */
    public void setSystemID(String systemID) {
        this.systemID = systemID;
    }

    public List getInternalDeclarations() {
        return internalDeclarations;
    }

    public void setInternalDeclarations(List internalDeclarations) {
        this.internalDeclarations = internalDeclarations;
    }

    public List getExternalDeclarations() {
        return externalDeclarations;
    }

    public void setExternalDeclarations(List externalDeclarations) {
        this.externalDeclarations = externalDeclarations;
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
