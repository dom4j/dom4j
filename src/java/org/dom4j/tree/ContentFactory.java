package org.dom4j.tree;

import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

/** <p><code>ContentFactory</code> represents a factory of XML content nodes
  * model for an XML element.
  * This interface is used to decompose an element implementations into smaller
  * resusable units.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public interface ContentFactory {

    public Attribute createAttribute(String name, String value);
    public Attribute createAttribute(String name, String value, Namespace namespace);

    // character data nodes
    public CDATA createCDATA(String text);
    public Comment createComment(String text);
    public Text createText(String text);

    // elements
    public Element createElement(String name);
    public Element createElement(String name, String prefix, String uri);
    public Element createElement(String name, Namespace namespace);
    
    
    public Entity createEntity(String name);
    public Entity createEntity(String name, String text);
    public Namespace createNamespace(String prefix, String uri);
    public ProcessingInstruction createProcessingInstruction(String target, String data);
    public ProcessingInstruction createProcessingInstruction(String target, Map data);
    
}
