/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


package org.dom4j.xpath;

import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.VariableContext;

import org.dom4j.xpath.function.Function;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/** <p>ContextSupport maintains information to aid in the
 *  execution of the XPath against a context node.</p>
 * 
 *  <p>It separates the knowledge of functions, variables
 *  and namespace-bindings from the context node to
 *  be walked.</p>
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class ContextSupport {
    
    static final ContextSupport     BASIC_CONTEXT_SUPPORT = new ContextSupport();

    private NamespaceContext  _nsContext        = null;
    private FunctionContext   _functionContext  = XPathFunctionContext.getInstance();
    private VariableContext   _variableContext  = null;

    /** Construct a semantically empty ContextSupport
     */
    public ContextSupport() {
        // intentionally left blank
    }

    /** Construct a semantically initialized ContextSupport
     *
     *  @param nsContext The NamespaceContext implementation
     *  @param functionContext The FunctionContext implementation
     *  @param variableContext The VariableContext implementation
     */
    public ContextSupport(
        NamespaceContext nsContext,
        FunctionContext functionContext,
        VariableContext variableContext
    ) {
        _nsContext = nsContext;
        _functionContext = functionContext;
        _variableContext = variableContext;
    }

    /** Set the NamespaceContext implementation
     *
     *  @param nsContext The NamespaceContext implementation
     */
    public void setNamespaceContext(NamespaceContext nsContext) {
        _nsContext = nsContext;
    }

    /** Set the FunctionContext implementation
     *
     *  @param functionContext The FunctionContext implementation
     */
    public void setFunctionContext(FunctionContext functionContext) {
        _functionContext = functionContext;
    }

    /** @return the current variable context
      */
    public VariableContext getVariableContext() {
        return _variableContext;
    }
    
    /** Set the VariableContext implementation
     *
     *  @param variableContext The FunctionContext implementation
     */
    public void setVariableContext(VariableContext variableContext) {
        _variableContext = variableContext;
    }

    /** Translate a namespace prefix into a URI
     *
     *  <p>Using the {@link org.dom4j.xpath.NamespaceContext}
     *  implementation, translate the prefix used in a component of an XPath
     *  into its expanded namespace URI.</p>
     *
     *  @param prefix The namespace prefix
     *
     *  @return The URI matching the prefix
     *
     *  @see #setNamespaceContext
     */
    public String translateNamespacePrefix(String prefix) {
        if (_nsContext == null) {
            return null;
        }
        return _nsContext.translateNamespacePrefix(prefix);
    }
    
/*    
    public QName getQName( String prefix, String localName ) {
        return QName.get( localName, getNamespaceByPrefix( prefix ) );
    }
    
    public Namespace getNamespaceByPrefix(String prefix) {
        if ( _nsContext != null ) {
            System.err.println("SHORT_CIRCUIT: NO_NAMESPACE");
            return Namespace.NO_NAMESPACE;
        }
        return _nsContext.getNamespaceByPrefix( prefix );
    }
*/    

    /** Retrieve a named function
     *
     *  <p>Retrieve the named function object, or null
     *  if no such function exists.  Delegates to the
     *  {@link org.dom4j.xpath.FunctionContext} implementation
     *  provided, if any.
     *  
     *  @param name The name of the function sought.
     *
     *  @return The {@link org.dom4j.xpath.function.Function}
     *          matching the specified name.
     *
     *  @see #setFunctionContext
     */
    public Function getFunction(String name) {
        return _functionContext.getFunction(name);
    }

    /** Resolve a variable binding
     *
     *  <p>Retrieve the currently bound value of the named
     *  variable, or null if no such binding exists.  Delegates
     *  to the {@link org.dom4j.VariableContext} implementation
     *  provided, if any.
     *
     *  @param name The name of the variable sought.
     *
     *  @return The currently bound value of the variable, or null.
     *
     *  @see #setVariableContext
     */
    public Object getVariableValue(String name) {
        if ( _variableContext != null ) {
            return _variableContext.getVariableValue(name);
        }
        return null;
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
