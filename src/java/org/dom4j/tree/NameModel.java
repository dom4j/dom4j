package org.dom4j.tree;

import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;

/** <p><code>NameModel</code> is used to model the name of an XML element or
  * attribute. 
  * It is an immutable bean containing the local, qualified and namespace .</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class NameModel {

    public static final NameModel EMPTY_NAME = new NameModel("");
    
    /** Cache of name instances */
    protected static NameModelCache cache = new NameModelCache();
    
    /** The name of the element */
    private String name;

    /** The qualified name of the element */
    private String qualifiedName;

    /** The <code>Namespace</code> for this elemenet */
    private Namespace namespace;

    
    public static NameModel get(String name) {
        return cache.get(name);
    }
    
    public static NameModel get(String name, Namespace namespace) {
        return cache.get(name, namespace);
    }
    
    
    public NameModel(String name) { 
        this.name = name;
        this.namespace = Namespace.NO_NAMESPACE;
    }

    public NameModel(String name, Namespace namespace) { 
        this.name = name;
        this.namespace = namespace;
    }

    public NameModel(String name, String qualifiedName, Namespace namespace) { 
        this.name = name;
        this.qualifiedName = qualifiedName;
        this.namespace = namespace;
    }

    public Namespace getNamespace() {
        return namespace;
    }
    
    public String getName() {
        return name;
    }
    
    public String getNamespacePrefix() {
        return (namespace != null) ? namespace.getPrefix() : "";
    }

    public String getNamespaceURI() {
        return (namespace != null) ? namespace.getURI() : "";
    }

    public String getQualifiedName() {
        if ( qualifiedName == null ) {
            qualifiedName = createQualifiedName();
        }
        return qualifiedName;
    }
    
    protected String createQualifiedName() {
        if (namespace != null ) {
            String prefix = namespace.getPrefix();
            if (prefix != null && prefix.length() > 0) {
                return prefix + ":" + getName();
            }
        }
        return getName();
    }

}
