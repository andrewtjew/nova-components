package org.nova.utils;

import java.util.ArrayList;
import java.util.List;

public class TypeUtils
{
    static public <OBJECT> OBJECT replaceIfNull(OBJECT value,OBJECT nullReplacementValue)
    {
        return (value==null)?nullReplacementValue:value;
    }
    static public boolean replaceIfNull(Boolean value,boolean nullReplacementValue)
    {
        return (value==null)?nullReplacementValue:value;
    }
    static public byte replaceIfNull(Byte value,byte nullReplacementValue)
    {
        return (value==null)?nullReplacementValue:value;
    }
    static public char replaceIfNull(Character value,char nullReplacementValue)
    {
        return (value==null)?nullReplacementValue:value;
    }
    static public short replaceIfNull(Short value,short nullReplacementValue)
    {
        return (value==null)?nullReplacementValue:value;
    }
    static public int replaceIfNull(Integer value,int nullReplacementValue)
    {
        return (value==null)?nullReplacementValue:value;
    }
    static public long replaceNull(Long value,long nullReplacementValue)
    {
        return (value==null)?nullReplacementValue:value;
    }
    static public float replaceIfNull(Float value,float nullReplacementValue)
    {
        return (value==null)?nullReplacementValue:value;
    }
    static public double replaceIfNull(Double value,double nullReplacementValue)
    {
        return (value==null)?nullReplacementValue:value;
    }
    
    static public List<Integer> intArrayToList(int[] values)
    {
        ArrayList<Integer> list=new ArrayList<>();
        for (int value:values)
        {
            list.add(value);
        }
        return list;
    }
    
    static public List<Long> longArrayToList(long[] values)
    {
        ArrayList<Long> list=new ArrayList<>();
        for (long value:values)
        {
            list.add(value);
        }
        return list;
    }
    static public long[] longListToArray(List<Long> list)
    {
        long[] array=new long[list.size()];
        for (int i=0;i<array.length;i++)
        {
            array[i]=list.get(i);
        }
        return array;
    }
    static public byte[] byteListToArray(List<Byte> list)
    {
        byte[] array=new byte[list.size()];
        for (int i=0;i<array.length;i++)
        {
            array[i]=list.get(i);
        }
        return array;
    }
    static public int[] intListToArray(List<Integer> list)
    {
        int[] array=new int[list.size()];
        for (int i=0;i<array.length;i++)
        {
            array[i]=list.get(i);
        }
        return array;
    }
    static public short[] shortListToArray(List<Short> list)
    {
        short[] array=new short[list.size()];
        for (int i=0;i<array.length;i++)
        {
            array[i]=list.get(i);
        }
        return array;
    }
    static public double[] doubleListToArray(List<Double> list)
    {
        double[] array=new double[list.size()];
        for (int i=0;i<array.length;i++)
        {
            array[i]=list.get(i);
        }
        return array;
    }

    public static int getAncestorDistance(Class<?> derived,Class<?> superClass)
    {
        int distance=0;
        while (derived!=superClass)
        {
            derived=derived.getSuperclass();
            if (derived==null)
            {
                return -1;
            }
            distance++;
        }
        return distance;
    }

    public static boolean isDerivedFrom(Class<?> type,Class<?> from)
    {
        while (type!=from)
        {
            type=type.getSuperclass();
            if (type==null)
            {
                return false;
            }
        }
        return true;
    }
    public static <T> void addToList(List<T> list,T[] array)
    {
        for (T item:array)
        {
            list.add(item);
        }
    }
    
    static public String bigEndianByteArrayToHexString(byte[] bytes)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) 
        {
            byte value=bytes[i];
            int high=(value&0xf0)>>4;
            if (high<10)
            {
                sb.append((char)(high+'0'));
            }
            else
            {
                sb.append((char)(high-10+'a'));
            }

            int low=value&0xf;
            if (low<10)
            {
                sb.append((char)(low+'0'));
            }
            else
            {
                sb.append((char)(low-10+'a'));
            }
        }
        return sb.toString();        
    }
    
    public static int bigEndianBytesToInt(byte[] bytes,int index) 
    {
        return ((bytes[index+0]&0xFF)<<24)|((bytes[index+1]&0xFF)<<16)|((bytes[index+2]&0xFF)<<8)|(bytes[index+3]&0xFF);
        
    }
    public static int bigEndianBytesToInt(byte[] bytes) 
    {
        return bigEndianBytesToInt(bytes,0);
    }
    public static int bigEndianIntToBytes(int value,byte[] bytes,int index) 
    {
        bytes[index++]=(byte)((value>>24)&0xFF);
        bytes[index++]=(byte)((value>>16)&0xFF);
        bytes[index++]=(byte)((value>>8)&0xFF);
        bytes[index++]=(byte)(value&0xFF);
        return index;
    }
    public static byte[] bigEndianIntToBytes(int value) 
    {
        byte[] bytes=new byte[4];
        bytes[0]=(byte)((value>>24)&0xFF);
        bytes[1]=(byte)((value>>16)&0xFF);
        bytes[2]=(byte)((value>>8)&0xFF);
        bytes[3]=(byte)(value&0xFF);
        return bytes;
    }
    public static int bigEndianLongToBytes(long value,byte[] bytes,int index) 
    {
        bytes[index++]=(byte)((value>>56)&0xFF);
        bytes[index++]=(byte)((value>>48)&0xFF);
        bytes[index++]=(byte)((value>>40)&0xFF);
        bytes[index++]=(byte)((value>>32)&0xFF);
        bytes[index++]=(byte)((value>>24)&0xFF);
        bytes[index++]=(byte)((value>>16)&0xFF);
        bytes[index++]=(byte)((value>>8)&0xFF);
        bytes[index++]=(byte)(value&0xFF);
        return index;
    }
    public static byte[] bigEndianLongToBytes(long value) 
    {
        byte[] bytes=new byte[8];
        bytes[0]=(byte)((value>>56)&0xFF);
        bytes[1]=(byte)((value>>48)&0xFF);
        bytes[2]=(byte)((value>>40)&0xFF);
        bytes[3]=(byte)((value>>32)&0xFF);
        bytes[4]=(byte)((value>>24)&0xFF);
        bytes[5]=(byte)((value>>16)&0xFF);
        bytes[6]=(byte)((value>>8)&0xFF);
        bytes[7]=(byte)(value&0xFF);
        return bytes;
    }
    public static long bigEndianBytesToLong(byte[] bytes,int index) 
    {
        return ((bytes[index+0]&0xFFL)<<56)|((bytes[index+1]&0xFFL)<<48)|((bytes[index+2]&0xFFL)<<40)|((bytes[index+3]&0xFFL)<<32)| 
                ((bytes[index+4]&0xFFL)<<24)|((bytes[index+5]&0xFFL)<<16)|((bytes[index+6]&0xFFL)<<8)|(bytes[index+7]&0xFFL);
    }
    public static long bigEndianBytesToLong(byte[] bytes) 
    {
        return bigEndianBytesToLong(bytes,0);
    }
    
    static public boolean isTrue(Boolean value)
    {
        if (value==null)
        {
            return false;
        }
        return value;
    }
    
    public static long parseLong(String value,long defaultValue)
    {
        if (value==null)
        {
            return defaultValue;
        }
        return Long.parseLong(value);
    }
}



