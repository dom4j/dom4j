
package org.dom4j.xpath;

/** <p>Specification of the interface required by
 *  {@link org.dom4j.xpath.ContextSupport} for delegation
 *  of variable resolution.</p>
 *
 *  @author bob mcwhirter (bob @ werken.com)
 */
public interface VariableContext
{

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
  Object getVariableValue(String name);
}
