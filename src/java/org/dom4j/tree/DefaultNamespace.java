/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.tree;

import org.dom4j.Element;
import org.dom4j.Namespace;

/**
 * <p>
 * <code>DefaultNamespace</code> implements a doubly linked node which
 * supports the parent relationship and is mutable. It is useful when returning
 * results from XPath expressions.
 * </p>
 * 
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @version $Revision$
 */
public class DefaultNamespace extends Namespace {
    /** The parent of this node */
    private Element parent;

    /**
     * DOCUMENT ME!
     * 
     * @param prefix
     *            is the prefix for this namespace
     * @param uri
     *            is the URI for this namespace
     */
    public DefaultNamespace(String prefix, String uri) {
        super(prefix, uri);
    }

    /**
     * DOCUMENT ME!
     * 
     * @param parent
     *            is the parent element
     * @param prefix
     *            is the prefix for this namespace
     * @param uri
     *            is the URI for this namespace
     */
    public DefaultNamespace(Element parent, String prefix, String uri) {
        super(prefix, uri);
        this.parent = parent;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the hash code based on the qualified name and the URI of the
     *         namespace and the hashCode() of the parent element.
     */
    protected int createHashCode() {
        int hashCode = super.createHashCode();

        if (parent != null) {
            hashCode ^= parent.hashCode();
        }

        return hashCode;
    }

    /**
     * Implements an identity based comparsion using the parent element as well
     * as the prefix and URI
     * 
     * @param object
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public boolean equals(Object object) {
        if (object instanceof DefaultNamespace) {
            DefaultNamespace that = (DefaultNamespace) object;

            if (that.parent == parent) {
                return super.equals(object);
            }
        }

        return false;
    }

    public int hashCode() {
        return super.hashCode();
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
