package com.nova.reflect;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Fields
{
    private final static HashMap<String,Field[]> SORTED_FIELDS=new HashMap<>();
    private final static HashMap<String,Map<String,Field>> FIELDS_MAPS=new HashMap<>();
    
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
    
    public final static Map<String,Field> getFieldsMap(Class<?> class_)
    {
        synchronized (FIELDS_MAPS)
        {
            String name=class_.getCanonicalName();
            Map<String,Field> fields=FIELDS_MAPS.get(name);
            if (fields==null)
            {
                fields=new HashMap<>();
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
