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

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.rule.Pattern;
import org.dom4j.xpath.impl.Context;

import org.saxpath.Axis;

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
        Axis axis, 
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
                if ( matchesAnyNamespace || matchesPrefix( element ) ) {
                    results.add( node );
                }
            }
            return results;
        }
        else if ( node instanceof Attribute ) {
            Attribute attribute = (Attribute) node;
            List results = new ArrayList();
           
            if ( matchesAnyName || attribute.getName().equals(_localName) ) {
                if ( matchesAnyNamespace || matchesPrefix( attribute ) ) {
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
                return attributes( (Element) node );
            }
            else {
                Element element = (Element) node;
                Attribute attr = null;
                
                if ( _namespacePrefix == null ) {
                    attr = element.attribute( _localName );
                }
                else {
                    Namespace namespace = element.getNamespaceForPrefix( _namespacePrefix );
                    if ( namespace == null ) {
                        System.out.println( "WARNING: couldn't find namespace for prefix: " + _namespacePrefix );
                        attr = element.attribute( _localName );
                    }
                    else {
                        QName qName = QName.get( _localName, namespace );
                        attr = element.attribute( qName );
                    }
                }
                
                if ( attr != null ) {
                    if ( ! attr.supportsParent() ) {
                        attr = (Attribute) attr.asXPathResult(element);
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
                        namespace = (Namespace) namespace.asXPathResult( element );
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
        
        if ( node instanceof Document ) {
            Element child = ((Document) node).getRootElement();
            
            if ( matchesAnyName || child.getName().equals( _localName ) ) {
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

            if ( _namespacePrefix != null ) {
                ns = element.getNamespaceForPrefix( _namespacePrefix );
            }
        
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
                matches = matchesAnyNamespace || matchesPrefix( element );
            }
        }
        else if ( node instanceof Attribute ) {
            Attribute attribute = (Attribute) node;
            ContextSupport support = context.getContextSupport();
            
            if ( matchesAnyName || attribute.getName().equals(_localName) ) {
                matches = matchesAnyNamespace || matchesPrefix( attribute );
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
    
    protected boolean matchesPrefix( Element element ) {
        // XXXX: should we map the prefix to a URI and compare that?
        return element.getNamespacePrefix().equals( _namespacePrefix );
    }
    
    protected boolean matchesPrefix( Attribute attribute ) {
        // XXXX: should we map the prefix to a URI and compare that?
        return attribute.getNamespacePrefix().equals( _namespacePrefix );
    }

    public String toString() {
        return "[NameTestStep [ name: " + _namespacePrefix + ":" + _localName + " " + super.toString() + " ]]";
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
