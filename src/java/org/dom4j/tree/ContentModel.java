package org.dom4j.tree;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

/** <p><code>ContentModel</code> represents an XML content model for an XML element.
  * This interface is used to decompose an element implementations into smaller
  * resusable units.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface ContentModel {

    public Namespace getNamespaceForPrefix(String prefix);
    
    public Namespace getNamespaceForURI(String uri);
    
    /** <p>Returns any additional namespaces declarations for this element 
      * other than namespace returned via the {@link Element#getNamespace()} method. 
      * If no additional namespace declarations are present for this
      * element then {@link Collections.EMPTY_LIST} will be returned.
      *
      * @return a list of any additional namespace declarations.
      */
    public List getAdditionalNamespaces(String defaultNamespaceURI);
    
    // Text API
    
    /** <p>Returns the text of this node.</p>
      *
      * @return the text for this node.
      */
    public String getText();
    


    public String getTextTrim();

    
    // Processing instruction API
    
    public List getProcessingInstructions();
    public List getProcessingInstructions(String target);
    public ProcessingInstruction getProcessingInstruction(String target);    
    public void setProcessingInstructions(List listOfPIs);
    

    
    
    
    /** <p>Returns true if this <code>Element</code> has mixed content.
      * Mixed content means that an element contains both textual data and
      * child elements.
      *
      * @return true if this element contains mixed content.
      */
    public boolean hasMixedContent();

    // this will allow String objects to be used to represent text
    // as the user doesn't mind too much about Nodes
    public List getContent();    
    public void setContent(List content);    
    public void clearContent();
    
    
    // node navigation API - return content as nodes
    // such as Text etc.
    public Node getNode(int index);
    public int getNodeCount();
    

    // return the child elements
    public Element getElementByID(String elementID);
    public Element getElement(String name);
    public Element getElement(String name, Namespace namespace);
    
    public List getElements();
    public List getElements(String name);
    public List getElements(String name, Namespace namespace);
    
    // add API which avoids explicit use of node classes

    public void addCDATA(ContentFactory factory, String cdata);
    public void addComment(ContentFactory factory, String comment);
    public void addText(ContentFactory factory, String text);
    
    public void addElement(ContentFactory factory, String name);
    public void addElement(ContentFactory factory, String name, Namespace namespace);
    
    public void addEntity(ContentFactory factory, String name, String text);
    public void addAdditionalNamespace(ContentFactory factory, String prefix, String uri);
    public void addProcessingInstruction(ContentFactory factory, String target, String data);
    public void addProcessingInstruction(ContentFactory factory, String target, Map data);

    public void setText(ContentFactory factory, String text);
    
    public boolean removeProcessingInstruction(String target);
    
    public void addNode(Node node);
    public boolean removeNode(Node node);
}
