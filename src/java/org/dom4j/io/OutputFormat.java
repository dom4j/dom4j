/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

/**
 * <p>
 * <code>OutputFormat</code> represents the format configuration used by
 * {@linkXMLWriter}and its base classes to format the XML output
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class OutputFormat implements Cloneable {
    /** standard value to indent by, if we are indenting */
    protected static final String STANDARD_INDENT = "  ";

    /**
     * Whether or not to suppress the XML declaration - default is
     * <code>false</code>
     */
    private boolean suppressDeclaration = false;

    /**
     * Whether or not to print new line after the XML declaration - default is
     * <code>true</code>
     */
    private boolean newLineAfterDeclaration = true;

    /** The encoding format */
    private String encoding = "UTF-8";

    /**
     * Whether or not to output the encoding in the XML declaration - default is
     * <code>false</code>
     */
    private boolean omitEncoding = false;

    /** The default indent is no spaces (as original document) */
    private String indent = null;

    /**
     * Whether or not to expand empty elements to
     * &lt;tagName&gt;&lt;/tagName&gt; - default is <code>false</code>
     */
    private boolean expandEmptyElements = false;

    /**
     * The default new line flag, set to do new lines only as in original
     * document
     */
    private boolean newlines = false;

    /** New line separator */
    private String lineSeparator = "\n";

    /** should we preserve whitespace or not in text nodes? */
    private boolean trimText = false;

    /** pad string-element boundaries with whitespace */
    private boolean padText = false;

    /** Whether or not to use XHTML standard. */
    private boolean doXHTML = false;

    /**
     * Controls when to output a line.separtor every so many tags in case of no
     * lines and total text trimming.
     */
    private int newLineAfterNTags = 0; // zero means don't bother.

    /** Quote character to use when writing attributes. */
    private char attributeQuoteChar = '\"';

    /**
     * Creates an <code>OutputFormat</code> with no additional whitespace
     * (indent or new lines) added. The whitespace from the element text content
     * is fully preserved.
     */
    public OutputFormat() {
    }

    /**
     * Creates an <code>OutputFormat</code> with the given indent added but no
     * new lines added. All whitespace from element text will be included.
     * 
     * @param indent
     *            is the indent string to be used for indentation (usually a
     *            number of spaces).
     */
    public OutputFormat(String indent) {
        this.indent = indent;
    }

    /**
     * Creates an <code>OutputFormat</code> with the given indent added with
     * optional newlines between the Elements. All whitespace from element text
     * will be included.
     * 
     * @param indent
     *            is the indent string to be used for indentation (usually a
     *            number of spaces).
     * @param newlines
     *            whether new lines are added to layout the
     */
    public OutputFormat(String indent, boolean newlines) {
        this.indent = indent;
        this.newlines = newlines;
    }

    /**
     * Creates an <code>OutputFormat</code> with the given indent added with
     * optional newlines between the Elements and the given encoding format.
     * 
     * @param indent
     *            is the indent string to be used for indentation (usually a
     *            number of spaces).
     * @param newlines
     *            whether new lines are added to layout the
     * @param encoding
     *            is the text encoding to use for writing the XML
     */
    public OutputFormat(String indent, boolean newlines, String encoding) {
        this.indent = indent;
        this.newlines = newlines;
        this.encoding = encoding;
    }

    public String getLineSeparator() {
        return lineSeparator;
    }

    /**
     * <p>
     * This will set the new-line separator. The default is <code>\n</code>.
     * Note that if the "newlines" property is false, this value is irrelevant.
     * To make it output the system default line ending string, call
     * <code>setLineSeparator(System.getProperty("line.separator"))</code>
     * </p>
     * 
     * @param separator
     *            <code>String</code> line separator to use.
     * 
     * @see #setNewlines(boolean)
     */
    public void setLineSeparator(String separator) {
        lineSeparator = separator;
    }

    public boolean isNewlines() {
        return newlines;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param newlines
     *            <code>true</code> indicates new lines should be printed,
     *            else new lines are ignored (compacted).
     * 
     * @see #setLineSeparator(String)
     */
    public void setNewlines(boolean newlines) {
        this.newlines = newlines;
    }

    public String getEncoding() {
        return encoding;
    }

    /**
     * DOCUMENT ME!
     * 
     * @param encoding
     *            encoding format
     */
    public void setEncoding(String encoding) {
        if (encoding != null) {
            this.encoding = encoding;
        }
    }

    public boolean isOmitEncoding() {
        return omitEncoding;
    }

    /**
     * <p>
     * This will set whether the XML declaration (<code>&lt;?xml version="1.0"
     * encoding="UTF-8"?&gt;</code>)
     * includes the encoding of the document. It is common to suppress this in
     * protocols such as WML and SOAP.
     * </p>
     * 
     * @param omitEncoding
     *            <code>boolean</code> indicating whether or not the XML
     *            declaration should indicate the document encoding.
     */
    public void setOmitEncoding(boolean omitEncoding) {
        this.omitEncoding = omitEncoding;
    }

    /**
     * <p>
     * This will set whether the XML declaration (<code>&lt;?xml version="1.0"
     * encoding="UTF-8"?&gt;</code>)
     * is included or not. It is common to suppress this in protocols such as
     * WML and SOAP.
     * </p>
     * 
     * @param suppressDeclaration
     *            <code>boolean</code> indicating whether or not the XML
     *            declaration should be suppressed.
     */
    public void setSuppressDeclaration(boolean suppressDeclaration) {
        this.suppressDeclaration = suppressDeclaration;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return true if the output of the XML declaration (<code>&lt;?xml
     *         version="1.0"?&gt;</code>)
     *         should be suppressed else false.
     */
    public boolean isSuppressDeclaration() {
        return suppressDeclaration;
    }

    /**
     * <p>
     * This will set whether a new line is printed after the XML declaration
     * (assuming it is not supressed.)
     * </p>
     * 
     * @param newLineAfterDeclaration
     *            <code>boolean</code> indicating whether or not to print new
     *            line following the XML declaration. The default is true.
     */
    public void setNewLineAfterDeclaration(boolean newLineAfterDeclaration) {
        this.newLineAfterDeclaration = newLineAfterDeclaration;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return true if a new line should be printed following XML declaration
     */
    public boolean isNewLineAfterDeclaration() {
        return newLineAfterDeclaration;
    }

    public boolean isExpandEmptyElements() {
        return expandEmptyElements;
    }

    /**
     * <p>
     * This will set whether empty elements are expanded from
     * <code>&lt;tagName&gt;</code> to
     * <code>&lt;tagName&gt;&lt;/tagName&gt;</code>.
     * </p>
     * 
     * @param expandEmptyElements
     *            <code>boolean</code> indicating whether or not empty
     *            elements should be expanded.
     */
    public void setExpandEmptyElements(boolean expandEmptyElements) {
        this.expandEmptyElements = expandEmptyElements;
    }

    public boolean isTrimText() {
        return trimText;
    }

    /**
     * <p>
     * This will set whether the text is output verbatim (false) or with
     * whitespace stripped as per <code>{@link
     * org.dom4j.Element#getTextTrim()}</code>.
     * </p>
     * 
     * <p>
     * </p>
     * 
     * <p>
     * Default: false
     * </p>
     * 
     * @param trimText
     *            <code>boolean</code> true=>trim the whitespace, false=>use
     *            text verbatim
     */
    public void setTrimText(boolean trimText) {
        this.trimText = trimText;
    }

    public boolean isPadText() {
        return padText;
    }

    /**
     * <p>
     * Ensure that text immediately preceded by or followed by an element will
     * be "padded" with a single space. This is used to allow make
     * browser-friendly HTML, avoiding trimText's transformation of, e.g.,
     * <code>The quick &lt;b&gt;brown&lt;/b&gt; fox</code> into <code>The
     * quick&lt;b&gt;brown&lt;/b&gt;fox</code>
     * (the latter will run the three separate words together into a single
     * word). This setting is not too useful if you haven't also called
     * {@link #setTrimText}.
     * </p>
     * 
     * <p>
     * The padding string will only be added if the text itself starts or ends
     * with some whitespace characters.
     * </p>
     * 
     * <p>
     * Default: false
     * </p>
     * 
     * @param padText
     *            <code>boolean</code> if true, pad string-element boundaries
     */
    public void setPadText(boolean padText) {
        this.padText = padText;
    }

    public String getIndent() {
        return indent;
    }

    /**
     * <p>
     * This will set the indent <code>String</code> to use; this is usually a
     * <code>String</code> of empty spaces. If you pass null, or the empty
     * string (""), then no indentation will happen.
     * </p>
     * Default: none (null)
     * 
     * @param indent
     *            <code>String</code> to use for indentation.
     */
    public void setIndent(String indent) {
        // nullify empty string to void unnecessary indentation code
        if ((indent != null) && (indent.length() <= 0)) {
            indent = null;
        }

        this.indent = indent;
    }

    /**
     * Set the indent on or off. If setting on, will use the value of
     * STANDARD_INDENT, which is usually two spaces.
     * 
     * @param doIndent
     *            if true, set indenting on; if false, set indenting off
     */
    public void setIndent(boolean doIndent) {
        if (doIndent) {
            this.indent = STANDARD_INDENT;
        } else {
            this.indent = null;
        }
    }

    /**
     * <p>
     * This will set the indent <code>String</code>'s size; an indentSize of
     * 4 would result in the indention being equivalent to the
     * <code>String</code> "&nbsp;&nbsp;&nbsp;&nbsp;" (four space characters).
     * </p>
     * 
     * @param indentSize
     *            <code>int</code> number of spaces in indentation.
     */
    public void setIndentSize(int indentSize) {
        StringBuffer indentBuffer = new StringBuffer();

        for (int i = 0; i < indentSize; i++) {
            indentBuffer.append(" ");
        }

        this.indent = indentBuffer.toString();
    }

    /**
     * <p>
     * Whether or not to use the XHTML standard: like HTML but passes an XML
     * parser with real, closed tags. Also, XHTML CDATA sections will be output
     * with the CDATA delimiters: ( &quot; <b>&lt;![CDATA[ </b>&quot; and &quot;
     * <b>]]&gt; </b>&quot; ) otherwise, the class HTMLWriter will output the
     * CDATA text, but not the delimiters.
     * </p>
     * 
     * <p>
     * Default is <code>false</code>
     * </p>
     * 
     * @return DOCUMENT ME!
     */
    public boolean isXHTML() {
        return doXHTML;
    }

    /**
     * <p>
     * This will set whether or not to use the XHTML standard: like HTML but
     * passes an XML parser with real, closed tags. Also, XHTML CDATA sections
     * will be output with the CDATA delimiters: ( &quot; <b>&lt;[CDATA[
     * </b>&quot; and &quot; <b>]]&lt; </b>) otherwise, the class HTMLWriter
     * will output the CDATA text, but not the delimiters.
     * </p>
     * 
     * <p>
     * Default: false
     * </p>
     * 
     * @param xhtml
     *            <code>boolean</code> true=>conform to XHTML, false=>conform
     *            to HTML, can have unclosed tags, etc.
     */
    public void setXHTML(boolean xhtml) {
        doXHTML = xhtml;
    }

    public int getNewLineAfterNTags() {
        return newLineAfterNTags;
    }

    /**
     * Controls output of a line.separator every tagCount tags when isNewlines
     * is false. If tagCount equals zero, it means don't do anything special. If
     * greater than zero, then a line.separator will be output after tagCount
     * tags have been output. Used when you would like to squeeze the html as
     * much as possible, but some browsers don't like really long lines. A tag
     * count of 10 would produce a line.separator in the output after 10 close
     * tags (including single tags).
     * 
     * @param tagCount
     *            DOCUMENT ME!
     */
    public void setNewLineAfterNTags(int tagCount) {
        newLineAfterNTags = tagCount;
    }

    public char getAttributeQuoteCharacter() {
        return attributeQuoteChar;
    }

    /**
     * Sets the character used to quote attribute values. The specified
     * character must be a valid XML attribute quote character, otherwise an
     * <code>IllegalArgumentException</code> will be thrown.
     * 
     * @param quoteChar
     *            The character to use when quoting attribute values.
     * 
     * @throws IllegalArgumentException
     *             If the specified character is not a valid XML attribute quote
     *             character.
     */
    public void setAttributeQuoteCharacter(char quoteChar) {
        if ((quoteChar == '\'') || (quoteChar == '"')) {
            attributeQuoteChar = quoteChar;
        } else {
            throw new IllegalArgumentException("Invalid attribute quote "
                    + "character (" + quoteChar + ")");
        }
    }

    /**
     * Parses command line arguments of the form <code>-omitEncoding
     * -indentSize 3 -newlines -trimText</code>
     * 
     * @param args
     *            is the array of command line arguments
     * @param i
     *            is the index in args to start parsing options
     * 
     * @return the index of first parameter that we didn't understand
     */
    public int parseOptions(String[] args, int i) {
        for (int size = args.length; i < size; i++) {
            if (args[i].equals("-suppressDeclaration")) {
                setSuppressDeclaration(true);
            } else if (args[i].equals("-omitEncoding")) {
                setOmitEncoding(true);
            } else if (args[i].equals("-indent")) {
                setIndent(args[++i]);
            } else if (args[i].equals("-indentSize")) {
                setIndentSize(Integer.parseInt(args[++i]));
            } else if (args[i].startsWith("-expandEmpty")) {
                setExpandEmptyElements(true);
            } else if (args[i].equals("-encoding")) {
                setEncoding(args[++i]);
            } else if (args[i].equals("-newlines")) {
                setNewlines(true);
            } else if (args[i].equals("-lineSeparator")) {
                setLineSeparator(args[++i]);
            } else if (args[i].equals("-trimText")) {
                setTrimText(true);
            } else if (args[i].equals("-padText")) {
                setPadText(true);
            } else if (args[i].startsWith("-xhtml")) {
                setXHTML(true);
            } else {
                return i;
            }
        }

        return i;
    }

    /**
     * A static helper method to create the default pretty printing format. This
     * format consists of an indent of 2 spaces, newlines after each element and
     * all other whitespace trimmed, and XMTML is false.
     * 
     * @return DOCUMENT ME!
     */
    public static OutputFormat createPrettyPrint() {
        OutputFormat format = new OutputFormat();
        format.setIndentSize(2);
        format.setNewlines(true);
        format.setTrimText(true);
        format.setPadText(true);

        return format;
    }

    /**
     * A static helper method to create the default compact format. This format
     * does not have any indentation or newlines after an alement and all other
     * whitespace trimmed
     * 
     * @return DOCUMENT ME!
     */
    public static OutputFormat createCompactFormat() {
        OutputFormat format = new OutputFormat();
        format.setIndent(false);
        format.setNewlines(false);
        format.setTrimText(true);

        return format;
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
