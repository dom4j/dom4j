package org.dom4j;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.dom4j.io.aelfred2.SAXDriver;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderAdapter;

/*
 * JAXPFactory.java
 *
 * Created on 16 april 2004, 15:14
 */

/**
 *
 * @author  Maarten Coene
 */
public class JAXPFactory extends SAXParserFactory {
    
    private Map features = new HashMap();

    /**
     * Constructs a factory which normally returns a non-validating
     * parser.
     */
    public JAXPFactory () { 
    }

    public SAXParser newSAXParser () throws ParserConfigurationException, SAXException {
        SAXParser result = new InternalParser();
	XMLReader parser = result.getXMLReader ();

	parser.setFeature(
                "http://xml.org/sax/features/namespaces",
                isNamespaceAware());
	parser.setFeature(
                "http://xml.org/sax/features/validation",
                isValidating());

        Iterator it = features.entrySet().iterator();
	while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String name = (String) entry.getKey();
            Boolean value = (Boolean) entry.getValue();
	    parser.setFeature (name, value.booleanValue ());
	}

	return result;
    }

    public void setFeature (String name, boolean value) {
	features.put(name, new Boolean(value));
    }

    public boolean getFeature(String name) {
	Boolean	value = (Boolean) features.get (name);
	
	if (value != null)
	    return value.booleanValue ();
	else {
            return false;
        }
    }

    private static class InternalParser extends SAXParser {
        
	private SAXDriver aelfred;
	private XMLReaderAdapter parser;

	InternalParser() { 
            super();
            
            aelfred = new SAXDriver();
            parser = new XMLReaderAdapter(aelfred);
        }

	public void setProperty(String id, Object value) throws SAXNotRecognizedException, 
                                                                SAXNotSupportedException {
            aelfred.setProperty(id, value); 
        }

	public Object getProperty(String id) throws SAXNotRecognizedException, 
                                                    SAXNotSupportedException {
            return aelfred.getProperty(id); 
        }

	public Parser getParser() { 
	    return parser;
	}

	public XMLReader getXMLReader() {
            return aelfred; 
        }

	public boolean isNamespaceAware() {
            try {
                return aelfred.getFeature("http://xml.org/sax/features/namespaces");
            } catch (Exception e) {
                return false;
            }
	}

	public boolean isValidating()	{
	    try {
		return aelfred.getFeature("http://xml.org/sax/features/validation");
	    } catch (Exception e) {
		return false;
	    }
	}
    }
}
