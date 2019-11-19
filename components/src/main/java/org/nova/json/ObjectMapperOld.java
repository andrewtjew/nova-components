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
package org.nova.json;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import org.nova.utils.FileUtils;

public class ObjectMapperOld
{
    final static String NULL="null";
    final private HashMap<String, ReadTypeInfo> readObjectCache=new HashMap<>();
    final private HashMap<String, FieldWriter[]> FIELD_WRITERS=new HashMap<>();
    final private HashMap<String, Writer> WRITERS=new HashMap<>();

    static abstract class Writer
    {
        abstract void write(StringBuilder sb,Object object) throws Throwable;
    }
    static class PrimitiveWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append(object);
        }
    }
    static class StringWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('"');
            writeString(sb, (String)object);
            sb.append('"');
        }
    }
    static class EnumWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('"');
            sb.append(object);
            sb.append('"');
        }
    }
    static class ObjectWriter extends  Writer
    {
        void write(StringBuilder sb,Object object) throws Throwable
        {
            sb.append('{');
            boolean needComma = false;
            FieldWriter[] fieldWriters = MAPPER.getFieldWriters(object.getClass());
            for (FieldWriter fieldWriter:fieldWriters)
            {
                Object fieldObject = fieldWriter.field.get(object);
                if (fieldObject!=null)
                {
                    fieldWriter.write(sb, needComma, fieldObject);
                    needComma=true;
                }
            }
            sb.append('}');
        }
    }
    static class booleanArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            boolean[] array = (boolean[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                sb.append(array[i]);
            }
            sb.append(']');
        }
    }
    static class byteArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            byte[] array = (byte[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                sb.append(array[i]);
            }
            sb.append(']');
        }
    }
    static class charArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            byte[] array = (byte[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                sb.append(array[i]);
            }
            sb.append(']');
        }
    }
    static class shortArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            byte[] array = (byte[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                sb.append(array[i]);
            }
            sb.append(']');
        }
    }
    static class intArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            int[] array = (int[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                sb.append(array[i]);
            }
            sb.append(']');
        }
    }
    static class longArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            long[] array = (long[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                sb.append(array[i]);
            }
            sb.append(']');
        }
    }
    static class floatArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            float[] array = (float[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                sb.append(array[i]);
            }
            sb.append(']');
        }
    }
    static class doubleArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            double[] array = (double[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                sb.append(array[i]);
            }
            sb.append(']');
        }
    }

    static class StringArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            String[] array = (String[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                if (array[i]==null)
                {
                    sb.append(NULL);
                }
                else
                {
                    sb.append('"');
                    writeString(sb,array[i]);
                    sb.append('"');
                }
            }
            sb.append(']');
        }
    }
    static class EnumArrayWriter extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            Enum<?>[] array = (Enum<?>[]) object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                if (array[i]==null)
                {
                    sb.append(NULL);
                }
                else
                {
                    sb.append(',');
                    sb.append('"');
                    sb.append(array[i]);
                    sb.append('"');
                }
            }
            sb.append(']');
        }
    }
    static class NullablePrimitiveArrayWriter<TYPE> extends  Writer
    {
        void write(StringBuilder sb,Object object)
        {
            sb.append('[');
            TYPE[] array = (TYPE[])object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                if (array[i]==null)
                {
                    sb.append(NULL);
                }
                else
                {
                    sb.append(array[i]);
                }
            }
            sb.append(']');
        }
    }
    static class ArrayWriter extends Writer
    {
        final Writer writer;
        ArrayWriter(Class<?> componentType)
        {
            this.writer=MAPPER.getWriter(componentType);
        }
        void write(StringBuilder sb,Object object) throws Throwable
        {
            sb.append('[');
            Object[] array = (Object[])object;
            for (int i = 0; i < array.length; i++)
            {
                if (i>0)
                {
                    sb.append(',');
                }
                if (array[i]==null)
                {
                    sb.append(NULL);
                }
                else
                {
                    this.writer.write(sb, array[i]);
                }
            }
            sb.append(']');
        }
    }
    static class ObjectArrayWriter extends Writer
    {
        void write(StringBuilder sb,Object object) throws Throwable
        {
            sb.append('[');
            Object[] array = (Object[])object;
            for (int i = 0; i < array.length; i++)
            {
                Object element=array[i];
                if (i>0)
                {
                    sb.append(',');
                }
                if (element==null)
                {
                    sb.append(NULL);
                }
                else
                {
                    MAPPER.getWriter(element.getClass()).write(sb, element);
                }
            }
            sb.append(']');
        }
    }
    
    static class FieldWriter
    {
        final Field field;
        final char[] jsonName;
  //      final char[] commaJsonName;
        final Writer writer;

        FieldWriter(Field field,Writer writer)
        {
            this.writer=writer;
            this.field = field;
//            this.commaJsonName = (",\"" + field.getName() + '"' + ':').toCharArray();
            this.jsonName = ('"' + field.getName() + '"' + ':').toCharArray();
        }
        void write(StringBuilder sb,boolean needComma,Object object) throws Throwable
        {
            if (needComma)
            {
                sb.append(',');
            }
            sb.append(jsonName);
            writer.write(sb, object);
        }
    }

    private Writer getArrayWriter(Class<?> componentType)
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
        else if (componentType == char.class)
        {
            return new charArrayWriter();
        }
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
    
    private Writer getWriter(Class<?> type)
    {
        Writer writer;
        synchronized(this.WRITERS)
        {
            writer=this.WRITERS.get(type.getName());
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
        synchronized(this.WRITERS)
        {
            this.WRITERS.put(type.getName(), writer);
        }
        return writer;
    }
    
    private FieldWriter[] getFieldWriters(Class<?> type) throws Exception
    {
        FieldWriter[] fieldWriters;
        synchronized (this.FIELD_WRITERS)
        {
            fieldWriters = this.FIELD_WRITERS.get(type.getName());
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
            synchronized (this.FIELD_WRITERS)
            {
                this.FIELD_WRITERS.put(type.getName(), fieldWriters);
            }
        }
        return fieldWriters;
    }

	final static ObjectMapperOld MAPPER=new ObjectMapperOld();
	
	// read
	
	public static <OBJECT> OBJECT read(InputStream inputStream, Class<OBJECT> type) throws Exception
	{
		return MAPPER.readObject(inputStream,type);
	}

	public static <OBJECT> OBJECT readFromFile(String fileName, Class<OBJECT> type) throws Exception
	{
		try (FileInputStream inputStream=new FileInputStream(FileUtils.toNativePath(fileName)))
		{
			return MAPPER.readObject(inputStream,type);
		}
	}

	public static <OBJECT> OBJECT read(String text, Class<OBJECT> type) throws Exception
	{
		return MAPPER.readObject(text,type);
	}
	
	
	public <OBJECT> OBJECT readObject(InputStream inputStream, Class<OBJECT> type) throws Exception
	{
		Lexer lexer = new Lexer(inputStream);
		char objectOrArray = lexer.getNextCharacter();
		if (objectOrArray == '[')
		{
 			return (OBJECT)readArray(lexer,type);
		}
		else if (objectOrArray == '{')
		{
			return readObject(lexer, type);
		}
		else if (objectOrArray==Character.MAX_VALUE)
		{
			return null;
		}
		throw new Exception("Invalid character " + (char) objectOrArray + " at " + lexer.getPosition());
	}

	@SuppressWarnings("unchecked")
	public <OBJECT> OBJECT readObject(String text, Class<OBJECT> type) throws Exception
	{
	    if ((text==null)||(text.length()==0))
	    {
	        return null;
	    }
		Lexer lexer = new Lexer(text);
		char objectOrArray = lexer.getNextCharacter();
		if (objectOrArray == '[')
		{
			return (OBJECT) readArray(lexer, type.getComponentType());
		}
		else if (objectOrArray == '{')
		{
			return readObject(lexer, type);
		}
		throw new Exception("Invalid character " + (char) objectOrArray + " at " + lexer.getPosition());
	}

	static class ReadTypeInfo
	{
		final HashMap<String, Field> fields;
		final Constructor<?> constructor;
		final Object[] parameters;

		ReadTypeInfo(Constructor<?> constructor, Object[] parameters, HashMap<String, Field> fields)
		{
			this.fields = fields;
			this.constructor = constructor;
			this.parameters = parameters;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Object readArray(Lexer lexer, Class<?> componentType) throws Exception
	{
		if (componentType.isPrimitive())
		{
			if (componentType == boolean.class)
			{
				ArrayList<Boolean> list = new ArrayList<>();
				char next = lexer.isCloseArray();
				if (next == 0)
				{
					for (;;)
					{
						list.add(lexer.getPrimitiveBoolean());
						next = lexer.isCommaOrCloseArray();
						if (next == ']')
						{
							break;
						}
						else if (next != ',')
						{
							throw new Exception(", or ] expected at " + lexer.getPosition());
						}

					}
				}
				boolean[] array = new boolean[list.size()];
				for (int i = 0; i < array.length; i++)
				{
					array[i] = list.get(i);
				}
				return array;
			}
			else if (componentType == int.class)
			{
				ArrayList<Integer> list = new ArrayList<>();
				char next = lexer.isCloseArray();
				if (next == 0)
				{
					for (;;)
					{
						list.add(lexer.getPrimitiveInteger());
						next = lexer.isCommaOrCloseArray();
						if (next == ']')
						{
							break;
						}
						else if (next != ',')
						{
							throw new Exception(", or ] expected at " + lexer.getPosition());
						}

					}
				}
				int[] array = new int[list.size()];
				for (int i = 0; i < array.length; i++)
				{
					array[i] = list.get(i);
				}
				return array;
			}
			else if (componentType == long.class)
			{
				ArrayList<Long> list = new ArrayList<>();
				char next = lexer.isCloseArray();
				if (next == 0)
				{
					for (;;)
					{
						list.add(lexer.getPrimitiveLong());
						next = lexer.isCommaOrCloseArray();
						if (next == ']')
						{
							break;
						}
						else if (next != ',')
						{
							throw new Exception(", or ] expected at " + lexer.getPosition());
						}

					}
				}
				long[] array = new long[list.size()];
				for (int i = 0; i < array.length; i++)
				{
					array[i] = list.get(i);
				}
				return array;
			}
			else if (componentType == float.class)
			{
				ArrayList<Float> list = new ArrayList<>();
				char next = lexer.isCloseArray();
				if (next == 0)
				{
					for (;;)
					{
						list.add(lexer.getPrimitiveFloat());
						next = lexer.isCommaOrCloseArray();
						if (next == ']')
						{
							break;
						}
						else if (next != ',')
						{
							throw new Exception(", or ] expected at " + lexer.getPosition());
						}

					}
				}
				float[] array = new float[list.size()];
				for (int i = 0; i < array.length; i++)
				{
					array[i] = list.get(i);
				}
				return array;
			}
			else if (componentType == double.class)
			{
				ArrayList<Double> list = new ArrayList<>();
				char next = lexer.isCloseArray();
				if (next == 0)
				{
					for (;;)
					{
						list.add(lexer.getPrimitiveDouble());
						next = lexer.isCommaOrCloseArray();
						if (next == ']')
						{
							break;
						}
						else if (next != ',')
						{
							throw new Exception(", or ] expected at " + lexer.getPosition());
						}

					}
				}
				double[] array = new double[list.size()];
				for (int i = 0; i < array.length; i++)
				{
					array[i] = list.get(i);
				}
				return array;
			}
			else if (componentType == byte.class)
			{
				ArrayList<Byte> list = new ArrayList<>();
				char next = lexer.isCloseArray();
				if (next == 0)
				{
					for (;;)
					{
						list.add(lexer.getPrimitiveByte());
						next = lexer.isCommaOrCloseArray();
						if (next == ']')
						{
							break;
						}
						else if (next != ',')
						{
							throw new Exception(", or ] expected at " + lexer.getPosition());
						}

					}
				}
				byte[] array = new byte[list.size()];
				for (int i = 0; i < array.length; i++)
				{
					array[i] = list.get(i);
				}
				return array;
			}
			else if (componentType == char.class)
			{
				ArrayList<Character> list = new ArrayList<>();
				char next = lexer.isCloseArray();
				if (next == 0)
				{
					for (;;)
					{
						list.add(lexer.getPrimitiveCharacter());
						next = lexer.isCommaOrCloseArray();
						if (next == ']')
						{
							break;
						}
						else if (next != ',')
						{
							throw new Exception(", or ] expected at " + lexer.getPosition());
						}

					}
				}
				char[] array = new char[list.size()];
				for (int i = 0; i < array.length; i++)
				{
					array[i] = list.get(i);
				}
				return array;
			}
			else if (componentType == short.class)
			{
				ArrayList<Short> list = new ArrayList<>();
				char next = lexer.isCloseArray();
				if (next == 0)
				{
					for (;;)
					{
						list.add(lexer.getPrimitiveShort());
						next = lexer.isCommaOrCloseArray();
						if (next == ']')
						{
							break;
						}
						else if (next != ',')
						{
							throw new Exception(", or ] expected at " + lexer.getPosition());
						}

					}
				}
				short[] array = new short[list.size()];
				for (int i = 0; i < array.length; i++)
				{
					array[i] = list.get(i);
				}
				return array;
			}
		}
		else if (componentType == String.class)
		{
			ArrayList<String> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
				    /*
					next = lexer.getNextCharacter();
					if (next != '"')
					{
						throw new Exception("String expected at " + lexer.getPosition());
					}
					*/
					list.add(lexer.getString());
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			return list.toArray(new String[list.size()]);
		}
		else if (componentType == Boolean.class)
		{
			ArrayList<Boolean> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
					list.add(lexer.getBoolean());
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			return list.toArray(new Boolean[list.size()]);
		}
		else if (componentType == Integer.class)
		{
			ArrayList<Integer> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
					list.add(lexer.getInteger());
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			return list.toArray(new Integer[list.size()]);
		}
		else if (componentType == Long.class)
		{
			ArrayList<Long> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
					list.add(lexer.getLong());
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			return list.toArray(new Long[list.size()]);
		}
		else if (componentType == Float.class)
		{
			ArrayList<Float> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
					list.add(lexer.getFloat());
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			return list.toArray(new Float[list.size()]);
		}
		else if (componentType == Double.class)
		{
			ArrayList<Double> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
					list.add(lexer.getDouble());
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			return list.toArray(new Double[list.size()]);
		}
		else if (componentType == Byte.class)
		{
			ArrayList<Byte> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
					list.add(lexer.getByte());
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			return list.toArray(new Byte[list.size()]);
		}
		else if (componentType == Character.class)
		{
			ArrayList<Character> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
					list.add(lexer.getCharacter());
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			return list.toArray(new Character[list.size()]);
		}
		else if (componentType == Short.class)
		{
			ArrayList<Short> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
					list.add(lexer.getShort());
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			return list.toArray(new Short[list.size()]);
		}
		else if (componentType.isEnum())
		{
			ArrayList<Enum> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
				    /*
					next = lexer.getNextCharacter();
					if (next != '"')
					{
						throw new Exception("String expected at " + lexer.getPosition());
					}
					*/
					list.add(Enum.valueOf((Class<Enum>) componentType, lexer.getString()));
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			Object array = Array.newInstance(componentType, list.size());
			for (int i = 0; i < list.size(); i++)
			{
				Array.set(array, i, list.get(i));
			}
			return array;
		}
		else
		{
			ArrayList<Object> list = new ArrayList<>();
			char next = lexer.isCloseArray();
			if (next == 0)
			{
				for (;;)
				{
					next = lexer.getNextCharacter();
					if (next == '{')
					{
	                    list.add(readObject(lexer, componentType));
					}
					else if (next=='[')
					{
					    list.add(readArray(lexer, componentType.getComponentType()));
					}
					else if (next=='n')
					{
					    lexer.getRestOfNull();
                        list.add(null);
					}
					else
					{
                        throw new Exception("Object expected at " + lexer.getPosition());
					}
					next = lexer.isCommaOrCloseArray();
					if (next == ']')
					{
						break;
					}
					else if (next != ',')
					{
						throw new Exception(", or ] expected at " + lexer.getPosition());
					}

				}
			}
			Object array = Array.newInstance(componentType, list.size());
			for (int i = 0; i < list.size(); i++)
			{
				Array.set(array, i, list.get(i));
			}
			return array;
		}
		return null; // dead code???
	}

	private ReadTypeInfo getReadTypeInfo(Class<?> type) throws Exception
	{
		Constructor<?>[] constructors = type.getDeclaredConstructors();
		synchronized (this.readObjectCache)
		{
			ReadTypeInfo typeInfo = this.readObjectCache.get(type.getName());
			if (typeInfo == null)
			{
				HashMap<String, Field> fields = new HashMap<>();
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
						if (fields.containsKey(field.getName()) == false)
						{
							field.setAccessible(true);
							fields.put(field.getName(), field);
						}
					}
				}
				if (constructors.length == 0)
				{
					typeInfo = new ReadTypeInfo(null, null, fields);
				}
				else
				{
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
					Object[] parameters = new Object[shortestConstructor.getParameterCount()];
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
						/*
						else if (parameterType.isEnum())
						{
							parameters[i] = null;
						}
						*/
					}
					shortestConstructor.setAccessible(true);
					typeInfo = new ReadTypeInfo(shortestConstructor, parameters, fields);
				}
				readObjectCache.put(type.getName(), typeInfo);
			}
			return typeInfo;
		}
	}

	@SuppressWarnings(
	{ "rawtypes", "unchecked" })
	private <OBJECT> OBJECT readObject(Lexer lexer, Class<OBJECT> type) throws Exception
	{
		ReadTypeInfo typeInfo = getReadTypeInfo(type);
		OBJECT object=(typeInfo.constructor==null)?type.newInstance():(OBJECT) typeInfo.constructor.newInstance(typeInfo.parameters);
		char next = lexer.getNextCharacter();
		for (;;)
		{
            if (next != '"')
            {
                if (next=='}')
                {
                    break;
                }
                throw new Exception("String or } expected at " + lexer.getPosition());
            }
			String string = lexer.getJSONName();
			Field field = typeInfo.fields.get(string);
			if (field==null)
			{
			    //throw new Exception("No such field. Name="+string+", type="+type.getCanonicalName());
			    lexer.skipJSONValue();
			}
			else
			{
    			Class<?> fieldType = field.getType();
    			if (fieldType.isPrimitive())
    			{
    				if (fieldType == boolean.class)
    				{
    					field.setBoolean(object, lexer.getPrimitiveBoolean());
    				}
    				else if (fieldType == int.class)
    				{
    					field.setInt(object, lexer.getPrimitiveInteger());
    				}
    				else if (fieldType == long.class)
    				{
    					field.setLong(object, lexer.getPrimitiveLong());
    				}
    				else if (fieldType == float.class)
    				{
    					field.setFloat(object, lexer.getPrimitiveFloat());
    				}
    				else if (fieldType == double.class)
    				{
    					field.setDouble(object, lexer.getPrimitiveDouble());
    				}
    				else if (fieldType == byte.class)
    				{
    					field.setByte(object, lexer.getPrimitiveByte());
    				}
    				else if (fieldType == char.class)
    				{
    					field.setChar(object, lexer.getPrimitiveCharacter());
    				}
    				else if (fieldType == short.class)
    				{
    					field.setShort(object, lexer.getPrimitiveShort());
    				}
    			}
    			else if (fieldType == String.class)
    			{
    				field.set(object, lexer.getString());
    			}
    			else if (fieldType.isArray())
    			{
    				next = lexer.getNextCharacter();
    				if (next=='[')
    				{
                        field.set(object, readArray(lexer, fieldType.getComponentType()));
    				}
    				else if (next == 'n')
                    {
                        lexer.getRestOfNull();
                        field.set(object, null);
                    }
    				else 
    				{
    					throw new Exception("Array expected at " + lexer.getPosition());
    				}
    			}
    			else if (fieldType == Boolean.class)
    			{
    				field.set(object, lexer.getBoolean());
    			}
    			else if (fieldType == Integer.class)
    			{
    				field.set(object, lexer.getInteger());
    			}
    			else if (fieldType == Long.class)
    			{
    				field.set(object, lexer.getLong());
    			}
    			else if (fieldType == Float.class)
    			{
    				field.set(object, lexer.getFloat());
    			}
    			else if (fieldType == Double.class)
    			{
    				field.set(object, lexer.getDouble());
    			}
    			else if (fieldType == Byte.class)
    			{
    				field.set(object, lexer.getByte());
    			}
    			else if (fieldType == Character.class)
    			{
    				field.set(object, lexer.getCharacter());
    			}
    			else if (fieldType == Short.class)
    			{
    				field.set(object, lexer.getShort());
    			}
                else if (fieldType.isEnum())
                {
                    String value=lexer.getString();
                    if (value!=null)
                    {
                        field.set(object, Enum.valueOf((Class<Enum>) fieldType, value));
                    }
                }
                else if (fieldType==java.lang.Enum.class)
                {
                    int value=lexer.getPrimitiveInteger();
                    //this is not tested...
                    field.set(object, value);
                }
                else if (fieldType==BigDecimal.class)
                {
                    next = lexer.getNextCharacter();
                    field.set(object, new BigDecimal(lexer.getValueText()));
                }
    			else
    			{
    				next = lexer.getNextCharacter();
    				if (next == '{')
    				{
    					field.set(object, readObject(lexer, fieldType));
    				}
                    else if (next=='n')
                    {
                        lexer.getRestOfNull();
                        field.set(object, null);
                    }
                    else
                    {
                        throw new Exception("error " + lexer.getPosition());
                    }
    			}
			}
			next = lexer.getNextCharacter();
			if (next == '}')
			{
				break;
			}
			else if (next == ',')
			{
				next = lexer.getNextCharacter();
				continue;
			}
			throw new Exception(", or } expected at " + lexer.getPosition());
		}
		return object;
	}


	public static String write(Object object) throws Throwable
	{
		return MAPPER.writeObjectAsString(object);
	}
	public static void write(OutputStream outputStream,Object object) throws Throwable
	{
		MAPPER.writeObject(outputStream,object);
	}

	public String writeObjectAsString(Object object) throws Throwable
	{
	    if (object==null)
	    {
	        return "";
	    }
		StringBuilder sb = new StringBuilder();
		Writer writer=MAPPER.getWriter(object.getClass());
		writer.write(sb, object);
		return sb.toString();
	}
	
	public void writeObject(OutputStream outputStream,Object object) throws Throwable
	{
		outputStream.write(writeObjectAsString(object).getBytes());
		
	}
	
	public static void writeString(StringBuilder sb,String text)
	{
		for (int index=0;index<text.length();index++)
		{
			char c=text.charAt(index);
			if (c=='\\')
			{
				sb.append(c);
				sb.append(c);
			}
			else if (c>'"')
			{ 
                sb.append(c);
			}
			else if (c=='"')
			{
				sb.append('\\');
				sb.append('"');
			}
			else if (c=='\b')
			{
				sb.append('\\');
				sb.append('b');
			}
			else if (c=='\f')
			{
				sb.append('\\');
				sb.append('f');
			}
			else if (c=='\n')
			{
				sb.append('\\');
				sb.append('n');
			}
			else if (c=='\r')
			{
				sb.append('\\');
				sb.append('r');
			}
			else if (c=='\t')
			{
				sb.append('\\');
				sb.append('t');
			}
			else
			{
                sb.append(c);
			}
		}
	}

    // read2
    
	
	
}
