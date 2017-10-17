package org.nova.configuration;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.HashMap;

import org.nova.annotations.Alias;
import org.nova.core.Utils;
import org.nova.json.ObjectMapper;

public class Configuration
{
	
	final private HashMap<String, ConfigurationItem> map;

	public Configuration() 
	{
		this.map = new HashMap<>();
	}

	public void addArgs(String[] args) 
	{
		for (String arg:args)
		{
			String[] parts=Utils.split(arg, '=');
			if (parts.length==2)
			{
				this.map.put(parts[0],new ConfigurationItem(parts[0], parts[1], ConfigurationSource.ARGS,null, null));
			}
		}
	}

	public void remove(String key)
	{
	    synchronized(this)
	    {
	        this.map.remove(key);
	    }
	}
	public void add(ConfigurationItem configurationItem)
	{
		synchronized(this)
		{
			this.map.put(configurationItem.getName(),configurationItem);
		}
	}
    public void add(String name,String value,String description)
    {
        synchronized(this)
        {
            this.map.put(name,new ConfigurationItem(name, value, ConfigurationSource.CODE, getSource(Thread.currentThread().getStackTrace()[2]), description));
        }
    }
    public void add(String name,String value)
    {
        add(name,value,null);
    }
	public ConfigurationItem getConfigurationItem(String name)
	{
		synchronized (this)
		{
			return this.map.get(name);
		}
	}

	public static String getSource(StackTraceElement element)
	{
		return element.getFileName()+"."+element.getLineNumber();
	}

	public ConfigurationItem getConfigurationItem(String name,String defaultValue)
	{
		synchronized (this)
		{
			ConfigurationItem item=this.map.get(name);
			if (item==null)
			{
				item=new ConfigurationItem(name,defaultValue, ConfigurationSource.DEFAULT, getSource(Thread.currentThread().getStackTrace()[4]),null);
				{
					this.map.put(name, item);
				}
			}
			return item;
		}
	}

	public String getValue(String name)
	{
		ConfigurationItem item=getConfigurationItem(name);
		if (item==null)
		{
			return null;
		}
		return item.getValue();
	}
	public String getValue(String name,String defaultValue)
	{
		ConfigurationItem item=getConfigurationItem(name,defaultValue);
		if (item==null)
		{
			return null;
		}
		return item.getValue();
	}
	
	public double getDoubleValue(String name)
	{
		String value=getValue(name);
		return Double.parseDouble(value);
	}
	public double getDoubleValue(String name,double defaultValue)
	{
		String value=getValue(name);
		if (value==null)
		{
		    return defaultValue;
		}
		return Double.parseDouble(value);
	}
    public Double getNullableDoubleValue(String name)
    {
        String value=getValue(name);
        if (value==null)
        {
            return null;
        }
        return Double.parseDouble(value);
    }
    public Double getNulllableDoubleValue(String name,Double defaultValue)
    {
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
        return Double.parseDouble(value);
    }
	public float getFloatValue(String name)
	{
		String value=getValue(name);
		return Float.parseFloat(value);
	}
	public float getFloatValue(String name,float defaultValue)
	{
		String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
		return Float.parseFloat(value);
	}
    public Float getNullableFloatValue(String name)
    {
        String value=getValue(name);
        if (value==null)
        {
            return null;
        }
        return Float.parseFloat(value);
    }
    public Float getNullableFloatValue(String name,Float defaultValue)
    {
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
        return Float.parseFloat(value);
    }
    
    private long parseLong(String value)
    {
        value=value.trim();
        long scale=1;
        if (value.endsWith("k")||value.endsWith("K"))
        {
            scale=1000L;
            value=value.substring(0, value.length()-1);
        }        
        else if (value.endsWith("m")||value.endsWith("M"))
        {
            scale=1000_000L;
            value=value.substring(0, value.length()-1);
        }        
        else if (value.endsWith("g")||value.endsWith("G"))
        {
            scale=1000_000_000L;
            value=value.substring(0, value.length()-1);
        }        
        else if (value.endsWith("t")||value.endsWith("T"))
        {
            scale=1000_000_000_000L;
            value=value.substring(0, value.length()-1);
        }        
        else if (value.endsWith("p")||value.endsWith("B"))
        {
            scale=1000_000_000_000_000L;
            value=value.substring(0, value.length()-1);
        }        
        long number=Long.parseLong(value);
        long scaledNumber=number*scale;
        if (scaledNumber/scale!=number)
        {
            throw new NumberFormatException("Overflow. value="+value);
        }
        return scaledNumber;
        
    }
    private int parseInt(String value)
    {
        value=value.trim();
        int scale=1;
        if (value.endsWith("k")||value.endsWith("K"))
        {
            scale=1000;
            value=value.substring(0, value.length()-1);           
        }        
        else if (value.endsWith("m")||value.endsWith("M"))
        {
            scale=1000_000;
            value=value.substring(0, value.length()-1);
        }        
        else if (value.endsWith("g")||value.endsWith("G"))
        {
            scale=1000_000_000;
            value=value.substring(0, value.length()-1);
        }        
        int number=Integer.parseInt(value);
        int scaledNumber=number*scale;
        if (scaledNumber/scale!=number)
        {
            throw new NumberFormatException("Overflow. value="+value);
        }
        return scaledNumber;
        
    }
    
    
    
	public long getLongValue(String name)
	{
		String value=getValue(name);
		return parseLong(value);
	}

	
	public long getLongValue(String name,long defaultValue)
	{
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
		return parseLong(value);
	}
    public Long getNullableLongValue(String name)
    {
        String value=getValue(name);
        if (value==null)
        {
            return null;
        }
        return parseLong(value);
    }

