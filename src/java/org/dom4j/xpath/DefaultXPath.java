/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.xpath;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.InvalidXPathException;
import org.dom4j.Node;
import org.dom4j.NodeFilter;
import org.dom4j.XPathException;
import org.jaxen.FunctionContext;
import org.jaxen.JaxenException;
import org.jaxen.NamespaceContext;
import org.jaxen.SimpleNamespaceContext;
import org.jaxen.VariableContext;
import org.jaxen.XPath;
import org.jaxen.dom4j.Dom4jXPath;
import org.saxpath.SAXPathException;


/** <p>Default implementation of {@link org.dom4j.XPath} which uses the
 * <a href="http://jaxen.org">Jaxen</a> project.</p>
 *
 *  @author bob mcwhirter (bob @ werken.com)
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
 */
public class DefaultXPath implements org.dom4j.XPath, NodeFilter, Serializable {

    private String text;
    private XPath xpath;
    private NamespaceContext namespaceContext;
    

    /** Construct an XPath
     */
    public DefaultXPath(String text) throws InvalidXPathException {
        this.text = text;
        this.xpath = parse( text );
    }

    public String toString() {
        return "[XPath: " + xpath + "]";
    }

    
    // XPath interface 
    
    /** Retrieve the textual XPath string used to initialize this Object
     *
     *  @return The XPath string
     */
    public String getText() {
        return text;
    }

    public FunctionContext getFunctionContext() {
        return xpath.getFunctionContext();
    }
    
    public void setFunctionContext(FunctionContext functionContext) {
        xpath.setFunctionContext(functionContext);
    }
    
    public NamespaceContext getNamespaceContext() {
        return namespaceContext;
    }
    
    public void setNamespaceURIs(Map map) {
        setNamespaceContext( new SimpleNamespaceContext( map ) );
    }
    
    public void setNamespaceContext(NamespaceContext namespaceContext) {
        this.namespaceContext = namespaceContext;
        xpath.setNamespaceContext(namespaceContext);
    }
    
    public VariableContext getVariableContext() {
        return xpath.getVariableContext();
    }
    
    public void setVariableContext(VariableContext variableContext) {
        xpath.setVariableContext(variableContext);
    }
    
    public Object evaluate(Object context) {
        try {
            setNSContext(context);
            List answer = xpath.selectNodes( context );
            if ( answer != null && answer.size() == 1 ) {
                return answer.get(0);
            }
            return answer;
        }
        catch (JaxenException e) {
            handleJaxenException(e);
            return null;
        }
    }

    public Object selectObject(Object context) {
        return evaluate(context);        
    }
    
    public List selectNodes(Object context) {
        try {
            setNSContext(context);
            return xpath.selectNodes( context );
        }
        catch (JaxenException e) {
            handleJaxenException(e);
            return Collections.EMPTY_LIST;
        }
    }
    
    
    public List selectNodes(Object context, org.dom4j.XPath sortXPath) {
        List answer = selectNodes( context );
        sortXPath.sort( answer );
        return answer;
    }
    
    public List selectNodes(Object context, org.dom4j.XPath sortXPath, boolean distinct) {
        List answer = selectNodes( context );
        sortXPath.sort( answer, distinct );
        return answer;
    }
    
    public Node selectSingleNode(Object context) {
        try {
            setNSContext(context);
            Object answer = xpath.selectSingleNode( context );
            if ( answer instanceof Node ) {
                return (Node) answer;
            }
            if ( answer == null ) {
                return null;
            }
            throw new XPathException( 
                "The result of the XPath expression is not a Node. It was: " 
                + answer + " of type: " + answer.getClass().getName() 
                + ". You might want to use a different method such as selectObject() to evaluate this XPath expression" 
            );
        }
        catch (JaxenException e) {
            handleJaxenException(e);
            return null;
        }
    }
    
    
    public String valueOf(Object context) {
        try {
            setNSContext(context);
            return xpath.valueOf( context );
        }
        catch (JaxenException e) {
            handleJaxenException(e);
            return "";
        }
    }

    public Number numberValueOf(Object context) {
        try {
            setNSContext(context);
            return xpath.numberValueOf( context );
        }
        catch (JaxenException e) {
            handleJaxenException(e);
            return null;
        }
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
        try {
            setNSContext(node);
            List answer = xpath.selectNodes( node );
            if ( answer != null && answer.size() > 0 ) {
                Object item = answer.get(0);
                if ( item instanceof Boolean ) {
                    return ((Boolean) item).booleanValue();
                }
                return answer.contains( node );
            }
            return false;
        }
        catch (JaxenException e) {
            handleJaxenException(e);
            return false;
        }
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
    
    protected static XPath parse(String text) {        
        try {
            return new Dom4jXPath( text );
        }
        catch (SAXPathException e) {
            throw new InvalidXPathException( text, e.getMessage() );
        }
        catch (RuntimeException e) {
        }
        throw new InvalidXPathException( text );
    }
    
    protected void setNSContext(Object context) {
        if ( namespaceContext == null ) {
            xpath.setNamespaceContext( DefaultNamespaceContext.create(context) );
        }
    }
        
    protected void handleJaxenException(JaxenException e) throws XPathException {
        throw new XPathException(text, e);
    }
}




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
