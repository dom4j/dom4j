package org.dom4j.tree;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentType;
import org.dom4j.Element;
import org.dom4j.IllegalAddNodeException;
import org.dom4j.XPathEngine;
import org.dom4j.XPathHelper;

/** <p><code>DefaultDocument</code> is the default DOM4J default implementation
  * of an XML document.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultDocument extends AbstractDocument {

    /** The name of the document */
    private String name;

    /** The <code>ContentModel</code> for this elemenet */
    private ContentModel contentModel;
    
    /** The root element of this document */
    private Element rootElement;
    
    /** The document type for this document */
    private DocumentType docType;
    
    /** The engine used to evaluate XPath expressions for this document */
    private XPathEngine xpathEngine;
    
    
    public DefaultDocument() { 
    }

    public DefaultDocument(String name) { 
        this.name = name;
    }

    public DefaultDocument(Element rootElement) { 
        this.rootElement = rootElement;
    }

    public DefaultDocument(Element rootElement, DocumentType docType) {
        this.rootElement = rootElement;
        this.docType = docType;
    }

    public DefaultDocument(String name, Element rootElement, DocumentType docType) {
        this.name = name;
        this.rootElement = rootElement;
        this.docType = docType;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    
    public Element getRootElement() {
        return rootElement;
    }
    
    public DocumentType getDocType() {
        return docType;
    }
    
    public void setDocType(DocumentType docType) {
        this.docType = docType;
    }
    
    public void setDocType(String name, String publicId, String systemId) {
        setDocType( createDocType( name, publicId, systemId ) );
    }
    
    public DocumentType createDocType(String name, String publicId, String systemId) {
        return new DefaultDocumentType( name, publicId, systemId );
    }
    
    public XPathEngine getXPathEngine() {
        if ( xpathEngine == null ) {
            xpathEngine = XPathHelper.getDefaultXPathEngine();
        }
        return xpathEngine;
    }
    
    public void setXPathEngine(XPathEngine xpathEngine) {
        this.xpathEngine = xpathEngine;
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


    /** A Factory Method pattern which lazily creates 
      * a ContentModel implementation 
      */
    protected ContentModel createContentModel() {
        return new DefaultContentModel();
    }
    
    protected void rootElementAdded(Element element) {
        this.rootElement = element;
        element.setDocument(this);
    }
    
}
