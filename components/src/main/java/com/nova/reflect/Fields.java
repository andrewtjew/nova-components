/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.nova.reflect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.TreeMap;

public class Fields
{
    private final static HashMap<String,Field[]> SORTED_FIELDS=new HashMap<>();
    private final static HashMap<String,FieldMap> FIELDS_MAPS=new HashMap<>();
    
    public final static Field[] getOrderedFields(Class<?> class_)
    {
        synchronized (SORTED_FIELDS)
        {
            String name=class_.getCanonicalName();
            Field[] fields=SORTED_FIELDS.get(name);
            if (fields==null)
            {
                TreeMap<Long,Field> sorted=new TreeMap<>();
                long noOrderIndex=1+(long)Integer.MAX_VALUE;
                for (Class<?> current=class_;current!=null;current=current.getSuperclass())
                {
                    for (Field field:current.getDeclaredFields())
                    {
                        field.setAccessible(true);
                        FieldOrder fieldOrder=(FieldOrder)field.getAnnotation(FieldOrder.class);
                        long order=fieldOrder!=null?fieldOrder.value():noOrderIndex++;
                        sorted.put(order, field);
                    }
                }
                fields=sorted.values().toArray(new Field[sorted.size()]);
                SORTED_FIELDS.put(name, fields);
            }
            return fields;
        }
    }
    
    public final static FieldMap getFieldsMap(Class<?> class_)
    {
        synchronized (FIELDS_MAPS)
        {
            String name=class_.getCanonicalName();
            FieldMap fields=FIELDS_MAPS.get(name);
            if (fields==null)
            {
                fields=new FieldMap();
                for (Class<?> current=class_;current!=null;current=current.getSuperclass())
                {
                    for (Field field:current.getDeclaredFields())
                    {
                        field.setAccessible(true);
                        fields.put(field.getName(),field);
                    }
                }
                FIELDS_MAPS.put(name, fields);
            }
            return fields;
        }
    }
}
