package org.dom4j;

import java.io.PrintWriter;
import java.util.List;

/** <p><code>Node</code> defines the polymorphic behavior 
  * for all XML nodes in a DOM4J tree.</p>
  *
  * <p>A node may optionally support the parent relationship and may be 
  * read only.</p>
  *
  * @see #supportsParent 
  * @see #isReadOnly
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface Node extends Cloneable {

    
    /** <p><code>supportsParent</code> returns true if this node supports the 
      * parent relationship.</p>
      * 
      * <p>Some XML tree implementations are singly linked and only support
      * downward navigation through children relationships. 
      * The default case is that both parent and children relationships are
      * supported though for memory and performance reasons the parent
      * relationship may not be supported.
      * </p>
      *
      * @return true if this node supports the parent relationship
      * or false it is not supported
      */
    public boolean supportsParent();

    /** <p><code>getParent</code> returns the parent <code>Element</code> 
      * if this node supports the parent relationship or null if it is 
      * the root element or does not support the parent relationship.</p>
      *
      * <p>This method is an optional feature and may not be supported
      * for all <code>Node</code> implementations.</p>
      *
      * @return the parent of this node or null if it is the root of the 
      * tree or the parent relationship is not supported.
      */
    public Element getParent();

    /** <p><code>setParent</code> sets the parent relationship of
      * this node if the parent relationship is supported or does nothing
      * if the parent relationship is not supported.</p>
      *
      * <p>This method should only be called from inside an 
      * <code>Element</code> implementation method and is not intended for 
      * general use.</p>
      *
      * @param parent is the new parent of this node.
      */
    public void setParent(Element parent);
    

    /** <p><code>getDocument</code> returns the <code>Document<code>
      * that this <code>Node</code> is part of if this node supports
      * the parent relationship.</p>
      *
      * <p>This method is an optional feature and may not be supported
      * for all <code>Node</code> implementations.</p>
      *
      * @return the document of this node or null if this feature is not 
      * supported or the node is not associated with a <code>Document</code>
      */
    public Document getDocument();

    /** <p><code>setDocument</code> sets the document of this node if the 
      * parent relationship is supported or does nothing if the parent 
      * relationship is not supported.</p>
      *
      * <p>This method should only be called from inside a
      * <code>Document</code> implementation method and is not intended for 
      * general use.</p>
      *
      * @param document is the new document of this node.
      */
    public void setDocument(Document document);
    
    
    /** <p><code>isReadOnly</code> returns true if this node is read only
      * and cannot be modified. 
      * Any attempt to modify a read-only <code>Node</code> will result in 
      * an <code>UnsupportedOperationException</code> being thrown.</p>
      *
      * @return true if this <code>Node</code> is read only 
      * and cannot be modified otherwise false.
      */
    public boolean isReadOnly();

    
    /** <p><code>getName</code> returns the name of this node.
      * This is the XML local name of the element, attribute, entity or 
      * processing instruction. 
      * For CDATA and Text nodes this method will return null.</p>
      *
      * @return the XML name of this node
      */
    public String getName();    

    
   /** <p>Sets the text data of this node or this method will 
     * throw an <code>UnsupportedOperationException</code> if it is 
     * read-only.</p>
     *
     * @param name is the new name of this node
     */
    public void setName(String name);

    /** <p>Returns the text of this node.</p>
      *
      * @return the text for this node.
      */
    public String getText();
    
   /** <p>Sets the text data of this node or this method will 
     * throw an <code>UnsupportedOperationException</code> if it is 
     * read-only.</p>
     *
     * @param text is the new textual value of this node
     */
    public void setText(String text);
    
    
    /** <p><code>asXML</code> returns a representation of this node as 
      * an XML <code>String</code> which is equivalent to the 
      * <code>string()<code> function in XPath.</p>
      *
      * @return the XML representation of this node
      */
    public String asXML();    

    /** <p><code>writeXML</code> writes this node as XML which is 
      * equivalent to the <code>string()<code> function in XPath.</p>
      *
      * @param writer is the <code>PrintWriter</code> to output the XML to
      */
    public void writeXML(PrintWriter writer);    

    
    
    /** <p><code>selectNodes</code> evaluates an XPath expression and returns 
      * the result as a <code>List</code> of <code>Node</code> instances or 
      * <code>String</code> instances depending on the XPath expression.</p>
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @return the list of <code>Node</code> or <code>String</code> instances 
      * depending on the XPath expression
      */
    public List selectNodes(String xpathExpression);
    
    /** <p><code>selectNodes</code> evaluates an XPath expression and returns 
      * the result as a <code>List</code> of <code>Node</code> instances or 
      * <code>String</code> instances depending on the XPath expression.</p>
      *
      * @param xpath is the XPath expression to be evaluated
      * @return the list of <code>Node</code> or <code>String</code> instances 
      * depending on the XPath expression
      */
    public List selectNodes(XPath xpath);
    
    /** <p><code>selectSingleNode</code> evaluates an XPath expression
      * and returns the result as a single <code>Node</code> instance.</p>
      *
      * @param xpathExpression is the XPath expression to be evaluated
      * @return the <code>Node<code> matching the XPath expression
      */
    public Node selectSingleNode(String xpathExpression);

    /** <p><code>selectSingleNode</code> evaluates an XPath expression
      * and returns the result as a single <code>Node</code> instance.</p>
      *
      * @param xpath is the XPath expression to be evaluated
      * @return the <code>Node<code> matching the XPath expression
      */
    public Node selectSingleNode(XPath xpath);

    /** <p><code>asXPathNode</code> returns an XPath compatable version
      * of iteself. If it supports the parent relationship it will return 
      * itself otherwise it will create a new node which is linked to its 
      * parent.
      *
      * @return a <code>Node<code> which supports the parent relationship
      */
    public Node asXPathNode(Element parent);

    
    /** <p><code>accept</code> is the method used in the Visitor Pattern.</p>
      *
      * @param visitor is the visitor in the Visitor Pattern
      */
    public void accept(TreeVisitor visitor);

    
    
    /** <p><code>clone</code> will return a deep clone or if this node is
      * read-only then clone will return the same instance.
      *
      * @@return a deep clone of myself or myself if I am read only.
      */
    public Object clone();
}