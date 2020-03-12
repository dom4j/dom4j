package org.dom4j;

import org.testng.annotations.Test;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * @author Filip Jirsák
 */
public class CommentTest extends AbstractTestCase {
  public void emptyCommentTest() throws Exception {
    Document document = getDocument("/xml/comment-empty.xml");
    final Element root = document.getRootElement();
    assertEquals(9, root.nodeCount());
    assertEquals(Node.TEXT_NODE, root.node(0).getNodeType());
    assertEquals(Node.COMMENT_NODE, root.node(1).getNodeType());
    assertEquals(Node.TEXT_NODE, root.node(2).getNodeType());
    assertEquals(Node.COMMENT_NODE, root.node(3).getNodeType());
    assertEquals(Node.TEXT_NODE, root.node(4).getNodeType());
    assertEquals(Node.COMMENT_NODE, root.node(5).getNodeType());
    assertEquals(Node.TEXT_NODE, root.node(6).getNodeType());
    assertEquals(Node.COMMENT_NODE, root.node(7).getNodeType());
    assertEquals(Node.TEXT_NODE, root.node(8).getNodeType());
    assertEquals("", root.node(1).getText());
    assertEquals(" ", root.node(3).getText());
    assertEquals("- ", root.node(5).getText());
    assertEquals(" - ", root.node(7).getText());
  }
}
