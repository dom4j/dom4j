/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 *
 * $Id$
 */

package org.dom4j.io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Node;
import org.xml.sax.SAXException;

/** <p><code>HTMLWriter</code> takes a DOM4J tree and formats it to a
  * stream as HTML.
  * This formatter is similar to XMLWriter but it outputs the text of CDATA
  * and Entity sections rather than the serialised format as in XML,
  * it has an XHTML mode, it retains whitespace in certain elements such as &lt;PRE&gt;,
  * and it supports certain elements which have no corresponding close tag such
  * as for &lt;BR&gt; and &lt;P&gt;.
  *
  * <p> The OutputFormat passed in to the constructor is checked for isXHTML() and isExpandEmptyElements().
  *   See {@link OutputFormat OutputFormat} for details.  Here are the rules for
  * <b>this class</b> based on an OutputFormat, "format", passed in to the constructor:<br/><br/>
  *  <ul>
  *     <li>If an element is in {@link #getOmitElementCloseSet() getOmitElementCloseSet}, then it is treated specially:</li>
  *     <ul>
  *        <li>It never expands, since some browsers treat this as two separate Horizontal Rules: &lt;HR&gt;&lt;/HR&gt;</li>
  *        <li>If {@link org.dom4j.io.OutputFormat#isXHTML() format.isXHTML()}, then it has a space before the closing single-tag slash, since Netscape 4.x- treats this: &lt;HR /&gt; as
  *            an element named "HR" with an attribute named "/", but that's better than when it refuses to recognize this:  &lt;hr/&gt;
  *            which it thinks is an element named "HR/". </li>
  *     </ul>
  *     <li>If {@link org.dom4j.io.OutputFormat#isXHTML() format.isXHTML()}, all elements must have
  *         either a close element, or be a closed single tag.</li>
  *     <li>If {@link org.dom4j.io.OutputFormat#isExpandEmptyElements() format.isExpandEmptyElements()}() is true,
  *         all elements are expanded except as above.</li>
  *  </ul>
  * <b>Examples</b>
  *
  *  <table border="1" cellpadding="0" cellspacing="0">
  *    <tr>
  *      <th colspan="3" align="left">isXHTML == true</th>
  *    </tr>
  *    <tr>
  *      <td width="25">&#160;</td>
  *      <th align="left">isExpandEmptyElements == true</th>
  *      <td><code>
  *      &lt;td&gt;&lt;/td&gt;<br />
  *      &lt;br&#160;/&gt;<br />
  *      &lt;foo&gt;&lt;/foo&gt;</code>
  *      </td>
  *    </tr>
  *    <tr>
  *      <td width="25">&#160;</td>
  *      <th align="left">isExpandEmptyElements == false</th>
  *      <td><code>
  *      &lt;td/&gt;<br />
  *      &lt;br&#160;/&gt;<br />
  *      &lt;foo/&gt;</code>
  *      </td>
  *    </tr>
  *    <tr>
  *      <th colspan="3" align="left">isXHTML == false</th>
  *    </tr>
  *    <tr>
  *      <td width="25">&#160;</td>
  *      <th align="left">isExpandEmptyElements == true</th>
  *      <td><code>
  *      &lt;td&gt;&lt;/td&gt;<br />
  *      &lt;br&gt;<br />
  *      &lt;foo&gt;&lt;/foo&gt;</code>
  *      </td>
  *    </tr>
  *    <tr>
  *      <td width="25">&#160;</td>
  *      <th align="left">isExpandEmptyElements == false</th>
  *      <td><code>
  *      &lt;td/&gt;<br />
  *      &lt;br&gt;<br />
  *      &lt;foo/&gt;</code>
  *      </td>
  *    </tr>
  *  </table>
  *  <p>
  *   <p>
  *  If isXHTML == true, CDATA sections look like this:
  *    <PRE>
  *    <b>&lt;myelement&gt;&lt;![CDATA[My data]]&gt;&lt;/myelement&gt;</b>
  *    </PRE>
  *  Otherwise, they look like this:
  *   <PRE>
  *    <b>&lt;myelement&gt;My data&lt;/myelement&gt;</b>
  *   </PRE>
  *   </p>
  *
  * Basically, {@link org.dom4j.io.OutputFormat#isXHTML() OutputFormat.isXHTML()} == true will produce valid XML,
  *  while {@link org.dom4j.io.OutputFormat#isExpandEmptyElements() format.isExpandEmptyElements()}
  *  determines whether empty elements are expanded
  *  if isXHTML is true, excepting the special HTML single tags.
  * </p>
  *
  *
  * <p>Also, HTMLWriter handles tags whose contents should be preformatted, that is, whitespace-preserved.
  * By default, this set includes the tags &lt;PRE&gt;, &lt;SCRIPT&gt;, &lt;STYLE&gt;, and &lt;TEXTAREA&gt;, case insensitively.
  * It does not include &lt;IFRAME&gt;.
  * Other tags, such as &lt;CODE&gt;, &lt;KBD&gt;, &lt;TT&gt;, &lt;VAR&gt;, are usually rendered in a different font in most browsers,
  * but don't preserve whitespace, so they also don't appear in the default list.  HTML Comments
  * are always whitespace-preserved.  However, the parser you use may store comments with linefeed-only
  * text nodes (\n) even if your platform uses another line.separator character, and HTMLWriter outputs
  * Comment nodes exactly as the DOM is set up by the parser.
  * See examples and discussion here: {@link #setPreformattedTags(java.util.Set) setPreformattedTags}</p>
  *
  * <p><b>Examples</b></p>
  *  <blockquote>
  * <p><b>Pretty Printing</b></p>
  * <p>This example shows how to pretty print a string containing a valid HTML document to a string.
  *     You can also just call the static methods of this class:<br/>
  *        {@link #prettyPrintHTML(String) prettyPrintHTML(String)}
  *     or<br/>
  *        {@link #prettyPrintHTML(String,boolean,boolean,boolean,boolean) prettyPrintHTML(String,boolean,boolean,boolean,boolean)}
  *     or, <br/>
  *        {@link #prettyPrintXHTML(String) prettyPrintXHTML(String)} for XHTML (note the X)
  *     </p>
  *     <pre>
  *       String testPrettyPrint(String html){
  *           StringWriter sw = new StringWriter();
  *           org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
  *           <font color='green'>//These are the default formats from createPrettyPrint, so you needn't set them:</font>
  *           <font color='green'>//  format.setNewlines(true);</font>
  *           <font color='green'>//  format.setTrimText(true);</font>
  *           format.setXHTML(true);  <font color='green'>//Default is false, this produces XHTML</font>
  *           org.dom4j.io.HTMLWriter writer = new org.dom4j.io.HTMLWriter(sw, format);
  *           org.dom4j.Document document = org.dom4j.DocumentHelper.parseText(html);
  *           writer.write(document);
  *           writer.flush();
  *           return sw.toString();
  *       }
  *     </pre>
  *
  *     <p>This example shows how to create a "squeezed" document, but one that will work in browsers
  *      even if the browser line length is limited.  No newlines are included, no extra whitespace
  *      at all, except where it it required by {@link #setPreformattedTags(java.util.Set) setPreformattedTags}.
  *     </p>
  *     <pre>
  *       String testCrunch(String html){
  *           StringWriter sw = new StringWriter();
  *           org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
  *           format.setNewlines(false);
  *           format.setTrimText(true);
  *           format.setIndent("");
  *           format.setXHTML(true);
  *           format.setExpandEmptyElements(false);
  *           format.setNewLineAfterNTags(20); <font color='green'>//print a line every so often.</font>
  *           org.dom4j.io.HTMLWriter writer = new org.dom4j.io.HTMLWriter(sw, format);
  *           org.dom4j.Document document = org.dom4j.DocumentHelper.parseText(html);
  *           writer.write(document);
  *           writer.flush();
  *           return sw.toString();
  *       }
  *     </pre>
  *
  *  </blockquote>
  *
  * </p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a> (james.strachan@metastuff.com)
  * @author Laramie Crocker
  * @version $Revision$
 */
