
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.3</b> <code><i>boolean</i> true()</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class TrueFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 0)
    {
      return evaluate();
    }

    // FIXME: Toss exception
    return null;
  }

  public static Boolean evaluate()
  {
    return Boolean.TRUE;
  }
}

