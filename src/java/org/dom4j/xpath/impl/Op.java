
package org.dom4j.xpath.impl;

public class Op {
  
  private String _name = "";
  
  private Op (String name) { _name = name; }
  public String toString() { return _name; }
  
  
  public final static Op OR        = new Op("or");
  public final static Op AND       = new Op("and");
  public final static Op EQUAL     = new Op("==");
  public final static Op NOT_EQUAL = new Op("!=");
  public final static Op LT        = new Op("<");
  public final static Op GT        = new Op(">");
  public final static Op LT_EQUAL  = new Op("<=");
  public final static Op GT_EQUAL  = new Op(">=");
  public final static Op MOD       = new Op("%");;
  public final static Op DIV       = new Op("/");
  public final static Op PLUS      = new Op("+");
  public final static Op MINUS     = new Op("-");
  public final static Op MULTIPLY  = new Op("*");
  
}
