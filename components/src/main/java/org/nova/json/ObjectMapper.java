package org.nova.json;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.nova.core.Utils;
import org.nova.json.Lexer.Reader;

public class ObjectMapper
{
	static com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

	public static void write2(OutputStream outputStream, Object object) throws Exception
	{
		ObjectMapper.objectMapper.writeValue(outputStream, object);
	}

	public static String write2(Object object) throws Exception
	{
		return objectMapper.writeValueAsString(object);
	}

	public static <OBJECT> OBJECT read2(String jsonText, Class<OBJECT> type) throws Exception
	{
		return ObjectMapper.objectMapper.readValue(jsonText, type);
	}

	public static <OBJECT> OBJECT read2(InputStream inputStream, Class<OBJECT> type) throws Exception
	{
		return ObjectMapper.objectMapper.readValue(inputStream, type);
	}

	
	final private HashMap<String, ReadTypeInfo> readObjectCache=new HashMap<>();
	final private HashMap<String, WriteTypeInfo> writeObjectCache=new HashMap<>();

	final static ObjectMapper mapper=new ObjectMapper();
	
	private ObjectMapper()
	{
	}

	public static <OBJECT> OBJECT read(InputStream inputStream, Class<OBJECT> type) throws Exception
	{
		return mapper.readObject(inputStream,type);
	}

	public static <OBJECT> OBJECT readFromFile(String fileName, Class<OBJECT> type) throws Exception
	{
		try (FileInputStream inputStream=new FileInputStream(fileName))
		{
			return mapper.readObject(inputStream,type);
		}
	}

	public static <OBJECT> OBJECT read(String text, Class<OBJECT> type) throws Exception
	{
		return mapper.readObject(text,type);
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
					next = lexer.getNextCharacter();
					if (next != '"')
					{
						throw new Exception("String expected at " + lexer.getPosition());
					}
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
					    list.add(readArray(lexer, componentType));
					}
					else if (lexer.getRestOfNull())
					{
                        list.add(null);
					}
					else
					{
                        throw new Exception("String expected at " + lexer.getPosition());
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
			    throw new Exception("No such field. Name="+string);
			}
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
				if (next != '[')
				{
					throw new Exception("Array expected at " + lexer.getPosition());
				}
				field.set(object, readArray(lexer, fieldType.getComponentType()));
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
                field.set(object, Enum.valueOf((Class<Enum>) fieldType, lexer.getString()));
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

	static class WriteFieldInfo
	{
		final Field field;
		final char[] jsonName;
		final char[] commaJsonName;

		WriteFieldInfo(Field field)
		{
			this.field = field;
			this.commaJsonName = (",\"" + field.getName() + '"' + ':').toCharArray();
			this.jsonName = ('"' + field.getName() + '"' + ':').toCharArray();
		}
	}

	static class WriteTypeInfo
	{
		final WriteFieldInfo[] writeFieldInfos;

		WriteTypeInfo(Field[] fields)
		{
			this.writeFieldInfos = new WriteFieldInfo[fields.length];
			for (int i = 0; i < fields.length; i++)
			{
				writeFieldInfos[fields.length - i - 1] = new WriteFieldInfo(fields[i]);
			}
		}
	}

	private WriteTypeInfo getWriteTypeInfo(Class<?> type) throws Exception
	{
		synchronized (this.writeObjectCache)
		{
			WriteTypeInfo typeInfo = this.writeObjectCache.get(type.getName());
			if (typeInfo == null)
			{
				//typeInfo = new WriteTypeInfo(type.getDeclaredFields());
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
				typeInfo = new WriteTypeInfo(fields.values().toArray(new Field[fields.size()]));
				this.writeObjectCache.put(type.getName(), typeInfo);
			}
			return typeInfo;
		}
	}

	public static String write(Object object) throws Exception
	{
		return mapper.writeObjectAsString(object);
	}
	public static void write(OutputStream outputStream,Object object) throws Exception
	{
		mapper.writeObject(outputStream,object);
	}

	public String writeObjectAsString(Object object) throws Exception
	{
	    if (object==null)
	    {
	        return "";
	    }
		StringBuilder sb = new StringBuilder();
		writeObjectOrArray(sb,object,object.getClass());
		return sb.toString();
	}
	void writeObjectOrArray(StringBuilder sb,Object object,Class<?> type) throws Exception
	{
        if (type.isArray())
        {
            writeArray(sb, object, type.getComponentType());
        }
        else
        {
            writeObject(sb, object, type);
        }
	    
	}
	
	public void writeObject(OutputStream outputStream,Object object) throws Exception
	{
		outputStream.write(writeObjectAsString(object).getBytes());
		
	}
	
	private char[] NULL_CHARS="null".toCharArray();
	private char[] COMMA_NULL_CHARS=",null".toCharArray();
	private char[] COMMA_DOUBLEQUOTE=",\"".toCharArray();

