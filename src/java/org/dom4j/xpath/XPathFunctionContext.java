
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
public class XPathFunctionContext implements FunctionContext
{

  /** Lock for Singleton creation and double-checked locking */
  private final static Object                _instanceLock = new Object();

  /** Singleton instance */
  private static       XPathFunctionContext  _instance     = null;

  /** Actual map of name-to-function. */
  private final        Map                   _functions    = new HashMap();

  /** Get the XPathFunctionContext singleton.
   *
   *  @return The global, immutable FunctionContext which matches
   *          the functions as described by the W3C XPath specification.
   */
  public static XPathFunctionContext getInstance()
  {
    if (_instance == null)
    {
      synchronized (_instanceLock)
      {
        if (_instance == null)
        {
          _instance = new XPathFunctionContext();
        }
      }
    }

    return _instance;
  }

  public XPathFunctionContext()
  {
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
  protected void addFunction(String name,
                             Function func)
  {
    _functions.put(name,
                   func);
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
  public Function getFunction(String name)
  {
    return (Function) _functions.get(name);
  }
      
}
