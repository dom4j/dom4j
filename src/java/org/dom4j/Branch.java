package org.dom4j;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** <p><code>Branch</code> interface defines common behaviour 
  * for XML elements and XML documents so that they can be treated in a 
  * polymorphic manner.</p>
  *
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface Branch extends Node {

    // node navigation API - return content as nodes
    // such as Text etc.
    public Node getNode(int index);
    public int getNodeCount();
    
    public List getContent();    
    public void setContent(List content);    
    public void clearContent();
    
    public List getProcessingInstructions();
    public List getProcessingInstructions(String target);
    public ProcessingInstruction getProcessingInstruction(String target);    
    public void setProcessingInstructions(List listOfPIs);
    
    
    public Comment addComment(String comment);
    public Element addElement(String name);
    public Element addElement(String name, String prefix, String uri);
    public Element addElement(String name, Namespace namespace);
    public ProcessingInstruction addProcessingInstruction(String target, String data);
    public ProcessingInstruction addProcessingInstruction(String target, Map data);

    public boolean removeProcessingInstruction(String target);

    
    
    /** Adds the given <code>Comment</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param comment is the comment to be added
      */
    public void add(Comment comment);
    
    /** Adds the given <code>Element</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param element is the element to be added
      */
    public void add(Element element);
    
    /** Adds the given <code>ProcessingInstruction</code> to this element.
      * If the given node already has a parent defined then an
      * <code>InvalidAddNodeException</code> will be thrown.
      *
      * @param pi is the processing instruction to be added
      */
    public void add(ProcessingInstruction pi);
        
    /** Removes the given <code>Comment</code> from this element.
      *
      * @param comment is the comment to be removed
      * @return true if the comment was removed
      */
    public boolean remove(Comment comment);
    
    /** Removes the given <code>Element</code> from this element.
      *
      * @param element is the element to be removed
      * @return true if the element was removed
      */
    public boolean remove(Element element);
    
    /** Removes the given <code>ProcessingInstruction</code> from this element.
      *
      * @param pi is the processing instruction to be removed
      * @return true if the processing instruction was removed
      */
    public boolean remove(ProcessingInstruction pi);
}
