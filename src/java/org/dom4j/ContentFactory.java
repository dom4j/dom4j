package org.dom4j;

import java.util.Map;

import org.dom4j.tree.DefaultAttribute;
import org.dom4j.tree.DefaultCDATA;
import org.dom4j.tree.DefaultComment;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultEntity;
import org.dom4j.tree.DefaultNamespace;
import org.dom4j.tree.DefaultText;
import org.dom4j.tree.DefaultProcessingInstruction;


/** <p><code>ContentFactory</code> is a collection of factory methods to allow
  * the easy creation of DOM4J XML content nodes. </p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class ContentFactory {

    /** The Singleton instance */
    protected static ContentFactory singleton = new ContentFactory();
 
    /** <p>Access to the Singleton instance of ContentFactory for
      * the default implementation.</p>
      *
      * @return the default singleon instance
      */
    public static ContentFactory getInstance() {
        return singleton;
    }
    
    
    
    // Factory methods
    
    public Attribute createAttribute(String name, String value) {
        return new DefaultAttribute(name, value);
    }
    
    public Attribute createAttribute(String name, String value, Namespace namespace) {
        return new DefaultAttribute(name, value, namespace);
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
    
    public Element createElement(String name, String prefix, String uri) {
        return createElement(name, createNamespace(prefix, uri));
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
        return DefaultNamespace.get(prefix, uri);
    }
    
    public ProcessingInstruction createProcessingInstruction(String target, String data) {
        return new DefaultProcessingInstruction(target, data);
    }
    
    public ProcessingInstruction createProcessingInstruction(String target, Map data) {
        return new DefaultProcessingInstruction(target, data);
    }
    
}
