// Attributes2.java - extended Attributes
// http://www.saxproject.org
// Public Domain: no warranty.
// $Id$

package org.xml.sax.ext;

import org.xml.sax.Attributes;


/**
 * SAX2 extension to augment the per-attribute information
 * provided though {@link Attributes}.
 * If an implementation supports this extension, the attributes
 * provided in {@link org.xml.sax.ContentHandler#startElement
 * ContentHandler.startElement() } will implement this interface,
 * and the <em>http://xml.org/sax/features/use-attributes2</em>
 * feature flag will have the value <em>true</em>.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * </blockquote>
 *
 * <p> XMLReader implementations are not required to support this
 * information, and it is not part of core-only SAX2 distributions.</p>
 *
 * @since SAX 2.0 (extensions 1.1 alpha)
 * @author David Brownell
 * @version TBS
 */
public interface Attributes2 extends Attributes
{
    /**
     * Returns true unless the attribute value was provided
     * by DTD defaulting.
     *
     * @param index The attribute index (zero-based).
     * @return true if the value was found in the XML text,
     *		false if the value was provided by DTD defaulting.
     * @exception java.lang.ArrayIndexOutOfBoundsException When the
     *            supplied index does not identify an attribute.
     */
    public boolean isSpecified (int index);

    /**
     * Returns true unless the attribute value was provided
     * by DTD defaulting.
     *
     * @param uri The Namespace URI, or the empty string if
     *        the name has no Namespace URI.
     * @param localName The attribute's local name.
     * @return true if the value was found in the XML text,
     *		false if the value was provided by DTD defaulting.
     * @exception java.lang.IllegalArgumentException When the
     *            supplied names do not identify an attribute.
     */
    public boolean isSpecified (String uri, String localName);

    /**
     * Returns true unless the attribute value was provided
     * by DTD defaulting.
     *
     * @param qName The XML 1.0 qualified name.
     * @return true if the value was found in the XML text,
     *		false if the value was provided by DTD defaulting.
     * @exception java.lang.IllegalArgumentException When the
     *            supplied name does not identify an attribute.
     */
    public boolean isSpecified (String qName);
}
