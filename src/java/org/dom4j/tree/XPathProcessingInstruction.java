package org.dom4j.tree;

import java.util.Map;

import org.dom4j.Node;
import org.dom4j.Element;

/** <p><code>XPathProcessingInstruction</code> implements a doubly linked node which 
  * supports the parent relationship and is mutable.
  * It is useful when evalutating XPath expressions.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class XPathProcessingInstruction extends DefaultProcessingInstruction {

    /** The parent of this node */
    private Element parent;

    /** <p>This will create a new PI with the given target and data</p>
      *
      * @param target is the name of the PI
      * @param data is the <code>Map</code> data for the PI
      */
    public XPathProcessingInstruction(String target, Map data) {
        super(target, data);
    }

    /** <p>This will create a new PI with the given target and data</p>
      *
      * @param target is the name of the PI
      * @param data is the data for the PI
      */
    public XPathProcessingInstruction(String target, String data) {
        super(target, data);
    }

    /** <p>This will create a new PI with the given target and data</p>
      *
      * @param parent is the parent element
      * @param target is the name of the PI
      * @param data is the data for the PI
      */
    public XPathProcessingInstruction(Element parent, String target, String data) {
        super(target, data);
        this.parent = parent;
    }
    
    public void setTarget(String target) {
        this.target = target;
    }
    
    public void setText(String rawData) {
        this.rawData = rawData;
        this.mapData = parseData(rawData);
    }
    
    public void setValues(Map mapData) {
        this.mapData = mapData;
        this.rawData = toString(mapData);
    }
    
    public void setValue(String name, String value) {
        mapData.put(name, value);
    }
    

    public Element getParent() {
        return parent;
    }

    public void setParent(Element parent) {
        this.parent = parent;
    }
    
    public boolean supportsParent() {
        return true;
    }

}
