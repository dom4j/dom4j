/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.tree;

import java.io.IOException;
import java.io.Writer;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.Visitor;

/**
 * <p>
 * <code>AbstractNamespace</code> is an abstract base class for tree
 * implementors to use for implementation inheritence.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public abstract class AbstractAttribute extends AbstractNode implements
        Attribute {
    public short getNodeType() {
        return ATTRIBUTE_NODE;
    }

    public void setNamespace(Namespace namespace) {
        String msg = "This Attribute is read only and cannot be changed";
        throw new UnsupportedOperationException(msg);
    }

    public String getText() {
        return getValue();
    }

    public void setText(String text) {
        setValue(text);
    }

    public void setValue(String value) {
        String msg = "This Attribute is read only and cannot be changed";
        throw new UnsupportedOperationException(msg);
    }

    public Object getData() {
        return getValue();
    }

    public void setData(Object data) {
        setValue((data == null) ? null : data.toString());
    }

    public String toString() {
        return super.toString() + " [Attribute: name " + getQualifiedName()
                + " value \"" + getValue() + "\"]";
    }

    public String asXML() {
        return getQualifiedName() + "=\"" + getValue() + "\"";
    }

    public void write(Writer writer) throws IOException {
        writer.write(getQualifiedName());
        writer.write("=\"");
        writer.write(getValue());
        writer.write("\"");
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    // QName methods
    public Namespace getNamespace() {
        return getQName().getNamespace();
    }

    public String getName() {
        return getQName().getName();
    }

    public String getNamespacePrefix() {
        return getQName().getNamespacePrefix();
    }

    public String getNamespaceURI() {
        return getQName().getNamespaceURI();
    }

    public String getQualifiedName() {
        return getQName().getQualifiedName();
    }

    public String getPath(Element context) {
        StringBuffer result = new StringBuffer();

        Element parent = getParent();

        if ((parent != null) && (parent != context)) {
            result.append(parent.getPath(context));
            result.append("/");
        }

        result.append("@");

        String uri = getNamespaceURI();
        String prefix = getNamespacePrefix();

        if ((uri == null) || (uri.length() == 0) || (prefix == null)
                || (prefix.length() == 0)) {
            result.append(getName());
        } else {
            result.append(getQualifiedName());
        }

        return result.toString();
    }

    public String getUniquePath(Element context) {
        StringBuffer result = new StringBuffer();

        Element parent = getParent();

        if ((parent != null) && (parent != context)) {
            result.append(parent.getUniquePath(context));
            result.append("/");
        }

        result.append("@");

        String uri = getNamespaceURI();
        String prefix = getNamespacePrefix();

        if ((uri == null) || (uri.length() == 0) || (prefix == null)
                || (prefix.length() == 0)) {
            result.append(getName());
        } else {
            result.append(getQualifiedName());
        }

        return result.toString();
    }

    protected Node createXPathResult(Element parent) {
        return new DefaultAttribute(parent, getQName(), getValue());
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
