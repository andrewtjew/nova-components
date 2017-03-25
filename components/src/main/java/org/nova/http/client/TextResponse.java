package org.nova.http.client;

import org.nova.http.Header;

public class TextResponse
{
	private final int statusCode;
	private final String content;
	private final Header[] headers;
	public TextResponse(int statusCode,String content,Header[] headers) 
	{
		this.statusCode=statusCode;
		this.content=content;
		this.headers=headers;
	}
	public String getText()
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
}
