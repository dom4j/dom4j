
package org.dom4j.xpath;

import org.dom4j.xpath.impl.Context;

import org.dom4j.Node;
import org.dom4j.NodeFilter;
import org.dom4j.XPathEngine;
import org.dom4j.rule.Pattern;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/** Default implementation of the DOM4J XPathEngine
  */
public class DefaultXPathEngine implements XPathEngine
{
    public org.dom4j.XPath createXPath(String xpathExpr)
    {
        return new XPath( xpathExpr );
    }

    public NodeFilter createXPathFilter(String xpathFilterExpr)
    {
        return new XPath( ".[" + xpathFilterExpr + "]" );        
    }

    public Pattern createPattern(String xpathPattern) 
    {
        return new XPathPattern( xpathPattern );
    }
}
