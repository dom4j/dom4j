
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;
import java.util.Iterator;

/**
   <p><b>4.2</b> <code><i>boolean</i> concat(<i>string</i>,<i>string</i>,<i>string*</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/

public class ConcatFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if ( args.size() >= 2 ) {
      return evaluate(args);
    }

    return null;
  }

  public static String evaluate(List list)
  {
    StringBuffer result = new StringBuffer();

    Iterator argIter = list.iterator();

    while ( argIter.hasNext() )
    {

      result.append( StringFunction.evaluate( argIter.next() ) );
    }
    
    return result.toString();
  }
}
