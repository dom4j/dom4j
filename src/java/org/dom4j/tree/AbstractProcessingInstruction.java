package org.dom4j.tree;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.dom4j.ProcessingInstruction;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractProcessingInstruction</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractProcessingInstruction extends AbstractNode implements ProcessingInstruction {

    public AbstractProcessingInstruction() {
    }
    
    public String toString() {
        return super.toString() + " [ProcessingInstruction: &" + getName() + ";]";
    }

    public String asXML() {
        return "<?" + getName() + " " + getText() + "?>";
    }
    
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);
    }

    public void setValue(String name, String value) {
        throw new UnsupportedOperationException( 
            "This PI is read-only and cannot be modified" 
        );
    }
    
    public void setValues(Map data) {
        throw new UnsupportedOperationException( 
            "This PI is read-only and cannot be modified" 
        );
    }
    
    public String getName() {
        return getTarget();
    }
    
    public void setName(String name) {
        setTarget(name);
    }
    
    public boolean removeValue(String name) {
        return false;
    }
    
    
    // Helper methods
    
    /** <p>This will convert the Map to a string representation.</p>
      *
      * @param mapData is a <code>Map</code> of PI data to convert
      */
    protected String toString(Map mapData) {
        StringBuffer buffer = new StringBuffer();
        
        for ( Iterator iter = mapData.entrySet().iterator(); iter.hasNext(); ) {
            Map.Entry entry = (Map.Entry) iter.next();
            String name = (String) entry.getKey();
            String value = (String) entry.getValue();
            
            buffer.append(name);
            buffer.append("=\"");
            buffer.append(value);
            buffer.append("\" ");
        }
        // remove the last space
        buffer.setLength(buffer.length() - 1);
        return buffer.toString();
    }

    /**<p>Parses the raw data of PI as a <code>Map</code>.</p>
      *
      * @param rawData <code>String</code> PI data to parse
      */
    protected Map parseData(String rawData) {
        Map data = new HashMap();

        // Break up name/value pairs
        StringTokenizer s =
            new StringTokenizer(rawData);

        // Iterate through the pairs
        while (s.hasMoreTokens()) {
            // Now break up pair on the = and (" or ') characters
            StringTokenizer t =
                new StringTokenizer(s.nextToken(), "='\"");

            if (t.countTokens() >= 2) {
                String name = t.nextToken();
                String value = t.nextToken();

                data.put(name, value);
            }
        }
        return data;
    }
}


