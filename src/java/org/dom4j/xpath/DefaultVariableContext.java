
package org.dom4j.xpath;

import java.util.Map;
import java.util.HashMap;

/** <p>A {@link org.dom4j.xpath.VariableContext}
 *  implementation based upon a java.util.HashMap for simple
 *  name-value mappings.</p>
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */

public class DefaultVariableContext implements VariableContext
{
  private Map _variables = new HashMap();

  /** Resolve a variable binding
   *
   *  <p>Retrieve the currently bound value of the named
   *  variable, or null if no such binding exists. 
   *
   *  @param name The name of the variable sought.
   *
   *  @return The currently bound value of the variable, or null.
   *
   *  @see org.dom4j.xpath.ContextSupport#getVariableValue
   *  @see org.dom4j.xpath.ContextSupport#setVariableContext
   */
  public Object getVariableValue(String name)
  {
    return _variables.get(name);
  }

  /** Set a variable finding
   *
   *  <p>Set the value of a named variable.
   *
   *  @param name The name of the variable to bind to the value
   *  @param value The value to bind to the variable name.
   */
  public void setVariableValue(String name,
                               Object value)
  {
    _variables.put(name,
                   value);
  }
}
