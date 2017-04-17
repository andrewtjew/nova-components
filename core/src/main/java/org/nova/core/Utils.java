package org.nova.core;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

public class Utils
{
    static public List<Integer> toList(int[] values)
    {
        ArrayList<Integer> list=new ArrayList<>();
        for (int value:values)
        {
            list.add(value);
        }
        return list;
    }
    static public List<Long> toList(long[] values)
    {
        ArrayList<Long> list=new ArrayList<>();
        for (long value:values)
        {
            list.add(value);
        }
        return list;
    }
    
    static public int compare(String a,String b)
    {
        if ((a==null)&&(b==null))
        {
            return 0;
        }
        if (a==null)
        {
            return -1;
        }
        return a.compareTo(b);
    }
    
    public static String[] splitUsingWhiteSpace(String string)
    {
        int count=0;
        for (int i=0;i<string.length();i++)
        {
            if (Character.isWhitespace(string.charAt(i))==false)
            {
                count++;
                for (i++;i<string.length();i++)
                {
                    if (Character.isWhitespace(string.charAt(i)))
                    {
                        break;
                    }
                }
            }
        }
        String[] fragments=new String[count];
        int index=0;
        for (int i=0;i<string.length();i++)
        {
            if (Character.isWhitespace(string.charAt(i))==false)
            {
                int begin=i;
                for (i++;i<string.length();i++)
                {
                    if (Character.isWhitespace(string.charAt(i)))
                    {
                        fragments[index++]=string.substring(begin,i);
                        break;
                    }
                }
            }
        }
        return fragments;
    }
    
    public static String[] split(String string,char splitCharacter)
	{
		int count=1;
		for (int i=0;i<string.length();i++)
		{
			if (string.charAt(i)==splitCharacter)
			{
				count++;
			}
		}
		String[] fragments=new String[count];
		int begin=0;
		int index=0;
		for (int i=0;i<string.length();i++)
		{
			if (string.charAt(i)==splitCharacter)
			{
				fragments[index++]=string.substring(begin,i);
				begin=i+1;
			}
		}
		fragments[index]=string.substring(begin,string.length());
		return fragments;
	}
    public static String combine(Iterable<?> iterable,String seperator)
	{
    	StringBuilder sb=new StringBuilder();
    	for (Object item:iterable)
    	{
    		if (sb.length()>0)
    		{
    			sb.append(seperator);
    		}
    		sb.append(item);
    	}
    	return sb.toString();
	}
	
	public static String readTextFile(String fileName,String charset) throws Exception
	{
		return readTextFile(fileName, Charset.forName(charset));
	}
	
	public static String readTextFile(String fileName) throws Exception
	{
		return readTextFile(fileName, StandardCharsets.UTF_8);
	}
	
	
	public static String readTextFile(String fileName,Charset charset) throws Exception
	{
		return new String(readFile(fileName),charset);
	}

	public static String toNativePath(String fileName)
	{
	    return fileName.replace('\\', File.separatorChar).replace('/',File.separatorChar);
	}
	
	public static byte[] readFile(String fileName) throws Exception
	{
	    fileName=toNativePath(fileName);
		File file=new File(fileName);
		if (file.isDirectory()==true)
		{
			throw new Exception("File is a directory. Filename="+fileName);
		}
		
		long length=file.length();
		if (length>Integer.MAX_VALUE)
		{
			throw new Exception("File too big. Filename="+fileName);
		}
				
		try (FileInputStream stream=new FileInputStream(file))
		{
			byte[] bytes=new byte[(int)file.length()];
			stream.read(bytes);
			return bytes;
		}
	}
	
	public static String nowToLocalDateTimeString()
	{
		return LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}