	public static void writeString(StringBuilder sb,String text)
	{
		int mark=0;
		int index;
		char c;
		for (index=0;index<text.length();index++)
		{
			c=text.charAt(index);
			if (c=='\\')
			{
				if (mark<index)
				{
					sb.append(text,mark,index);
					mark=index+1;
				}
				sb.append(c);
				sb.append(c);
			}
			else if (c>'"')
			{
			}
			else if (c=='"')
			{
				if (mark<index)
				{
					sb.append(text,mark,index);
				}
				sb.append('\\');
				sb.append('"');
                mark=index+1;
			}
			else if (c=='\b')
			{
				if (mark<index)
				{
					sb.append(text,mark,index);
				}
				sb.append('\\');
				sb.append('b');
                mark=index+1;
			}
			else if (c=='\f')
			{
				if (mark<index)
				{
					sb.append(text,mark,index);
				}
				sb.append('\\');
				sb.append('f');
                mark=index+1;
			}
			else if (c=='\n')
			{
				if (mark<index)
				{
					sb.append(text,mark,index);
				}
				sb.append('\\');
				sb.append('n');
                mark=index+1;
			}
			else if (c=='\r')
			{
				if (mark<index)
				{
					sb.append(text,mark,index);
				}
				sb.append('\\');
				sb.append('r');
                mark=index+1;
			}
			else if (c=='\t')
			{
				if (mark<index)
				{
					sb.append(text,mark,index);
				}
				sb.append('\\');
				sb.append('t');
                mark=index+1;
			}
			else
			{
			    //Invalid characters are skipped.
			}
		}
		if (mark<index)
		{
			sb.append(text,mark,index);
		}
	}
	
	
	private void writeArray(StringBuilder sb, Object array, Class<?> componentType) throws Exception
	{
		sb.append('[');
		if (componentType.isPrimitive())
		{
			if (componentType == boolean.class)
			{
				boolean[] values = (boolean[]) array;
				if (values.length > 0)
				{
					sb.append(values[0]);
					for (int i = 1; i < values.length; i++)
					{
						sb.append(',');
						sb.append(values[i]);
					}
				}
			}
			else if (componentType == int.class)
			{
				int[] values = (int[]) array;
				if (values.length > 0)
				{
					sb.append(values[0]);
					for (int i = 1; i < values.length; i++)
					{
						sb.append(',');
						sb.append(values[i]);
					}
				}
			}
			else if (componentType == long.class)
			{
				long[] values = (long[]) array;
				if (values.length > 0)
				{
					sb.append(values[0]);
					for (int i = 1; i < values.length; i++)
					{
						sb.append(',');
						sb.append(values[i]);
					}
				}
			}
			else if (componentType == float.class)
			{
				float[] values = (float[]) array;
				if (values.length > 0)
				{
					sb.append(values[0]);
					for (int i = 1; i < values.length; i++)
					{
						sb.append(',');
						sb.append(values[i]);
					}
				}
			}
			else if (componentType == double.class)
			{
				double[] values = (double[]) array;
				if (values.length > 0)
				{
					sb.append(values[0]);
					for (int i = 1; i < values.length; i++)
					{
						sb.append(',');
						sb.append(values[i]);
					}
				}
			}
			else if (componentType == byte.class)
			{
				byte[] values = (byte[]) array;
				if (values.length > 0)
				{
					sb.append(values[0]);
					for (int i = 1; i < values.length; i++)
					{
						sb.append(',');
						sb.append(values[i]);
					}
				}
			}
			else if (componentType == char.class)
			{
				char[] values = (char[]) array;
				if (values.length > 0)
				{
					sb.append(values[0]);
					for (int i = 1; i < values.length; i++)
					{
						sb.append(',');
						sb.append(values[i]);
					}
				}
			}
			else if (componentType == short.class)
			{
				short[] values = (short[]) array;
				if (values.length > 0)
				{
					sb.append(values[0]);
					for (int i = 1; i < values.length; i++)
					{
						sb.append(',');
						sb.append(values[i]);
					}
				}
			}
		}
		else if (componentType == String.class)
		{
			String[] values = (String[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append('"');
					sb.append(values[0]);
					sb.append('"');
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(COMMA_DOUBLEQUOTE);
						writeString(sb,values[i]);
						sb.append('"');
					}
					else
					{
						sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else if ((componentType.isEnum()))
		{
			Enum<?>[] values = (Enum<?>[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append('"');
					sb.append(values[0]);
					sb.append('"');
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(COMMA_DOUBLEQUOTE);
						sb.append(values[i]);
						sb.append('"');
					}
					else
					{
						sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else if (componentType == Boolean.class)
		{
			Boolean[] values = (Boolean[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append((values[0].booleanValue()));
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(',');
						sb.append((values[i].booleanValue()));
					}
					else
					{
						sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else if (componentType == Integer.class)
		{
			Integer[] values = (Integer[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append((values[0].intValue()));
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(',');
						sb.append((values[i].intValue()));
					}
					else
					{
						sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else if (componentType == Long.class)
		{
			Long[] values = (Long[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append((values[0].longValue()));
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(',');
						sb.append((values[i].longValue()));
					}
					else
					{
						sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else if (componentType == Float.class)
		{
			Float[] values = (Float[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append((values[0].floatValue()));
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(',');
						sb.append((values[i].floatValue()));
					}
					else
					{
						sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else if (componentType == Double.class)
		{
			Double[] values = (Double[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append((values[0].doubleValue()));
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(',');
						sb.append((values[i].doubleValue()));
					}
					else
					{
						sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else if (componentType == Byte.class)
		{
			Byte[] values = (Byte[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append((values[0].byteValue()));
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(',');
						sb.append((values[i].byteValue()));
					}
					else
					{
						sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else if (componentType == Character.class)
		{
			Character[] values = (Character[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append((values[0].charValue()));
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(',');
						sb.append((values[i].charValue()));
					}
					else
					{
						sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else if (componentType == Short.class)
		{
			Short[] values = (Short[]) array;
			if (values.length > 0)
			{
				if (values[0]!=null)
				{
					sb.append((values[0].shortValue()));
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < values.length; i++)
				{
					if (values[i]!=null)
					{
						sb.append(',');
						sb.append((values[i].shortValue()));
					}
					else
					{
                        sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		else
		{
			int length=Array.getLength(array);
			if (length > 0)
			{
				Object value=Array.get(array, 0);
				if (value!=null)
				{
					writeObjectOrArray(sb,value,value.getClass());
				}
				else
				{
					sb.append(NULL_CHARS);
				}
				for (int i = 1; i < length; i++)
				{
					value=Array.get(array, i);
					if (value!=null)
					{
	                    sb.append(',');
						writeObjectOrArray(sb,value,value.getClass());
					}
					else
					{
                        sb.append(COMMA_NULL_CHARS);
					}
				}
			}
		}
		sb.append(']');
	}

	private void writeObject(StringBuilder sb, Object object, Class<?> type) throws Exception
	{
		sb.append('{');
		boolean needComma = false;
		WriteTypeInfo typeInfo = getWriteTypeInfo(type);
		for (WriteFieldInfo fieldInfo : typeInfo.writeFieldInfos)
		{
			Field field = fieldInfo.field;
			Class<?> fieldType = field.getType();
			if (fieldType.isPrimitive())
			{
				if (needComma == false)
				{
					needComma = true;
					sb.append(fieldInfo.jsonName);
				}
				else
				{
					sb.append(fieldInfo.commaJsonName);
				}
				if (fieldType == boolean.class)
				{
					sb.append(field.getBoolean(object));
				}
				else if (fieldType == int.class)
				{
					sb.append(field.getInt(object));
				}
				else if (fieldType == long.class)
				{
					sb.append(field.getLong(object));
				}
				else if (fieldType == float.class)
				{
					sb.append(field.getFloat(object));
				}
				else if (fieldType == double.class)
				{
					sb.append(field.getDouble(object));
				}
				else if (fieldType == byte.class)
				{
					sb.append(field.getByte(object));
				}
				else if (fieldType == char.class)
				{
					sb.append(field.getChar(object));
				}
				else if (fieldType == short.class)
				{
					sb.append(field.getShort(object));
				}
			}
			else if (fieldType == String.class)
			{
				String value = (String) field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append('"');
					writeString(sb,value);
					sb.append('"');
				}
			}
			else if (fieldType.isArray())
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					writeArray(sb, value, fieldType.getComponentType());
				}
			}
			else if (fieldType.isEnum())
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append('"');
					sb.append(value.toString());
					sb.append('"');
				}
			}
			else if (fieldType == Boolean.class)
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append(((Boolean) value).booleanValue());
				}
			}
			else if (fieldType == Integer.class)
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append(((Integer) value).intValue());
				}
			}
			else if (fieldType == Long.class)
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append(((Long) value).longValue());
				}
			}
			else if (fieldType == Float.class)
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append(((Float) value).floatValue());
				}
			}
			else if (fieldType == Double.class)
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append(((Double) value).doubleValue());
				}
			}
			else if (fieldType == Byte.class)
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append(((Byte) value).byteValue());
				}
			}
			else if (fieldType == Character.class)
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append(((Character) value).charValue());
				}
			}
			else if (fieldType == Short.class)
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					sb.append(((Short) value).shortValue());
				}
			}
            else if (fieldType == java.lang.Enum.class)
            {
                Object value = field.get(object);
                if (value != null)
                {
                    if (needComma == false)
                    {
                        needComma = true;
                        sb.append(fieldInfo.jsonName);
                    }
                    else
                    {
                        sb.append(fieldInfo.commaJsonName);
                    }
                    sb.append(value);
                }
                
            }
            else if (fieldType == BigDecimal.class)
            {
                Object value = field.get(object);
                if (value != null)
                {
                    if (needComma == false)
                    {
                        needComma = true;
                        sb.append(fieldInfo.jsonName);
                    }
                    else
                    {
                        sb.append(fieldInfo.commaJsonName);
                    }
                    String text=value.toString();
                    sb.append(text);
                }
            }
			else
			{
				Object value = field.get(object);
				if (value != null)
				{
					if (needComma == false)
					{
						needComma = true;
						sb.append(fieldInfo.jsonName);
					}
					else
					{
						sb.append(fieldInfo.commaJsonName);
					}
					writeObjectOrArray(sb, value, fieldType);
				}
			}
		}
		sb.append('}');
	}
}
