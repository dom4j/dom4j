package org.dom4j.tree;

/** <p><code>DefaultNamespace</code> is the DOM4J default implementation
  * of <code>Namespace</code>.</p>
  *
  * @version $Revision$
  */
public class DefaultNamespace extends AbstractNamespace {

    /** The prefix mapped to this namespace */
    private String prefix;

    /** The URI for this namespace */
    private String uri;

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


}
