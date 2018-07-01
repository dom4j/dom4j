/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.regex.Pattern;

import org.dom4j.tree.QNameCache;
import org.dom4j.util.SingletonStrategy;

/**
 * <code>QName</code> represents a qualified name value of an XML element or
 * attribute. It consists of a local name and a {@link Namespace}instance. This
 * object is immutable.
 *
 * @author <a href="mailto:jstrachan@apache.org">James Strachan </a>
 * @author Filip Jirsák
 */
public class QName implements Serializable {
    /** The Singleton instance */
    private static SingletonStrategy<QNameCache> singleton = null;

    /**
     * {@code NameStartChar} without colon.
     *
     * <pre>NameStartChar	::=	":" | [A-Z] | "_" | [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] | [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] | [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]</pre>
     *
     * @see <a href="https://www.w3.org/TR/xml/#sec-common-syn">XML 1.0 – 2.3 Common Syntactic Constructs</a>
     * @see <a href="https://www.w3.org/TR/2006/REC-xml11-20060816/#sec-common-syn">XML 1.1 – 2.3 Common Syntactic Constructs</a>
     */
    private static final String NAME_START_CHAR = "_A-Za-z\u00C0-\u00D6\u00D8-\u00F6\u00F8-\u02FF\u0370-\u037D\u037F-\u1FFF\u200C-\u200D\u2070-\u218F\u2C00-\u2FEF\u3001-\uD7FF\uF900-\uFDCF\uFDF0-\uFFFD";

    /**
     * {@code NameChar} without colon.
     *
     * <pre>NameChar	::=	NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] | [#x203F-#x2040]</pre>
     *
     * @see <a href="https://www.w3.org/TR/xml/#sec-common-syn">XML 1.0 – 2.3 Common Syntactic Constructs</a>
     * @see <a href="https://www.w3.org/TR/2006/REC-xml11-20060816/#sec-common-syn">XML 1.1 – 2.3 Common Syntactic Constructs</a>
     */
    private static final String NAME_CHAR = NAME_START_CHAR + "-.0-9\u00B7\u0300-\u036F\u203F-\u2040";

    /**
     * {@code NCName}
     *
     * <pre>
     * NCName		::=	NCNameStartChar NCNameChar*	(An XML Name, minus the ":")
     * NCNameChar	::=	NameChar -':'
     * NCNameStartChar	::=	NameStartChar -':'
     * </pre>
     *
     * @see <a href="https://www.w3.org/TR/xml-names/#ns-qualnames">Namespaces in XML 1.0 – 4 Qualified Names</a>
     * @see <a href="https://www.w3.org/TR/2006/REC-xml-names11-20060816/#ns-qualnames">Namespaces in XML 1.1 – 4 Qualified Names</a>
     */
    private static final String NCNAME = "["+NAME_START_CHAR+"]["+NAME_CHAR+"]*";

    /**
     * Regular expression for {@code Name} (with colon).
     *
     * <pre>Name	::=	NameStartChar (NameChar)*</pre>
     *
     * @see <a href="https://www.w3.org/TR/xml/#sec-common-syn">XML 1.0 – 2.3 Common Syntactic Constructs</a>
     * @see <a href="https://www.w3.org/TR/2006/REC-xml11-20060816/#sec-common-syn">XML 1.1 – 2.3 Common Syntactic Constructs</a>
     */
    private static final Pattern RE_NAME = Pattern.compile("[:"+NAME_START_CHAR+"][:"+NAME_CHAR+"]*");

    /**
     * Regular expression for {@code NCName}.
     *
     * <pre>
     * NCName		::=	NCNameStartChar NCNameChar*	(An XML Name, minus the ":")
     * NCNameChar	::=	NameChar -':'
     * NCNameStartChar	::=	NameStartChar -':'
     * </pre>
     *
     * @see <a href="https://www.w3.org/TR/xml-names/#ns-qualnames">Namespaces in XML 1.0 – 4 Qualified Names</a>
     * @see <a href="https://www.w3.org/TR/2006/REC-xml-names11-20060816/#ns-qualnames">Namespaces in XML 1.1 – 4 Qualified Names</a>
     */
    private static final Pattern RE_NCNAME = Pattern.compile(NCNAME);

    /**
     * Regular expression for {@code QName}.
     *
     * <pre>
     * QName		::=	PrefixedName | UnprefixedName
     * PrefixedName	::=	Prefix ':' LocalPart
     * UnprefixedName	::=	LocalPart
     * Prefix		::=	NCName
     * LocalPart	::=	NCName
     * </pre>
     *
     * @see <a href="https://www.w3.org/TR/xml-names/#ns-qualnames">Namespaces in XML 1.0 – 4 Qualified Names</a>
     * @see <a href="https://www.w3.org/TR/2006/REC-xml-names11-20060816/#ns-qualnames">Namespaces in XML 1.1 – 4 Qualified Names</a>
     */
    private static final Pattern RE_QNAME = Pattern.compile("(?:"+NCNAME+":)?"+NCNAME);

