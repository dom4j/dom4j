/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.tree;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.Visitor;

/**
 * <p>
 * <code>AbstractDocumentType</code> is an abstract base class for tree
 * implementors to use for implementation inheritence.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public abstract class AbstractDocumentType extends AbstractNode implements
        DocumentType {
    public AbstractDocumentType() {
    }

    public short getNodeType() {
        return DOCUMENT_TYPE_NODE;
    }

    public String getName() {
        return getElementName();
    }

    public void setName(String name) {
        setElementName(name);
    }

    public String getPath(Element context) {
        // not available in XPath
        return "";
    }

    public String getUniquePath(Element context) {
        // not available in XPath
        return "";
    }

    /**
     * Returns the text format of the declarations if applicable, or the empty
     * String
     * 
     * @return DOCUMENT ME!
     */
    public String getText() {
        List list = getInternalDeclarations();

        if ((list != null) && (list.size() > 0)) {
            StringBuffer buffer = new StringBuffer();
            Iterator iter = list.iterator();

            if (iter.hasNext()) {
                Object decl = iter.next();
                buffer.append(decl.toString());

                while (iter.hasNext()) {
                    decl = iter.next();
                    buffer.append("\n");
                    buffer.append(decl.toString());
                }
            }

            return buffer.toString();
        }

        return "";
    }

    public String toString() {
        return super.toString() + " [DocumentType: " + asXML() + "]";
    }

    public String asXML() {
        StringBuffer buffer = new StringBuffer("<!DOCTYPE ");
        buffer.append(getElementName());

        boolean hasPublicID = false;
        String publicID = getPublicID();

        if ((publicID != null) && (publicID.length() > 0)) {
            buffer.append(" PUBLIC \"");
            buffer.append(publicID);
            buffer.append("\"");
            hasPublicID = true;
        }

        String systemID = getSystemID();

        if ((systemID != null) && (systemID.length() > 0)) {
            if (!hasPublicID) {
                buffer.append(" SYSTEM");
            }

            buffer.append(" \"");
            buffer.append(systemID);
            buffer.append("\"");
        }

        buffer.append(">");

        return buffer.toString();
    }

    public void write(Writer writer) throws IOException {
        writer.write("<!DOCTYPE ");
        writer.write(getElementName());

        boolean hasPublicID = false;
        String publicID = getPublicID();

        if ((publicID != null) && (publicID.length() > 0)) {
            writer.write(" PUBLIC \"");
            writer.write(publicID);
            writer.write("\"");
            hasPublicID = true;
        }

        String systemID = getSystemID();

        if ((systemID != null) && (systemID.length() > 0)) {
            if (!hasPublicID) {
                writer.write(" SYSTEM");
            }

            writer.write(" \"");
            writer.write(systemID);
            writer.write("\"");
        }

        List list = getInternalDeclarations();

        if ((list != null) && (list.size() > 0)) {
            writer.write(" [");

            for (Iterator iter = list.iterator(); iter.hasNext();) {
                Object decl = iter.next();
                writer.write("\n  ");
                writer.write(decl.toString());
            }

            writer.write("\n]");
        }

        writer.write(">");
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
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
