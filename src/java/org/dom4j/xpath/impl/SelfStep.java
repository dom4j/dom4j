
package org.dom4j.xpath.impl;

import org.dom4j.xpath.ContextSupport;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SelfStep extends AbbrStep
{
  
  public SelfStep()
  {
  }

  public List applyToSelf(Object node,
                          ContextSupport support)
  {
    System.err.println("SelfStep.applyToSelf(" + node + ")");
    List results = new ArrayList(1);
    results.add(node);

    return results;
  }
}
