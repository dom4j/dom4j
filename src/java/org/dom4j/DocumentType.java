package org.dom4j;

/** <p><code>DocumentType</code> defines an XML DOCTYPE declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface DocumentType extends Node {

    /** This method is the equivalent to the {@link #getName} 
      * method. It is added for clarity.
      *
      * @returns the root element name for the document type.
      */
    public String getElementName();
    
    /** This method is the equivalent to the {@link #setName} 
      * method. It is added for clarity.
      */
    public void setElementName(String elementName);

    public String getPublicID();
    public void setPublicID(String publicID);

    public String getSystemID();
    public void setSystemID(String systemID);

}