    public Long getNullableLongValue(String name,Long defaultValue)
    {
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
        return parseLong(value);
    }
	
    public int getIntegerValue(String name)
    {
        String value=getValue(name);
        return parseInt(value);
    }

    public int getIntegerValue(String name,int defaultValue)
    {
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
        return parseInt(value);
    }
    public Integer getNullableIntegerValue(String name)
    {
        String value=getValue(name);
        if (value==null)
        {
            return null;
        }
        return parseInt(value);
    }

    public Integer getNullableIntegerValue(String name,Integer defaultValue)
    {
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
        return parseInt(value);
    }
    
	public short getShortValue(String name)
	{
		String value=getValue(name);
		return Short.parseShort(value);
	}

	public short getShortValue(String name,short defaultValue)
	{
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
		return Short.parseShort(value);
	}
    public Short getNullableShortValue(String name)
    {
        String value=getValue(name);
        if (value==null)
        {
            return null;
        }
        return Short.parseShort(value);
    }

    public Short getNullableShortValue(String name,Short defaultValue)
    {
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
        return Short.parseShort(value);
    }
	
    public byte getByteValue(String name)
    {
        String value=getValue(name);
        return Byte.parseByte(value);
    }

    public byte getByteValue(String name,byte defaultValue)
    {
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
        return Byte.parseByte(value);
    }
    public Byte getNullableByteValue(String name)
    {
        String value=getValue(name);
        if (value==null)
        {
            return null;
        }
        return Byte.parseByte(value);
    }

    public Byte getNullableByteValue(String name,Byte defaultValue)
    {
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
        return Byte.parseByte(value);
    }
    
	public boolean getBooleanValue(String name)
	{
		String value=getValue(name);
		return Boolean.parseBoolean(value);
	}

	public boolean getBooleanValue(String name,boolean defaultValue)
	{
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
		return Boolean.parseBoolean(value);
	}
    public Boolean getNullableBooleanValue(String name)
    {
        String value=getValue(name);
        if (value==null)
        {
            return null;
        }
        return Boolean.parseBoolean(value);
    }

    public Boolean getNullableBooleanValue(String name,Boolean defaultValue)
    {
        String value=getValue(name);
        if (value==null)
        {
            return defaultValue;
        }
        return Boolean.parseBoolean(value);
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <ENUM> ENUM getEnumValue(String name,Class<ENUM> type)
	{
		return (ENUM) Enum.valueOf((Class<Enum>)type, getValue(name));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <ENUM> ENUM getEnumValue(String name,ENUM defaultValue,Class<ENUM> type)
	{
		return (ENUM) Enum.valueOf((Class<Enum>)type, getValue(name,defaultValue.toString()));
	}
	
	public <OBJECT> OBJECT getJSONObject(String name,Class<OBJECT> type) throws Exception
	{
		return ObjectMapper.read(getValue(name),type);
	}

	public <OBJECT> OBJECT getJSONObject(String name,OBJECT defaultValue,Class<OBJECT> type) throws Exception
	{
	    ConfigurationItem item=getConfigurationItem(name);
	    if (item==null)
	    {
	        return defaultValue;
	    }
		return ObjectMapper.read(getValue(name),type);
	}
    public <OBJECT> OBJECT getJSONObject(String name,Class<OBJECT> type,String defaultText) throws Exception
    {
        return ObjectMapper.read(getValue(name,defaultText),type);
    }

	public ConfigurationItem[] getConfigurationItemSnapshot()
	{
		synchronized (this)
		{
			return this.map.values().toArray(new ConfigurationItem[this.map.size()]);
		}
	}
	
	public <OBJECT> OBJECT getNamespaceObject(String namespace,Class<OBJECT> type) throws Exception
	{
	    OBJECT object=type.newInstance();
	    for (Field field:type.getDeclaredFields())
	    {
	        Class<?> fieldType=field.getType();
	        
	        String key=namespace+"."+field.getName();
            Alias alias=field.getDeclaredAnnotation(Alias.class);
            if (alias!=null)
            {
                key=namespace+"."+alias.value();
            }
	        ConfigurationItem item=getConfigurationItem(key);
	        if (item==null)
	        {
	            continue;
	        }
            if (fieldType==String.class)
            {
                field.set(object, getValue(key));
            }
            else if (fieldType==long.class)
            {
                field.setLong(object, getLongValue(key));
            }
            else if (fieldType==int.class)
            {
                field.setInt(object, getIntegerValue(key));
            }
            else if (fieldType==double.class)
            {
                field.setDouble(object, getDoubleValue(key));
            }
            else if (fieldType==float.class)
            {
                field.setFloat(object, getFloatValue(key));
            }
            else if (fieldType==short.class)
            {
                field.setShort(object, getShortValue(key));
            }
            else if (fieldType==byte.class)
            {
                field.setByte(object, getByteValue(key));
            }
            else if (fieldType==Long.class)
            {
                field.set(object, getNullableLongValue(key));
            }
            else if (fieldType==Integer.class)
            {
                field.set(object, getNullableIntegerValue(key));
            }
            else if (fieldType==Double.class)
            {
                field.set(object, getNullableDoubleValue(key));
            }
            else if (fieldType==Float.class)
            {
                field.set(object, getNullableFloatValue(key));
            }
            else if (fieldType==Short.class)
            {
                field.set(object, getNullableShortValue(key));
            }
            else if (fieldType==Byte.class)
            {
                field.set(object, getNullableByteValue(key));
            }
            else if (type.isEnum())
            {
                field.set(object, Enum.valueOf((Class<Enum>) fieldType, getValue(key)));
            }
            else 
            {
                field.set(object, ObjectMapper.read(getValue(key), fieldType));
            }
	    }
	    return object;
	}
	
	
}
