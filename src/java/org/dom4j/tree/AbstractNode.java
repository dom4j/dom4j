package org.dom4j.tree;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.TreeVisitor;
import org.dom4j.XPath;
import org.dom4j.XPathEngine;
import org.dom4j.XPathHelper;

/** <p><code>AbstractNode</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
 *
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
 * @version $Revision$
 */
public abstract class AbstractNode implements Node, Cloneable, Serializable {
    
    public AbstractNode() {
    }

    public Document getDocument() {
        Element element = getParent();
        return ( element != null ) ? element.getDocument() : null;
    }
    
    public void setDocument(Document document) {
    }
    
    public Element getParent() {
        return null;
    }

    public void setParent(Element parent) {
    }
    
    public boolean supportsParent() {
        return false;
    }

    public boolean isReadOnly() {
        return true;
    }

    public Object clone() {
        if ( ! isReadOnly() ) {
            throw new RuntimeException( "clone() not implemented yet for "
                + "this type which is not read-only" );
        }
        return this;
    }

    public String getName() {
        return null;
    }

    public void setName(String name) {
        throw new UnsupportedOperationException( "This node cannot be modified" );
    }

    public String getText() {
        return null;
    }
    
    public void setText(String text) {
        throw new UnsupportedOperationException( "This node cannot be modified" );
    }
    
    
    public void writeXML(PrintWriter writer) {
        writer.print( asXML() );
    }
        

    // XPath methods
    
    public XPathEngine getXPathEngine() {
        XPathEngine answer = null;
        Document document = getDocument();
        if ( document != null ) {
            answer = document.getXPathEngine();
        }
        if ( answer == null ) {
            answer = XPathHelper.getDefaultXPathEngine();
        }
        return answer;
    }
    
    public List selectNodes(XPath xpath) {
        return getXPathEngine().selectNodes(this, xpath);
    }
    
    public List selectNodes(String xpathExpression) {
        XPathEngine engine = getXPathEngine();
        XPath xpath = engine.createXPath(xpathExpression);
        return engine.selectNodes(this, xpath);
    }
    
    public Node selectSingleNode(XPath xpath) {
        return getXPathEngine().selectSingleNode(this, xpath);
    }
    
    public Node selectSingleNode(String xpathExpression) {
        XPathEngine engine = getXPathEngine();
        XPath xpath = engine.createXPath(xpathExpression);
        return engine.selectSingleNode(this, xpath);
    }
    
    public Node asXPathNode(Element parent) {
        if (supportsParent()) {
            return this;
        }
        throw new RuntimeException("asXPathNode() not yet implemented fully");
    }
    
}
