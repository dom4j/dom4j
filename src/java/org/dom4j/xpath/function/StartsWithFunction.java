
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.2</b> <code><i>boolean</i> starts-with(<i>string</i>,<i>string</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/

public class StartsWithFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 2)
    {
      return evaluate(args.get(0), args.get(1));
    }

    // FIXME: Toss exception
    return null;
  }

  public static Boolean evaluate(Object strArg,
                                 Object matchArg)
  {
    String str = StringFunction.evaluate(strArg);
    String match = StringFunction.evaluate(matchArg);

    return ( str.startsWith(match)
             ? Boolean.TRUE
             : Boolean.FALSE
             );
  }
}
