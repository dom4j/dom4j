/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 *
 * $Id$
 */

package org.dom4j.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;

/**
 * DOCUMENT ME!
 *
 * @version 1.0
 */
public class PersitenceManager implements Initiator {
    private static PersitenceManager orginator;
    private List mementos;

    protected PersitenceManager() {
        mementos = new ArrayList();
    }

    public static PersitenceManager getInstance() {
        if (PersitenceManager.orginator == null) {
            PersitenceManager.orginator = new PersitenceManager();
        }

        return PersitenceManager.orginator;
    }

    public Memento createMemento(Document doc, String systemId,
                                 MarshallingContext context)
                          throws Exception {
        DocumentMemento menento = new DocumentMemento(systemId, context);
        menento.setState(doc);
        this.mementos.add(menento);

        return menento;
    }

    public List getMemeneto() {
        return mementos;
    }

    public Iterator getMemenetoIterator() {
        return mementos.iterator();
    }

    public Memento getMemento(String systemId) throws Exception {
        boolean searching = true;
        Memento targetedMemento = null;
        Iterator mememtoIter = this.getMemenetoIterator();

        while (searching) {
            targetedMemento = (Memento) mememtoIter.next();

            if (((Document) targetedMemento.getState()).getDocType()
                     .getSystemID().equals(systemId)) {
                searching = false;
            }
        }

        return targetedMemento;
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
 * 5. Due credit should be given to the DOM4J Project -
 *    http://www.dom4j.org
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
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * $Id$
 */
