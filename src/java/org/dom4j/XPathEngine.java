package org.dom4j;

import java.util.List;

/** <p><code>XPathEngine</code> implements an XPath engine for
  * creating XPath objects and navigation using a DOM4J Document model.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface XPathEngine {

    /** <p><code>createXPath</code> parses an XPath expression
      * and creates a new XPath <code>XPath</code> instance.</p>
      *
      * @param xpathExpression is the XPath expression to evaluate
      * @return a new <code>XPath</code> instance
      */
    public XPath createXPath(String xpathExpression);
    
    /** <p><code>selectNodes</code> evaluates an XPath expression
      * on the current node and returns the result as a <code>List</code> of 
      * <code>Node</code> instances.</p>
      *
      * @param contextNode is the context node of this element on which to 
      *     process the XPath expression
      * @param xpath is the XPath expression to evaluate
      * @return a list of <code>Node</code> instances 
      */
    public List selectNodes(Node contextNode, XPath xpath);
    
    /** <p><code>selectSingleNode</code> evaluates an XPath expression
      * on the current node and returns the result as a single
      * <code>Node</code> instance.</p>
      *
      * @param contextNode is the context node of this element on which to 
      *     process the XPath expression
      * @param xpath is the XPath expression to evaluate
      * @return a single matching <code>Node</code> instance
      */
    public Node selectSingleNode(Node contextNode, XPath xpath);
    
}
