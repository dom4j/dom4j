
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.2</b> <code><i>string</i> substring(<i>string</i>,<i>number</i>,<i>number?</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/

public class SubstringFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 2)
    {
      return evaluate( args.get(0),
                       args.get(1) );
    }
    else if (args.size() == 3)
    {
      return evaluate( args.get(0),
                       args.get(1),
                       args.get(2) );
    }

    // FIXME: Toss exception
    return null;
  }

  public static String evaluate(Object strArg,
                                Object startArg)
  {

    String str = StringFunction.evaluate(strArg);

    int start = RoundFunction.evaluate( NumberFunction.evaluate(startArg) ).intValue();

    start += 1;

    return str.substring(start);

  }

  public static String evaluate(Object strArg,
                                Object startArg,
                                Object lenArg)
  {

    String str = StringFunction.evaluate(strArg);

    int start = RoundFunction.evaluate(
      NumberFunction.evaluate(startArg) ).intValue();

    int len = RoundFunction.evaluate(
      NumberFunction.evaluate(lenArg) ).intValue();

    start += 1;

    int end = start + len;

    return str.substring(start,
                         end);

  }
}
