
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.3</b> <code><i>boolean</i> not(<i>boolean</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/

public class NotFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 1)
    {
      return evaluate( args.get(0) );
    }

    // FIXME: Toss exception
    return null;
  }

  public static Boolean evaluate(Object obj)
  {
    return ( ( BooleanFunction.evaluate(obj).booleanValue() )
             ? Boolean.FALSE
             : Boolean.TRUE
             );
  }
}
