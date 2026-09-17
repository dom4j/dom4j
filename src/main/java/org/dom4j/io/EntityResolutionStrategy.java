/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.io;

import org.dom4j.DocumentException;
import org.xml.sax.EntityResolver;

import java.util.Locale;

/**
 * Controls how a {@link SAXReader} handles references to external resources.
 *
 * <p>
 * It applies only while no {@link EntityResolver} has been set with
 * {@link SAXReader#setEntityResolver(EntityResolver)}.
 *
 * <p>
 * The three values are named after those of the JDK {@code jdk.xml.dtd.support} property:
 * <ul>
 * <li>{@link #IGNORE}: ignores all external resources, resolving them to empty content instead. This is the <strong>default</strong> of non-validating readers since version 2.3.0.</li>
 * <li>{@link #ALLOW}: fetches all external resources. This is the closest to the behavior of dom4j before versions 2.0.3 and 2.1.3.</li>
 * <li>{@link #DENY}: throws whenever an external resource (DTD subset, entity) is referenced. This is the <strong>default</strong> of validating readers since version 2.3.0.</li>
 * </ul>
 *
 * <p>The default can be changed for all readers
 * with the {@value #SYSTEM_PROPERTY} system property (case-insensitive value),
 * or per reader with
 * {@link SAXReader#setEntityResolutionStrategy(EntityResolutionStrategy)}.
 * <p>
 * Whatever the strategy, the SAX parsers created by dom4j never fetch an
 * external resource on their own: every fetch goes through the
 * {@link EntityResolver} installed on the reader, either the one
 * set by the application or the one implied by the strategy.
 *
 * @since 2.3.0
 */
public enum EntityResolutionStrategy {
    /**
     * External resources resolve to empty content.
     *
     * <p>The document is parsed as if the external DTD subset and every external entity were empty.</p>
     *
     * <p>This is the <strong>default</strong> of non-validating readers since
     * 2.3.0.</p>
     */
    IGNORE,

    /**
     * External resources are fetched from their system identifier.
     *
     * <p>This is the closest to the behavior of dom4j before versions 2.0.3
     * and 2.1.3, and is only appropriate for documents from a trusted
     * source.</p>
     */
    ALLOW,

    /**
     * Any reference to an external resource fails the parse with a {@link DocumentException}.
     */
    DENY;

    /**
     * Name of the system property that selects the default strategy.
     */
    public static final String SYSTEM_PROPERTY = "org.dom4j.io.entityResolutionStrategy";

    /**
     * Returns the default strategy of a reader.
     *
     * <p>The value of the {@value #SYSTEM_PROPERTY} system property, if it is
     * set to a valid value and can be read. Otherwise {@link #DENY} for a
     * validating reader and {@link #IGNORE} for a non-validating one.</p>
     *
     * @param validating whether the reader validates documents against their
     *                   DTD
     * @return the default strategy
     */
    public static EntityResolutionStrategy getDefault(boolean validating) {
        try {
            String value = System.getProperty(SYSTEM_PROPERTY);
            if (value != null) {
                return valueOf(value.trim().toUpperCase(Locale.ROOT));
            }
        } catch (IllegalArgumentException | SecurityException e) {
            // fall through to the safe default
        }
        return validating ? DENY : IGNORE;
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
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 */
