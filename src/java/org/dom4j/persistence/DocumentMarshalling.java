package org.dom4j.persistence;

import org.dom4j.Node;

/**
 * @author  Tobias Rademacher
 * @version 1.0
 */

public abstract class DocumentMarshalling implements MarshallingStrategy {

  public static DocumentMarshalling getInstance(MarshallingContext context) throws Exception {
/*
 * Temporarily commented out for now... Will try put back when the build works on latest XMLDb
 *       
    if (context.getMarshallingStrategy().equals("nativ")) {
       return new org.dom4j.persistence.nativ.XMLDBStrategy(context);
    }
*/  
    return null;
  }

  public abstract void marshal(String systemId, Node aNode) throws Exception;

  public abstract Node unmarshal(String systemId);



}
