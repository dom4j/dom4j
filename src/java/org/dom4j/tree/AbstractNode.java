/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.tree;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.NodeFilter;
import org.dom4j.XPath;
import org.dom4j.rule.Pattern;

/**
 * <p>
 * <code>AbstractNode</code> is an abstract base class for tree implementors
 * to use for implementation inheritence.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public abstract class AbstractNode implements Node, Cloneable, Serializable {
    protected static final String[] NODE_TYPE_NAMES = {"Node", "Element",
            "Attribute", "Text", "CDATA", "Entity", "Entity",
            "ProcessingInstruction", "Comment", "Document", "DocumentType",
            "DocumentFragment", "Notation", "Namespace", "Unknown" };

    /** The <code>DocumentFactory</code> instance used by default */
    private static final DocumentFactory DOCUMENT_FACTORY = DocumentFactory
            .getInstance();

    public AbstractNode() {
    }

    public short getNodeType() {
        return UNKNOWN_NODE;
    }

    public String getNodeTypeName() {
        int type = getNodeType();

        if ((type < 0) || (type >= NODE_TYPE_NAMES.length)) {
            return "Unknown";
        }

        return NODE_TYPE_NAMES[type];
    }

    public Document getDocument() {
        Element element = getParent();

        return (element != null) ? element.getDocument() : null;
    }

    public void setDocument(Document document) {
    }

    public Element getParent() {
        return null;
    }

    public void setParent(Element parent) {
    }

    public boolean supportsParent() {
        return false;
    }

    public boolean isReadOnly() {
        return true;
    }

    public boolean hasContent() {
        return false;
    }

    public String getPath() {
        return getPath(null);
    }

    public String getUniquePath() {
        return getUniquePath(null);
    }

    public Object clone() {
        if (isReadOnly()) {
            return this;
        } else {
            try {
                Node answer = (Node) super.clone();
                answer.setParent(null);
                answer.setDocument(null);

                return answer;
            } catch (CloneNotSupportedException e) {
                // should never happen
                throw new RuntimeException("This should never happen. Caught: "
                        + e);
            }
        }
    }

    public Node detach() {
        Element parent = getParent();

        if (parent != null) {
            parent.remove(this);
        } else {
            Document document = getDocument();

            if (document != null) {
                document.remove(this);
            }
        }

        setParent(null);
        setDocument(null);

        return this;
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {
        throw new UnsupportedOperationException("This node cannot be modified");
    }

    public String getText() {
        return null;
    }

    public String getStringValue() {
        return getText();
    }

    public void setText(String text) {
        throw new UnsupportedOperationException("This node cannot be modified");
    }

    public void write(Writer writer) throws IOException {
        writer.write(asXML());
    }

    // XPath methods
    public Object selectObject(String xpathExpression) {
        XPath xpath = createXPath(xpathExpression);

        return xpath.evaluate(this);
    }

    public List selectNodes(String xpathExpression) {
        XPath xpath = createXPath(xpathExpression);

        return xpath.selectNodes(this);
    }

    public List selectNodes(String xpathExpression,
            String comparisonXPathExpression) {
        return selectNodes(xpathExpression, comparisonXPathExpression, false);
    }

    public List selectNodes(String xpathExpression,
            String comparisonXPathExpression, boolean removeDuplicates) {
        XPath xpath = createXPath(xpathExpression);
        XPath sortBy = createXPath(comparisonXPathExpression);

        return xpath.selectNodes(this, sortBy, removeDuplicates);
    }

    public Node selectSingleNode(String xpathExpression) {
        XPath xpath = createXPath(xpathExpression);

        return xpath.selectSingleNode(this);
    }

    public String valueOf(String xpathExpression) {
        XPath xpath = createXPath(xpathExpression);

        return xpath.valueOf(this);
    }

    public Number numberValueOf(String xpathExpression) {
        XPath xpath = createXPath(xpathExpression);

        return xpath.numberValueOf(this);
    }

    public boolean matches(String patternText) {
        NodeFilter filter = createXPathFilter(patternText);

        return filter.matches(this);
    }

    public XPath createXPath(String xpathExpression) {
        return getDocumentFactory().createXPath(xpathExpression);
    }

    public NodeFilter createXPathFilter(String patternText) {
        return getDocumentFactory().createXPathFilter(patternText);
    }

    public Pattern createPattern(String patternText) {
        return getDocumentFactory().createPattern(patternText);
    }

    public Node asXPathResult(Element parent) {
        if (supportsParent()) {
            return this;
        }

        return createXPathResult(parent);
    }

    protected DocumentFactory getDocumentFactory() {
        return DOCUMENT_FACTORY;
    }

    protected Node createXPathResult(Element parent) {
        throw new RuntimeException("asXPathResult() not yet implemented fully "
                + "for: " + this);
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
