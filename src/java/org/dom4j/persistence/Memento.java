package org.dom4j.persistence;

import org.dom4j.Node;

/**
 *
 * @author
 * @version 1.0
 */

public interface Memento {

  public Node getState() throws Exception ;
  public void setState(Node node) throws Exception;
  public MarshallingStrategy getMarshaller();
  String getSystemId();

}
