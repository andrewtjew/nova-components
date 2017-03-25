package org.nova.configuration;

public class ConfigurationItem
{
	final private String name;
	final private String description;
	final private String value;
	final private String sourceContext;
	final private ConfigurationSource source;
	public ConfigurationItem(String name,String value,ConfigurationSource source,String sourceContext,String description)
	{
//		System.out.println(source);
		this.name=name;
		this.description=description;
		this.value=value;
		this.sourceContext=sourceContext;
		this.source=source;
	}
	public String getName()
	{
		return this.name;
	}
	public String getDescription()
	{
		return description;
	}
	public String getValue()
	{
		return value;
	}
	public String getSourceContext()
	{
		return sourceContext;
	}
	public ConfigurationSource getSource()
	{
		return source;
	}
	
	
}
