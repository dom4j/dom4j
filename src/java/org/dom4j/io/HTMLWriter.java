package org.dom4j.io;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;
   
import org.dom4j.CDATA;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Entity;

/** <p><code>HTMLWriter</code> takes a DOM4J tree and formats it to a
  * stream as HTML.  
  * This formatter is similar to XMLWriter but outputs the text of CDATA 
  * and Entity sections rather than the serialised format as in XML and
  * also supports certain element which have no corresponding close tag such 
  * as for &lt;BR&gt; and &lt;P&gt;.
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a> (james.strachan@metastuff.com)
  * @version $Revision$ 
 */
public class HTMLWriter extends XMLWriter {

    /** Used to store the qualified element names which 
      * should have no close element tag 
      */
    private Set omitElementCloseSet;

    public HTMLWriter() {
    }

    public HTMLWriter(String indent) {
       super( indent );
    }

    public HTMLWriter(String indent, boolean newlines) {
       super( indent, newlines );
    }

    public HTMLWriter(String indent,boolean newlines,String encoding) {
       super( indent, newlines, encoding );
    }
    
    public HTMLWriter(XMLWriter that) {
        super( that );
    }

 
    /** Overriden method to only output the text of CDATA sections
      * rather than the XML format
      */
    protected void printCDATASection(CDATA cdata, Writer out, int indentLevel) throws IOException {
        indent(out, indentLevel);
        out.write(cdata.getText());
        maybePrintln(out);
    }
    
    /** Overriden method to only output the content of the Entity
      * rather than the XML format
      */
    protected void printEntity(Entity entity, Writer out) throws IOException {
        out.write(entity.getText());
    }
    
    /** Overriden method to not close certain element names to avoid
      * wierd behaviour from browsers for versions up to 5.x
      */
    protected void printElementClose(Element element, Writer out) throws IOException {
        String name = element.getQualifiedName();
        if ( ! getOmitElementCloseSet().contains( name ) ) {
            super.printElementClose(element, out);
        }
    }
    
    protected void printEmptyElementClose(Element element, Writer out) throws IOException {
        String name = element.getQualifiedName();
        if ( ! getOmitElementCloseSet().contains( name ) ) {
            super.printElementClose(element, out);
        }
        else {
            out.write(">");
        }
    }
    
    protected Set getOmitElementCloseSet() {
        if (omitElementCloseSet == null) {
            omitElementCloseSet = new HashSet();
            loadOmitElementCloseSet(omitElementCloseSet);
        }
        return omitElementCloseSet;
    }
    
    protected void loadOmitElementCloseSet(Set set) {
        set.add( "p" );
        set.add( "P" );
        set.add( "br" );
        set.add( "BR" );
    }
}
