
package org.dom4j.xpath.impl;

import org.dom4j.xpath.ContextSupport;
import org.dom4j.xpath.impl.Context;
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

    public String toString() {
        return ("[Context " + _nodeSet + "]");
    }
}
