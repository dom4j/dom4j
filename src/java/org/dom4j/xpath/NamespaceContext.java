
package org.dom4j.xpath;

import org.dom4j.Namespace;

/** <p>Specification of the interface required by
 *  {@link org.dom4j.xpath.ContextSupport} for delegation
 *  of namespace prefix binding resolution.<p>
 *
 *  @autho bob mcwhirter (bob @ werken.com)
 */
public interface NamespaceContext
{

    /** Translate a namespace prefix into a URI
     *
     *  Translate the prefix used in a component of an XPath
     *  into its expanded namespace URI.</p>
     *
     *  @param prefix The namespace prefix
     *
     *  @return The URI matching the prefix
     *
     *  @see org.dom4j.xpath.ContextSupport#setNamespaceContext
     */
    String translateNamespacePrefix(String prefix);

    Namespace getNamespaceByPrefix(String prefix);
}