    static {
        try {
            String defaultSingletonClass = "org.dom4j.util.SimpleSingleton";
            Class<SingletonStrategy> clazz = null;
            try {
                String singletonClass = defaultSingletonClass;
                singletonClass = System.getProperty(
                        "org.dom4j.QName.singleton.strategy", singletonClass);
                clazz = (Class<SingletonStrategy>) Class.forName(singletonClass);
            } catch (Exception exc1) {
                try {
                    String singletonClass = defaultSingletonClass;
                    clazz = (Class<SingletonStrategy>) Class.forName(singletonClass);
                } catch (Exception exc2) {
                }
            }
            singleton = clazz.newInstance();
            singleton.setSingletonClassName(QNameCache.class.getName());
        } catch (Exception exc3) {
        }
    }

    /** The local name of the element or attribute */
    private String name;

    /** The qualified name of the element or attribute */
    private String qualifiedName;

    /** The Namespace of this element or attribute */
    private transient Namespace namespace;

    /** A cached version of the hashcode for efficiency */
    private int hashCode;

    /** The document factory used for this QName if specified or null */
    private DocumentFactory documentFactory;

    public QName(String name) {
        this(name, Namespace.NO_NAMESPACE);
    }

    public QName(String name, Namespace namespace) {
        this.name = (name == null) ? "" : name;
        this.namespace = (namespace == null) ? Namespace.NO_NAMESPACE
                : namespace;
        if (this.namespace.equals(Namespace.NO_NAMESPACE)) {
            validateName(this.name);
        } else {
            validateNCName(this.name);
        }
    }

    public QName(String name, Namespace namespace, String qualifiedName) {
        this.name = (name == null) ? "" : name;
        this.qualifiedName = qualifiedName;
        this.namespace = (namespace == null) ? Namespace.NO_NAMESPACE
                : namespace;
        validateNCName(this.name);
        validateQName(this.qualifiedName);
    }

    public static QName get(String name) {
        return getCache().get(name);
    }

    public static QName get(String name, Namespace namespace) {
        return getCache().get(name, namespace);
    }

    public static QName get(String name, String prefix, String uri) {
        if (((prefix == null) || (prefix.length() == 0)) && (uri == null)) {
            return QName.get(name);
        } else if ((prefix == null) || (prefix.length() == 0)) {
            return getCache().get(name, Namespace.get(uri));
        } else if (uri == null) {
            return QName.get(name);
        } else {
            return getCache().get(name, Namespace.get(prefix, uri));
        }
    }

    public static QName get(String qualifiedName, String uri) {
        if (uri == null) {
            return getCache().get(qualifiedName);
        } else {
            return getCache().get(qualifiedName, uri);
        }
    }

    public static QName get(String localName, Namespace namespace,
            String qualifiedName) {
        return getCache().get(localName, namespace, qualifiedName);
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the local name
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the qualified name in the format <code>prefix:localName</code>
     */
    public String getQualifiedName() {
        if (qualifiedName == null) {
            String prefix = getNamespacePrefix();

            if ((prefix != null) && (prefix.length() > 0)) {
                qualifiedName = prefix + ":" + name;
            } else {
                qualifiedName = name;
            }
        }

        return qualifiedName;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the namespace of this QName
     */
    public Namespace getNamespace() {
        return namespace;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the namespace URI of this QName
     */
    public String getNamespacePrefix() {
        if (namespace == null) {
            return "";
        }

        return namespace.getPrefix();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the namespace URI of this QName
     */
    public String getNamespaceURI() {
        if (namespace == null) {
            return "";
        }

        return namespace.getURI();
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the hash code based on the qualified name and the URI of the
     *         namespace.
     */
    public int hashCode() {
        if (hashCode == 0) {
            hashCode = getName().hashCode() ^ getNamespaceURI().hashCode();

            if (hashCode == 0) {
                hashCode = 0xbabe;
            }
        }

        return hashCode;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (object instanceof QName) {
            QName that = (QName) object;

            // we cache hash codes so this should be quick
            if (hashCode() == that.hashCode()) {
                return getName().equals(that.getName())
                        && getNamespaceURI().equals(that.getNamespaceURI());
            }
        }

        return false;
    }

    public String toString() {
        return super.toString() + " [name: " + getName() + " namespace: \""
                + getNamespace() + "\"]";
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the factory that should be used for Elements of this QName
     */
    public DocumentFactory getDocumentFactory() {
        return documentFactory;
    }

    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        
        // We use writeObject() and not writeUTF() to minimize space
        // This allows for writing pointers to already written strings
        out.writeObject(namespace.getPrefix());
        out.writeObject(namespace.getURI());
    }

    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        
        String prefix = (String) in.readObject();
        String uri = (String) in.readObject();

        namespace = Namespace.get(prefix, uri);
    }

    private static QNameCache getCache() {
        QNameCache cache = singleton.instance();
        return cache;
    }

    private static void validateName(String name) {
        if (!RE_NAME.matcher(name).matches()) {
            throw new IllegalArgumentException(String.format("Illegal character in name: '%s'.", name));
        }
    }

    protected static void validateNCName(String ncname) {
        if (!RE_NCNAME.matcher(ncname).matches()) {
            throw new IllegalArgumentException(String.format("Illegal character in local name: '%s'.", ncname));
        }
    }

    private static void validateQName(String qname) {
        if (!RE_QNAME.matcher(qname).matches()) {
            throw new IllegalArgumentException(String.format("Illegal character in qualified name: '%s'.", qname));
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
