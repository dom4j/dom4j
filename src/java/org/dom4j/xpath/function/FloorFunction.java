
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.4</b> <code><i>number</i> floor(<i>number</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class FloorFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 1)
    {
      return evaluate(args.get(1));
    }

    // FIXME: Toss exception
    return null;
  }

  public static Double evaluate(Object obj)
  {
    double value = NumberFunction.evaluate(obj).doubleValue();
    return new Double( Math.floor( value ) );
  }
}

