package org.dom4j.tool.dtd;

import java.io.*;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/** <p><code>DTDHandler</code> reads DTD events from a SAX2 parser
  * and generates a document declaration model.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class DTDHandler implements DeclHandler {

    private DocumentDecl documentDecl = new DocumentDecl();
    private ElementDecl elementDecl;
    
    public DocumentDecl getDocumentDecl() {
        return documentDecl;
    }
    
    public void setDocumentDecl(DocumentDecl documentDecl) {
        this.documentDecl = documentDecl;
    }
    
    public void attributeDecl(String eName, String aName, String type, String valueDefault, String value) throws SAXException {
        //System.out.println( "Attribute: " + aName + " for element: " + eName + " type: " + type + " value: " + value + " default: " + valueDefault );
        
        elementDecl.add( new AttributeDecl( eName, aName, type, valueDefault, value ) );
    }
    
    public void elementDecl(String name, String model) throws SAXException {
        //System.out.println( "Element: " + name + " has model: " + model );
        
        elementDecl = new ElementDecl( name, model );
        documentDecl.add( elementDecl );
    }
    
    public void externalEntityDecl(String name, String publicId, String systemId) throws SAXException {
    }
    
    public void internalEntityDecl(String name, String value) throws SAXException {
    }
        
}
