/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */


package org.dom4j.xpath;

import org.dom4j.xpath.function.*;

import java.util.Map;
import java.util.HashMap;

/** <p>Implementation of {@link org.dom4j.xpath.FunctionContext} which
 *  matches the core function library as described by the W3C XPath
 *  Specification.</p>
 *
 *  <p>May be directly instantiated or subclassed.  A Singleton is
 *  provided for ease-of-use in the default case of bare XPaths.<p>
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */
public class XPathFunctionContext implements FunctionContext {

    /** Singleton instance */
    private static       XPathFunctionContext  _instance     = new XPathFunctionContext();

    /** Actual map of name-to-function. */
    private final        Map                   _functions    = new HashMap();

    /** Get the XPathFunctionContext singleton.
      *
      *  @return The global, immutable FunctionContext which matches
      *          the functions as described by the W3C XPath specification.
      */
    public static XPathFunctionContext getInstance() {
        return _instance;
    }

    public XPathFunctionContext() {
        // ----------------------------------------
        //     Node-set Functions
        //
        //     Section 4.1
        // ----------------------------------------

        addFunction( "last",
                     new LastFunction() );

        addFunction( "position",
                     new PositionFunction() );

        addFunction( "count",
                     new CountFunction() );

        addFunction( "local-name",
                     new LocalNameFunction() );

        addFunction( "namespace-uri",
                     new NamespaceUriFunction() );

        addFunction( "name",
                     new NameFunction() );

        // ----------------------------------------
        //     String Functions
        //
        //     Section 4.2
        // ----------------------------------------

        addFunction( "string",
                     new StringFunction() );

        addFunction( "concat",
                     new ConcatFunction() );

        addFunction( "starts-with",
                     new StartsWithFunction() );

        addFunction( "contains",
                     new ContainsFunction() );

        addFunction( "substring-before",
                     new SubstringBeforeFunction() );

        addFunction( "substring-after",
                     new SubstringAfterFunction() );

        addFunction( "substring",
                     new SubstringFunction() );

        addFunction( "string-length",
                     new StringLengthFunction() );

        addFunction( "normalize-space",
                     new NormalizeSpaceFunction() );

        // ----------------------------------------
        //     Boolean Functions
        //
        //     Section 4.3
        // ----------------------------------------

        addFunction( "boolean",
                     new BooleanFunction() );

        addFunction( "not",
                     new NotFunction() );

        addFunction( "true",
                     new TrueFunction() );

        addFunction( "false",
                     new FalseFunction() );

        // ----------------------------------------
        //     Number Functions
        //
        //     Section 4.4
        // ----------------------------------------

        addFunction( "number",
                     new NumberFunction());

        addFunction( "sum",
                     new SumFunction());

        addFunction( "floor",
                     new FloorFunction());

        addFunction( "ceiling",
                     new CeilingFunction());

        addFunction( "round",
                     new RoundFunction());
    }

    /** Add a function to this FunctionContext
      *
      *  @param name The name of the function.
      *  @param func The implementing Function Object.
      */
    protected void addFunction(String name, Function func) {
        _functions.put(name, func);
    }

    /** Retrieve a named function
      *
      *  <p>Retrieve the named function object, or null
      *  if no such function exists.  
      *  
      *  @param name The name of the function sought.
      *
      *  @return The {@link org.dom4j.xpath.function.Function}
      *          matching the specified name.
      *
      *  @see org.dom4j.xpath.ContextSupport#setFunctionContext
      */
    public Function getFunction(String name) {
        return (Function) _functions.get(name);
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
