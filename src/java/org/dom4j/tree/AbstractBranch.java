package org.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Branch;
import org.dom4j.Comment;
import org.dom4j.Element;
import org.dom4j.IllegalAddNodeException;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.io.XMLWriter;

/** <p><code>AbstractBranch</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractBranch extends AbstractNode implements Branch {

    /** The XML writer used by default */
    protected static final XMLWriter writer = new XMLWriter( "  ", true );

    
    public AbstractBranch() { 
    }
 
    // Content Model methods
    
    public String getText() {
        return getContentModel().getText();
    }

    public String getTextTrim() {
        return getContentModel().getTextTrim();
    }

    public List getProcessingInstructions() {
        return getContentModel().getProcessingInstructions();
    }
    
    public List getProcessingInstructions(String target) {
        return getContentModel().getProcessingInstructions(target);
    }
    
    public ProcessingInstruction getProcessingInstruction(String target) {
        return getContentModel().getProcessingInstruction(target);
    }
    
    public void setProcessingInstructions(List listOfPIs) {
        getContentModel().setProcessingInstructions(listOfPIs);
    }
    
    public boolean removeProcessingInstruction(String target) {
        return getContentModel().removeProcessingInstruction(target);
    }
       
    public List getContent() {
        return getContentModel().getContent();
    }
    
    public void setContent(List content) {
        getContentModel().setContent(content);
    }
    
    public void clearContent() {
        getContentModel().clearContent();
    }
    
    public Node getNode(int index) {
        return getContentModel().getNode(index);
    }
    
    public int getNodeCount() {
        return getContentModel().getNodeCount();
    }
    
    public void addComment(String comment) {
        getContentModel().addComment(getContentFactory(), comment);
    }
    
    public void addElement(String name) {
        getContentModel().addElement(getContentFactory(), name);
    }
    
    public void addElement(String name, Namespace namespace) {
        getContentModel().addElement(getContentFactory(), name, namespace);
    }
    
    public void addProcessingInstruction(String target, String data) {
        getContentModel().addProcessingInstruction(getContentFactory(), target, data);
    }
    
    public void addProcessingInstruction(String target, Map data) {
        getContentModel().addProcessingInstruction(getContentFactory(), target, data);
    }
    
    public void addComment(ContentFactory factory, String comment) {
        addNode(createComment(comment));
    }
    
    public void addElement(ContentFactory factory, String name) {
        addNode(createElement(name));
    }
    
    public void addElement(ContentFactory factory, String name, Namespace namespace) {
        addNode(createElement(name, namespace));
    }
    

    // typesafe versions using node classes
    public void add(Comment comment) {
        addNode(comment);
    }
    
    public void add(Element element) {
        addNode(element);
    }
    
    public void add(ProcessingInstruction pi) {
        addNode(pi);
    }
    
    public boolean remove(Comment comment) {
        return removeNode(comment);
    }
    
    public boolean remove(Element element) {
        return removeNode(element);
    }
    
    public boolean remove(ProcessingInstruction pi) {
        return removeNode(pi);
    }
    
    
    
    
    
    // Factory methods - delegate to the ContentFactory
    
    public Comment createComment(String text) {
        return getContentFactory().createComment(text);
    }
    
    public Element createElement(String name) {
        return getContentFactory().createElement(name);
    }
    
    public Element createElement(String name, String prefix, String uri) {
        return getContentFactory().createElement(name, prefix, uri);
    }
    
    public Element createElement(String name, Namespace namespace) {
        return getContentFactory().createElement(name, namespace);
    }
    
    public Namespace createNamespace(String prefix, String uri) {
        return getContentFactory().createNamespace(prefix, uri);
    }
    
    
    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        return getContentFactory().createProcessingInstruction(target, data);
    }
    
    public ProcessingInstruction createProcessingInstruction(String target, Map data) {
        return getContentFactory().createProcessingInstruction(target, data);
    }
    
    
    
    
    
    
    // Implementation methods
    
    protected void addNode(Node node) {
        if (node.getParent() != null) {
            // XXX: could clone here
            String message = "The Node already has an existing parent of \"" 
                + node.getParent().getQualifiedName() + "\"";
            throw new IllegalAddNodeException(this, node, message);
        }
        getContentModel().addNode(node);
    }

    protected boolean removeNode(Node node) {
        boolean answer = getContentModel().removeNode(node);
        if (answer) {
            node.setParent(null);
        }
        return answer;
    }
    
    
    /** Allows derived classes to override the factory behaviour */
    protected ContentFactory getContentFactory() {
        return DefaultContentFactory.getInstance();
    }

    /** Allows derived classes to override the content model */
    protected abstract ContentModel getContentModel();
}
