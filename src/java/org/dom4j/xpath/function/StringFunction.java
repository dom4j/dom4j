
package org.dom4j.xpath.function;

import org.dom4j.xpath.impl.Context;

import org.dom4j.Node;

import java.util.List;
import java.util.Iterator;

/**
 * <p><b>4.2</b> <code><i>string</i> string(<i>object</i>)</code>
 *
 * @author bob mcwhirter (bob @ werken.com)
 */
public class StringFunction implements Function {
    
    public Object call(Context context, List args) {
        int size = args.size();
        if ( size == 0 ) {
            return evaluate( context );
        }
        else if ( size == 1 ) {
            return evaluate( args.get(0) );
        }
        else {
            // FIXME: Toss an exception
            return evaluate( args.get(0) );
        }
    }
    
    public static String evaluate(Object obj) {
        if (obj instanceof String) {
            return (String) obj;
        }
        else if (obj instanceof Node) {
            Node node = (Node) obj;
            return node.getString();
        }
        else if (obj instanceof List) {
            List list = (List) obj;
            int size = list.size();
            if ( size > 0 ) {
                // the XPath string() function only returns the
                // string value of the first node in a nodeset
                return evaluate( list.get(0) );
            }
        }
        else if (obj != null ) {
            return obj.toString();
        }
        return "";
    }
}
