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

    /** The parent of this node */
    private Element parent;

    /** The <code>NameModel</code> for this element */
    private NameModel nameModel;
    
    /** The <code>ContentModel</code> for this element */
    private ContentModel contentModel;
    
    /** The <code>AttributeModel</code> for this element */
    private AttributeModel attributeModel;

    
    public DefaultElement() { 
        this.nameModel = NameModel.EMPTY_NAME;
    }

    public DefaultElement(String name) { 
        this.nameModel = NameModel.get(name);
    }

    public DefaultElement(NameModel nameModel) { 
        this.nameModel = nameModel;
    }

    public DefaultElement(String name, Namespace namespace) { 
        this.nameModel = NameModel.get(name, namespace);
    }

    public void setNamespace(Namespace namespace) {
        this.nameModel = NameModel.get(getName(), namespace);
    }
    
    public void setName(String name) {
        this.nameModel = NameModel.get(name, getNamespace());
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

    protected NameModel getNameModel() {
        return nameModel;
    }
    
    /** Allow derived classes to change the name model */
    protected void setNameModel(NameModel nameModel) {
        this.nameModel = nameModel;
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
