
package org.dom4j.xpath.impl;

import org.dom4j.xpath.ContextSupport;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.rule.Pattern;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class NameTestStep extends UnAbbrStep {
    
    private String _namespacePrefix;
    private String _localName;
    private boolean matchesAnyName = false;
    private boolean matchesAnyNamespace = false;
    
    
    public NameTestStep( 
        String axis, 
        String namespacePrefix, 
        String localName 
    ) {
        super(axis);
        _namespacePrefix = namespacePrefix;
        _localName = localName;
        matchesAnyName = "*".equals( _localName );
        matchesAnyNamespace = _namespacePrefix == null || _namespacePrefix.equals( "*" );
    }
    
    public List applyToSelf(Object node, ContextSupport support) {
        if ( node instanceof Element ) {
            Element element = (Element) node;
            List results = new ArrayList();
            
            if ( matchesAnyName || element.getName().equals(_localName) ) {
                if ( matchesAnyNamespace
                    || element.getNamespaceURI().equals( 
                        support.translateNamespacePrefix( _namespacePrefix ) ) ) 
                {
                    results.add( node );
                }
            }
            return results;
        }
        else if ( node instanceof Attribute ) {
            Attribute attribute = (Attribute) node;
            List results = new ArrayList();
           
            if ( attribute.getName().equals(_localName) ) {
                if ( matchesAnyNamespace
                    || attribute.getNamespaceURI().equals( 
                        support.translateNamespacePrefix( 
                            _namespacePrefix ) ) ) 
                {
                    results.add( node );
                }
            }
            return results;
        }
        return Collections.EMPTY_LIST;
    }
    
    public List applyToAttribute(Object node, ContextSupport support) {
        if ( node instanceof Element ) {
            if ( matchesAnyName ) {
                return getAttributes( (Element) node );
            }
            else {
                Element element = (Element) node;
                Attribute attr = null;
                
                if ( _namespacePrefix == null ) {
                    attr = element.getAttribute( _localName );
                }
                else {
                    QName qName = support.getQName( _namespacePrefix, _localName );                    
                    attr = element.getAttribute( qName );
                }
                
                if ( attr != null ) {
                    if ( ! attr.supportsParent() ) {
                        attr = (Attribute) attr.asXPathNode(element);
                    }
                    List results = new ArrayList();
                    results.add( attr );
                    return results;
                }
            }
        }
        return Collections.EMPTY_LIST;
    }
    
    public List applyToNamespace(Object node, ContextSupport support) {
        if ( node instanceof Element ) {
            Element element = (Element) node;
            
            if ( matchesAnyName ) {
                return getNamespaces( element );
            }
            else {
                Namespace namespace = element.getNamespaceForPrefix( _localName );
                if ( namespace != null ) {
                    if ( ! namespace.supportsParent() ) {
                        namespace = (Namespace) namespace.asXPathNode( element );
                    }
                    List results = new ArrayList();
                    results.add( namespace );
                    return results;
                }
            }
        }
        return Collections.EMPTY_LIST;
    }
    
    public List applyToChild(Object node, ContextSupport support) {
        List results = new ArrayList();
        String nsURI = null;
        Namespace ns = null;
        
        if ( _namespacePrefix != null ) {
            ns = support.getNamespaceByPrefix( _namespacePrefix );
        }
        
        if ( node instanceof Document ) {
            Element child = ((Document) node).getRootElement();
            
            if ( child.getName().equals( _localName ) ) {
                if ( ns == null ) {
                    results.add( child );
                }
                else if ( ns.equals( child.getNamespace() ) ) {
                    results.add( child );
                }
            }
        }
        else if ( node instanceof Element ) {
            Element element = (Element) node;            
            if ( matchesAnyName ) {
                Iterator iter = element.elementIterator();
                if ( ns == null ) {
                    while ( iter.hasNext() ) {
                        results.add( iter.next() );
                    }
                }
                else {
                    while ( iter.hasNext() ) {
                        Element nodeChild = (Element) iter.next();                        
                        if ( ns.equals( nodeChild.getNamespace() ) ) {
                            results.add( nodeChild );
                        }
                    }
                }
            }
            else {
                Iterator iter = ( ns != null ) 
                    ? element.elementIterator( QName.get( _localName, ns ) )
                    : element.elementIterator( _localName );
                while ( iter.hasNext() ) {
                    results.add( iter.next() );
                }
            }
        }
        return results;
    }

    // Pattern methods
    
    public boolean matches( Context context, Node node ) {
        boolean matches = false;
        if ( node instanceof Element ) {
            Element element = (Element) node;
            ContextSupport support = context.getContextSupport();
            
            if ( matchesAnyName || element.getName().equals(_localName) ) {
                matches = matchesAnyNamespace || element.getNamespaceURI().equals( 
                    support.translateNamespacePrefix( _namespacePrefix ) 
                );
            }
        }
        else if ( node instanceof Attribute ) {
            Attribute attribute = (Attribute) node;
            ContextSupport support = context.getContextSupport();
            
            if ( matchesAnyName || attribute.getName().equals(_localName) ) {
                matches = matchesAnyNamespace || attribute.getNamespaceURI().equals( 
                    support.translateNamespacePrefix( _namespacePrefix ) 
                );
            }
        }
        if ( matches ) {
            // evaluate the predicates
            return super.matches( context, node );
        }
        return false;
    }

    public double getPriority() {
        // If the pattern has the form of a QName preceded by a 
        // ChildOrAttributeAxisSpecifier or has the form 
        // processing-instruction(Literal) preceded by a 
        // ChildOrAttributeAxisSpecifier, then the priority is 0.
        
        // If the pattern has the form NCName:* preceded by a 
        // ChildOrAttributeAxisSpecifier, then the priority is -0.25.
        if ( ! matchesAnyName ) {
            return 0;
        }
        else if ( ! matchesAnyNamespace ) { 
            return -0.25;
        }
        return Pattern.DEFAULT_PRIORITY;
    }
    
    public String getMatchesNodeName() {
        if ( ! matchesAnyName && _localName != null ) {
            return _localName;
        }
        return null;
    }
    
    public String toString() {
        return "[NameTestStep [ name: " + _namespacePrefix + ":" + _localName + " " + super.toString() + " ]]";
    }
}
