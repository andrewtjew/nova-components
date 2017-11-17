package org.nova.http.client;

import java.net.URLEncoder;

import org.nova.json.ObjectMapper;

public class PathAndQueryBuilder
{
	private StringBuilder sb;
	private char separator='?'; 
    public PathAndQueryBuilder(String path)
    {
        this.sb=new StringBuilder(path);
    }
    public PathAndQueryBuilder()
    {
        this.sb=new StringBuilder();
    }
    public PathAndQueryBuilder addSegment(String segment) throws Exception
    {
        if (this.separator!='?')
        {
            throw new Exception();
        }
        this.sb.append(segment);
        return this;
    }
    @Deprecated
    public PathAndQueryBuilder addSegments(Object...segments) throws Exception
    {
        if (this.separator!='?')
        {
            throw new Exception();
        }
        for (Object segment:segments)
        {
            this.sb.append('/').append(segment);
        }
        return this;
    }
	public PathAndQueryBuilder addQuery(String key,Object value) throws Exception
	{
        if (this.separator=='#')
        {
            throw new Exception();
        }
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=');
		this.sb.append(URLEncoder.encode(value.toString(), "UTF-8"));
		return this;
	}
	public PathAndQueryBuilder addQuery(String key,long value) throws Exception
	{
        if (this.separator=='#')
        {
            throw new Exception();
        }
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=').append(value);
		return this;
	}
    public PathAndQueryBuilder addJSONQuery(String key,Object value) throws Throwable
    {
        return addQuery(key,ObjectMapper.write(value));
    }
	public PathAndQueryBuilder addQuery(String key,int value) throws Exception
	{
        if (this.separator=='#')
        {
            throw new Exception();
        }
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=').append(value);
		return this;
	}
	public PathAndQueryBuilder addQuery(String key,String value) throws Exception
	{
        if (this.separator=='#')
        {
            throw new Exception();
        }
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=').append(URLEncoder.encode(value, "UTF-8"));
		return this;
	}
	public PathAndQueryBuilder addQuery(String key,short value) throws Exception
	{
        if (this.separator=='#')
        {
            throw new Exception();
        }
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=').append(value);
		return this;
	}
    public PathAndQueryBuilder addFragment(String fragment) throws Exception
    {
        if (this.separator=='#')
        {
            throw new Exception();
        }
        this.separator='#';
        this.sb.append(this.separator).append(fragment);
        return this;
    }
	public String toString()
	{
		return this.sb.toString();
	}
}
