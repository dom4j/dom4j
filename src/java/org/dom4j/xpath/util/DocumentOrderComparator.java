
package org.dom4j.xpath.util;

import org.dom4j.Document;
import org.dom4j.Element;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

import java.io.Serializable;

public class DocumentOrderComparator implements Serializable
{

  private Map _orderings = null;

  public DocumentOrderComparator(Document document)
  {
    Element rootElement = document.getRootElement();

    if (rootElement != null)
    {

      List orderedElements = Partition.documentOrderDescendants( rootElement );

      _orderings = new HashMap(orderedElements.size() + 1);

      _orderings.put(rootElement, new Integer(0));

      Iterator elemIter = orderedElements.iterator();
      int counter = 1;

      while (elemIter.hasNext())
      {
        _orderings.put( elemIter.next(),
                        new Integer(counter) );
        ++counter;
      }
    }
    else
    {
      _orderings = Collections.EMPTY_MAP;
    }

  }

  public int compare(Object lhsIn,
                     Object rhsIn)
    throws ClassCastException
  {
    Element lhs = (Element) lhsIn;
    Element rhs = (Element) rhsIn;

    if (lhs.equals(rhs))
    {
      return 0;
    }

    int lhsIndex = ((Integer)_orderings.get(lhs)).intValue();
    int rhsIndex = ((Integer)_orderings.get(rhs)).intValue();

    if (lhsIndex < rhsIndex)
    {
      return -1;
    }
    else if (lhsIndex > rhsIndex)
    {
      return 1;
    } 

    return 0;
  }

}
