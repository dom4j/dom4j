package org.dom4j;

import org.dom4j.XPathEngine;

/** <p><code>Document</code> defines an XML Document.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface Document extends Branch {

    /** Returns the root {@link Element} for this document.
      *
      * @return the root element for this document
      */
    public Element getRootElement();
    
    /** Sets the root element for this document
      *
      * @param rootElement the new root element for this document
      */    
    public void setRootElement(Element rootElement);

    public DocumentType getDocType();
    public void setDocType(DocumentType docType);
    
    public void setDocType(String name, String publicId, String systemId);    
    public DocumentType createDocType(String name, String publicId, String systemId);
    
    public XPathEngine getXPathEngine();
    public void setXPathEngine(XPathEngine xpathEngine);

}



