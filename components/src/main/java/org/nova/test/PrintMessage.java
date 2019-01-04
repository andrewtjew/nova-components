package org.nova.test;

import org.nova.utils.Utils;

public class PrintMessage
{
	public String host;
	public long created;
	public String source;
	public String text;
	public String category;
	
	public PrintMessage()
	{
	}
	public PrintMessage(String category,String source,String text) throws Exception
	{
		this.category=category;
		this.host=Utils.getLocalHostName();
		this.created=System.currentTimeMillis();
		this.source=source;
		this.text=text;
	}
	
	
}
