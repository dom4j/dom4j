package org.dom4j;

import org.dom4j.tree.DefaultDocument;

/** <p><code>DocumentFactory</code> is a collection of factory methods to allow
  * easy custom building of DOM4J trees. The default tree that is built uses
  * a doubly linked tree. </p>
  *
  * <p>The tree built allows full XPath expressions from anywhere on the 
  * tree.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DocumentFactory {

    /** The Singleton instance */
    protected static DocumentFactory singleton = new DocumentFactory();
 
    /** The <code>{@link XPathEngine}</code> used to resolve XPath expressions */
    protected XPathEngine XPathEngine;
    
    // Some static helper methods
    public static Document create() {
        return singleton.createDocument();
    }
    
    /** <p>Access to singleton implementation of DocumentFactory which 
      * is used if no DocumentFactory is specified when building using the 
      * standard builders.</p>
      *
      * @return the default singleon instance
      */
    public static DocumentFactory getInstance() {
        return singleton;
    }
    
    
    
    /** <p>This will return the <code>{@link XPathEngine}</code>
      * instance which will be used for all <code>Document</code>
      * instances created by this factory.</p>
      *
      * @return the resolver of XPath expressions
      */
    public XPathEngine getXPathEngine() {
        if ( XPathEngine == null ) {
            XPathEngine = createXPathEngine();
        }
        return XPathEngine;
    }

    /** <p>This will set the <code>{@link XPathEngine}</code>
      * instance which will be used for all <code>Document</code>
      * instances created by this factory.</p>
      *
      * @param XPathEngine <code>XPathEngine</code> - the resolver of XPath expressions
      */
    public void setXPathEngine(XPathEngine XPathEngine) {
        this.XPathEngine = XPathEngine;
    }

    
    // Factory methods
    
    public Document createDocument() {
        Document answer = new DefaultDocument();
        answer.setXPathEngine( getXPathEngine() );
        return answer;
    }
    
    
    /** <p>A Factory Method pattern to create the default 
      * <code>{@link XPathEngine}</code> instance which will be used for 
      * all <code>Document</code> instances created by this factory.</p>
      *
      * @return the new resolver of XPath expressions
      */
    protected XPathEngine createXPathEngine() {
        // use the default
        return XPathHelper.getDefaultXPathEngine();
    }

    
}
