package org.dom4j.tree;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.dom4j.Attribute;
import org.dom4j.CDATA;
import org.dom4j.CharacterData;
import org.dom4j.Comment;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Node;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;

/** <p><code>AbstractContentModel</code> is an abstract base class for 
  * tree implementors to use for implementation inheritence.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractContentModel implements ContentModel {

    public String getText() {
        List content = getContent();
        if (content != null) {
            int size = content.size();
            if (size >= 1) {
                Object first = content.get(0);
                if (first != null) {
                    // If we hold only a String, return it directly
                    if (size == 1 ) {
                        if ( first instanceof String) {
                            return (String) first;
                        }
                        else if ( first instanceof Text ) {
                            Text text = (Text) first;
                            return text.getText();
                        }
                    }
                    
                    // Else build String up
                    StringBuffer textContent = new StringBuffer();
                    boolean hasText = false;
                    for ( Iterator i = content.iterator(); i.hasNext(); ) {
                        Object obj = i.next();
                        if (obj instanceof String) {
                            textContent.append((String)obj);
                        } else if (obj instanceof CharacterData) {
                            textContent.append(((CharacterData)obj).getText());
                        }
                    }
                    return textContent.toString();
                }
            }
        }
        return "";
    }

    public String getTextTrim() {
        String text = getText();

        StringBuffer textContent = new StringBuffer();
        StringTokenizer tokenizer = new StringTokenizer(text);
        while (tokenizer.hasMoreTokens()) {
            String str = tokenizer.nextToken();
            textContent.append(str);
            if (tokenizer.hasMoreTokens()) {
                textContent.append(" ");  // separator
            }
        }

        return textContent.toString();
    }

    public void setText(ContentFactory factory, String text) {
        clearContent();
        addText(factory, text);
    }

    public boolean hasMixedContent() {
        List content = getContent();
        if (content == null || content.isEmpty() || content.size() < 2) {
            return false;
        }

        Class prevClass = null;
        for ( Iterator iter = content.iterator(); iter.hasNext(); ) {
            Object object = iter.next();
            Class newClass = object.getClass();
            if (newClass != prevClass) {
               if (prevClass != null) {
                  return true;
               }
               prevClass = newClass;
            }
        }
        return false;
    }
    
    public void setProcessingInstructions(List listOfPIs) {
        for ( Iterator iter = listOfPIs.iterator(); iter.hasNext(); ) {
            ProcessingInstruction pi = (ProcessingInstruction) iter.next();
            addNode(pi);
        }
    }
    
    public Element getElement(String name) {
        return getElement(name, Namespace.NO_NAMESPACE);
    }
    
    public List getElements(String name) {
        return getElements(name, Namespace.NO_NAMESPACE);
    }
        
    public CDATA addCDATA(ContentFactory factory, String cdata) {
        CDATA node = factory.createCDATA( cdata );
        addNode( node );
        return node;
    }
    
    public Comment addComment(ContentFactory factory, String comment) {
        Comment node = factory.createComment( comment );
        addNode( node );
        return node;
    }
    
    public Text addText(ContentFactory factory, String text) {
        Text node = factory.createText( text );
        addNode( node );
        return node;
    }
    
    public Element addElement(ContentFactory factory, String name) {
        Element node = factory.createElement( name );
        addNode( node );
        return node;
    }
    
    public Element addElement(ContentFactory factory, String name, String prefix, String uri) {
        Element node = factory.createElement( name, prefix, uri );
        addNode( node );
        return node;
    }
    
    public Element addElement(ContentFactory factory, String name, Namespace namespace) {
        Element node = factory.createElement( name, namespace );
        addNode( node );
        return node;
    }
    
    public Entity addEntity(ContentFactory factory, String name) {
        Entity node = factory.createEntity( name );
        addNode( node );
        return node;
    }
    
    public Entity addEntity(ContentFactory factory, String name, String text) {
        Entity node = factory.createEntity( name, text );
        addNode( node );
        return node;
    }
    
    public Namespace addAdditionalNamespace(ContentFactory factory, String prefix, String uri) {
        Namespace node = factory.createNamespace( prefix, uri );
        addNode( node );
        return node;
    }
    
    public ProcessingInstruction addProcessingInstruction(ContentFactory factory, String target, String data) {
        ProcessingInstruction node = factory.createProcessingInstruction( target, data );
        addNode( node );
        return node;
    }
    
    public ProcessingInstruction addProcessingInstruction(ContentFactory factory, String target, Map data) {
        ProcessingInstruction node = factory.createProcessingInstruction( target, data );
        addNode( node );
        return node;
    }
}
