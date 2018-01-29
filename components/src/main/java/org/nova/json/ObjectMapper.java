package org.nova.json;

import java.io.OutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ObjectMapper
{
    final static String NULL="null";
    final static private HashMap<String, FieldWriter[]> FIELD_WRITERS=new HashMap<>();
    final static private HashMap<String, Writer> WRITERS=new HashMap<>();

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
            FieldWriter[] fieldWriters = getFieldWriters(object.getClass());
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
            this.writer=getWriter(componentType);
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
                    getWriter(element.getClass()).write(sb, element);
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

    public static String write(Object object) throws Throwable
    {
        return writeObjectAsString(object);
    }
    public static void write(OutputStream outputStream,Object object) throws Throwable
    {
        writeObject(outputStream,object);
    }

    static public String writeObjectAsString(Object object) throws Throwable
    {
        if (object==null)
        {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Writer writer=getWriter(object.getClass());
        writer.write(sb, object);
        return sb.toString();
    }
    
    static public void writeObject(OutputStream outputStream,Object object) throws Throwable
    {
        outputStream.write(writeObjectAsString(object).getBytes());
        
    }
    
    // ------ read

    
   
    
    
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
                throw new Exception("{ expected: "+getError());
            }
        }
        
        public String getName() throws Exception
        {
            char c=nextNonWhiteSpaceCharacter();
            if (c!='"')
            {
                throw new Exception("\" expected before name: "+getError());
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
                    throw new Exception(": expected after name: "+getError());
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
                        return sb.toString();
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
                                    throw new Exception("Invalid unicode escape: "+getError());
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
            throw new Exception("String or null expected: "+getError());
        }

        public void skip() throws Exception
        {
            char c=nextNonWhiteSpaceCharacter();
            this.position--;
            if (c=='"')
            {
                getString(); //we can optimize this by noticing that we don't need the computed string.
            }
            else if (c=='{')
            {
                if (isNonEmptyElements())
                {
                    for (;;)
                    {
                        if (getName()==null)
                        {
                            break;
                        }
                        skip();
                        if (isEndOfElements())
                        {
                            break;
                        }
                    }
                }
            }
            else if (c=='[')
            {
                if (isNonEmptyArray())
                {
                    for (;;)
                    {
                        skip();
                        if (isEndOfArray())
                        {
                            break;
                        }
                    }
                }
            }
            else
            {
                getValueText();
            }
        }
        private char next()
        {
            try
            {
                return this.text.charAt(this.position++);
            }
            catch (StringIndexOutOfBoundsException ex)
            {
                return 0;
            }
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
            throw new Exception(", or } expected: "+getError());
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
            throw new Exception(", or ] expected: "+getError());
        }
        
        public boolean isNonEmptyElements() throws Exception
        {
            char c=nextNonWhiteSpaceCharacter();
            if (c!='{')
            {
                if (c=='n')
                {
                    expectRestOfNull();
                    return false;
                }
                throw new Exception("{ expected: "+getError());
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
                if (c=='n')
                {
                    expectRestOfNull();
                    return false;
                }
                throw new Exception("[ expected: "+getError());
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
            throw new Exception("null expected: "+getError());
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
            throw new Exception("Boolean value or null expected: "+getError());
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
            throw new Exception("Boolean value expected: "+getError());
        }

        public String getValueText() throws Exception
        {
            char character=nextNonWhiteSpaceCharacter();
            int start=this.position-1;
            for (;;)
            {
                if (this.position>this.text.length())
                {
                    String value=this.text.substring(start, this.position);
                    return value;
                }
                else if ((character==',')||(character=='}')||(character==']'))
                {
                    String value=this.text.substring(start, --this.position);
                    return value;
                }
                else if (character<=0x20)
                {
                    if ((character==0x20)||(character==0x09)||(character==0x0A)||(character==0x0D))
                    {
                        String value=this.text.substring(start, --this.position);
                        return value;
                    }
                }
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
                throw new Exception("byte value expected: "+getError());
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
                throw new Exception("char value expected: "+getError());
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
                throw new Exception("short value expected: "+getError());
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
                throw new Exception("int value expected: "+getError());
            }
        }
        
        int BEFORE=50;
        int AFTER=50;
        private String getError()
        {
            int start=this.position-BEFORE;
            if (start<0)
            {
                start=0;
            }
            int end=this.position+AFTER;
            if (end>=this.text.length())
            {
                end=this.text.length()-1;
            }
            String message="position="+this.position+", text="+text.substring(start, this.position)+"^"+this.text.substring(this.position, end);
            return message.replace("\n", "").replace("\r","");
        }
        
        public long getPrimitiveLong() throws Exception
        {
            String value=getValueText();
            try
            {
                return Long.parseLong(value);
            }
            catch (Throwable t)
            {
                throw new Exception("long value expected: "+getError());
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
                throw new Exception("float value expected: "+getError());
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
                throw new Exception("double value expected: "+getError());
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
                throw new Exception("Byte value expected: "+getError());
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
                throw new Exception("Character value expected: "+getError());
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
                throw new Exception("Short value expected: "+getError());
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
                throw new Exception("Integer value expected: "+getError());
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
                throw new Exception("Long value expected: "+getError());
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
                throw new Exception("Float value expected: "+getError());
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
                throw new Exception("Double value expected: "+getError());
            }
        }
        
    }
    
    static abstract class Reader
    {
        abstract Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable;
    }
    static class PrimitiveBooleanReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getPrimitiveBoolean();
        }
    }
    static class PrimitiveIntegerReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getPrimitiveInteger();
        }
    }
    static class PrimitiveLongReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getPrimitiveLong();
        }
    }
    static class PrimitiveFloatReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getPrimitiveFloat();
        }
    }
    static class PrimitiveDoubleReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getPrimitiveDouble();
        }
    }
    static class PrimitiveByteReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getPrimitiveByte();
        }
    }
    static class PrimitiveCharacterReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getPrimitiveCharacter();
        }
    }
    static class PrimitiveShortReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getPrimitiveShort();
        }
    }
    static class StringReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getString();
        }
    }
    static class BooleanReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getBoolean();
        }
    }
    static class IntegerReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getInteger();
        }
    }
    static class LongReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getLong();
        }
    }
    static class FloatReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getFloat();
        }
    }
    static class DoubleReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getDouble();
        }
    }
    static class ByteReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getByte();
        }
    }
    static class CharacterReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getCharacter();
        }
    }
    static class ShortReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return parser.getShort();
        }
    }
    static class EnumReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            String value=parser.getString();
            return Enum.valueOf((Class<Enum>) type, value);
        }
    }
    static class BigDecimalReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            return new BigDecimal(parser.getValueText()); 
        }
    }
    static class ArrayReader extends Reader
    {
        @Override
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
        {
            ArrayList<Object> list=new ArrayList<>();
            if (parser.isNonEmptyArray())
            {
                Class<?> componentType=type.getComponentType();
                Reader reader=getReader(componentType);
                for (;;)
                {
                    list.add(reader.read(parser, componentType,ignoreUnknownFields));
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
        Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
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
                    if (fieldReader==null)
                    {
                        if (ignoreUnknownFields)
                        {
                            parser.skip();
                        }
                        else
                        {
                            throw new Exception("Unknown name "+name+parser.getError());
                        }
                    }
                    else
                    {
                        fieldReader.read(parser, object,ignoreUnknownFields);
                    }
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
        void read(Parser parser,Object object,boolean ignoreUnknownFields) throws Throwable
        {
            field.set(object, reader.read(parser,this.field.getType(),ignoreUnknownFields));
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
        if ((text==null)||(text.length()==0))
        {
            return null;
        }
        return read(text,type,true);
    }
    
    static public <OBJECT> OBJECT read(String text,Class<OBJECT> type,boolean ignoreUnknownFields) throws Throwable
    {
        Parser parser=new Parser(text);
        return (OBJECT)read(new Parser(text),type,ignoreUnknownFields);
    }

    static Object read(Parser parser,Class<?> type,boolean ignoreUnknownFields) throws Throwable
    {
        Reader reader=getReader(type);
        return reader.read(parser,type,ignoreUnknownFields);
    }
    
    
    
}