public class HTMLWriter extends XMLWriter {

    public HTMLWriter(Writer writer) {
        super( writer, defaultHtmlFormat );
    }

    public HTMLWriter(Writer writer, OutputFormat format) {
        super( writer, format );
    }

    public HTMLWriter() throws UnsupportedEncodingException {
        super( defaultHtmlFormat );
    }

    public HTMLWriter(OutputFormat format) throws UnsupportedEncodingException {
        super( format );
    }

    public HTMLWriter(OutputStream out) throws UnsupportedEncodingException {
        super( out, defaultHtmlFormat );
    }

    public HTMLWriter(OutputStream out, OutputFormat format) throws UnsupportedEncodingException {
        super( out, format );
    }


    //Allows us to the current state of the format in this struct on the m_formatStack.
    private class FormatState {
        public FormatState(boolean newLines, boolean trimText, String indent){
            this.m_Newlines = newLines;
            this.m_TrimText = trimText;
            this.m_indent = indent;
        }
        private boolean m_Newlines = false;
        public boolean isNewlines(){return m_Newlines;}
        private boolean m_TrimText = false;
        public boolean isTrimText(){return m_TrimText;}
        private String  m_indent = "";
        public String  getIndent(){return m_indent;}
    }



    private java.util.Stack m_formatStack = new java.util.Stack();

