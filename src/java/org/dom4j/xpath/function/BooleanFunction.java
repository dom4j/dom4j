
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;

/**
   <p><b>4.3</b> <code><i>boolean</i> boolean(<i>object</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/

public class BooleanFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if ( args.size() == 1 )
    {
      return evaluate( args.get(0) );
    }

    // FIXME: Toss an exception
    return null;
  }

  public static Boolean evaluate(Object obj)
  {

    boolean result = false;

    if (obj instanceof Boolean)
    {
      return (Boolean) obj;
      //result = obj.booleanValue();
    }
    else if (obj instanceof Double)
    {
      if (  ! ( ((Double)obj).isNaN() )
           && ( ((Double)obj).doubleValue() != 0) )
      {
        result = true;
      }
    } 
    else if (obj instanceof List)
    {
      result = ( ((List)obj).size() > 0 );
    }
    else if (obj instanceof String)
    {
      result = ( ((String)obj).length() > 0 );
    }

    return (result
            ? Boolean.TRUE
            : Boolean.FALSE
            );
  }
}
