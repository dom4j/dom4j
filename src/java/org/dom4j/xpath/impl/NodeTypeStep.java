/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


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
                results.add( aNode.asXPathResult( null ) );
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
                results.add( node.asXPathResult( parent ) );
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
