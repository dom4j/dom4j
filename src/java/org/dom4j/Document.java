package org.dom4j;

import org.dom4j.XPathEngine;

/** <p><code>Document</code> defines an XML Document.</p>
  *
  * @version $Revision$
  */
public interface Document extends Branch {

    public Element getRootElement();
    public void setRootElement(Element rootElement);

    public DocumentType getDocType();
    public void setDocType(DocumentType docType);
    
    public void setDocType(String name, String publicId, String systemId);    
    public DocumentType createDocType(String name, String publicId, String systemId);
    
    public XPathEngine getXPathEngine();
    public void setXPathEngine(XPathEngine xpathEngine);

}



