
package org.dom4j.xpath.impl;

import org.dom4j.xpath.function.BooleanFunction;

class OpBooleanAny extends Operator
{

  static Object evaluate(Context context,
                         Op op,
                         Object lhsValue,
                         Object rhsValue)
  {
    Boolean lhs = BooleanFunction.evaluate(lhsValue);
    Boolean rhs = BooleanFunction.evaluate(rhsValue);

    if ( op == Op.EQUAL )
    {
      return ( ( lhs.equals(rhs) )
               ? Boolean.TRUE
               : Boolean.FALSE
               );

    }
    else if ( op == Op.NOT_EQUAL )
    {
      return ( ( ! lhs.equals(rhs) )
               ? Boolean.TRUE
               : Boolean.FALSE
               );
    }

    return null;
  }
}
