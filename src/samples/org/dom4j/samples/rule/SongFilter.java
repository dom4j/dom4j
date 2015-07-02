/*
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * This software is open source. 
 * See the bottom of this file for the licence.
 * 
 * $Id: SongFilter.java,v 1.4 2005/01/29 14:53:14 maartenc Exp $
 */

package org.dom4j.samples.rule;

import java.net.URL;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.rule.Action;
import org.dom4j.rule.Rule;
import org.dom4j.rule.Stylesheet;

/**
 * This class is 1:1 representation of the stlyesheet
 * <code>SongFilter.xsl</code> in package.
 * 
 * It demonstrates the usage of dom4j <i>Declarative Rule API </i>
 * 
 * For more information see the
 * 
 * @link{ http://www.dom4j.org/cookbook.html cookbook }.
 * 
 * @author <a href="mailto:toby-wan-kenobi@gmx.de">Tobias Rademacher </a>
 * @version $Revision $Date
 * 
 */
public class SongFilter {

    private Document resultDoc;

    private Element songElement;

    private Element currentSongElement;

    private Stylesheet style;

    /** Creates a new instance of SongFilter */
    public SongFilter() {
        this.songElement = DocumentHelper.createElement("song");
    }

    public Document filtering(org.dom4j.Document doc) throws Exception {
        Element resultRoot = DocumentHelper.createElement("result");
        this.resultDoc = DocumentHelper.createDocument(resultRoot);

        Rule songElementRule = new Rule();
        songElementRule.setPattern(DocumentHelper
                .createPattern("/Songs/song/mp3/id3"));
        songElementRule.setAction(new SongElementBuilder());

        Rule titleTextNodeFilter = new Rule();
        titleTextNodeFilter.setPattern(DocumentHelper
                .createPattern("/Songs/song/mp3/id3/title"));
        titleTextNodeFilter.setAction(new NodeTextFilter());

        this.style = new Stylesheet();
        this.style.addRule(songElementRule);
        this.style.addRule(titleTextNodeFilter);

        style.run(doc);

        return this.resultDoc;
    }

    private class SongElementBuilder implements Action {
        public void run(Node node) throws Exception {
            currentSongElement = songElement.createCopy();
            resultDoc.getRootElement().add(currentSongElement);

            style.applyTemplates(node);
        }
    }

    private class NodeTextFilter implements Action {
        public void run(Node node) throws Exception {
            if (currentSongElement != null) {
                currentSongElement.setText(node.getText());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SongFilter filter = new SongFilter();
        URL source = filter.getClass().getResource(
                "/org/dom4j/samples/rule/Songs.xml");
        Document result = filter.filtering(new SAXReader().read(source));

        XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());
        writer.setOutputStream(System.out);
        writer.write(result);

    }

}

/*
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "DOM4J" must not be used to endorse or promote products derived
 * from this Software without prior written permission of MetaStuff, Ltd. For
 * written permission, please contact dom4j-info@metastuff.com.
 * 
 * 4. Products derived from this Software may not be called "DOM4J" nor may
 * "DOM4J" appear in their names without prior written permission of MetaStuff,
 * Ltd. DOM4J is a registered trademark of MetaStuff, Ltd.
 * 
 * 5. Due credit should be given to the DOM4J Project - http://www.dom4j.org
 * 
 * THIS SOFTWARE IS PROVIDED BY METASTUFF, LTD. AND CONTRIBUTORS ``AS IS'' AND
 * ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL METASTUFF, LTD. OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * Copyright 2001-2004 (C) MetaStuff, Ltd. All Rights Reserved.
 * 
 * $Id: SongFilter.java,v 1.4 2005/01/29 14:53:14 maartenc Exp $
 */
