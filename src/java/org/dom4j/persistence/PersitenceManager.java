package org.dom4j.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;

/**
 *
 * @author
 * @version 1.0
 */

public class PersitenceManager implements Initiator {

  private static PersitenceManager orginator;
  private List mementos;

  protected PersitenceManager() {
    mementos = new ArrayList();
  }

  public static PersitenceManager getInstance() {
    if (PersitenceManager.orginator == null)
      PersitenceManager.orginator = new PersitenceManager();

    return PersitenceManager.orginator;
  }

  public Memento createMemento(Document doc, String systemId, MarshallingContext context) throws Exception {
    DocumentMemento menento = new DocumentMemento(systemId, context);
    menento.setState(doc);
    this.mementos.add(menento);
    return menento;
  }

  public List getMemeneto() {
     return mementos;
  }

  public Iterator getMemenetoIterator() {
    return mementos.iterator();
  }


  public Memento getMemento(String systemId) throws Exception
  {
    boolean searching = true;
    Memento targetedMemento = null;
    Iterator mememtoIter = this.getMemenetoIterator();
    while(searching)
    {
      targetedMemento = (Memento) mememtoIter.next();
      if (((Document)targetedMemento.getState()).getDocType().getSystemID().equals(systemId)) {
        searching = false;
    }
  }

    return targetedMemento;
  }



}
