package org.dom4j.persistence;

import org.dom4j.Node;

/**
 * @author
 * @version 1.0
 */

public interface MarshallingStrategy {

  public void marshal(String systemId, Node aNode) throws Exception;
  public Node unmarshal(String aSystemId);
  public void setContext(MarshallingContext aContext);
}
