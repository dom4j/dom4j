
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.2</b> <code><i>string</i> substring-after(<i>string</i>,<i>string</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/

public class SubstringAfterFunction implements Function
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

  public static String evaluate(Object strArg,
                                Object matchArg)
  {
    String str = StringFunction.evaluate(strArg);
    String match = StringFunction.evaluate(matchArg);
    
    int loc = str.indexOf(match);

    if ( loc < 0 )
    {
      return "";
    }

    return str.substring(loc+1);

  }
}
