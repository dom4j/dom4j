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
import org.dom4j.xpath.util.Partition;
import org.dom4j.xpath.impl.Context;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;

import org.saxpath.Axis;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public abstract class UnAbbrStep extends Step {
    
    private Axis _axis;
    private int _axisCode;
    private List _predicates;
    
    public UnAbbrStep(Axis axis) {
        _axis = axis;
        _axisCode = axis.getEnumValue();
    }    
    
    public Axis getAxis() {
        return _axis;
    }
    
    public int getAxisCode() {
        return _axisCode;
    }
    
    public void addPredicate(org.jaxpath.expr.Predicate pred) {
        if ( _predicates == null ) {
            _predicates = new ArrayList();
        }        
        _predicates.add(pred);        
    }
    
    public List getPredicates() {
        if ( _predicates == null ) {
            return Collections.EMPTY_LIST;
        }        
        return _predicates;
    }
    
    
    public Context applyTo(Context context) {
        if ( context.isEmpty() ) {
            return context;
        }
        List results = applyTo(
            context.getNodeSet(), context.getContextSupport(), _axisCode, true
        );        
        context.setNodeSet(results);
        return context;
    }
    
    public List applyTo(List nodeSet, ContextSupport support, int axis) {
        return applyTo(nodeSet, support, axis, false);
    }
    
    public List applyTo(List nodeSet, ContextSupport support, int axisCode, boolean doPreds) {
        List aggregateResults = new ArrayList();
        
        for ( int i = 0, size = nodeSet.size(); i < size; i++ ) {
            Object each = nodeSet.get(i);
            List results = null;            
            
            switch ( axisCode ) {
                case Axis.Enum.SELF:
                    results = applyToSelf( each, support );
                    break;
                case Axis.Enum.ANCESTOR:
                    results = applyToAncestor( each, support );
                    break;
                case Axis.Enum.ANCESTOR_OR_SELF:
                    results = applyToAncestorOrSelf( each, support );
                    break;
                case Axis.Enum.ATTRIBUTE:
                    results = applyToAttribute( each, support );
                    break;
                case Axis.Enum.CHILD:
                    results = applyToChild( each, support );
                    break;
                case Axis.Enum.DESCENDANT:
                    results = applyToDescendant( each, support );
                    break;
                case Axis.Enum.DESCENDANT_OR_SELF:
                    results = applyToDescendantOrSelf( each, support );
                    break;
                case Axis.Enum.FOLLOWING:
                    results = applyToFollowing( each, support );
                    break;
                case Axis.Enum.FOLLOWING_SIBLING:
                    results = applyToFollowingSibling( each, support );
                    break;
                case Axis.Enum.NAMESPACE:
                    results = applyToNamespace( each, support );
                    break;
                case Axis.Enum.PARENT:
                    results = applyToParent( each, support );
                    break;
                case Axis.Enum.PRECEDING:
                    results = applyToPreceeding( each, support );
                    break;
                case Axis.Enum.PRECEDING_SIBLING:
                    results = applyToPreceedingSibling( each, support );
                    break;
            }
            if ( results != null ) {
                if ( doPreds ) {
                    List list = applyPredicates( results, support );
                    if ( list != null ) {
                        aggregateResults.addAll( list );
                    }
                }
                else {
                    aggregateResults.addAll( results );
                }
            }
        }
        return aggregateResults;
    }
    
    private List applyPredicates(List nodeSet, ContextSupport support) {
        
        List results = nodeSet;
        if ( _predicates == null ) {
            return nodeSet;
        }
        
        Iterator predIter = _predicates.iterator();        
        while ( predIter.hasNext() ) {
            Predicate eachPred = (Predicate) predIter.next();
            
            results = eachPred.evaluateOn( results, support, _axisCode );
        }
        return results;
    }
    
    public List applyToSelf(Object node, ContextSupport support) {
        return Collections.EMPTY_LIST;
    }
    
    public List applyToChild(Object node, ContextSupport support) {
        return Collections.EMPTY_LIST;
    }
    
    public List applyToDescendant(Object node, ContextSupport support) {
        List results = new ArrayList();
        
        results.addAll( applyToChild( node, support ) );
        
        if ( node instanceof Element ) {
            List children = ((Element)node).content();            
            results.addAll( 
                applyTo( children, support, Axis.Enum.DESCENDANT ) 
            );
        }
        else if ( node instanceof Document ) {
            List children = ((Document)node).content();            
            results.addAll( 
                applyTo( children, support, Axis.Enum.DESCENDANT ) 
            );
        }        
        return results;
    }
    
    public List applyToNamespace(Object node, ContextSupport support) {
        if ( node instanceof Element ) {
            Element element = (Element) node;
            List namespaces = getNamespaces((Element) node);
            return applyTo( namespaces, support, Axis.Enum.NAMESPACE );
        }
        else if ( node instanceof Attribute ) {
            Namespace namespace = getNamespace((Attribute) node);
            if ( namespace != null ) {
                List namespaces = new ArrayList(1);
                namespaces.add(namespace);
                return applyTo( namespaces, support, Axis.Enum.NAMESPACE );
            }
        }
        return Collections.EMPTY_LIST;
    }
    
    public List applyToDescendantOrSelf(Object node, ContextSupport support) {        
        List results = new ArrayList();
        
        results.addAll( applyToSelf( node, support ) );
        
        if ( node instanceof Element ) {
            List children = ((Element)node).content();            
            results.addAll( applyTo( children, support, Axis.Enum.DESCENDANT_OR_SELF ) );
        }
        else if ( node instanceof Document ) {
            List children = ((Document)node).content();            
            results.addAll( applyTo( children, support, Axis.Enum.DESCENDANT_OR_SELF ) );
        }
        return results;
    }
    
    public List applyToParent(Object node, ContextSupport support) {
        Object parent = ParentStep.findParent(node);
        List results = new ArrayList();
        results.addAll( applyToSelf( parent, support ) );
        return results;
    }
    
    public List applyToAncestor(Object node, ContextSupport support) {
        List results = new ArrayList();
        
        //results.addAll( applyToParent( node, support ) );
        Object parent = ParentStep.findParent( node );        
        if ( parent != null ) {
            results.addAll( applyToSelf( parent, support ) );
        
            results.addAll( 
                applyToAncestor( parent, support ) 
            );
        }
        return results;
    }
    
    public List applyToAncestorOrSelf(Object node, ContextSupport support) {
        List results = new ArrayList();
        
        results.addAll( applyToSelf( node, support ) );        
        results.addAll( applyToAncestor( node, support ) );        
        return results;
    }
    
    public List applyToAttribute(Object node, ContextSupport support) {
        return Collections.EMPTY_LIST;
    }
    
    public List applyToPreceeding(Object node, ContextSupport support) {
        List results = new ArrayList();
        
        if ( node instanceof Element) {
            List preceeding = Partition.preceeding( (Element)node );
            
            results.addAll( applyTo( preceeding, support, Axis.Enum.SELF ) );
        }        
        return results;
    }
    
    public List applyToFollowing(Object node, ContextSupport support) {
        List results = new ArrayList();
        
        if ( node instanceof Element) {
            List following = Partition.following( (Element)node );
            
            results.addAll( applyTo( following, support, Axis.Enum.SELF ) );
        }        
        return results;
    }
    
    public List applyToPreceedingSibling(Object node, ContextSupport support) {
        
        List results = new ArrayList();        
        if ( node instanceof Element) {
            
            List preceedingSiblings = Partition.preceedingSiblings( (Element)node );
            
            results.addAll( applyTo( preceedingSiblings, support, Axis.Enum.SELF ) );
        }
        return results;
    }
    
    public List applyToFollowingSibling(Object node, ContextSupport support) {
        List results = new ArrayList();
        
        if ( node instanceof Element) {
            
            List followingSiblings = Partition.followingSiblings( (Element)node );
            
            results.addAll( applyTo( followingSiblings, support, Axis.Enum.SELF ) );
        }        
        return results;
    }

    // Pattern methods
    
    public boolean matches( Context context, Node node ) {
        if ( _predicates != null ) {
            for ( int i = 0, size = _predicates.size(); i < size; i++ ) {
                Predicate predicate = (Predicate) _predicates.get(i);
                if ( ! predicate.matches( context, node ) ) {
                    return false;
                }
            }
        }
        return true;
    }

    /** create the next context to be used for matching which is the reverse
     * of normal XPath evaluation.
     * e.g. the pattern A/B/C matches C first then sets the 
     * context to be B then matches B then sets the context to be A etc.
     */
    public Context nextMatchContext( Context context, Node node ) {
        Element parent = null;
        
        switch ( _axisCode ) {
            case Axis.Enum.SELF:
            case Axis.Enum.ATTRIBUTE:
                return null;
                
            case Axis.Enum.CHILD:
                parent = node.getParent();
                if ( parent == null ) {
                    return null;
                }
                context.setNodeSet( parent );
                return context;
                
            case Axis.Enum.DESCENDANT: 
            case Axis.Enum.DESCENDANT_OR_SELF: 
                parent = node.getParent(); 
                if ( parent != null ) {
                    List parents = new ArrayList();
                    do {
                        parents.add( parent );
                        parent = parent.getParent();
                    }
                    while ( parent != null );
                    
                    context.setNodeSet( parents );
                    return context;
                }
                return null;
                
            case Axis.Enum.FOLLOWING:
            case Axis.Enum.FOLLOWING_SIBLING:
            case Axis.Enum.NAMESPACE:
            case Axis.Enum.PARENT:
            case Axis.Enum.PRECEDING:
            case Axis.Enum.PRECEDING_SIBLING:
            case Axis.Enum.ANCESTOR:
            case Axis.Enum.ANCESTOR_OR_SELF:
                // not implemented yet
                break;
        }
        throw new RuntimeException( 
            "The axis: " + _axis 
            + " has not been implemented as an XSLT Patterns yet" 
        );
    }
    

    
    // Implementation methods
    
  /** @return a list of {@link Namespace} instances which
   * support the parent relationship
   */
    protected List getNamespaces(Element element) {
        List results = element.declaredNamespaces();
        supportsParentList(element, results);
        return results;
    }
    
  /** @return a {@link Namespace} instances which
   * support the parent relationship for the given attribute
   * or null if one is not declared
   */
    protected Namespace getNamespace(Attribute attribute) {
        Namespace namespace = attribute.getNamespace();
        if ( namespace.getURI().length() > 0 ) {
            if ( ! namespace.supportsParent() ) {
                namespace = (Namespace) namespace.asXPathResult(attribute.getParent());
            }
            return namespace;
        }
        return null;
    }
    
  /** @return a list of {@link Attribute} instances which
   * support the parent relationship
   */
    protected List attributes(Element element) {
        List results = element.attributes();
        supportsParentList(element, results);
        return results;
    }
    
  /** ensures that each node in the list supports the parent relationship.
   */
    protected void supportsParentList(Element element, List list) {
        int size = list.size();
        for ( int i = 0; i < size; i++ ) {
            Node node = (Node) list.get(i);
            if ( ! node.supportsParent() ) {
                node = node.asXPathResult(element);
                list.set(i, node);
            }
        }
    }
    
    public String toString() {
        return "[UnAbbrStep: axis: " + _axis + " predicates: " +  _predicates + "]";
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
