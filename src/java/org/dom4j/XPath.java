package org.dom4j;


/** <p><code>XPath</code> represents an XPath expression after 
  * it has been parsed from a String.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface XPath {

    /** <p><code>getText</code> will return the textual version of 
      * the XPath expression.</p>
      *
      * @return the textual format of the XPath expression.
      */
    public String getText();
        
}
