package org.dom4j.tree;

import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.IllegalAddNodeException;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.TreeVisitor;

/** <p><code>AbstractDocument</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractDocument extends AbstractBranch implements Document {
    
    public AbstractDocument() { 
    }
 
    public String asXML() {
        try {
            StringWriter out = new StringWriter();
            writer.output(this, out);
            return out.toString();
        } 
        catch (IOException e) {
            throw new RuntimeException("Wierd IOException while generating textual representation: " + e.getMessage());
        }
    }

    public void writeXML(PrintWriter out) {
        try {
            writer.output(this, out);
        }
        catch (IOException e) {
            throw new RuntimeException("Wierd IOException while generating textual representation: " + e.getMessage());
        }
    }
        
    /** <p><code>accept</code> method is the <code>Visitor Pattern</code> method.
      * </p>
      *
      * @param visitor <code>TreeVisitor</code> is the visitor.
      */
    public void accept(TreeVisitor visitor) {
        visitor.visit(this);

        DocumentType docType = getDocType();
        if ( docType != null ) {
            visitor.visit( docType );
        }
        
        // visit content
        List content = getContent();
        if (content != null) {
            for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
                Object object = iter.next();
                if (object instanceof String) {
                    DefaultText text = new DefaultText((String) object);
                    visitor.visit(text);
                } 
                else {
                    Node node = (Node) object;
                    node.accept(visitor);
                }
            }            
        }
    }
    
    public String toString() {
        return super.toString() + " [Document: name " + getName() + "]";
    }
       
    public Element addElement(String name) {
        checkAddElementAllowed();
        Element node = super.addElement(name);
        rootElementAdded(node);
        return node;
    }
    
    public Element addElement(String name, String prefix, String uri) {
        checkAddElementAllowed();
        Element node = super.addElement(name, prefix, uri);
        rootElementAdded(node);
        return node;
    }
    
    public Element addElement(String name, Namespace namespace) {
        checkAddElementAllowed();
        Element node = super.addElement(name, namespace);
        rootElementAdded(node);
        return node;
    }

    public void setRootElement(Element rootElement) {
        clearContent();
        super.add(rootElement);
        rootElementAdded(rootElement);
    }

    public void add(Element element) {
        checkAddElementAllowed();
        super.add(element);
        rootElementAdded(element);
    }
    
    public boolean remove(Element element) {
        boolean answer = super.remove(element);        
        Element root = getRootElement();
        if ( root != null && answer ) {
            setRootElement(null);
        }
        element.setDocument(null);
        return answer;
    }
    
    
    
    
    protected void childAdded(Node node) {
        if (node != null ) {
            node.setDocument(this);
        }
    }
    
    protected void childRemoved(Node node) {
        if ( node != null ) {
            node.setDocument(null);
        }
    }     

    protected void checkAddElementAllowed() {
        Element root = getRootElement();
        if ( root != null ) {
            throw new IllegalAddNodeException(  
                this, 
                root, 
                "Cannot add another element to this Document as it already has "
                + " a root element of: " + root.getQualifiedName()
            );
        }
    }

    /** Called to set the root element variable */
    protected abstract void rootElementAdded(Element rootElement);
    
}
