
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.1</b> <code><i>number</i> position()</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class PositionFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    System.err.println("*******************************");
    System.err.println("position(" + context + ", " + args + ")");
    if (args.size() == 0)
    {
      return evaluate( context );
    }

    // FIXME: Toss exception
    return null;
  }

  public static Double evaluate(Context context)
  {
    return new Double( context.getPosition() );
  }
}

