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

/** <p><code>DefaultContentFactory</code> is the default content factory of 
  * XML content nodes for an XML elements.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultContentFactory implements ContentFactory {

    /** Singleton instance */
    private static DefaultContentFactory singleton = new DefaultContentFactory();
    
    /** @return the singleton instance
      */
    public static DefaultContentFactory getInstance() {
        return singleton;
    }
    
    public DefaultContentFactory() {
    }
    
    public Attribute createAttribute(String name, String value) {
        return new DefaultAttribute(name, value);
    }
    
    public Attribute createAttribute(String name, String value, Namespace namespace) {
        return new DefaultAttribute(name, value);
    }

    public CDATA createCDATA(String text) {
        return new DefaultCDATA(text);
    }
    
    public Comment createComment(String text) {
        return new DefaultComment(text);
    }
    
    public Text createText(String text) {
        return new DefaultText(text);
    }
    

    // elements
    public Element createElement(String name) {
        return new DefaultElement(name);
    }
    
    public Element createElement(String name, Namespace namespace) {
        return new DefaultElement(name, namespace);
    }
    
    
    public Entity createEntity(String name) {
        return new DefaultEntity(name);
    }
    
    public Entity createEntity(String name, String text) {
        return new DefaultEntity(name, text);
    }
    
    public Namespace createNamespace(String prefix, String uri) {
        return new DefaultNamespace(prefix, uri);
    }
    
    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        return new DefaultProcessingInstruction(target, data);
    }
    
    public ProcessingInstruction createProcessingInstruction(String target, Map data) {
        return new DefaultProcessingInstruction(target, data);
    }
    
}
