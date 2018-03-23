package org.nova.sqldb;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.HashMap;

public class FieldMaps
{
    private static HashMap<String, ConstructorFieldMap> maps = new HashMap<>();

    static class ConstructorFieldMap
    {
        final public Object[] parameters;
        final public Constructor<?> constructor;
        final public HashMap<String, Field> map;

        
        public ConstructorFieldMap(Class<?> type, HashMap<String, Field> map) throws Throwable
        {
            Constructor<?>[] constructors = type.getDeclaredConstructors();
            this.map = map;
            int shortest = Integer.MAX_VALUE;
            Constructor<?> shortestConstructor = null;
            for (Constructor<?> constructor : constructors)
            {
                if (constructor.getParameterCount() < shortest)
                {
                    shortest = constructor.getParameterCount();
                    shortestConstructor = constructor;
                }
            }
            this.constructor = shortestConstructor;
            this.parameters = new Object[shortestConstructor.getParameterCount()];
            for (int i = 0; i < shortestConstructor.getParameterCount(); i++)
            {
                Class<?> parameterType = shortestConstructor.getParameterTypes()[i];
                if (parameterType.isPrimitive())
                {
                    if (parameterType == boolean.class)
                    {
                        parameters[i] = false;
                    }
                    else if (parameterType == int.class)
                    {
                        parameters[i] = 0;
                    }
                    else if (parameterType == long.class)
                    {
                        parameters[i] = 0L;
                    }
                    else if (parameterType == float.class)
                    {
                        parameters[i] = 0.0f;
                    }
                    else if (parameterType == double.class)
                    {
                        parameters[i] = 0.0;
                    }
                    else if (parameterType == byte.class)
                    {
                        parameters[i] = (byte) 0;
                    }
                    else if (parameterType == char.class)
                    {
                        parameters[i] = (char) 0;
                    }
                    else if (parameterType == short.class)
                    {
                        parameters[i] = (short) 0;
                    }
                }
                else if (parameterType.isEnum())
                {
                    parameters[i] = parameterType.newInstance();
                }
            }
            shortestConstructor.setAccessible(true);
        }
        public Object newInstance() throws Throwable
        {
            return this.constructor.newInstance(this.parameters);
        }
    }

    public static ConstructorFieldMap get(Class<?> type) throws Throwable
    {
        String key = type.getCanonicalName();
        synchronized (maps)
        {
            ConstructorFieldMap map = maps.get(key);
            if (map != null)
            {
                return map;
            }
        }
        HashMap<String, Field> map = new HashMap<>();
        for (Field field : type.getDeclaredFields())
        {
            ColumnNameAlias alias = (ColumnNameAlias) field.getAnnotation(ColumnNameAlias.class);
            String name = alias != null ? alias.value() : field.getName();
            field.setAccessible(true);
            map.put(name, field);
        }
        ConstructorFieldMap constructorFieldMap=new ConstructorFieldMap(type, map);
        synchronized (maps)
        {
            maps.put(key, constructorFieldMap);
        }
        return constructorFieldMap;
    }
}
