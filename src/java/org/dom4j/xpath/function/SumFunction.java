
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;
import java.util.Iterator;

/**
   <p><b>4.4</b> <code><i>number</i> sum(<i>node-set</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class SumFunction implements Function
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

    double sum = 0;

    if (obj instanceof List)
    {
      Iterator nodeIter = ((List)obj).iterator();

      while (nodeIter.hasNext())
      {
        sum += NumberFunction.evaluate(nodeIter.next()).doubleValue();
      }
    }

    return new Double(sum);
  }
}

