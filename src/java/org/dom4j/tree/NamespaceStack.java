/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.DocumentFactory;
import org.dom4j.Namespace;
import org.dom4j.QName;

/** NamespaceStack implements a stack of namespaces and optionally
  * maintains a cache of all the fully qualified names (<code>QName</code>)
  * which are in scope. This is useful when building or navigating a <i>dom4j</i>
  * document.
  *
  * @author <a href="mailto:jstrachan@apache.org">James Strachan</a>
  * @version $Revision$
  */
public class NamespaceStack {
 
    /** The factory used to create new <code>Namespace</code> instances */
    private DocumentFactory documentFactory;
    
    /** The Stack of namespaces */
    private ArrayList namespaceStack = new ArrayList();
    
    public NamespaceStack() {
        this.documentFactory = DocumentFactory.getInstance();
    }
  
    public NamespaceStack(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }
  
    /** Pushes the given namespace onto the stack so that its prefix
      * becomes available.
      * 
      * @param namespace is the <code>Namespace</code> to add to the stack.
      */
    public void push(Namespace namespace) {
        namespaceStack.add( namespace );
    }      
    
    /** Pops the most recently used <code>Namespace</code> from
      * the stack
      * 
      * @return Namespace popped from the stack
      */
    public Namespace pop() {
        Namespace namespace = (Namespace) namespaceStack.remove( namespaceStack.size() - 1 );
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
        if ( prefix == null ) {
            prefix = "";
        }
        for ( int i = namespaceStack.size() - 1; i >= 0; i-- ) {
            Namespace namespace = (Namespace) namespaceStack.get(i);
            if ( prefix.equals( namespace.getPrefix() ) ) {
                return namespace;
            }
        }
        return null;
    }
    
    /** @return the URI for the given prefix or null if it 
      * could not be found.
      */
    public String getURI( String prefix ) {
        Namespace namespace = getNamespaceForPrefix( prefix );
        return ( namespace != null ) ? namespace.getURI() : null;        
    }
    
    /** @return true if the given prefix is in the stack.
      */
    public boolean contains( Namespace namespace ) {
        return namespaceStack.contains(namespace);
    }
    
    public QName getQName( String namespaceURI, String localName, String qualifiedName ) {
        if ( localName == null ) {
            localName = qualifiedName;
        }
        else if ( qualifiedName == null ) {
            qualifiedName = localName;
        }
        if ( namespaceURI == null ) {
            namespaceURI = "";
        }
        String prefix = "";
        int index = qualifiedName.indexOf(":");
        if (index > 0) {
            prefix = qualifiedName.substring(0, index);
        }
        Namespace namespace = createNamespace( prefix, namespaceURI );
        return pushQName( localName, qualifiedName, namespace, prefix );
    }

    /** Adds a namepace to the stack with the given prefix and URI */
    public void push( String prefix, String uri ) {        
        if ( uri == null ) {
            uri = "";
        }
        Namespace namespace = createNamespace( prefix, uri );
        push( namespace );
    }
    
    /** Adds a new namespace to the stack */
    public Namespace addNamespace( String prefix, String uri ) {        
        Namespace namespace = createNamespace( prefix, uri );
        push( namespace );
        return namespace;
    }
    
    /** Pops a namepace from the stack with the given prefix and URI */
    public Namespace pop( String prefix ) {        
        if ( prefix == null ) {
            prefix = "";
        }
        Namespace namespace = null;
        for (int i = namespaceStack.size() - 1; i >= 0; i-- ) {
            Namespace ns = (Namespace) namespaceStack.get(i);            
            if ( prefix.equals( ns.getPrefix() ) ) {
                namespaceStack.remove(i);
                namespace = ns;
                break;
            }
        }
        if ( namespace == null ) {
            System.out.println( "Warning: missing namespace prefix ignored: " + prefix );
        }
        return namespace;
    }
    
    public String toString() {
        return super.toString() + " Stack: " + namespaceStack.toString();
    }

    public DocumentFactory getDocumentFactory() {
        return documentFactory;
    }
    
    public void setDocumentFactory(DocumentFactory documentFactory) {
        this.documentFactory = documentFactory;
    }
    
    // Implementation methods
    //-------------------------------------------------------------------------    
    
    /** Adds the QName to the stack of available QNames
      */
    protected QName pushQName( String localName, String qualifiedName, Namespace namespace, String prefix ) {
        return createQName( localName, qualifiedName, namespace );
    }

    /** Factory method to creeate new QName instances. By default this method
      * interns the QName
      */
    protected QName createQName( String localName, String qualifiedName, Namespace namespace ) {
        return documentFactory.createQName( localName, namespace );
    }
    
    /** Factory method to creeate new Namespace instances. By default this method
      * interns the Namespace
      */
    protected Namespace createNamespace( String prefix, String namespaceURI ) {
        return documentFactory.createNamespace( prefix, namespaceURI );
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