    private static String m_lineSeparator = System.getProperty("line.separator");

    private String m_lastText = "";

    private int m_tagsOuput = 0;

    private int m_newLineAfterNTags = -1;  //legal values are 0+, but -1 signifies lazy initialization.

    protected static final HashSet defaultPreformattedTags;

    static {
        //If you change this list, update the javadoc examples, above in the class javadoc,
        //   in writeElement, and in setPreformattedTags().
        defaultPreformattedTags = new HashSet();
        defaultPreformattedTags.add("PRE");
        defaultPreformattedTags.add("SCRIPT");
        defaultPreformattedTags.add("STYLE");
        defaultPreformattedTags.add("TEXTAREA");
    }

    private HashSet preformattedTags = defaultPreformattedTags;

    protected static final OutputFormat defaultHtmlFormat;

    static {
        defaultHtmlFormat = new OutputFormat( "  ", true );
        defaultHtmlFormat.setTrimText( true );
        defaultHtmlFormat.setSuppressDeclaration( true );
    }

    /** Used to store the qualified element names which
      * should have no close element tag
      */
    private HashSet omitElementCloseSet; //keep as a HashSet, but only show as a Set when asked for by getOmitElementCloseSet().

    public void startCDATA() throws SAXException {
    }

    public void endCDATA() throws SAXException {
    }

    // Overloaded methods

    // laramiec 3/21/2002 added isXHTML() stuff so you get the CDATA brackets if you desire.
    protected void writeCDATA(String text) throws IOException {
        // XXX: Should we escape entities?
        // writer.write( escapeElementEntities( text ) );
        if ( getOutputFormat().isXHTML() ) {
            super.writeCDATA(text);
        } else {
            writer.write( text );
        }
        lastOutputNodeType = Node.CDATA_SECTION_NODE;
    }

    protected void writeEntity(Entity entity) throws IOException {
        writer.write(entity.getText());
        lastOutputNodeType = Node.ENTITY_REFERENCE_NODE;
    }

    protected void writeDeclaration() throws IOException {
    }

    protected void writeString(String text) throws IOException {
        //DOM stores \n at the end of text nodes that are newlines.  This is significant if
        // we are in a PRE section.  However, we only want to output the system line.separator, not \n.
        // This is a little brittle, but this function appears to be called with these lineseparators
        // as a separate TEXT_NODE.  If we are in a preformatted section, output the right line.separator,
        // otherwise ditch.  If the single \n character is not the text, then do the super thing
        // to output the text.
        // Also, we store the last text that was not a \n since it may be used by writeElement in this class to
        // line up preformatted tags.
        if ( text.equals("\n")){
            if ( ! m_formatStack.empty() ) {
                super.writeString(m_lineSeparator);
            }
            return;
        }
        m_lastText = text;
        if ( m_formatStack.empty() ) {
            super.writeString(text.trim());
        } else {
            super.writeString(text);
        }
    }

    /** Overriden method to not close certain element names to avoid
      * wierd behaviour from browsers for versions up to 5.x
      */
    protected void writeClose(String qualifiedName) throws IOException {
        if ( ! omitElementClose( qualifiedName ) ) {
            super.writeClose(qualifiedName);
        }
    }

    protected void writeEmptyElementClose(String qualifiedName) throws IOException {
        if (getOutputFormat().isXHTML()){
            //xhtml, always check with format object whether to expand or not.
            if ( omitElementClose(qualifiedName) ) {
                // it was a special omit tag, do it the XHTML way: "<br/>", ignoring the expansion option,
                // since <br></br> is OK XML, but produces twice the linefeeds desired in the browser.
                // for netscape 4.7, though all are fine with it, write a space before the close slash.
                writer.write(" />");
            } else {
                super.writeEmptyElementClose(qualifiedName);
            }
        } else {
            //html, not xhtml
            if ( omitElementClose(qualifiedName) ) {
                // it was a special omit tag, do it the old html way: "<br>".
                writer.write(">");
            } else {
                // it was NOT a special omit tag, check with format object whether to expand or not.
                super.writeEmptyElementClose(qualifiedName);
            }
        }
    }

