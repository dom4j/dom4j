package org.dom4j.tree;

import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;

/** <p><code>DefaultNamespace</code> is the DOM4J default implementation
  * of <code>Namespace</code>.</p>
  *
  * @version $Revision$
  */
public class DefaultNamespace extends AbstractNamespace {

    /** Cache of Namespace instances */
    protected static NamespaceCache cache = new NamespaceCache();
    
    /** The prefix mapped to this namespace */
    private String prefix;

    /** The URI for this namespace */
    private String uri;

    
    public static Namespace get(String prefix, String uri) {
        return cache.get(prefix, uri);
    }
    
    /** @param prefix is the prefix for this namespace
      * @param uri is the URI for this namespace
      */
    public DefaultNamespace(String prefix, String uri) {
        this.prefix = prefix;
        this.uri = uri;
    }

    /** @return the prefix for this <code>Namespace</code>.
      */
    public String getPrefix() {
        return prefix;
    }

    /** @return the URI for this <code>Namespace</code>.
      */
    public String getURI() {
        return uri;
    }


    protected Node createXPathNode(Element parent) {
        return new XPathNamespace( parent, getPrefix(), getURI() );
    }
}
