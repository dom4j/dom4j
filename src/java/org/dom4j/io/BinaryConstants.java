package org.dom4j.io;

/** <p><code>BinaryConstants</code> defines the constants used by the 
  * binary DOM4J stream implementation.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
interface BinaryConstants {

    public static final String HEADER = "DOM4J-Binary";

    // contants
    
    public static final byte UNKNOWN = 0;
    public static final byte ELEMENT_START = 1;
    public static final byte ELEMENT_END = 2;
    public static final byte CDATA_START = 3;
    public static final byte COMMENT_START = 4;
    public static final byte ENTITY_START = 5;
    public static final byte NAMESPACE_START = 6;
    public static final byte PI_START = 7;
    public static final byte TEXT_START = 8;
}
