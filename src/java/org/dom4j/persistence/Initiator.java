package org.dom4j.persistence;

import org.dom4j.Document;

/**
 * Überschrift:   HL7 API
 * Beschreibung:
 * Copyright:     Copyright (c) 2001
 * Organisation:  ceyoniq healthcare
 * @author
 * @version 1.0
 */

public interface Initiator {

  public Memento createMemento(Document doc, String systemId, MarshallingContext context) throws Exception ;

}