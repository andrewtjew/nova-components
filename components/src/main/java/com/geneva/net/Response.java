package com.geneva.net;

public class Response
{
	final private byte[] content;
	final private Status status;
	Response(Status status,byte[] content)
	{
		this.content=content;
		this.status=status;
	}
	public byte[] getContent()
	{
		return content;
	}
	public Status getStatus()
	{
		return status;
	}
	
}
