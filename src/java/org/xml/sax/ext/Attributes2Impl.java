// Attributes2Impl.java - extended AttributesImpl
// http://www.saxproject.org
// Public Domain: no warranty.
// $Id$

package org.xml.sax.ext;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.AttributesImpl;


/**
 * SAX2 extension helper for additional Attributes information,
 * implementing the {@link Attributes2} interface.
 *
 * <blockquote>
 * <em>This module, both source code and documentation, is in the
 * Public Domain, and comes with <strong>NO WARRANTY</strong>.</em>
 * </blockquote>
 *
 * <p>This is not part of core-only SAX2 distributions.</p>
 *
 * <p>The <em>specified</em> flag for each attribute will always
 * be true, unless it has been set to false in the copy constructor
 * or using {@link #setSpecified}.  </p>
 *
 * @since SAX 2.0 (extensions 1.1 alpha)
 * @author David Brownell
 * @version TBS
 */
public class Attributes2Impl extends AttributesImpl implements Attributes2
{
    private boolean	flags [];


    /**
     * Construct a new, empty Attributes2Impl object.
     */
    public Attributes2Impl () { }


    /**
     * Copy an existing Attributes or Attributes2 object.
     * If the object implements Attributes2, values of the
     * <em>specified</em> flag for each attribute are copied,
     * otherwise the flag values are set to <em>true</em>. 
     *
     * <p>This constructor is especially useful inside a
     * {@link org.xml.sax.ContentHandler#startElement startElement} event.</p>
     *
     * @param atts The existing Attributes object.
     */
    public Attributes2Impl (Attributes atts)
    {
	super (atts);
    }


    ////////////////////////////////////////////////////////////////////
    // Implementation of Attributes2
    ////////////////////////////////////////////////////////////////////


    /**
     * Returns the current value of an attribute's "specified" flag.
     *
     * @param index The attribute index (zero-based).
     * @return current flag value
     * @exception java.lang.ArrayIndexOutOfBoundsException When the
     *            supplied index does not identify an attribute.
     */
    public boolean isSpecified (int index)
    {
	if (index < 0 || index >= getLength ())
	    throw new ArrayIndexOutOfBoundsException (
		"No attribute at index: " + index);
	return flags [index];
    }


    /**
     * Returns the current value of an attribute's "specified" flag.
     *
     * @param uri The Namespace URI, or the empty string if
     *        the name has no Namespace URI.
     * @param localName The attribute's local name.
     * @return current flag value
     * @exception java.lang.IllegalArgumentException When the
     *            supplied names do not identify an attribute.
     */
    public boolean isSpecified (String uri, String localName)
    {
	int index = getIndex (uri, localName);

	if (index < 0)
	    throw new IllegalArgumentException (
		"No such attribute: local=" + localName
		+ ", namespace=" + uri);
	return flags [index];
    }


    /**
     * Returns the current value of an attribute's "specified" flag.
     *
     * @param qName The XML 1.0 qualified name.
     * @return current flag value
     * @exception java.lang.IllegalArgumentException When the
     *            supplied name does not identify an attribute.
     */
    public boolean isSpecified (String qName)
    {
	int index = getIndex (qName);

	if (index < 0)
	    throw new IllegalArgumentException (
		"No such attribute: " + qName);
	return flags [index];
    }


    ////////////////////////////////////////////////////////////////////
    // Manipulators
    ////////////////////////////////////////////////////////////////////


    /**
     * Copy an entire Attributes object.  The "specified" flags are
     * assigned as true, unless the object is an Attributes2 object
     * in which case those values are copied.
     *
     * @see AttributesImpl#setAttributes
     */
    public void setAttributes (Attributes atts)
    {
	int length = atts.getLength ();

	super.setAttributes (atts);
	flags = new boolean [length];

	if (atts instanceof Attributes2) {
	    Attributes2	a2 = (Attributes2) atts;
	    for (int i = 0; i < length; i++)
		flags [i] = a2.isSpecified (i);
	} else {
	    for (int i = 0; i < length; i++)
		flags [i] = true;
	}

    }


    /**
     * Add an attribute to the end of the list, setting its
     * "specified" flag to true.  To set that flag's value
     * to false, use {@link #setSpecified}.
     *
     * @see AttributesImpl#addAttribute
     */
    public void addAttribute (String uri, String localName, String qName,
			      String type, String value)
    {
	super.addAttribute (uri, localName, qName, type, value);

	int length = getLength ();

	if (length < flags.length) {
	    boolean	newFlags [] = new boolean [length];
	    System.arraycopy (flags, 0, newFlags, 0, flags.length);
	    flags = newFlags;
	}

	flags [length - 1] = true;
    }


    // javadoc entirely from superclass
    public void removeAttribute (int index)
    {
	int origMax = getLength () - 1;

	super.removeAttribute (index);
	if (index != origMax)
	    System.arraycopy (flags, index + 1, flags, index,
		    origMax - index);
    }



    /**
     * Assign a value to the "specified" flag of a specific attribute.
     * This is the only way this flag can be cleared, except clearing
     * by initialization with the copy constructor.
     *
     * @param index The index of the attribute (zero-based).
     * @param value The desired flag value.
     * @exception java.lang.ArrayIndexOutOfBoundsException When the
     *            supplied index does not identify an attribute.
     */
    public void setSpecified (int index, boolean value)
    {
	if (index < 0 || index >= getLength ())
	    throw new ArrayIndexOutOfBoundsException (
		"No attribute at index: " + index);
	flags [index] = value;
    }
}
