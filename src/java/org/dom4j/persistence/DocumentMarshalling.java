package org.dom4j.persistence;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXWriter;
import org.dom4j.io.SAXContentHandler;

import org.dom4j.persistence.MarshallingContext;

/**
 * @author  Tobias Rademacher
 * @version 1.0
 */

public abstract class DocumentMarshalling implements MarshallingStrategy {

  public static DocumentMarshalling getInstance(MarshallingContext context) throws Exception {
    if (context.getMarshallingStrategy().equals("nativ")) {
       return new org.dom4j.persistence.nativ.XMLDBStrategy(context);
    }
    return null;
  }

  public abstract void marshal(String systemId, Node aNode) throws Exception;

  public abstract Node unmarshal(String systemId);



}
