
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import org.dom4j.Attribute;
import org.dom4j.Element;

import java.util.List;

/**
   <p><b>4.1</b> <code><i>string</i> local-name(<i>node-set?</i>)</code> 
   
   @author bob mcwhirter (bob @ werken.com)
*/
public class LocalNameFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() == 0)
    {
      return evaluate( context );
    }

    // FIXME: Toss exception
    return null;
  }

  public static String evaluate(Context context)
  {
    List list = context.getNodeSet();
    
    if ( ! list.isEmpty() )
    {
      Object first = list.get(0);
      
      if (first instanceof Element)
      {
        return ((Element)first).getName();
      }
      else if (first instanceof Attribute)
      {
        return ((Attribute)first).getName();
      }
    }

    return "";
  }
}
