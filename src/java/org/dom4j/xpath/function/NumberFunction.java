
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.4</b> <code><i>number</i> number(<i>object</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class NumberFunction implements Function
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

    Double result = null;

    if (obj instanceof Double)
    {
      result = (Double) obj;
    }
    else
    {
      result = Double.valueOf( StringFunction.evaluate(obj) );
    }

    return result;
  }
}

