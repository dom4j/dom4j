
package org.dom4j.xpath.util;

import org.dom4j.Element;

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;


public class Partition
{

  public static List descendants(Element node)
  {
    List results = new ArrayList();

    List children = node.getElements();

    results.addAll(children);
    
    Iterator childIter = children.iterator();
    
    while (childIter.hasNext())
    {
      results.addAll( Partition.descendants( (Element) childIter.next() ) );
    }

    if (results.isEmpty())
    {
      return Collections.EMPTY_LIST;
    }

    return results;
  }

  public static List documentOrderDescendants(Element node)
  {
    List results = new ArrayList();

    List children = node.getElements();

    if (children.isEmpty())
    {
      return Collections.EMPTY_LIST;
    }

    Iterator childIter = children.iterator();

    Element each = null;

    while (childIter.hasNext())
    {
      each = (Element) childIter.next();
      results.add(each);
      results.addAll( documentOrderDescendants( each ) );
    }

    if (results.isEmpty())
    {
      return Collections.EMPTY_LIST;
    }

    return results;
  }

  public static List followingSiblings(Element node)
  {
    Element parent = node.getParent();

    if (parent == null)
    {
      return Collections.EMPTY_LIST;
    }

    List siblings = parent.getElements();
    int selfIndex = siblings.indexOf(node);

    if (selfIndex < 0)
    {
      return Collections.EMPTY_LIST;
    }

    int total = siblings.size();

    if (selfIndex == (total - 1))
    {
      return Collections.EMPTY_LIST;
    }

    return new ArrayList( siblings.subList( (selfIndex + 1),
                                            siblings.size() ) );
  }

  public static List preceedingSiblings(Element node)
  {
    Element parent = node.getParent();

    if (parent == null)
    {
      return Collections.EMPTY_LIST;
    }

    List siblings = parent.getElements();

    int selfIndex = siblings.indexOf(node);

    if ( (selfIndex < 1) || (siblings.size() == 1) )
    {
      return Collections.EMPTY_LIST;
    }

    List results = new ArrayList();

    results = siblings.subList(0,
                               selfIndex);

    return results;
  }

  public static List following(Element node)
  {
    List results = new ArrayList();

    List followingSiblings = Partition.followingSiblings(node);

    results.addAll(followingSiblings);

    Iterator sibIter = followingSiblings.iterator();
    Element each = null;

    while (sibIter.hasNext())
    {
      each = (Element) sibIter.next();

      results.addAll( Partition.descendants( each ) );
    }

    Element parent = node.getParent();

    if (parent != null)
    {
      results.addAll( Partition.following( parent ) );
    }

    if (results.isEmpty())
    {
      return Collections.EMPTY_LIST;
    }

    return results;
  }

  public static List preceeding(Element node)
  {
    List results = new ArrayList();

    List preceedingSiblings = Partition.preceedingSiblings(node);

    results.addAll(preceedingSiblings);

    Iterator sibIter = preceedingSiblings.iterator();
    Element each = null;

    while (sibIter.hasNext())
    {
      each = (Element) sibIter.next();

      results.addAll( Partition.descendants( each ) );
    }

    Element parent = node.getParent();

    if (parent != null)
    {
      results.addAll( Partition.preceeding( parent ) );
    }

    if (results.isEmpty())
    {
      return Collections.EMPTY_LIST;
    }
    
    return results;
  }
}
