
package org.dom4j.xpath;

import org.dom4j.xpath.function.Function;

/** <p>Specification of the interface required by
 *  {@link org.dom4j.xpath.ContextSupport} for delegation
 *  of function resolution.</p>
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */
public interface FunctionContext
{

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
  Function getFunction(String name);
}
