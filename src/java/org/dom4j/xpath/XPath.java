package org.dom4j.xpath;

import org.dom4j.rule.Pattern;

import org.dom4j.xpath.parser.XPathLexer;
import org.dom4j.xpath.parser.XPathRecognizer;

import org.dom4j.xpath.impl.Context;
import org.dom4j.xpath.impl.Expr;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import org.antlr.CharBuffer;
import org.antlr.*;

import java.io.StringReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/** <p>Main run-time interface into the XPath functionality</p>
 * 
 *  <p>The XPath object embodies a textual XPath as described by
 *  the W3C XPath specification.  It can be applied against a
 *  context node (or nodeset) along with context-helpers to
 *  produce the result of walking the XPath.</p>
 *
 *  <p>Example usage:</p>
 *
 *  <pre>
 *  <code>
 *
 *      // Create a new XPath
 *      XPath xpath = new XPath("a/b/c/../d/.[@name="foo"]);
 *
 *      // Apply the XPath to your root context.
 *      Object results = xpath.applyTo(myContext);
 *
 *  </code>
 *  </pre>
 *
 *
 *  @see org.dom4j.xpath.ContextSupport
 *  @see org.dom4j.xpath.NamespaceContext
 *  @see org.dom4j.xpath.VariableContext
 *  @see org.dom4j.xpath.FunctionContext
 *  @see org.dom4j.xpath.XPathFunctionContext
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class XPath implements org.dom4j.XPath {

    private String      _xpath = "";
    private Expr        _expr  = null;

    /** Construct an XPath
     */
    public XPath(String xpath) {
        _xpath = xpath;
        parse();
    }

    public String toString() {
        return "[XPath: " + _xpath + " " + _expr + "]";
    }

    
    // XPath interface 
    
    /** Retrieve the textual XPath string used to initialize this Object
     *
     *  @return The XPath string
     */
    public String getText() {
        return _xpath;
    }

    /** <p><code>selectNodes</code> performs this XPath expression
      * on the given {@link Node} or {@link List} of {@link Node}s 
      * instances appending all the results together into a single list.</p>
      *
      * @param context is either a node or a list of nodes on which to 
      *    evalute the XPath
      * @return the results of all the XPath evaluations as a single list
      */
    public List selectNodes(Object context) {
        return applyTo( context );
    }
    
    
    /** <p><code>selectNodes</code> evaluates the XPath expression
      * on the given {@link Node} or {@link List} of {@link Node}s 
      * and returns the result as a <code>List</code> of 
      * <code>Node</code>s sorted by the sort XPath expression.</p>
      *
      * @param context is either a node or a list of nodes on which to 
      *    evalute the XPath
      * @param sortXPath is the XPath expression to sort by
      * @return a list of <code>Node</code> instances 
      */
    public List selectNodes(Object context, org.dom4j.XPath sortXPath) {
        List answer = applyTo( context );
        sortXPath.sort( answer );
        return answer;
    }
    
    /** <p><code>selectNodes</code> evaluates the XPath expression
      * on the given {@link Node} or {@link List} of {@link Node}s 
      * and returns the result as a <code>List</code> of 
      * <code>Node</code>s sorted by the sort XPath expression.</p>
      *
      * @param context is either a node or a list of nodes on which to 
      *    evalute the XPath
      * @param sortXPath is the XPath expression to sort by
      * @param distinct specifies whether or not duplicate values of the 
      *     sort expression are allowed. If this parameter is true then only 
      *     distinct sort expressions values are included in the result
      * @return a list of <code>Node</code> instances 
      */
    public List selectNodes(Object context, org.dom4j.XPath sortXPath, boolean distinct) {
        List answer = applyTo( context );
        sortXPath.sort( answer, distinct );
        return answer;
    }
    
    
    /** <p><code>selectSingleNode</code> evaluates this XPath expression
      * on the given {@link Node} or {@link List} of {@link Node}s 
      * and returns the result as a single <code>Node</code> instance.</p>
      *
      * @param context is either a node or a list of nodes on which to 
      *    evalute the XPath
      * @return a single matching <code>Node</code> instance
      */
    public Node selectSingleNode(Object context) {
        List answer = applyTo( context );
        if ( answer == null || answer.isEmpty() ) {
            return null;
        }
        return (Node) answer.get(0);
    }
    
    
    /** <p><code>valueOf</code> evaluates this XPath expression
      * and returns the textual representation of the results using the 
      * XPath string() function.</p>
      *
      * @param context is either a node or a list of nodes on which to 
      *    evalute the XPath
      * @return the string representation of the results of the XPath expression
      */
    public String valueOf(Object context) {
        if ( context instanceof Node )  {
            return valueOf( ContextSupport.BASIC_CONTEXT_SUPPORT, (Node) context );
        }
        else if ( context instanceof List ) {
            return valueOf( ContextSupport.BASIC_CONTEXT_SUPPORT, (List) context );
        }
        return "";
    }
    
    /** <p><code>sort</code> sorts the given List of Nodes
      * using this XPath expression as a {@link Comparator}.</p>
      *
      * @param list is the list of Nodes to sort
      */
    public void sort( List list ) {
        sort( list, false );
    }
    
    /** <p><code>sort</code> sorts the given List of Nodes
      * using this XPath expression as a {@link Comparator} 
      * and optionally removing duplicates.</p>
      *
      * @param list is the list of Nodes to sort
      * @param distinct if true then duplicate values (using the sortXPath for 
      *     comparisions) will be removed from the List
      */
    public void sort( List list, boolean distinct ) {
        if ( list != null && ! list.isEmpty() )  {
            int size = list.size();
            HashMap sortValues = new HashMap( size );
            for ( int i = 0; i < size; i++ ) {
                Object object = list.get(i);
                if ( object instanceof Node ) {
                    Node node = (Node) object;
                    Object expression = getCompareValue(node);
                    sortValues.put(node, expression);
                }
            }
            sort( list, sortValues );

            if (distinct) {
                removeDuplicates( list, sortValues );
            }
        }
    }
    
    public boolean matches( Node node ) {
        return _expr.matches( new Context(), node );
    }
    
    /** Sorts the list based on the sortValues for each node
      */
    protected void sort( List list, final Map sortValues ) {
        Collections.sort(
            list,
            new Comparator() {
                public int compare(Object o1, Object o2) {
                    o1 = sortValues.get(o1);
                    o2 = sortValues.get(o2);
                    if ( o1 == o2 ) {
                        return 0;
                    }
                    else if ( o1 instanceof Comparable )  {
                        Comparable c1 = (Comparable) o1;
                        return c1.compareTo(o2);
                    }
                    else if ( o1 == null )  {
                        return 1;
                    }
                    else if ( o2 == null ) {
                        return -1;
                    }
                    else {
                        return o1.equals(o2) ? 0 : -1;
                    }
                }
            }
        );
    }

    // Implementation methods
    
    
    
    /** Removes items from the list which have duplicate values
      */
    protected void removeDuplicates( List list, Map sortValues ) {
        // remove distinct
        HashSet distinctValues = new HashSet();
        for ( Iterator iter = list.iterator(); iter.hasNext(); ) {
            Object node = iter.next();
            Object value = sortValues.get(node);
            if ( distinctValues.contains( value ) ) {
                iter.remove();
            }
            else {
                distinctValues.add( value );
            }
        }
    }
    
    /** @return the node expression used for sorting comparisons
      */
    protected Object getCompareValue(Node node) {
        return valueOf( node );
    }
    
    
    
    private void parse() {

        StringReader reader   = new StringReader(_xpath);
        InputBuffer  buf      = new CharBuffer(reader);

        XPathLexer      lexer = new XPathLexer(buf);
        XPathRecognizer recog = new XPathRecognizer(lexer);

        try {
            _expr = recog.xpath();
        }
        catch (RecognitionException e) {
            e.printStackTrace();
        }
        catch (TokenStreamException e) {
            e.printStackTrace();
        }
    }
    
    protected List applyTo(Object context) {
        if ( context instanceof Node ) {
            return applyTo( (Node) context );
        }
        else if ( context instanceof List ) {
            return applyTo( (List) context );
        }
        return Collections.EMPTY_LIST;
    }
    

    public List applyTo(Node node) {
        return applyTo( 
            ContextSupport.BASIC_CONTEXT_SUPPORT, node 
        );
    }

    public List applyTo(List nodes) {
        return applyTo( 
            ContextSupport.BASIC_CONTEXT_SUPPORT, nodes 
        );
    }
    
    public List applyTo( ContextSupport contextSupport, Node node ) {
        return asList( 
            _expr.evaluate( new Context( node, contextSupport ) ) 
        );
    }

    /** Apply this XPath to a list of nodes
     *
     * @param contextSupport Walk-assisting state
     * @param nodes Root NodeSet context
     */
    public List applyTo( ContextSupport contextSupport, List nodes ) {
        return asList( 
            _expr.evaluate( new Context( nodes, contextSupport ) ) 
        );
    }
  
    /** Perform the string() function on the return values of an XPath
     */
    public String valueOf(ContextSupport contextSupport, Node node) {
        return _expr.valueOf( new Context( node, contextSupport ) );
    }

    public String valueOf(ContextSupport contextSupport, List nodes) {
        return _expr.valueOf( new Context( nodes, contextSupport ) );
    }

    
    /** A helper method to detect for non List return types */
    private final List asList(Object object)  {
        if ( object instanceof List )  {
            return (List) object;
        }
        else if ( object == null ) {
            return Collections.EMPTY_LIST;
        }
        ArrayList results = new ArrayList(1);
        results.add(object);
        return results;
    }
}
