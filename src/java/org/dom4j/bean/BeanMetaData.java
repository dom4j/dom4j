/*
 * Copyright 2001-2005 (C) MetaStuff, Ltd. All Rights Reserved.
 *
 * This software is open source.
 * See the bottom of this file for the licence.
 */

package org.dom4j.bean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentFactory;
import org.dom4j.QName;

/**
 * <p>
 * <code>BeanMetaData</code> contains metadata about a bean class.
 * </p>
 * 
 * @author <a href="mailto:james.strachan@metastuff.com">James Strachan </a>
 * @version $Revision$
 */
public class BeanMetaData {
    /** Empty arguments for reflection calls */
    protected static final Object[] NULL_ARGS = {};

    /** Singleton cache */
    private static Map singletonCache = new HashMap();

    private static final DocumentFactory DOCUMENT_FACTORY = BeanDocumentFactory
            .getInstance();

    /** The class of the bean */
    private Class beanClass;

    /** Property descriptors for the bean */
    private PropertyDescriptor[] propertyDescriptors;

    /** QNames for the attributes */
    private QName[] qNames;

    /** Read methods used for getting properties */
    private Method[] readMethods;

    /** Write methods used for setting properties */
    private Method[] writeMethods;

    /** Index of names and QNames to indices */
    private Map nameMap = new HashMap();

    public BeanMetaData(Class beanClass) {
        this.beanClass = beanClass;

        if (beanClass != null) {
            try {
                BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
                propertyDescriptors = beanInfo.getPropertyDescriptors();
            } catch (IntrospectionException e) {
                handleException(e);
            }
        }

        if (propertyDescriptors == null) {
            propertyDescriptors = new PropertyDescriptor[0];
        }

        int size = propertyDescriptors.length;
        qNames = new QName[size];
        readMethods = new Method[size];
        writeMethods = new Method[size];

        for (int i = 0; i < size; i++) {
            PropertyDescriptor propertyDescriptor = propertyDescriptors[i];
            String name = propertyDescriptor.getName();
            QName qName = DOCUMENT_FACTORY.createQName(name);
            qNames[i] = qName;
            readMethods[i] = propertyDescriptor.getReadMethod();
            writeMethods[i] = propertyDescriptor.getWriteMethod();

            Integer index = new Integer(i);
            nameMap.put(name, index);
            nameMap.put(qName, index);
        }
    }

    /**
     * Static helper method to find and cache meta data objects for bean types
     * 
     * @param beanClass
     *            DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static BeanMetaData get(Class beanClass) {
        BeanMetaData answer = (BeanMetaData) singletonCache.get(beanClass);

        if (answer == null) {
            answer = new BeanMetaData(beanClass);
            singletonCache.put(beanClass, answer);
        }

        return answer;
    }

    /**
     * DOCUMENT ME!
     * 
     * @return the number of attribtutes for this bean type
     */
    public int attributeCount() {
        return propertyDescriptors.length;
    }

    public BeanAttributeList createAttributeList(BeanElement parent) {
        return new BeanAttributeList(parent, this);
    }

    public QName getQName(int index) {
        return qNames[index];
    }

    public int getIndex(String name) {
        Integer index = (Integer) nameMap.get(name);

        return (index != null) ? index.intValue() : (-1);
    }

    public int getIndex(QName qName) {
        Integer index = (Integer) nameMap.get(qName);

        return (index != null) ? index.intValue() : (-1);
    }

    public Object getData(int index, Object bean) {
        try {
            Method method = readMethods[index];

            return method.invoke(bean, NULL_ARGS);
        } catch (Exception e) {
            handleException(e);

            return null;
        }
    }

    public void setData(int index, Object bean, Object data) {
        try {
            Method method = writeMethods[index];
            Object[] args = {data};
            method.invoke(bean, args);
        } catch (Exception e) {
            handleException(e);
        }
    }

    // Implementation methods
    // -------------------------------------------------------------------------
    protected void handleException(Exception e) {
        // ignore introspection exceptions
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
