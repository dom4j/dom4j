/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

/*-- 

 Copyright (C) 2000 Brett McLaughlin & Jason Hunter.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 
 1. Redistributions of source code must retain the above copyright
    notice, this list of conditions, and the following disclaimer.
 
 2. Redistributions in binary form must reproduce the above copyright
    notice, this list of conditions, and the disclaimer that follows 
    these conditions in the documentation and/or other materials 
    provided with the distribution.

 3. The name "DOM4J" must not be used to endorse or promote products
    derived from this software without prior written permission.  For
    written permission, please contact license@jdom.org.
 
 4. Products derived from this software may not be called "DOM4J", nor
    may "DOM4J" appear in their name, without prior written permission
    from the DOM4J Project Management (pm@jdom.org).
 
 In addition, we request (but do not require) that you include in the 
 end-user documentation provided with the redistribution and/or in the 
 software itself an acknowledgement equivalent to the following:
     "This product includes software developed by the
      DOM4J Project (http://www.jdom.org/)."
 Alternatively, the acknowledgment may be graphical using the logos 
 available at http://www.jdom.org/images/logos.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 This software consists of voluntary contributions made by many 
 individuals on behalf of the DOM4J Project and was originally 
 created by Brett McLaughlin <brett@jdom.org> and 
 Jason Hunter <jhunter@jdom.org>.  For more information on the 
 DOM4J Project, please see <http://www.jdom.org/>.
 
 */

// This is a port of a file from the JDOM project
// The original file was org.jdom.output.Namespace

package org.dom4j.io;

import java.util.Stack;

import org.dom4j.Namespace;

/**
 * <p><code>NamespaceStack</code> is a helper class used by both
 *   <code>{@link XMLOutputter}</code> and 
 *   <code>{@link SAXOutputter}</code> to manage namespaces
 *   in a DOM4J Document during output.
 * </p>
 *
 * @author Elliotte Rusty Harolde
 * @author Fred Trimble
 * @author Brett McLaughlin
 */
class NamespaceStack {
 
    /** The prefixes available */
    private Stack prefixes;

    /** The URIs available */
    private Stack uris;        

    /**
     * <p> This creates the needed storage. </p>
     */
    public NamespaceStack() {
        prefixes = new Stack();
        uris = new Stack();
    }
  
    /**
     * <p>
     *  This will add a new <code>{@link Namespace}</code>
     *    to those currently available.
     * </p>
     * 
     * @param ns <code>Namespace</code> to add.
     */
    public void push(Namespace ns) {
        prefixes.push(ns.getPrefix());
        uris.push(ns.getURI());
    }      
    
    /**
     * <p>
     *  This will remove the topmost (most recently added)
     *    <code>{@link Namespace}</code>, and return its prefix.
     * </p>
     *
     * @return <code>String</code> - the popped namespace prefix.
     */
    public String pop() {      
        String prefix = (String)prefixes.pop();
        uris.pop();

        return prefix;
    }
    
    /**
     * <p> This returns the number of available namespaces. </p>
     *
     * @return <code>int</code> - size of the namespace stack.
     */
    public int size() {
        return prefixes.size();     
    }    
  
    /**
     * <p>
     *  Given a prefix, this will return the namespace URI most 
     *    rencently (topmost) associated with that prefix.
     * </p>
     *
     * @param prefix <code>String</code> namespace prefix.
     * @return <code>String</code> - the namespace URI for that prefix.
     */
    public String getURI(String prefix) {
        int index = prefixes.lastIndexOf(prefix);
        if (index == -1) {
            return null;
        }
        String uri = (String)uris.elementAt(index);
        return uri;       
    }
    
    /**
     * <p>
     *  This will print out the size and current stack, from the
     *    most recently added <code>{@link Namespace}</code> to
     *    the "oldest," all to <code>System.out</code>.
     * </p>
     */
    public void printStack() {
        System.out.println("Stack: " + prefixes.size());
        for (int i = 0; i < prefixes.size(); i++) {
            System.out.println(prefixes.elementAt(i) + "&" + uris.elementAt(i));
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
