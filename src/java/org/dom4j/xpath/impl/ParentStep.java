
package org.dom4j.xpath.impl;

import org.dom4j.Element;

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class ParentStep extends AbbrStep
{
  
  public ParentStep()
  {
  }

  public Context applyTo(Context context)
  {
    context.setNodeSet( ParentStep.findParents( context.getNodeSet() ) );

    return context;
  }

  static public Object findParent(Object node)
  {
    if ( node instanceof Element )
    {
      return ((Element)node).getParent();
    }

    return null;
  }

  static public List findParents(List nodeSet)
  {
    // FIXME: Unable to find parents of anything
    //        except Element at this point.
    Set results = new HashSet();

    Iterator elemIter = nodeSet.iterator();
    Object   each     = null;
    Element  parent   = null;

    while (elemIter.hasNext())
    {
      each = elemIter.next();

      if ( each instanceof Element )
      {
        parent = ((Element)each).getParent();

        if (parent != null)
        {
          results.add(parent);
        }
      }
    }

    return ( results.isEmpty() ? Collections.EMPTY_LIST : new ArrayList(results) );
  }
}