    protected boolean omitElementClose( String qualifiedName ) {
        return internalGetOmitElementCloseSet().contains( qualifiedName.toUpperCase() );
    }

    private HashSet internalGetOmitElementCloseSet() {
        if (omitElementCloseSet == null) {
            omitElementCloseSet = new HashSet();
            loadOmitElementCloseSet(omitElementCloseSet);
        }
        return omitElementCloseSet;
    }

    //If you change this, change the javadoc for getOmitElementCloseSet.
    protected void loadOmitElementCloseSet(Set set) {
        set.add( "AREA" );
        set.add( "BASE" );
        set.add( "BR" );
        set.add( "COL" );
        set.add( "HR" );
        set.add( "IMG" );
        set.add( "INPUT" );
        set.add( "LINK" );
        set.add( "META" );
        set.add( "P" );
        set.add( "PARAM" );
    }

    //let the people see the set, but not modify it.
    /** A clone of the Set of elements that can have their close-tags omitted.  By default it
     *  should be
     *   "AREA",
     *   "BASE",
     *   "BR",
     *   "COL",
     *   "HR",
     *   "IMG",
     *   "INPUT",
     *   "LINK",
     *   "META",
     *   "P",
     *   "PARAM"
     *  @return A clone of the Set.
     */
    public Set getOmitElementCloseSet(){
        return (Set)(internalGetOmitElementCloseSet().clone());
    }

    /** To use the empty set, pass an empty Set, or null:
     *  <pre>
     *     setOmitElementCloseSet(new HashSet());
     *   or
     *     setOmitElementCloseSet(null);
     * </pre>
     */
    public void setOmitElementCloseSet(Set newSet){
        omitElementCloseSet = new HashSet();  //resets, and safely empties it out if newSet is null.
        if (newSet != null){
            omitElementCloseSet = new HashSet();
            Object aTag;
            Iterator iter = newSet.iterator();
            while ( iter.hasNext() ) {
                aTag = iter.next();
                if (aTag != null){
                    omitElementCloseSet.add(aTag.toString().toUpperCase());
                }
            }

        }
    }


    protected String getPadText() {
        return " ";
    }

    /** @see #setPreformattedTags(java.util.Set) setPreformattedTags
     */
    public Set getPreformattedTags(){
        return (Set)(preformattedTags.clone());
    }

    /**
      * <p>Override the default set, which includes PRE, SCRIPT, STYLE, and TEXTAREA, case insensitively.</p>
      *
      * <p><b>Setting Preformatted Tags</b></p>
      *
      *
      * <p>Pass in a Set of Strings, one for each tag name that should be treated like a PRE tag.
      *  You may pass in null or an empty Set to assign the empty set, in which case no tags
      *  will be treated as preformatted, except that HTML Comments will continue to be preformatted.
      *  If a tag is included in the set of preformatted tags, all whitespace within the tag will be preserved,
      *  including whitespace on the same line preceding the close tag.  This will generally make the close tag
      *  not line up with the start tag, but it preserves the intention of the whitespace within the tag.
      *  </p>
      *  <p>The browser considers leading whitespace before the close tag to be significant,
      *  but leading whitespace before the open tag to be insignificant.
      *  For example, if the HTML author doesn't put the close TEXTAREA tag flush to the left margin,
      *  then the TEXTAREA control in the browser will have spaces on the last line inside the control.  This may be
      *  the HTML author's intent.  Similarly, in a PRE, the browser treats a flushed left close PRE tag as different from
      *  a close tag with leading whitespace.  Again, this must be left up to the HTML author.</p>
      *
      * <p><b>Examples</b></p>
      *  <blockquote>
      * <p>
      * Here is an example of how you can set the PreformattedTags list using setPreformattedTags
      * to include IFRAME, as well as the default set,
      * if you have an instance of this class named myHTMLWriter:
      * <pre>
      *     Set current = myHTMLWriter.getPreformattedTags();
      *     current.add("IFRAME");
      *     myHTMLWriter.setPreformattedTags(current);
      *
      *     <font color='green'>//The set is now <b>{PRE, SCRIPT, STYLE, TEXTAREA, IFRAME}</b></font>
      * </pre>
      *
      * Similarly, you can simply replace it with your own:
      * <pre>
      *     HashSet newset = new HashSet();
      *     newset.add("PRE");
      *     newset.add("TEXTAREA");
      *     myHTMLWriter.setPreformattedTags(newset);
      *
      *     <font color='green'>//The set is now <b>{PRE, TEXTAREA}</b></font>
      * </pre>
      *
      * You can remove all tags from the preformatted tags list, with an empty set, like this:
      * <pre>
      *     myHTMLWriter.setPreformattedTags(new HashSet());
      *
      *     <font color='green'>//The set is now <b>{}</b></font>
      * </pre>
      *
      * or with null, like this:
      * <pre>
      *     myHTMLWriter.setPreformattedTags(null);
      *
      *     <font color='green'>//The set is now <b>{}</b></font>
      * </pre>
      *
      * </blockquote>
      *
     */
    public void setPreformattedTags(Set newSet){
        // no fancy merging, just set it, assuming they did a getExcludeTrimTags()
        //   first if they wanted to preserve the default set.
        preformattedTags = new HashSet();  //resets, and safely empties it out if newSet is null.
        if ( newSet != null ) {
            Object aTag;
            Iterator iter = newSet.iterator();
            while ( iter.hasNext() ) {
                aTag = iter.next();
                if (aTag != null){
                    preformattedTags.add(aTag.toString().toUpperCase());
                }
            }
        }
    }


