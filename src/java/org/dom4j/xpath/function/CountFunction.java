
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.1</b> <code><i>number</i> count(<i>node-set</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/

public class CountFunction implements Function
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

  public static Double evaluate(Object obj)
  {
    if (obj instanceof List)
    {
      return new Double( ((List)obj).size() );
    }

    return null;
  }
}
