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
import org.dom4j.xpath.function.Function;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

public class Context implements Cloneable {
    static final ContextSupport     BASIC_CONTEXT_SUPPORT = new ContextSupport();
    
    private ContextSupport _contextSupport = null;
    private List           _nodeSet        = null;
    private int            _position       = 0;


    public Context(Node node, ContextSupport contextSupport) {
        _nodeSet = new ArrayList(1);
        _nodeSet.add( node );

        _contextSupport = contextSupport;
    }

    public Context(Document doc, ContextSupport contextSupport) {
        _nodeSet = new ArrayList(1);
        _nodeSet.add(doc);

        _contextSupport = contextSupport;
    }

    public Context(Element elem, ContextSupport contextSupport) {
        _nodeSet = new ArrayList(1);
        _nodeSet.add(elem);

        _contextSupport = contextSupport;
    }

    public Context(List nodeSet, ContextSupport contextSupport) {
        _nodeSet = nodeSet;
    
        _contextSupport = contextSupport;
    }

    public Context(ContextSupport contextSupport) {
        _nodeSet = new ArrayList();    
        _contextSupport = contextSupport;
    }

    public Context() {
        this(BASIC_CONTEXT_SUPPORT);
    }

    
    public List getNodeSet() {
        if ( _position == 0 ) {
            return _nodeSet;
        }

        List nodeOnly = new ArrayList(1);

        nodeOnly.add( getContextNode() );

        return nodeOnly;
    }

    public int getSize() {
        return _nodeSet.size();
    }

    public boolean isEmpty() {
        return _nodeSet.isEmpty();
    }

    public int getPosition() {
        return _position;
    }

    public void setPosition(int position) {
        _position = position;
    }

    public Node getContextNode() {
        int idx = _position - 1;
        if ( idx < 0 ) {
            idx = 0;
        }
        else if ( idx >= _nodeSet.size() ) {
            return null;
        }
        return (Node) _nodeSet.get(idx);
    }

    public Node getNode(int index) {
        return (Node) _nodeSet.get( index - 1 );
    }

    /** Set the current nodeset to be a nodeset containing the 
      * given node 
      */
    public void setNodeSet(Node node) {
        ArrayList list = new ArrayList(1);
        list.add( node );
        setNodeSet( list );
    }
    
    public void setNodeSet(List nodeSet) {
        if ( nodeSet.isEmpty() ) {
            _nodeSet = Collections.EMPTY_LIST;
        }
        else {
            _nodeSet = nodeSet;
        }
        _position = 0;
    }

    public ContextSupport getContextSupport() {
        return _contextSupport;
    }

    public Iterator iterator() {
        return _nodeSet.iterator();
    }

    public String translateNamespacePrefix(String prefix) {
        return _contextSupport.translateNamespacePrefix( prefix );
    }

    public Object getVariableValue(String variableName) {
        return _contextSupport.getVariableValue( variableName );
    }

    public Function getFunction(String name) {
        return _contextSupport.getFunction( name );
    }

    public Context duplicate() {
        Context dupe = null;

        try {
            dupe = (Context) this.clone();
            dupe._nodeSet = new ArrayList( this._nodeSet.size() );
            dupe._nodeSet.addAll( this._nodeSet );
        }
        catch (CloneNotSupportedException e) {
            dupe = null;
        }
        return dupe;
    }

    public Context duplicate( List nodeSet ) {
        Context dupe = null;
        try {
            dupe = (Context) this.clone();
            dupe._nodeSet = nodeSet;
        }
        catch (CloneNotSupportedException e) {
            dupe = null;
        }
        return dupe;
    }

    public String toString() {
        return ("[Context " + _nodeSet + "]");
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
