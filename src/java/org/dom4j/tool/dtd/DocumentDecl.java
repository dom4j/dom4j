package org.dom4j.tool.dtd;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** <p><code>DocumentDecl</code> represents the collection of element 
  * declarations taken from a DTD.</p>
  *
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DocumentDecl {

    /** The element declarations */
    private ArrayList elementList = new ArrayList();
    /** The name map of the element declarations */
    private HashMap elementMap = new HashMap();
    
    public DocumentDecl() {
    }
    
    /** Adds the given {@link ElementDecl} object.
      * 
      * @param elementDecl the declaration to be added
      */
    public void add(ElementDecl elementDecl) {
        elementList.add(elementDecl);
        elementMap.put(elementDecl.getName(), elementDecl);
    }
    
    /** Removes the given {@link ElementDecl} object.
      * 
      * @param elementDecl the declaration to be removed
      */
    public void remove(ElementDecl elementDecl) {
        elementList.remove(elementDecl);
        elementMap.remove(elementDecl.getName());
    }
    
    /** Removes all {@link ElementDecl} objects 
      */
    public void clear() {
        elementList.clear();
        elementMap.clear();
    }
    
    /** @return an iterator across all {@link ElementDecl} instances
      */
    public Iterator iterator() {
        return elementList.iterator();
    }
    
    /** @return the {@link ElementDecl} instance for the given name or null
      */
    public ElementDecl get(String name) {
        return (ElementDecl) elementMap.get(name);
    }
    
    /** Writes the state of the model to the given writer
      * 
      * @param out is the writer to output to
      */
    public void write( PrintWriter out ) {
        for ( Iterator iter = iterator(); iter.hasNext(); ) {
            ElementDecl elementDecl = (ElementDecl) iter.next();
            elementDecl.write(out);
            out.println();
        }
    }
    
}