	public static LocalDateTime millisToLocalDateTime(long millis)
	{
		return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), TimeZone.getDefault().toZoneId());
	}

	public static String millisToLocalDateTimeString(long millis)
	{
		return millisToLocalDateTime(millis).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
	}
	public static String millisToDateTimeString(long dateTime,String defaultValue)
	{
		if (dateTime<=0)
		{
			return defaultValue;
		}
		return millisToLocalDateTimeString(dateTime);
	}

    static public DateTimeFormatter LOCALDATETIME_FILENAME_FORMATTER=DateTimeFormatter.ofPattern("yyyy_MM_dd_HH_mm_ss_SSS"); 

    public static String millisToLocalDateTimeFileName(long millis)
	{
		return millisToLocalDateTime(millis).format(LOCALDATETIME_FILENAME_FORMATTER);
	}
    
    

	public static String millisToDurationString(long millis)
	{
		StringBuilder sb=new StringBuilder();
		long days=millis/(1000*3600*24);
		long hours=(millis-days*1000*3600*24)/(1000*3600);
		long minutes=(millis-days*1000*3600*24-hours*1000*3600)/(1000*60);
		long seconds=(millis-days*1000*3600*24-hours*1000*3600-minutes*1000*60)/1000;
		long milliseconds=(millis-days*1000*3600*24-hours*1000*3600-minutes*1000*60-seconds*1000);
		if (days<1)
		{
			return hours+":"+minutes+":"+seconds+"."+milliseconds;
		}
		return days+" days "+hours+":"+minutes+":"+seconds+"."+milliseconds;
		
	}

	public static String nanosToDurationString(long nanos)
	{
		return millisToDurationString(nanos/1000000);
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
	@SuppressWarnings("unchecked")
	public static <T> T[] toArray(List<?> list,T[] array)
	{
		for (int index=0;index<array.length;index++)
		{
			array[index]=(T)list.get(index);
		}
		return array;
	}
	public static int simpleHash(byte[] array)
	{
		int hash=0x811c9dc5;
		for (byte item:array)
		{
			hash=(hash^item)*0x01000193;
		}
		return hash;
	}
	
	public static String getLocalHostName() throws Exception
	{
		return java.net.InetAddress.getLocalHost().getHostName();
	}
	
	public static String toString(StackTraceElement[] elements) 
	{
		return toString(elements,0);
	}

	public static String toString(StackTraceElement[] elements,int start) 
	{
		StringBuffer sb=new StringBuffer();
		for (int i=start;i<elements.length;i++)
		{
			sb.append(elements[i].toString());
			sb.append("\r\n");
		}
		return sb.toString();
	}
	private static String[] HTML_ESCAPES=new String[]{"&", "\"", "<", ">"};
	private static String[] HTML_ESCAPEDS=new String[]{"&amp;", "&quot;", "&lt;", "&gt;"};
	public static String escapeHtml(String text)
	{
		StringBuilder sb=new StringBuilder();
		int start=0;
		for (int i=0;i<text.length();i++)
		{
			char c=text.charAt(i);
			if (c=='&')
			{
				if (start<i)
				{
					sb.append(text.substring(start,i));
					sb.append("&amp;");
				}
				start=i+1;
			}
			else if (c=='"')
			{
				if (start<i)
				{
					sb.append(text.substring(start,i));
					sb.append("&quot;");
				}
				start=i+1;
			}
			else if (c=='<')
			{
				if (start<i)
				{
					sb.append(text.substring(start,i));
					sb.append("&lt;");
				}
				start=i+1;
			}
			else if (c=='>')
			{
				if (start<i)
				{
					sb.append(text.substring(start,i));
					sb.append("&gt;");
				}
				start=i+1;
			}

		}
		if (start<text.length())
		{
			sb.append(text.substring(start,text.length()));
		}
		return sb.toString();
	}
	
	
	public static String getStrackTraceAsString(Throwable t) throws Exception
	{
		
		try (ByteArrayOutputStream outputStream=new ByteArrayOutputStream())
		{
			try (PrintWriter writer=new PrintWriter(outputStream)) 
			{
				t.printStackTrace(writer);
				writer.flush();
			}
			outputStream.flush();
			return new String(outputStream.toByteArray());
		}
			
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
	public static String toString(Throwable t)
	{
		try (ByteArrayOutputStream outputStream=new ByteArrayOutputStream())
		{
			try (PrintStream printStream=new PrintStream(outputStream))
			{
				t.printStackTrace(printStream);
				printStream.flush();
				return new String(outputStream.toByteArray());
			}
		}
		catch (IOException e)
		{
			return null;
		}
	}
	public static String readString(InputStream stream,int bufferSize,Charset charset) throws IOException
	{
		int count=0;
		byte[] buffer=new byte[bufferSize];
		byte[] stringBuffer=new byte[bufferSize];
		for (int bytesRead=stream.read(buffer);bytesRead>0;bytesRead=stream.read(buffer))
		{
			if (bytesRead+count>stringBuffer.length)
			{
				byte[] newStringBuffer=new byte[stringBuffer.length*2];
				System.arraycopy(stringBuffer, 0, newStringBuffer, 0, count);
				stringBuffer=newStringBuffer;
			}
			System.arraycopy(buffer,0,stringBuffer,count,bytesRead);
			count+=bytesRead;
		}
		return new String(stringBuffer,0,count,charset);
	}

	public static String readString(InputStream stream,Charset charset) throws IOException
	{
		return readString(stream,4096,charset);
	}

	public static String readString(InputStream stream) throws IOException
	{
		return readString(stream,StandardCharsets.UTF_8);
	}

	public static void writeTextFile(String fileName,String text) throws Exception
	{
		writeTextFile(fileName,text,StandardCharsets.UTF_8);
	}
	public static void writeTextFile(String fileName,String text,Charset charset) throws Exception
	{
		File file=new File(fileName);
		if (file.isDirectory()==true)
		{
			throw new Exception("File is a directory. Filename="+fileName);
		}
		
		try (OutputStream stream=new FileOutputStream(file))
		{
			stream.write(text.getBytes(charset));
		}
	}
	public static int occurs(String string,String pattern)
	{
		int occurs=0;
		for (int index=string.indexOf(pattern);index>=0;index=string.indexOf(pattern,index+pattern.length()))
		{
			occurs++;
		}
		return occurs;
	}
	
}