    /**
     * @return true if the qualifiedName passed in matched (case-insensitively)
     * a tag in the preformattedTags set,
     * or false if not found or if the set is empty or null.
     * @see #setPreformattedTags(java.util.Set) setPreformattedTags
     */
    public boolean isPreformattedTag(String qualifiedName){
        //A null set implies that the user called setPreformattedTags(null), which means they want
        //no tags to be preformatted.
        return (preformattedTags != null) && (preformattedTags.contains(qualifiedName.toUpperCase()));
    }

    /** This override handles any elements that should not remove whitespace,
     *  such as &lt;PRE&gt;, &lt;SCRIPT&gt;, &lt;STYLE&gt;, and &lt;TEXTAREA&gt;.
     *  Note: the close tags won't line up with the open tag, but we can't alter that.
     * See javadoc note at setPreformattedTags.
     *
     * @see #setPreformattedTags(java.util.Set) setPreformattedTags
     *  @throws java.io.IOException When the stream could not be written to.
     *
     */
    protected void writeElement(Element element) throws IOException {
        if ( m_newLineAfterNTags == -1 ) { //lazy initialization check
            lazyInitNewLinesAfterNTags();
        }
        if ( m_newLineAfterNTags > 0 ) {
            if ( (m_tagsOuput>0) && (m_tagsOuput % m_newLineAfterNTags == 0)) {
                super.writer.write(m_lineSeparator);
            }
        }
        m_tagsOuput++;

        String qualifiedName = element.getQualifiedName();
        String saveLastText = m_lastText;
        int size = element.nodeCount();
        if (  isPreformattedTag(qualifiedName)  ) {
            OutputFormat currentFormat = getOutputFormat();
            boolean saveNewlines = currentFormat.isNewlines();
            boolean saveTrimText = currentFormat.isTrimText();
            String currentIndent = currentFormat.getIndent();
            //You could have nested PREs, or SCRIPTS within PRE... etc., therefore use push and pop.
            m_formatStack.push(new FormatState(saveNewlines, saveTrimText, currentIndent));
            try {
                super.writePrintln(); //do this manually, since it won't be done while outputting the tag.
                if ( saveLastText.trim().length() == 0 && currentIndent != null && currentIndent.length()>0) {
                    //We are indenting, but we want to line up with the close tag.
                    //m_lastText was the indent (whitespace, no \n) before the preformatted start tag.
                    //So write it out instead of the current indent level.  This makes it line up with its
                    //close tag.
                    super.writer.write(justSpaces(saveLastText));
                }
                currentFormat.setNewlines(false);//actually, newlines are handled in this class by writeString, depending on if the stack is empty.
                currentFormat.setTrimText(false);
                currentFormat.setIndent("");
                //This line is the recursive one:
                super.writeElement(element);
            } finally {
                FormatState state = (FormatState)m_formatStack.pop();
                currentFormat.setNewlines(state.isNewlines());
                currentFormat.setTrimText(state.isTrimText());
                currentFormat.setIndent(state.getIndent());
            }
        } else {
            super.writeElement(element);
        }
    }

