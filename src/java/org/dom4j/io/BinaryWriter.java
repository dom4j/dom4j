/*
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id$
 */

package org.dom4j.io;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.CDATA;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.Entity;
import org.dom4j.Namespace;
import org.dom4j.ProcessingInstruction;
import org.dom4j.Text;
import org.dom4j.TreeException;

/** <p><code>BinaryWriter</code> writes a DOM4J tree to a binary stream.
  * This is useful when communicating DOM4J structures with 
  * multiple processes when performance is important.</p>
  *
  * @author <a href="mailto:james.strachan@metastuff.com">James Strachan</a>
  * @version $Revision$
  */
public class BinaryWriter implements BinaryConstants {

    public BinaryWriter() {
    }

    /** <p>Writes a Document from the given stream using SAX</p>
      *
      * @param document is the documetn to write
      * @param in <code>OutputStream</code> to write from.
      * @throws TreeException if an error occurs during parsing.
      */
    public void write(Document document, OutputStream in) throws TreeException {
        DataOutputStream dataOut = null;
        try {
            dataOut = createDataOutputStream(in);

            writeDocument(document, dataOut);
        }
        catch (IOException e) {
            throw new TreeException(e);
        }
        finally {
            if ( dataOut != null ) {
                try {
                    dataOut.close();
                }
                catch (Exception e) {
                }
            }
        }
    }

    
    // Implementation methods
    
    protected void writeDocument(Document document, DataOutputStream out) throws IOException, TreeException {
        writeString(out, document.getName());
        
        writeBranchContents(document, out);
        
        writeOpCode(out, ELEMENT_END);
    }
    
    protected void writeBranchContents(Branch branch, DataOutputStream out) throws IOException, TreeException {
        List list = branch.getContent();
        if ( list != null ) {
            int size = list.size();
            for ( int i = 0; i < size; i++ ) {
                Object object = list.get(i);
                writeObject(object, out);
            }
        }
    }
    
    protected void writeObject(Object object, DataOutputStream out) throws IOException, TreeException {
        if ( object instanceof Element ) {
            writeElement( (Element) object, out );
        }
        else if ( object instanceof Text ) {
            Text text = (Text) object;
            writeText( text.getText(), out );
        }
        else if ( object instanceof String ) {
            writeText( (String) object, out );
        }
        else if ( object instanceof CDATA ) {
            writeCDATA( (CDATA) object, out );
        }
        else if ( object instanceof Comment ) {
            writeComment( (Comment) object, out );
        }
        else if ( object instanceof Entity ) {
            writeEntity( (Entity) object, out );
        }
        else if ( object instanceof Namespace ) {
            writeNamespace( (Namespace) object, out );
        }
        else if ( object instanceof ProcessingInstruction ) {
            writeProcessingInstruction( (ProcessingInstruction) object, out );
        }
    }
    
    protected void writeElement(Element element, DataOutputStream out) throws IOException, TreeException {
        writeOpCode(out, ELEMENT_START);
        
        writeString(out, element.getName());
        writeString(out, element.getNamespacePrefix());
        writeString(out, element.getNamespaceURI());

        List attributes = element.getAttributes();
        int attributeCount = attributes.size();        
        writeAttributeCount(out, attributeCount);
        for (int i = 0; i < attributeCount; i++ ) {
            Attribute attribute = (Attribute) attributes.get(i);
            writeAttribute(attribute, out);
        }
        writeBranchContents(element, out);
        
        writeOpCode(out, ELEMENT_END);
    }
    
    protected void writeAttribute(Attribute attribute, DataOutputStream out) throws IOException, TreeException {
        writeString(out, attribute.getName());
        writeString(out, attribute.getValue());
        writeString(out, attribute.getNamespacePrefix());
    }
    
    protected void writeCDATA(CDATA node, DataOutputStream out) throws IOException, TreeException {
        writeOpCode(out, CDATA_START);        
        writeString(out, node.getText());
    }
    
    protected void writeComment(Comment node, DataOutputStream out) throws IOException, TreeException {
        writeOpCode(out, COMMENT_START);        
        writeString(out, node.getText());
    }
    
    protected void writeText(String text, DataOutputStream out) throws IOException, TreeException {
        writeOpCode(out, TEXT_START);        
        writeString(out, text);
    }
    
    protected void writeEntity(Entity node, DataOutputStream out) throws IOException, TreeException {
        writeOpCode(out, ENTITY_START);        
        writeString(out, node.getName());
        writeString(out, node.getText());
    }
    
    protected void writeNamespace(Namespace node, DataOutputStream out) throws IOException, TreeException {
        writeOpCode(out, NAMESPACE_START);        
        writeString(out, node.getPrefix());
        writeString(out, node.getURI());
    }
    
    protected void writeProcessingInstruction(ProcessingInstruction node, DataOutputStream out) throws IOException, TreeException {
        writeOpCode(out, PI_START);        
        writeString(out, node.getTarget());
        writeString(out, node.getText());
    }
    
    protected void writeString(DataOutputStream out, String text) throws IOException {
        out.writeUTF(text);
    }
    
    protected void writeOpCode(DataOutputStream out, byte opCode) throws IOException {
        out.writeByte(opCode);
    }
    
    protected void writeAttributeCount(DataOutputStream out, int count) throws IOException {
        out.writeInt(count);
    }
    
    protected DataOutputStream createDataOutputStream(OutputStream in) throws IOException, TreeException {
        DataOutputStream dataIn = new DataOutputStream(in);
        writeHeader(dataIn);
        return dataIn;
    }

    protected void writeHeader(DataOutputStream out) throws IOException, TreeException {
        out.writeUTF(HEADER);
    }
    
}




/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided
 * that the following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright
 *    statements and notices.  Redistributions must also contain a
 *    copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the
 *    above copyright notice, this list of conditions and the
 *    following disclaimer in the documentation and/or other
 *    materials provided with the distribution.
 *
 * 3. The name "DOM4J" must not be used to endorse or promote
 *    products derived from this Software without prior written
 *    permission of MetaStuff, Ltd.  For written permission,
 *    please contact dom4j-info@metastuff.com.
 *
 * 4. Products derived from this Software may not be called "DOM4J"
 *    nor may "DOM4J" appear in their names without prior written
 *    permission of MetaStuff, Ltd. DOM4J is a registered
 *    trademark of MetaStuff, Ltd.
 *
 * 5. Due credit should be given to the DOM4J Project
 *    (http://dom4j.org/).
 *
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS
 * ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 * METASTUFF, LTD. OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Copyright 2001 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
