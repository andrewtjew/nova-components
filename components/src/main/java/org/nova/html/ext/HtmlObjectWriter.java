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
package org.nova.html.ext;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.nova.html.elements.QuotationMark;
import org.nova.utils.FileUtils;

public class HtmlObjectWriter
{
    final static private HashMap<String, FieldWriter[]> FIELD_WRITERS=new HashMap<>();
    final static private HashMap<String, Writer> WRITERS=new HashMap<>();

    static abstract class Writer
    {
        abstract void write(WriteState writeState,Object object) throws Throwable;
    }
    static class PrimitiveWriter extends  Writer
    {
        void write(WriteState writeState,Object object)
        {
            writeState.write(object.toString());
        }
    }
    static class StringWriter extends  Writer
    {
        void write(WriteState writeState,Object object)
        {
            writeState.writeEscapedString((String)object);
        }
    }
    static class ScriptWriter extends  Writer
    {
        void write(WriteState writeState,Object object)
        {
            writeState.write(((Script)object).script);
        }
    }
    static class EnumWriter extends  Writer
    {
        void write(WriteState writeState,Object object)
        {
            if (object==null)
            {
                writeState.writeNull();
            }
            else
            {
                writeState.writeQuoted(object.toString());
            }
        }
    }
    
    static class ObjectWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            Class<?> type=object.getClass();
            Writer writer=null;
            if (type.isPrimitive())
            {
                writer=new PrimitiveWriter();
            }
            else if (type == String.class)
            {
                writer=new StringWriter();
            }
            else if (type.isArray())
            {
                writer=getArrayWriter(type.getComponentType());
            }
            else if (type.isEnum())
            {
                writer=new EnumWriter();
            }
            else if (type == Boolean.class)
            {
                writer=new PrimitiveWriter();
            }
            else if (type == Integer.class)
            {
                writer=new PrimitiveWriter();
            }
            else if (type == Long.class)
            {
                writer=new PrimitiveWriter();
            }
            else if (type == Float.class)
            {
                writer=new PrimitiveWriter();
            }
            else if (type == Double.class)
            {
                writer=new PrimitiveWriter();
            }
            else if (type == Byte.class)
            {
                writer=new PrimitiveWriter();
            }
            else if (type == Character.class)
            {
                writer=new PrimitiveWriter();
            }
            else if (type == Short.class)
            {
                writer=new PrimitiveWriter();
            }
            else if (type == java.lang.Enum.class)
            {
                writer=new EnumWriter();
            }
            else if (type == BigDecimal.class)
            {
                writer=new PrimitiveWriter();
            }
            if (writer!=null)
            {
                writer.write(writeState, object);
            }
            else
            {
                writeState.begin('{');
                boolean needComma = false;
                FieldWriter[] fieldWriters = getFieldWriters(object.getClass());
                for (FieldWriter fieldWriter:fieldWriters)
                {
                    Object fieldObject = fieldWriter.field.get(object);
                    if (fieldObject!=null)
                    {
                        fieldWriter.write(writeState, needComma, fieldObject);
                        needComma=true;
                    }
                }
                writeState.end('}');
            }
        }
    }
    static class booleanArrayWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            boolean[] array = (boolean[]) object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                writeState.write(Boolean.toString(array[i]));
            }
            writeState.end(']');
        }
    }
    static class byteArrayWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            byte[] array = (byte[]) object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                writeState.write(Byte.toString(array[i]));
            }
            writeState.end(']');
        }
    }
    static class shortArrayWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            short[] array = (short[]) object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                writeState.write(Short.toString(array[i]));
            }
            writeState.end(']');
        }
    }
    static class intArrayWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            int[] array = (int[]) object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                writeState.write(Integer.toString(array[i]));
            }
            writeState.end(']');
        }
    }
    static class longArrayWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            long[] array = (long[]) object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                writeState.write(Long.toString(array[i]));
            }
            writeState.end(']');
        }
    }
    static class floatArrayWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            float[] array = (float[]) object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                writeState.write(Float.toString(array[i]));
            }
            writeState.end(']');
        }
    }
    static class doubleArrayWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            double[] array = (double[]) object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                writeState.write(Double.toString(array[i]));
            }
            writeState.end(']');
        }
    }

    static class StringArrayWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            String[] array = (String[]) object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                if (array[i]==null)
                {
                    writeState.writeNull();
                }
                else
                {
                    writeState.writeEscapedString(array[i]);
                }
            }
            writeState.end(']');
        }
    }
    static class EnumArrayWriter extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            Enum<?>[] array = (Enum<?>[]) object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                if (array[i]==null)
                {
                    writeState.writeNull();
                }
                else
                {
                    writeState.writeQuoted(array[i].toString());
                }
            }
            writeState.end(']');
        }
    }
    static class NullablePrimitiveArrayWriter<TYPE> extends  Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            TYPE[] array = (TYPE[])object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                if (array[i]==null)
                {
                    writeState.writeNull();
                }
                else
                {
                    writeState.write(array[i].toString());
                }
            }
            writeState.end(']');
        }
    }
    static class ArrayWriter extends Writer
    {
        final Writer writer;
        ArrayWriter(Class<?> componentType)
        {
            this.writer=getWriter(componentType);
        }
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            Object[] array = (Object[])object;
            for (int i = 0; i < array.length; i++)
            {
                writeState.writeSeperator(i>0);
                if (array[i]==null)
                {
                    writeState.writeNull();
                }
                else
                {
                    this.writer.write(writeState, array[i]);
                }
            }
            writeState.end(']');
        }
    }
    static class ObjectArrayWriter extends Writer
    {
        void write(WriteState writeState,Object object) throws Throwable
        {
            writeState.begin('[');
            Object[] array = (Object[])object;
            for (int i = 0; i < array.length; i++)
            {
                Object element=array[i];
                writeState.writeSeperator(i>0);
                if (element==null)
                {
                    writeState.writeNull();
                }
                else
                {
                    getWriter(element.getClass()).write(writeState, element);
                }
            }
            writeState.end(']');
        }
    }
    
    static class FieldWriter
    {
        final Field field;
        final String fieldName;
        final Writer writer;

        FieldWriter(Field field,Writer writer)
        {
            this.writer=writer;
            this.field = field;
            this.fieldName = field.getName()+':';
        }
        void write(WriteState writeState,boolean needComma,Object object) throws Throwable
        {
            writeState.writeSeperator(needComma);
            writeState.writeName(this.fieldName);
            writer.write(writeState,object);
        }
    }

    static private Writer getArrayWriter(Class<?> componentType)
    {
        if (componentType == boolean.class)
        {
            return new booleanArrayWriter();
        }
        else if (componentType == int.class)
        {
            return new intArrayWriter();
        }
        else if (componentType == long.class)
        {
            return new longArrayWriter();
        }
        else if (componentType == float.class)
        {
            return new floatArrayWriter();
        }
        else if (componentType == double.class)
        {
            return new doubleArrayWriter();
        }
        else if (componentType == byte.class)
        {
            return new byteArrayWriter();
        }
        /*
        else if (componentType == char.class)
        {
            return new charArrayWriter();
        }
        */
        else if (componentType == short.class)
        {
            return new shortArrayWriter();
        }
        if (componentType == String.class)
        {
            return new StringArrayWriter();
        }
        if ((componentType.isEnum()))
        {
            return new EnumArrayWriter();
        }
        else if (componentType == Boolean.class)
        {
            return new NullablePrimitiveArrayWriter<Boolean>();
        }
        else if (componentType == Integer.class)
        {
            return new NullablePrimitiveArrayWriter<Integer>();
        }
        else if (componentType == Long.class)
        {
            return new NullablePrimitiveArrayWriter<Long>();
        }
        else if (componentType == Float.class)
        {
            return new NullablePrimitiveArrayWriter<Float>();
        }
        else if (componentType == Double.class)
        {
            return new NullablePrimitiveArrayWriter<Double>();
        }
        else if (componentType == Byte.class)
        {
            return new NullablePrimitiveArrayWriter<Byte>();
        }
        else if (componentType == Character.class)
        {
            return new NullablePrimitiveArrayWriter<Character>();
        }
        else if (componentType == Short.class)
        {
            return new NullablePrimitiveArrayWriter<Short>();
        }
        else if (componentType!=Object.class)
        {
            return new ArrayWriter(componentType);
        }
        else 
        {
            return new ObjectArrayWriter();
        }
        
    }
    
    static private Writer getWriter(Class<?> type)
    {
        Writer writer;
        synchronized(WRITERS)
        {
            writer=WRITERS.get(type.getName());
        }
        if (writer!=null)
        {
            return writer;
        }
        if (type.isPrimitive())
        {
            writer=new PrimitiveWriter();
        }
        else if (type == String.class)
        {
            writer=new StringWriter();
        }
        else if (type == Script.class)
        {
            writer=new ScriptWriter();
        }
        else if (type.isArray())
        {
            writer=getArrayWriter(type.getComponentType());
        }
        else if (type.isEnum())
        {
            writer=new EnumWriter();
        }
        else if (type == Boolean.class)
        {
            writer=new PrimitiveWriter();
        }
        else if (type == Integer.class)
        {
            writer=new PrimitiveWriter();
        }
        else if (type == Long.class)
        {
            writer=new PrimitiveWriter();
        }
        else if (type == Float.class)
        {
            writer=new PrimitiveWriter();
        }
        else if (type == Double.class)
        {
            writer=new PrimitiveWriter();
        }
        else if (type == Byte.class)
        {
            writer=new PrimitiveWriter();
        }
        else if (type == Character.class)
        {
            writer=new PrimitiveWriter();
        }
        else if (type == Short.class)
        {
            writer=new PrimitiveWriter();
        }
        else if (type == java.lang.Enum.class)
        {
            writer=new EnumWriter();
        }
        else if (type == BigDecimal.class)
        {
            writer=new PrimitiveWriter();
        }
        else
        {
            writer=new ObjectWriter();
        }
        synchronized(WRITERS)
        {
            WRITERS.put(type.getName(), writer);
        }
        return writer;
    }
    
    static private FieldWriter[] getFieldWriters(Class<?> type) throws Exception
    {
        FieldWriter[] fieldWriters;
        synchronized (FIELD_WRITERS)
        {
            fieldWriters = FIELD_WRITERS.get(type.getName());
        }
        if (fieldWriters == null)
        {
            HashMap<String, FieldWriter> map = new HashMap<>();
            for (Class<?> c = type; c != null; c = c.getSuperclass())
            {
                for (Field field : c.getDeclaredFields())
                {
                    int modifiers = field.getModifiers();
                    if (Modifier.isTransient(modifiers))
                    {
                        continue;
                    }
                    if (Modifier.isStatic(modifiers))
                    {
                        continue;
                    }
                    if (map.containsKey(field.getName()) == false)
                    {
                        field.setAccessible(true);
                        Writer writer=getWriter(field.getType());
                        map.put(field.getName(), new FieldWriter(field,writer));
                    }
                }
            }
            fieldWriters = map.values().toArray(new FieldWriter[map.size()]);
            synchronized (FIELD_WRITERS)
            {
                FIELD_WRITERS.put(type.getName(), fieldWriters);
            }
        }
        return fieldWriters;
    }
    
    static public String writeObject(Object object,QuotationMark mark) throws Throwable
    {
        if (object!=null)
        {
            WriteState writeState=new WriteState(mark);
            Writer writer=getWriter(object.getClass());
            writer.write(writeState, object);
            return writeState.toString();
        }
        else
        {
            return null;
        }
    }
    
    
}
