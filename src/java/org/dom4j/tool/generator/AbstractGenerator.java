package org.dom4j.tool.generator;

import java.util.Iterator;

import org.dom4j.tool.dtd.ElementDecl;
import org.dom4j.tool.dtd.AttributeDecl;

import org.metastuff.coder.*;

/** <p><code>AbstractGenerator</code> is an abstract base class of all
  * generators used to build a DOM4J schema from a DTD declaration.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public abstract class AbstractGenerator extends AbstractJClassProducer {

    protected JClass jclass;

    public AbstractGenerator(String nameExpression) {
        super( nameExpression );
    }
    
    protected void enrich(JClass jclass) {
        this.jclass = jclass;
    }
}
