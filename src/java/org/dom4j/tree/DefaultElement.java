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

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.IllegalAddNodeException;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

/** <p><code>DefaultElement</code> is the default DOM4J default implementation
  * of an XML element.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultElement extends AbstractElement {

    /** The name of the element */
    private String name;

    /** The <code>Namespace</code> for this elemenet */
    private Namespace namespace;

    /** The parent of this node */
    private Element parent;

    /** The <code>ContentModel</code> for this elemenet */
    private ContentModel contentModel;
    
    /** The <code>AttributeModel</code> for this elemenet */
    private AttributeModel attributeModel;

    
    public DefaultElement() { 
        this.namespace = Namespace.NO_NAMESPACE;
    }

    public DefaultElement(String name) { 
        this.name = name;
        this.namespace = Namespace.NO_NAMESPACE;
    }

    public DefaultElement(String name, Namespace namespace) { 
        this.name = name;
        this.namespace = namespace;
    }

    public Namespace getNamespace() {
        return namespace;
    }
    
    public void setNamespace(Namespace namespace) {
        if ( namespace == null ) {
            namespace = Namespace.NO_NAMESPACE;
        }
        this.namespace = namespace;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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

    /** Allows derived classes to override the content model */
    protected ContentModel getContentModel() {
        if ( contentModel == null ) {
            contentModel = createContentModel();
        }
        return contentModel;
    }
    
    /** Allow derived classes to set the <code>ContentModel</code>
      */
    protected void setContentModel(ContentModel contentModel) {
        this.contentModel = contentModel;
    }

    protected AttributeModel getAttributeModel() {
        if ( attributeModel == null ) {
            attributeModel = createAttributeModel();
        }
        return attributeModel;
    }

    /** Allow derived classes to set the <code>AttributeModel</code>
      */
    protected void setAttributeModel(AttributeModel attributeModel) {
        this.attributeModel = attributeModel;
    }


    /** A Factory Method pattern which lazily creates 
      * a ContentModel implementation 
      */
    protected ContentModel createContentModel() {
        return new DefaultContentModel();
    }
    
    /** A Factory Method pattern which lazily creates 
      * an AttributeModel implementation 
      */
    protected AttributeModel createAttributeModel() {
        return new DefaultAttributeModel();
    }
}
