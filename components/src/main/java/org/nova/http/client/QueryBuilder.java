package org.nova.http.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;

public class QueryBuilder
{
	private StringBuilder sb;
	private char separator='?'; 
	public QueryBuilder(String path)
	{
		this.sb=new StringBuilder(path);
	}
	public QueryBuilder add(String key,Object value) throws Exception
	{
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=');
		this.sb.append(URLEncoder.encode(value.toString(), "UTF-8"));
		return this;
	}
	public QueryBuilder add(String key,long value)
	{
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=').append(value);
		return this;
	}
	public QueryBuilder add(String key,int value)
	{
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=').append(value);
		return this;
	}
	public QueryBuilder add(String key,String value) throws Exception
	{
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=').append(URLEncoder.encode(value, "UTF-8"));
		return this;
	}
	public QueryBuilder add(String key,short value)
	{
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=').append(value);
		return this;
	}
	public String toString()
	{
		return this.sb.toString();
	}
}
