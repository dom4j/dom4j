/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import org.dom4j.tree.AbstractNode;
import org.dom4j.tree.DefaultNamespace;
import org.dom4j.tree.NamespaceCache;

/**
 * <code>Namespace</code> is a Flyweight Namespace that can be shared amongst
 * nodes.
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision: 1.22 $
 */
public class Namespace extends AbstractNode {
    /** Cache of Namespace instances */
    protected static final NamespaceCache CACHE = new NamespaceCache();

    /** XML Namespace */
    public static final Namespace XML_NAMESPACE = CACHE.get("xml",
            "http://www.w3.org/XML/1998/namespace");

    /** No Namespace present */
    public static final Namespace NO_NAMESPACE = CACHE.get("", "");

    /** The prefix mapped to this namespace */
    private String prefix;

    /** The URI for this namespace */
    private String uri;

    /** A cached version of the hashcode for efficiency */
    private int hashCode;

    /**
     * DOCUMENT ME!
     * 
     * @param prefix
     *            is the prefix for this namespace
     * @param uri
     *            is the URI for this namespace
     */
    public Namespace(String prefix, String uri) {
        this.prefix = (prefix != null) ? prefix : "";
        this.uri = (uri != null) ? uri : "";

        if (this.prefix.length() > 0) {
            QName.validateNCName(this.prefix);
        }
    }

    /**
     * A helper method to return the Namespace instance for the given prefix and
     * URI
     * 
     * @param prefix
     *            DOCUMENT ME!
     * @param uri
     *            DOCUMENT ME!
     * 
     * @return an interned Namespace object
     */
    public static Namespace get(String prefix, String uri) {
        return CACHE.get(prefix, uri);
    }

    /**
     * A helper method to return the Namespace instance for no prefix and the
     * URI
     * 
     * @param uri
     *            DOCUMENT ME!
     * 
     * @return an interned Namespace object
     */
    public static Namespace get(String uri) {
        return CACHE.get(uri);
    }

    public short getNodeType() {
        return NAMESPACE_NODE;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the hash code based on the qualified name and the URI of the
     *         namespace.
     */
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = createHashCode();
        }

        return hashCode;
    }

    /**
     * Factory method to create the hashcode allowing derived classes to change
     * the behaviour
     * 
     * @return DOCUMENT ME!
     */
    protected int createHashCode() {
        int result = uri.hashCode() ^ prefix.hashCode();

        if (result == 0) {
            result = 0xbabe;
        }

        return result;
    }

    /**
     * Checks whether this Namespace equals the given Namespace. Two Namespaces
     * are equals if their URI and prefix are equal.
     * 
     * @param object
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof Namespace) {
            Namespace that = (Namespace) object;

            // we cache hash codes so this should be quick
            if (hashCode() == that.hashCode()) {
                return uri.equals(that.getURI())
                        && prefix.equals(that.getPrefix());
            }
        }

        return false;
    }

    public String getText() {
        return uri;
    }

    public String getStringValue() {
        return uri;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the prefix for this <code>Namespace</code>.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the URI for this <code>Namespace</code>.
     */
    public String getURI() {
        return uri;
    }

    public String getXPathNameStep() {
        if ((prefix != null) && !"".equals(prefix)) {
            return "namespace::" + prefix;
        }

        return "namespace::*[name()='']";
    }

    public String getPath(Element context) {
        StringBuffer path = new StringBuffer(10);
        Element parent = getParent();

        if ((parent != null) && (parent != context)) {
            path.append(parent.getPath(context));
            path.append('/');
        }

        path.append(getXPathNameStep());

        return path.toString();
    }

    public String getUniquePath(Element context) {
        StringBuffer path = new StringBuffer(10);
        Element parent = getParent();

        if ((parent != null) && (parent != context)) {
            path.append(parent.getUniquePath(context));
            path.append('/');
        }

        path.append(getXPathNameStep());

        return path.toString();
    }

    public String toString() {
        return super.toString() + " [Namespace: prefix " + getPrefix()
                + " mapped to URI \"" + getURI() + "\"]";
    }

    public String asXML() {
        StringBuffer asxml = new StringBuffer(10);
        String pref = getPrefix();

        if ((pref != null) && (pref.length() > 0)) {
            asxml.append("xmlns:");
            asxml.append(pref);
            asxml.append("=\"");
        } else {
            asxml.append("xmlns=\"");
        }

        asxml.append(getURI());
        asxml.append("\"");

        return asxml.toString();
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    protected Node createXPathResult(Element parent) {
        return new DefaultNamespace(parent, getPrefix(), getURI());
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
