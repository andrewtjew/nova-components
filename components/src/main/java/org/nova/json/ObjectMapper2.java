package org.nova.json;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectMapper2
{

    
   
    
    
/*  
     %x20 /              ; Space
     %x09 /              ; Horizontal tab
     %x0A /              ; Line feed or New line
     %x0D                ; Carriage return
*/
   
    static class Parser
    {
        private int position;
        final private String text;
        public Parser(String text)
        {
            this.text=text;
        }
     
        public int getPosition()
        {
            return this.position;
        }
        public char nextNonWhiteSpaceCharacter() throws Exception
        {
            for (;;)
            {
                char character=next();
                if (character>0x20)
                {
                    return character;
                }
                if ((character!=0x20)&&(character!=0x09)&&(character!=0x0A)&&(character!=0x0D))
                {
                    return character;
                }
            }
        }
        
        public void expectOpenBrace() throws Exception
        {
            if (nextNonWhiteSpaceCharacter()!='{')
            {
                throw new Exception("{ expected at "+getPosition());
            }
        }
        
        public String getName() throws Exception
        {
            char c=nextNonWhiteSpaceCharacter();
            if (c!='"')
            {
                throw new Exception("\" expected before name at "+getPosition());
            }
            int start=this.position;
            for (;;)
            {
                c=next();
                if (c=='"')
                {
                    int end=this.position-1;
                    c=next();
                    if (c==':')
                    {
                        return this.text.substring(start, end);
                    }
                    throw new Exception(": expected after name at "+getPosition());
                }
            }
        }
        
        public String getString() throws Exception
        {
            char character=nextNonWhiteSpaceCharacter();
            StringBuilder sb=new StringBuilder();
            if (character=='"')
            {
                int start=this.position;
                for (;;)
                {
                    character=next();
                    if (character=='"')
                    {
                        int end=this.position-1;
                        return this.text.substring(start, end);
                    }
                    else if (character=='\\')
                    {
                        int next=next();
                        if (next=='"')
                        {
                            sb.append('"');
                        }
                        else if (next=='\\')
                        {
                            sb.append('\\');
                        }
                        else if (next=='/')
                        {
                            sb.append('/');
                        }
                        else if (next=='b')
                        {
                            sb.append('\b');
                        }
                        else if (next=='f')
                        {
                            sb.append('\f');
                        }
                        else if (next=='n')
                        {
                            sb.append('\n');
                        }
                        else if (next=='r')
                        {
                            sb.append('\r');
                        }
                        else if (next=='t')
                        {
                            sb.append('\t');
                        }
                        else if (next=='u')
                        {
                            char c=0;
                            for (int i=0;i<4;i++)
                            {
                                char digit=next();
                                if ((digit>='0')&&(digit<='9'))
                                {
                                    c=(char)(c*16+digit-'0');
                                }
                                else if ((digit>='A')&&(digit<='E'))
                                {
                                    c=(char)(c*16+digit-'A'+10);
                                }
                                else if ((digit>='a')&&(digit<='e'))
                                {
                                    c=(char)(c*16+digit-'a'+10);
                                }
                                else
                                {
                                    throw new Exception("Invalid unicode escape at "+getPosition());
                                }
                            }
                            sb.append(c);
                        }
                    }
                    else
                    {
                        sb.append(character);
                    }
                }
            }
            else if  (character=='n')
            {
                expectRestOfNull();
                return null;
            }
            throw new Exception("String or null expected at "+getPosition());
        }

        private char next()
        {
            return this.text.charAt(this.position++);
        }
        
        public boolean isEndOfElements() throws Exception
        {
            char c=nextNonWhiteSpaceCharacter();
            if (c==',')
            {
                return false;
            }
            if (c=='}')
            {
                return true;
            }
            throw new Exception(", or } expected at "+getPosition());
        }
        public boolean isEndOfArray() throws Exception
        {
            char c=nextNonWhiteSpaceCharacter();
            if (c==',')
            {
                return false;
            }
            if (c==']')
            {
                return true;
            }
            throw new Exception(", or ] expected at "+getPosition());
        }
        
        public boolean isNonEmptyElements() throws Exception
        {
            char c=nextNonWhiteSpaceCharacter();
            if (c!='{')
            {
                throw new Exception("{ expected at "+getPosition());
            }
            c=nextNonWhiteSpaceCharacter();
            if (c=='}')
            {
                return false;
            }
            this.position--;
            return true;
        }
        public boolean isNonEmptyArray() throws Exception
        {
            char c=nextNonWhiteSpaceCharacter();
            if (c!='[')
            {
                throw new Exception("[ expected at "+getPosition());
            }
            c=nextNonWhiteSpaceCharacter();
            if (c==']')
            {
                return false;
            }
            this.position--;
            return true;
        }
        
        public void expectRestOfNull() throws Exception
        {
            if (next()=='u')
            {
                if (next()=='l')
                {
                    if (next()=='l')
                    {
                        return;
                    }
                }
            }
            throw new Exception("null expected at "+getPosition());
        }

        public Boolean getBoolean() throws Exception
        {
            char character=nextNonWhiteSpaceCharacter();
            if (character=='t')
            {
                if (next()=='r')
                {
                    if (next()=='u')
                    {
                        if (next()=='e')
                        {
                            return true;
                        }
                    }
                }
            }
            else if (character=='f')
            {
                if (next()=='a')
                {
                    if (next()=='l')
                    {
                        if (next()=='s')
                        {
                            if (next()=='e')
                            {
                                return false;
                            }
                        }
                    }
                }
            }
            else if (character=='n')
            {
                if (next()=='u')
                {
                    if (next()=='l')
                    {
                        if (next()=='l')
                        {
                            return null;
                        }
                    }
                }
            }
            throw new Exception("Boolean value or null expected at "+getPosition());
        }

        public boolean getPrimitiveBoolean() throws Exception
        {
            char character=nextNonWhiteSpaceCharacter();
            if (character=='t')
            {
                if (next()=='r')
                {
                    if (next()=='u')
                    {
                        if (next()=='e')
                        {
                            return true;
                        }
                    }
                }
            }
            else if (character=='f')
            {
                if (next()=='a')
                {
                    if (next()=='l')
                    {
                        if (next()=='s')
                        {
                            if (next()=='e')
                            {
                                return false;
                            }
                        }
                    }
                }
            }
            throw new Exception("Boolean value expected at "+getPosition());
        }

        public String getValueText() throws Exception
        {
            char character=nextNonWhiteSpaceCharacter();
            int start=this.position-1;
            for (;;)
            {
                if (this.position>=this.text.length())
                {
                    return this.text.substring(start, this.position);
                }
                else if ((character==',')||(character=='}')||(character==']'))
                {
                    return this.text.substring(start, --this.position);
                }
                /*
                if (character<=0x20)
                {
                    if ((character==0x20)||(character==0x09)||(character==0x0A)||(character==0x0D))
                    {
                        this.last=0;
                        skipWhiteSpace();
                        break;
                    }
                }
                */
                character=next();
            }
        }

        public byte getPrimitiveByte() throws Exception
        {
            try
            {
                return Byte.parseByte(getValueText());
            }
            catch (Throwable t)
            {
                throw new Exception("byte value expected at "+getPosition());
            }
        }
        public char getPrimitiveCharacter() throws Exception
        {
            try
            {
                return (char)Integer.parseUnsignedInt(getValueText());
            }
            catch (Throwable t)
            {
                throw new Exception("char value expected at "+getPosition());
            }
        }
        public short getPrimitiveShort() throws Exception
        {
            try
            {
                return Short.parseShort(getValueText());
            }
            catch (Throwable t)
            {
                throw new Exception("short value expected at "+getPosition());
            }
        }
        public int getPrimitiveInteger() throws Exception
        {
            String text=getValueText();
            try
            {
                return Integer.parseInt(text);
            }
            catch (Throwable t)
            {
                throw new Exception("int value expected at "+getPosition()+", text="+text);
            }
        }
        public long getPrimitiveLong() throws Exception
        {
            try
            {
                return Long.parseLong(getValueText());
            }
            catch (Throwable t)
            {
                throw new Exception("long value expected at "+getPosition());
            }
        }
        public float getPrimitiveFloat() throws Exception
        {
            try
            {
                return Float.parseFloat(getValueText());
            }
            catch (Throwable t)
            {
                throw new Exception("float value expected at "+getPosition());
            }
        }
        public double getPrimitiveDouble() throws Exception
        {
            try
            {
                return Double.parseDouble(getValueText());
            }
            catch (Throwable t)
            {
                throw new Exception("double value expected at "+getPosition());
            }
        }
        
        //-------------
        public Byte getByte() throws Exception
        {
            try
            {
                String text=getValueText();
                if ("null".equals(text))
                {
                    return null;
                }
                return Byte.parseByte(text);
            }
            catch (Throwable t)
            {
                throw new Exception("Byte value expected at "+getPosition());
            }
        }
        public Character getCharacter() throws Exception
        {
            try
            {
                String text=getValueText();
                if ("null".equals(text))
                {
                    return null;
                }
                return (char)Integer.parseUnsignedInt(text);
            }
            catch (Throwable t)
            {
                throw new Exception("Character value expected at "+getPosition());
            }
        }
        public Short getShort() throws Exception
        {
            try
            {
                String text=getValueText();
                if ("null".equals(text))
                {
                    return null;
                }
                return Short.parseShort(text);
            }
            catch (Throwable t)
            {
                throw new Exception("Short value expected at "+getPosition());
            }
        }
        public Integer getInteger() throws Exception
        {
            try
            {
                String text=getValueText();
                if ("null".equals(text))
                {
                    return null;
                }
                return Integer.parseInt(text);
            }
            catch (Throwable t)
            {
                throw new Exception("Integer value expected at "+getPosition());
            }
        }
        public Long getLong() throws Exception
        {
            try
            {
                String text=getValueText();
                if ("null".equals(text))
                {
                    return null;
                }
                return Long.parseLong(text);
            }
            catch (Throwable t)
            {
                throw new Exception("Long value expected at "+getPosition());
            }
        }
        public Float getFloat() throws Exception
        {
            try
            {
                String text=getValueText();
                if ("null".equals(text))
                {
                    return null;
                }
                return Float.parseFloat(text);
            }
            catch (Throwable t)
            {
                throw new Exception("Float value expected at "+getPosition());
            }
        }
        public Double getDouble() throws Exception
        {
            try
            {
                String text=getValueText();
                if ("null".equals(text))
                {
                    return null;
                }
                return Double.parseDouble(text);
            }
            catch (Throwable t)
            {
                throw new Exception("Double value expected at "+getPosition());
            }
        }
        
    }
    
    static abstract class Reader
    {
        abstract Object read(Parser parser,Class<?> type) throws Throwable;
    }
    static class PrimitiveBooleanReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getPrimitiveBoolean();
        }
    }
    static class PrimitiveIntegerReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getPrimitiveInteger();
        }
    }
    static class PrimitiveLongReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getPrimitiveLong();
        }
    }
    static class PrimitiveFloatReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getPrimitiveFloat();
        }
    }
    static class PrimitiveDoubleReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getPrimitiveDouble();
        }
    }
    static class PrimitiveByteReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getPrimitiveByte();
        }
    }
    static class PrimitiveCharacterReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getPrimitiveCharacter();
        }
    }
    static class PrimitiveShortReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getPrimitiveShort();
        }
    }
    static class StringReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getString();
        }
    }
    static class BooleanReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getBoolean();
        }
    }
    static class IntegerReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getInteger();
        }
    }
    static class LongReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getLong();
        }
    }
    static class FloatReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getFloat();
        }
    }
    static class DoubleReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getDouble();
        }
    }
    static class ByteReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getByte();
        }
    }
    static class CharacterReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getCharacter();
        }
    }
    static class ShortReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return parser.getShort();
        }
    }
    static class EnumReader extends Reader
    {
        @SuppressWarnings("unchecked")
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            String value=parser.getString();
            return Enum.valueOf((Class<Enum>) type, value);
        }
    }
    static class BigDecimalReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            return new BigDecimal(parser.getValueText()); 
        }
    }
    static class ArrayReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            ArrayList<Object> list=new ArrayList<>();
            if (parser.isNonEmptyArray())
            {
                Class<?> componentType=type.getComponentType();
                Reader reader=getReader(componentType);
                for (;;)
                {
                    list.add(reader.read(parser, componentType));
                    if (parser.isEndOfArray())
                    {
                        break;
                    }
                }
                Object array = Array.newInstance(componentType, list.size());
                for (int i = 0; i < list.size(); i++)
                {
                    Array.set(array, i, list.get(i));
                }
                return array;
            }
            return null;
        }
    }
    static class ObjectReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type) throws Throwable
        {
            ClassReader reader=getFieldReaders(type);
            Object object=reader.newInstance();
            if (parser.isNonEmptyElements())
            {
                for (;;)
                {
                    String name=parser.getName();
                    if (name==null)
                    {
                        break;
                    }
                    FieldReader fieldReader=reader.getFieldReader(name);
                    fieldReader.read(parser, object);
                    if (parser.isEndOfElements())
                    {
                        break;
                    }
                }
            }
            return object;
        }
    }
    

    static class FieldReader
    {
        final Field field;
        final Reader reader;

        FieldReader(Field field,Reader reader)
        {
            this.reader=reader;
            this.field = field;
        }
        void read(Parser parser,Object object) throws Throwable
        {
            field.set(object, reader.read(parser,this.field.getType()));
        }
    }

    final static private HashMap<String, ClassReader> CLASS_READERS=new HashMap<>();
    final static private HashMap<String, Reader> READERS=new HashMap<>();
    
    static Reader getReader(Class<?> type)
    {
        Reader reader;
        synchronized(READERS)
        {
            reader=READERS.get(type.getName());
        }
        if (reader!=null)
        {
            return reader;
        }
        if (type.isPrimitive())
        {
            if (type == boolean.class)
            {
                reader=new PrimitiveBooleanReader();
            }
            else if (type == int.class)
            {
                reader=new PrimitiveIntegerReader();
            }
            else if (type == long.class)
            {
                reader=new PrimitiveLongReader();
            }
            else if (type == float.class)
            {
                reader=new PrimitiveFloatReader();
            }
            else if (type == double.class)
            {
                reader=new PrimitiveDoubleReader();
            }
            else if (type == byte.class)
            {
                reader=new PrimitiveByteReader();
            }
            else if (type == char.class)
            {
                reader=new PrimitiveCharacterReader();
            }
            else if (type == short.class)
            {
                reader=new PrimitiveShortReader();
            }
        }
        else if (type == String.class)
        {
            reader=new StringReader();
        }
        else if (type.isArray())
        {
            reader=new ArrayReader();
        }
        else if (type == Boolean.class)
        {
            reader=new BooleanReader();
        }
        else if (type == Integer.class)
        {
            reader=new IntegerReader();
        }
        else if (type == Long.class)
        {
            reader=new LongReader();
        }
        else if (type == Float.class)
        {
            reader=new BooleanReader();
        }
        else if (type == Double.class)
        {
            reader=new DoubleReader();
        }
        else if (type == Byte.class)
        {
            reader=new ByteReader();
        }
        else if (type == Character.class)
        {
            reader=new CharacterReader();
        }
        else if (type == Short.class)
        {
            reader=new ShortReader();
        }
        else if (type.isEnum())
        {
            reader=new EnumReader();
        }
        else if (type==java.lang.Enum.class)
        {
            reader=new EnumReader();
        }
        else if (type==BigDecimal.class)
        {
            reader=new BigDecimalReader();
        }
        else
        {
            reader=new ObjectReader();
        }
        synchronized(READERS)
        {
            READERS.put(type.getName(), reader);
        }
        return reader;
    }


    static class ClassReader
    {
        final Map<String,FieldReader> fieldReaders;
        final Class<?> type;
        final Constructor constructor;
        final Object[] parameters;

        public ClassReader(Class<?> type,Map<String,FieldReader> fieldReaders)
        {
            this.type=type;
            this.fieldReaders=fieldReaders;
            Object[] parameters=null;
            Constructor<?> shortestConstructor = null;

            Constructor<?>[] constructors = type.getDeclaredConstructors();
            if (constructors.length>0)
            {
                int shortest = Integer.MAX_VALUE;
                for (Constructor<?> constructor: constructors)
                {
                    if (constructor.getParameterCount() < shortest)
                    {
                        shortest = constructor.getParameterCount();
                        shortestConstructor = constructor;
                    }
                }
                parameters = new Object[shortestConstructor.getParameterCount()];
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
                }
                shortestConstructor.setAccessible(true);
            }
            this.constructor=shortestConstructor;
            this.parameters=parameters;
        }
        
        public Object newInstance() throws Throwable
        {
            if (this.constructor==null)
            {
                return this.type.newInstance();
            }
            return this.constructor.newInstance(this.parameters);
        }
        public FieldReader getFieldReader(String name)
        {
            return this.fieldReaders.get(name);
        }
    }

    
    
    static private ClassReader getFieldReaders(Class<?> type) throws Exception
    {
        ClassReader classReader; 
        synchronized (CLASS_READERS)
        {
            classReader = CLASS_READERS.get(type.getName());
        }
        if (classReader == null)
        {
            Map<String,FieldReader> fieldReaders= new HashMap<>();
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
                    if (fieldReaders.containsKey(field.getName()) == false)
                    {
                        field.setAccessible(true);
                        Reader reader=getReader(field.getType());
                        fieldReaders.put(field.getName(), new FieldReader(field,reader));
                    }
                }
            }
            classReader=new ClassReader(type,fieldReaders);
            synchronized (CLASS_READERS)
            {
                CLASS_READERS.put(type.getName(), classReader);
            }
        }
        return classReader;
    }

    static public <OBJECT> OBJECT read(String text,Class<OBJECT> type) throws Throwable
    {
        Parser parser=new Parser(text);
        return (OBJECT)read(new Parser(text),type);
    }
    
    static Object read(Parser parser,Class<?> type) throws Throwable
    {
        Reader reader=getReader(type);
        return reader.read(parser,type);
    }
}
