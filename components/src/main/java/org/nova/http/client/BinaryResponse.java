package org.nova.http.client;

import org.nova.http.Header;

public class BinaryResponse
{
	private final int statusCode;
	private final byte[] content;
	private final Header[] headers;
	private final String contentType;
	public BinaryResponse(int statusCode,byte[] content,Header[] headers,String contentType) 
	{
		this.statusCode=statusCode;
		this.content=content;
		this.headers=headers;
		this.contentType=contentType;
	}
	public byte[] get()
	{
		return content;
	}
	public int getStatusCode()
	{
		return this.statusCode;
	}
	public Header[] getHeaders()
	{
	    return this.headers;
	}
	public String getContentType()
	{
	    return this.contentType;
	}
}
