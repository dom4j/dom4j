package org.dom4j.tree;

import org.dom4j.CDATA;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.TreeVisitor;

/** <p><code>DefaultCDATA</code> is the default DOM4J implementation of a 
  * singly linked read only XML CDATA.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultCDATA extends AbstractCDATA implements CDATA {

    /** Text of the <code>CDATA</code> node */
    protected String text;

    /** @param text is the CDATA text
      */
    public DefaultCDATA(String text) {
	this.text = text;
    }

    public String getText() {
	return text;
    }
    
    protected Node createXPathNode(Element parent) {
        return new XPathCDATA( parent, getText() );
    }
}
