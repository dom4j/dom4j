/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.datatype;

import com.sun.msv.datatype.DatabindableDatatype;
import com.sun.msv.datatype.SerializationContext;
import com.sun.msv.datatype.xsd.XSDatatype;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.tree.AbstractAttribute;

import org.relaxng.datatype.DatatypeException;
import org.relaxng.datatype.ValidationContext;

/**
 * <p>
 * <code>DatatypeAttribute</code> represents an Attribute which supports the
 * <a href="http://www.w3.org/TR/xmlschema-2/">XML Schema Data Types </a>
 * specification.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class DatatypeAttribute extends AbstractAttribute implements
        SerializationContext, ValidationContext {
    /** The parent <code>Element</code> of the <code>Attribute</code> */
    private Element parent;

    /** The <code>QName</code> for this element */
    private QName qname;

    /** The <code>XSDatatype</code> of the <code>Attribute</code> */
    private XSDatatype datatype;

    /** The data (Object) value of the <code>Attribute</code> */
    private Object data;

    /** The text value of the <code>Attribute</code> */
    private String text;

    public DatatypeAttribute(QName qname, XSDatatype datatype) {
        this.qname = qname;
        this.datatype = datatype;
    }

    public DatatypeAttribute(QName qname, XSDatatype datatype, String text) {
        this.qname = qname;
        this.datatype = datatype;
        this.text = text;
        this.data = convertToValue(text);
    }

    public String toString() {
        return getClass().getName() + hashCode() + " [Attribute: name "
                + getQualifiedName() + " value \"" + getValue() + "\" data: "
                + getData() + "]";
    }

    /**
     * Returns the MSV XSDatatype for this node
     * 
     * @return DOCUMENT ME!
     */
    public XSDatatype getXSDatatype() {
        return datatype;
    }

    // SerializationContext interface
    // -------------------------------------------------------------------------
    public String getNamespacePrefix(String uri) {
        Element parentElement = getParent();

        if (parentElement != null) {
            Namespace namespace = parentElement.getNamespaceForURI(uri);

            if (namespace != null) {
                return namespace.getPrefix();
            }
        }

        return null;
    }

    // ValidationContext interface
    // -------------------------------------------------------------------------
    public String getBaseUri() {
        // XXXX: could we use a Document for this?
        return null;
    }

    public boolean isNotation(String notationName) {
        // XXXX: no way to do this yet in dom4j so assume false
        return false;
    }

    public boolean isUnparsedEntity(String entityName) {
        // XXXX: no way to do this yet in dom4j so assume valid
        return true;
    }

    public String resolveNamespacePrefix(String prefix) {
        // first lets see if this is our attribute's prefix
        if (prefix.equals(getNamespacePrefix())) {
            return getNamespaceURI();
        } else {
            Element parentElement = getParent();

            if (parentElement != null) {
                Namespace namespace = parentElement
                        .getNamespaceForPrefix(prefix);

                if (namespace != null) {
                    return namespace.getURI();
                }
            }
        }

        return null;
    }

    // Attribute interface
    // -------------------------------------------------------------------------
    public QName getQName() {
        return qname;
    }

    public String getValue() {
        return text;
    }

    public void setValue(String value) {
        validate(value);

        this.text = value;
        this.data = convertToValue(value);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        String s = datatype.convertToLexicalValue(data, this);
        validate(s);
        this.text = s;
        this.data = data;
    }

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }

    public boolean supportsParent() {
        return true;
    }

    public boolean isReadOnly() {
        return false;
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void validate(String txt) throws IllegalArgumentException {
        try {
            datatype.checkValid(txt, this);
        } catch (DatatypeException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    protected Object convertToValue(String txt) {
        if (datatype instanceof DatabindableDatatype) {
            DatabindableDatatype bindable = (DatabindableDatatype) datatype;

            return bindable.createJavaObject(txt, this);
        } else {
            return datatype.createValue(txt, this);
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
