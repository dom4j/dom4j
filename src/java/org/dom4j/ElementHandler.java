package org.dom4j;

/** <p><code>ElementHandler</code> interface defines a handler of 
  *  <code>Element</code> objects. 
  * It is used primarily in event based processing models such as for 
  * processing large XML documents as they are being parsed rather than 
  * waiting until the whole document is parsed.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface ElementHandler {

 
    /** Called by an event based processor to process the element
      * in some way.
      *
      * @param element is the element to process
      */
    public void handle(Element element);

}
