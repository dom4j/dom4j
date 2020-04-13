package org.dom4j.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.dom4j.DocumentFactory;
import org.dom4j.Namespace;
import org.dom4j.QName;

/**
 * <p>
 * <code>QNameCache</code> caches instances of <code>QName</code> for reuse
 * both across documents and within documents.
 * </p>< < < < < < < QNameCache.java
 */
public class QNameCache {

    /** Cache of {@link QName}instances with no namespace */
    protected ThreadLocal<Map<String, QName>> noNamespaceCache =
        ThreadLocal.withInitial(WeakHashMap::new);

    /**
     * Cache of {@link Map}instances indexed by namespace which contain caches
     * of {@link QName}for each name
     */
    protected ThreadLocal<Map<Namespace, Map<String, QName>>> namespaceCache =
        ThreadLocal.withInitial(WeakHashMap::new);

    /**
     * The document factory associated with new QNames instances in this cache
     * or null if no instances should be associated by default
     */
    private DocumentFactory documentFactory;

    public QNameCache() {
    }

    public QNameCache(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }

    /**
     * Returns a list of all the QName instances currently used
     *
     * @return DOCUMENT ME!
     */
    public List<QName> getQNames() {
        List<QName> answer = new ArrayList();
        answer.addAll((this.noNamespaceCache.get()).values());

        for (Map<String, QName> map : (this.namespaceCache.get()).values()){
            answer.addAll(map.values());
        }

        return answer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name
     *            DOCUMENT ME!
     *
     * @return the QName for the given name and no namepsace
     */
    public QName get(String name) {
        QName answer = null;

        if (name != null) {
            answer = (this.noNamespaceCache.get()).get(name);
        } else {
            name = "";
        }

        if (answer == null) {
            answer = createQName(name);
            answer.setDocumentFactory(this.documentFactory);
            (this.noNamespaceCache.get()).put(name, answer);
        }

        return answer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name
     *            DOCUMENT ME!
     * @param namespace
     *            DOCUMENT ME!
     *
     * @return the QName for the given local name and namepsace
     */
    public QName get(String name, Namespace namespace) {
        Map<String, QName> cache = getNamespaceCache(namespace);
        QName answer = null;

        if (name != null) {
            answer = cache.get(name);
        } else {
            name = "";
        }

        if (answer == null) {
            answer = createQName(name, namespace);
            answer.setDocumentFactory(this.documentFactory);
            cache.put(name, answer);
        }

        return answer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param localName
     *            DOCUMENT ME!
     * @param namespace
     *            DOCUMENT ME!
     * @param qName
     *            DOCUMENT ME!
     *
     * @return the QName for the given local name, qualified name and namepsace
     */
    public QName get(String localName, Namespace namespace, String qName) {
        Map<String, QName> cache = getNamespaceCache(namespace);
        QName answer = null;

        if (localName != null) {
            answer = cache.get(localName);
        } else {
            localName = "";
        }

        if (answer == null) {
            answer = createQName(localName, namespace, qName);
            answer.setDocumentFactory(this.documentFactory);
            cache.put(localName, answer);
        }

        return answer;
    }

    public QName get(String qualifiedName, String uri) {
        int index = qualifiedName.indexOf(':');

        if (index < 0) {
            return get(qualifiedName, Namespace.get(uri));
        } else if (index == 0) {
            throw new IllegalArgumentException("Qualified name cannot start with ':'.");
        } else {
            String name = qualifiedName.substring(index + 1);
            String prefix = qualifiedName.substring(0, index);

            return get(name, Namespace.get(prefix, uri));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param qname
     *            DOCUMENT ME!
     *
     * @return the cached QName instance if there is one or adds the given qname
     *         to the cache if not
     */
    public QName intern(QName qname) {
        return get(qname.getName(), qname.getNamespace(), qname.getQualifiedName());
    }

    /**
     * DOCUMENT ME!
     *
     * @param namespace
     *            DOCUMENT ME!
     *
     * @return the cache for the given namespace. If one does not currently
     *         exist it is created.
     */
    protected Map<String, QName> getNamespaceCache(Namespace namespace) {
        if (namespace == Namespace.NO_NAMESPACE) {
            return this.noNamespaceCache.get();
        }

        Map<String, QName> answer = null;

        if (namespace != null) {
            answer = (this.namespaceCache.get()).get(namespace);
        }

        if (answer == null) {
            answer = createMap();
            (this.namespaceCache.get()).put(namespace, answer);
        }

        return answer;
    }

    /**
     * A factory method
     *
     * @return a newly created {@link Map}instance.
     */
    protected Map<String, QName> createMap() {
        return new HashMap<String, QName>();
    }

    /**
     * Factory method to create a new QName object which can be overloaded to
     * create derived QName instances
     *
     * @param name
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected QName createQName(String name) {
        return new QName(name);
    }

    /**
     * Factory method to create a new QName object which can be overloaded to
     * create derived QName instances
     *
     * @param name
     *            DOCUMENT ME!
     * @param namespace
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected QName createQName(String name, Namespace namespace) {
        return new QName(name, namespace);
    }

    /**
     * Factory method to create a new QName object which can be overloaded to
     * create derived QName instances
     *
     * @param name
     *            DOCUMENT ME!
     * @param namespace
     *            DOCUMENT ME!
     * @param qualifiedName
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected QName createQName(String name, Namespace namespace, String qualifiedName) {
        return new QName(name, namespace, qualifiedName);
    }
}