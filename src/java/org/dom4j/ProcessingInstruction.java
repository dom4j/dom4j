package org.dom4j;

import java.util.Map;
    
/** <p><code>ProcessingInstruction</code> defines an XML processing instruction.
  * The {@link Node#getName} method will return the target of the PI and the
  * {@link Node#getText} method will return the data from all of the instructions.
  * </p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface ProcessingInstruction extends Node {

    /** <p>Returns the value of a specific name in the PI.</p>
      *
      * @param name is the name of the attribute to lookup.
      * @return the value of the named attribute
      */
    public String getValue(String name);

    public void setValue(String name, String value);    
    public void setValues(Map data);
    public boolean removeValue(String name);
    
}


