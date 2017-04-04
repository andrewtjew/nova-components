package org.nova.json;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;

import org.nova.annotations.Description;
import org.nova.core.Utils;

public class ObjectExample
{
	static public void write(OutputStream outputStream, Class<?> contentType) throws IOException
	{
		new ObjectExample().writeExample(outputStream, contentType);
	}
	
	private final HashSet<String> shownClasses=new HashSet<>();
	

	private StringBuilder writeIndent(StringBuilder sb,int indentLevel)
	{
		for (int i=0;i<indentLevel;i++)
		{
			sb.append("  ");
		}
		return sb;
	}

	private int integerValue=2;
	private int fractionValue=1;
	private void writeExample(StringBuilder sb,Class<?> type,int indentLevel)
	{
		if ((type==Boolean.class)||(type==boolean.class))
		{
			sb.append("true");
		}
		else if ((type==Integer.class)||(type==int.class)||(type==Short.class)||(type==short.class)||(type==Long.class)||(type==long.class)||(type==byte.class)||(type==Byte.class))
		{
			sb.append(integerValue++);
		}
		else if ((type==Float.class)||(type==float.class)||(type==Double.class)||(type==double.class))
		{
			sb.append(integerValue+++"."+fractionValue++);
		}
		else if (type==String.class)
		{
			sb.append("\""+integerValue+++" bottles\"");
		}
		else if (type.isArray())
		{
            if (this.shownClasses.contains(type.getComponentType().getName())==false)
            {
    			sb.append("[");
    			writeExample(sb,type.getComponentType(),indentLevel+1);
    			sb.append(",");
    			writeExample(sb,type.getComponentType(),indentLevel+1);
    			sb.append("\r\n");
    			writeIndent(sb, indentLevel).append("]");
            }
		}
		else
		{
			this.shownClasses.add(type.getName());
			sb.append("{\r\n");
			boolean commaNeeded=false;
			for (Field field: type.getDeclaredFields())
			{
                Class<?> fieldType=field.getType();
                if (this.shownClasses.contains(fieldType.getName())==false)
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
    				if (commaNeeded)
    				{
    					sb.append(",\r\n");
    				}
    				else
    				{
    					commaNeeded=true;
    				}
	                writeIndent(sb, indentLevel+1).append("\""+field.getName()+"\": ");
	                writeExample(sb,fieldType,indentLevel+1);
		        }
			}
			sb.append("\r\n");
			writeIndent(sb, indentLevel).append("}");
		}
	}
	
	
	public void writeExample(OutputStream outputStream, Class<?> contentType) throws IOException
	{
		StringBuilder sb=new StringBuilder();
		writeExample(sb,contentType,0);
		outputStream.write(sb.toString().getBytes(StandardCharsets.UTF_8));
	}
	

}
