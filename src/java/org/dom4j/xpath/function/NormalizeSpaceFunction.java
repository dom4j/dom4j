
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import java.util.List;
import java.util.StringTokenizer;

/**
   <p><b>4.2</b> <code><i>string</i> normalize-space(<i>string</i>)</code> 
   
   @author James Strachan (james@metastuff.com)
*/

public class NormalizeSpaceFunction implements Function
{

  public Object call(Context context,
                     List args)
  {
    if (args.size() >= 1)
    {
      return evaluate( args.get(0) );
    }
    
    // FIXME: Toss exception
    return null;
  }

  public static String evaluate(Object strArg)
  {
    String str = StringFunction.evaluate(strArg).trim();
    if ( str.length() <= 1 ) {
        return str;
    }
    StringBuffer buffer = new StringBuffer();
    boolean first = true;
    StringTokenizer tokenizer = new StringTokenizer(str);
    while (tokenizer.hasMoreTokens()) {
        if (first)
        {
            first = false;
        }
        else 
        {
            buffer.append(" ");
        }
        buffer.append(tokenizer.nextToken());
    }
    return buffer.toString();
  }
}
