
package org.dom4j.xpath.impl;

import org.dom4j.xpath.ContextSupport;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.rule.Pattern;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class NodeTypeStep extends UnAbbrStep {
    
    private int _nodeType = Pattern.ANY_NODE;
    
    public NodeTypeStep(String axis, String nodeType) {
        super(axis);
        nodeType = nodeType.trim();                
        if ( "attribute".equals( nodeType ) ) {
            _nodeType = Node.ATTRIBUTE_NODE;
        }
        else if ( "comment".equals( nodeType ) ) { 
            _nodeType = Node.COMMENT_NODE;
        }
        else if ( "element".equals( nodeType ) ) {
            _nodeType = Node.ELEMENT_NODE;
        }
        else if ( "processing-instruction".equals( nodeType ) ) {
            _nodeType = Node.PROCESSING_INSTRUCTION_NODE;
        }
        else if ( "text".equals( nodeType ) ) {
            _nodeType = Node.TEXT_NODE;
        }
    }
    
    public NodeTypeStep(String axis, int nodeType) {
        super(axis);
        _nodeType = nodeType;
    }
    
    public List applyToNode(Object node) {
        if ( matches( node ) ) {
            List results = new ArrayList(1);
            if ( node instanceof Node ) {
                Node aNode = (Node) node;
                results.add( aNode.asXPathNode( null ) );
            }
            return results;
        }
        else {
            return Collections.EMPTY_LIST;
        }
    }
    
    public void applyToNodes(List results, Branch owner) {
        Iterator nodeIter = owner.nodeIterator();
        while ( nodeIter.hasNext() ) {
            Object each = nodeIter.next();
            if ( matches( each ) ) {
                Node node = (Node) each;
                Element parent = (owner instanceof Element)
                    ? (Element) owner : null;
                results.add( node.asXPathNode( parent ) );
            }
        }
    }
    
    public List applyToChild(Object node, ContextSupport support) {
        List results = new ArrayList();        
        if ( node instanceof Element ) {
            if ( isAbsolute() ) {
                applyToNodes( results, ((Element)node).getDocument() );
            }
            else {
                applyToNodes( results, (Element)node );
            }
        }
        else if ( node instanceof Document ) {
            applyToNodes( results, (Document)node );
        }        
        return results;
    }
    
    public List applyToSelf(Object node, ContextSupport support) {
        return applyToNode( node );
    }

    // Pattern methods
    
    public boolean matches( Context context, Node node ) {
        if ( matches( node ) ) {
            return super.matches( context, node );
        }
        return false;
    }
    
    public String toString() {
        return "[NodeTypeStep [" + _nodeType + "]]";
    }
    
    // Implementation methods
    
    protected boolean matches(Object object) {
        if ( object instanceof Node ) {
            return matches( (Node) object );
        }
        else if ( object instanceof String ) {
            return _nodeType == Node.TEXT_NODE;
        }
        return false;
    }
    
    protected boolean matches(Node node) {
        if ( _nodeType == Pattern.ANY_NODE ) {
            return true;
        }
        else {
            return _nodeType == node.getNodeType();
        }
    }
}