    private String justSpaces(String text){
        int size = text.length();
        StringBuffer res = new StringBuffer(size);
        char c;
        for (int i=0; i < size; i++) {
            c = text.charAt(i);
            switch ( c ) {
                case '\r':
                case '\n':
                    continue;
                default:
                    res.append(c);
            }
        }
        return res.toString();
    }

    private void lazyInitNewLinesAfterNTags(){
        if ( getOutputFormat().isNewlines() ) {
            m_newLineAfterNTags = 0; //don't bother, newlines are going to happen anyway.
        } else {
            m_newLineAfterNTags = getOutputFormat().getNewLineAfterNTags();
        }
    }

    // Convenience methods, static, with bunch-o-defaults

    /** Convenience method to just get a String result.
     *
     *  @return a pretty printed String from the source string,
     *  preserving whitespace in the defaultPreformattedTags set,
     *  and leaving the close tags off of the default omitElementCloseSet set.
     *
     *  Use one of the write methods if you want stream output.
     *  @throws java.io.IOException
     *  @throws java.io.UnsupportedEncodingException
     *  @throws org.dom4j.DocumentException
     */
    public static String prettyPrintHTML(String html)
    throws java.io.IOException, java.io.UnsupportedEncodingException, org.dom4j.DocumentException {
        return prettyPrintHTML(html, true, true, false, true);
    }

    /** Convenience method to just get a String result, but <b>As XHTML</b>.
     *
     *  @return a pretty printed String from the source string,
     *  preserving whitespace in the defaultPreformattedTags set,
     *  but conforming to XHTML: no close tags are omitted (though if empty, they will
     *  be converted to XHTML empty tags: &lt;HR/&gt;
     *
     *  Use one of the write methods if you want stream output.
     *  @throws java.io.IOException
     *  @throws java.io.UnsupportedEncodingException
     *  @throws org.dom4j.DocumentException
     */
    public static String prettyPrintXHTML(String html)
    throws java.io.IOException, java.io.UnsupportedEncodingException, org.dom4j.DocumentException {
        return prettyPrintHTML(html, true, true, true, false);
    }

    /** @return a pretty printed String from the source string,
     *  preserving whitespace in the defaultPreformattedTags set,
     *  and leaving the close tags off of the default omitElementCloseSet set.
     *  This override allows you to specify various formatter options.
     *  Use one of the write methods if you want stream output.
     *  @throws java.io.IOException
     *  @throws java.io.UnsupportedEncodingException
     *  @throws org.dom4j.DocumentException
     */
    public static String prettyPrintHTML(String html,
                                         boolean newlines,
                                         boolean trim,
                                         boolean isXHTML,
                                         boolean expandEmpty)
    throws java.io.IOException, java.io.UnsupportedEncodingException, org.dom4j.DocumentException {
        StringWriter sw = new StringWriter();
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setNewlines(newlines);
        format.setTrimText(trim);
        format.setXHTML(isXHTML);
        format.setExpandEmptyElements(expandEmpty);
        HTMLWriter writer = new HTMLWriter(sw, format);
        Document document = DocumentHelper.parseText(html);
        writer.write(document);
        writer.flush();
        return sw.toString();
    }

}

//==================== Test xml file: ================================
/*
<html>
<head>
<title>My Title</title>
    <style>
        .foo {
            text-align: Right;
        }
    </style>
    <script>
        function mojo(){
            return "bar";
        }
    </script>
    <script language="JavaScript">
    <!--
        //this is the canonical javascript hiding.
        function foo(){
            return "foo";
        }
    //-->
    </script>
</head>
<!-- this
   is a comment
   -->
<body bgcolor="#A4BFDD" mojo="&amp;">
entities: &#160; &amp; &quot; &lt; &gt; %23
<p></p>
<mojo></mojo>
<foo />
<table border="1">
<tr>
<td>
<pre>line0
    <hr />
    line1
    <b>line2, should line up, indent-wise</b>
    line 3
    line 4
     </pre>
</td>
<td></td>
</tr>
</table>
<myCDATAElement><![CDATA[My data]]></myCDATAElement>
</body>
</html>
*/
//====================================================




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */