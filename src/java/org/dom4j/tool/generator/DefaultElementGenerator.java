package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.Attribute;
import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.AttributeDecl;

import org.metastuff.coder.JClass;
import org.metastuff.coder.codelet.LazyPropertyCodelet;

/** <p><code>AttributeGenerator</code> generates {@link Attribute} 
  * implementations from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DefaultElementGenerator extends ElementGenerator {

    public DefaultElementGenerator() {
    }
    
    protected void addAttributeModel() {
        jclass.addImportStatement( "org.dom4j.tree.AttributeModel" );
        jclass.addImportStatement( "org.dom4j.tree.DefaultAttributeModel" );
        LazyPropertyCodelet.addLazyProperty( jclass, "attributeModel", "AttributeModel", "new DefaultAttributeModel()" );
    }
    
    protected void addContentModel() {
        jclass.addImportStatement( "org.dom4j.tree.ContentModel" );
        jclass.addImportStatement( "org.dom4j.tree.DefaultContentModel" );
        LazyPropertyCodelet.addLazyProperty( jclass, "contentModel", "ContentModel", "new DefaultContentModel()" );
    }
    
}
