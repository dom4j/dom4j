package org.dom4j.persistence;

import org.dom4j.Document;
import org.dom4j.Node;

/*
 * @author  <a href="mailto:toby-wan-kenobi@gmx.de">Tobias Rademacher</a>
 * @version 1.0
 */

public class DocumentMemento implements Memento {

  protected String systemId;
  protected MarshallingStrategy marshaller;

  public DocumentMemento(String aSystemId, MarshallingContext context) throws Exception {
    this.systemId = aSystemId;
    this.marshaller = DocumentMarshalling.getInstance(context);
  }

  public Node getState() {
    return this.marshaller.unmarshal(this.systemId);
  }

  public void setState(Node node) throws Exception {
    if (this.systemId != null || !this.systemId.equals(""))
      this.marshaller.marshal(this.systemId, node);
  }

  public void setState(Document aState) {
    this.setState(aState);
  }

  public MarshallingStrategy getMarshaller() {
    return this.marshaller;
  }

  public String getSystemId() {
    return this.systemId;
  }


}
