
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/** <p>Interface for Function objects to conform to,
 *  for extensible function libraries.
 */
public interface Function
{
  /** Call the function object.
   *
   *  @param context The current context the function operates upon
   *  @param args The argument list to the function.
   *
   *  @return The result from calling the function.
   */
  Object call(Context context,
              List args);
}
