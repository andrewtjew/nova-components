package org.nova.configuration;

import java.util.HashMap;

import org.nova.core.Utils;
import org.nova.json.ObjectMapper;

public class Configuration
{
	
	final private HashMap<String, ConfigurationItem> map;

	public Configuration() 
	{
		this.map = new HashMap<>();
	}

	public void addCommandLineConfigurations(String[] args) 
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
		String value=getValue(name,Double.toString(defaultValue));
		return Double.parseDouble(value);
	}
	public float getFloatValue(String name)
	{
		String value=getValue(name);
		return Float.parseFloat(value);
	}
	public float getFloatValue(String name,float defaultValue)
	{
		String value=getValue(name,Float.toString(defaultValue));
		return Float.parseFloat(value);
	}
	public long getLongValue(String name)
	{
		String value=getValue(name);
		return Long.parseLong(value);
	}

	public long getLongValue(String name,long defaultValue)
	{
		String value=getValue(name,Long.toString(defaultValue));
		return Long.parseLong(value);
	}
	
	public int getIntegerValue(String name)
	{
		String value=getValue(name);
		return Integer.parseInt(value);
	}

	public int getIntegerValue(String name,int defaultValue)
	{
		String value=getValue(name,Integer.toString(defaultValue));
		return Integer.parseInt(value);
	}
	
	public boolean getBooleanValue(String name)
	{
		String value=getValue(name);
		return Boolean.parseBoolean(value);
	}

	public boolean getBooleanValue(String name,boolean defaultValue)
	{
		String value=getValue(name,Boolean.toString(defaultValue));
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
	
	public <OBJECT> OBJECT getJsonValue(String name,Class<OBJECT> type) throws Exception
	{
		return ObjectMapper.read(getValue(name),type);
	}

	public <OBJECT> OBJECT getJsonValue(String name,String defaultValue,Class<OBJECT> type) throws Exception
	{
		return ObjectMapper.read(getValue(name,defaultValue),type);
	}

	public ConfigurationItem[] getConfigurationItemSnapshot()
	{
		synchronized (this)
		{
			return this.map.values().toArray(new ConfigurationItem[this.map.size()]);
		}
	}
	
}
