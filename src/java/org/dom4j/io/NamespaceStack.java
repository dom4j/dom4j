/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Namespace;
import org.dom4j.QName;

/** NamespaceStack implements a stack of namespaces and optionally
  * maintains a cache of all the fully qualified names (<code>QName</code>)
  * which are in scope. This is useful when building or navigating a DOM4J 
  * document.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
class NamespaceStack {
 
    /** The Stack of namespaces */
    private ArrayList namespaceStack = new ArrayList();

    /** Index of namespace prefix -> Namespace */
    private HashMap prefixToNamespaceMap = new HashMap();
    
    /** Index of namespace prefix -> qualified name List */
    private HashMap prefixToQualifiedNamesMap = new HashMap();

    /** Index of qualified Name -> QName object */
    private HashMap qNameMap = new HashMap();

    
    public NamespaceStack() {
    }
  
    /** Pushes the given namespace onto the stack so that its prefix
      * becomes available.
      * 
      * @param namespace is the <code>Namespace</code> to add to the stack.
      */
    public void push(Namespace namespace) {
        namespaceStack.add( namespace );
        
        String prefix = namespace.getPrefix();
        prefixToNamespaceMap.put( prefix, namespace );
    }      
    
    /** Pops the most recently used <code>Namespace</code> from
      * the stack
      * 
      * @return Namespace popped from the stack
      */
    public Namespace pop() {
        Namespace namespace = (Namespace) namespaceStack.remove( namespaceStack.size() - 1 );
        remove( namespace );
        return namespace;
    }
    
    /** @return the number of namespaces on the stackce stack.
     */
    public int size() {
        return namespaceStack.size();     
    }    

    /** Clears the stack 
      */
    public void clear() {
        qNameMap.clear();
        prefixToNamespaceMap.clear();
        prefixToQualifiedNamesMap.clear();
        namespaceStack.clear();
    }
    
    /** @return the namespace at the specified index on the stack 
      */
    public Namespace getNamespace( int index ) {
        return (Namespace) namespaceStack.get( index );
    }
    
    /** @return the namespace for the given prefix or null
      * if it could not be found.
      */
    public Namespace getNamespaceForPrefix( String prefix ) {
        return (Namespace) prefixToNamespaceMap.get( prefix );
    }
    
    /** @return the URI for the given prefix or null if it 
      * could not be found.
      */
    public String getURI( String prefix ) {
        Namespace namespace = (Namespace) prefixToNamespaceMap.get( prefix );
        if ( namespace != null ) {
            return namespace.getURI();
        }
        return null;
    }
    
    /** @return true if the given prefix is in the stack.
      */
    public boolean containsPrefix( String prefix ) {
        return prefixToNamespaceMap.get( prefix ) != null;
    }
    
    public QName getQName( String namespaceURI, String localName, String qualifiedName ) {
        QName qName = (QName) qNameMap.get( qualifiedName );
        if ( qName == null ) {
            String prefix = "";
            int index = qualifiedName.indexOf(":");
            if (index > 0) {
                prefix = qualifiedName.substring(0, index);
            }
            Namespace namespace = getNamespaceForPrefix( prefix );
            if ( namespace == null ) {
                namespace = createNamespace( prefix, namespaceURI );
                push(namespace);
            }
            qName = pushQName( localName, qualifiedName, namespace, prefix );
        }
        return qName;
    }

    /** Adds a namepace to the stack with the given prefix and URI */
    public void push( String prefix, String uri ) {        
        Namespace namespace = getNamespaceForPrefix( prefix );
        if ( namespace == null ) {
            namespace = createNamespace( prefix, uri );
            push( namespace );
        }
        else {
            System.out.println( "Warning: duplicate namespace prefix ignored: " + prefix );
        }
    }
    
    /** Pops a namepace from the stack with the given prefix and URI */
    public Namespace pop( String prefix ) {        
        Namespace namespace = getNamespaceForPrefix( prefix );
        if ( namespace != null ) {
            namespaceStack.remove( namespace );
            remove( namespace );
        }
        else {
            System.out.println( "Warning: missing namespace prefix ignored: " + prefix );
        }
        return namespace;
    }
    
    public String toString() {
        return super.toString() + " Stack: " + namespaceStack.toString();
    }

    /** Removes the given namespace from the stack */
    protected void remove( Namespace namespace ) {
        String prefix = namespace.getPrefix();
        prefixToNamespaceMap.remove( prefix );

        // remove all QNames from the stack for the given prefix
        List list = (List) prefixToQualifiedNamesMap.get( prefix );
        if ( list != null ) {
            for ( int i = 0, size = list.size(); i < size; i++ ) {
                String qualifiedName = (String) list.get(i);                
                qNameMap.remove( qualifiedName );
            }
            list.clear();
        }
    }
    
    /** Adds the QName to the stack of available QNames
      */
    protected QName pushQName( String localName, String qualifiedName, Namespace namespace, String prefix ) {
        QName qName = createQName( localName, qualifiedName, namespace );
        qNameMap.put( qualifiedName, qName );
        
        // maintain list of QNames for a given prefix
        List list = (List) prefixToQualifiedNamesMap.get( prefix );
        if ( list == null ) {
            list = new ArrayList();
            prefixToQualifiedNamesMap.put( prefix, list );
        }
        list.add( qualifiedName );
        return qName;
    }

    /** Factory method to creeate new QName instances. By default this method
      * interns the QName
      */
    protected QName createQName( String localName, String qualifiedName, Namespace namespace ) {
        return QName.get( localName, namespace, qualifiedName );
    }
    /** Factory method to creeate new Namespace instances. By default this method
      * interns the Namespace
      */
    protected Namespace createNamespace( String prefix, String namespaceURI ) {
        return Namespace.get( prefix, namespaceURI );
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
