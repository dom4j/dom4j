
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.2</b> <code><i>number</i> string-length(<i>string</i>)</code> 

   @author bob mcwhirter (bob @ werken.com)
*/

public class StringLengthFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 0)
    {
      return evaluate(context);
    } 
    else if (args.size() == 1)
    {
      return evaluate(args.get(0));
    }

    // FIXME: Toss exception
    return null;
  }

  public static Double evaluate(Object obj)
  {
    String str = StringFunction.evaluate(obj);

    return new Double(str.length());
  }
}
