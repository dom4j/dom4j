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

    // typesafe versions using node classes
    public void add(Comment comment);
    public void add(Element element);
    public void add(ProcessingInstruction pi);
        
    public boolean remove(Comment comment);
    public boolean remove(Element element);
    public boolean remove(ProcessingInstruction pi);
}
