package org.dom4j.tree;

import java.util.Map;

import org.dom4j.ProcessingInstruction;

/** <p><code>DefaultProcessingInstruction</code> is the DOM4J default implementation
  * of a singly linked, read-only XML Processing Instruction.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultProcessingInstruction extends AbstractProcessingInstruction {

    /** The target of the PI */
    protected String target;

    /** The data for the PI as a String */
    protected String rawData;

    /** The data for the PI in name/value pairs */
    protected Map mapData;

    /** A default constructor for implementors to use.
      */
    protected DefaultProcessingInstruction() { }

    /** <p>This will create a new PI with the given target and data</p>
      *
      * @param target is the name of the PI
      * @param data is the <code>Map</code> data for the PI
      */
    public DefaultProcessingInstruction(String target, Map data) {
        this.target = target;
        setValues(data);
    }

    /** <p>This will create a new PI with the given target and data</p>
      *
      * @param target is the name of the PI
      * @param data is the data for the PI
      */
    public DefaultProcessingInstruction(String target, String data) {
        this.target = target;
        this.rawData = data;
        this.mapData = parseData(data);
    }

    public String getName() {
        return target;
    }

    public String getText() {
        return rawData;
    }
    
    public String getValue(String name) {
        String answer = (String) mapData.get(name);
        if (answer == null) {
            return "";
        }
        return answer;
    }
}


