package org.nova.http.client;

import java.net.URLEncoder;

import org.nova.json.ObjectMapper;

public class PathAndQuery
{
	private StringBuilder sb;
	private char separator='?'; 
    public PathAndQuery(String path)
    {
        this.sb=new StringBuilder(path);
    }
    public PathAndQuery()
    {
        this.sb=new StringBuilder();
    }
    public PathAndQuery addSegment(String segment) throws Exception
    {
        if (this.separator!='?')
        {
            throw new Exception();
        }
        this.sb.append(segment);
        return this;
    }
//    @Deprecated
//    public PathAndQuery addSegments(Object...segments) throws Exception
//    {
//        if (this.separator!='?')
//        {
//            throw new Exception();
//        }
//        for (Object segment:segments)
//        {
//            this.sb.append('/').append(segment);
//        }
//        return this;
//    }
	public PathAndQuery addQuery(String key,Object value) throws Exception
	{
	    if (value==null)
	    {
	        return this;
	    }
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
	public PathAndQuery addQuery(String key,long value) throws Exception
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
    public PathAndQuery addJSONQuery(String key,Object value) throws Throwable
    {
        return addQuery(key,ObjectMapper.writeObjectToString(value));
    }
	public PathAndQuery addQuery(String key,int value) throws Exception
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
	public PathAndQuery addQuery(String key,String value) throws Exception
	{
	    if (value==null)
	    {
	        return this;
	    }
        if (this.separator=='#')
        {
            throw new Exception();
        }
		this.sb.append(this.separator);
		this.separator='&';
		this.sb.append(key).append('=').append(URLEncoder.encode(value, "UTF-8"));
		return this;
	}
	public PathAndQuery addQuery(String key,short value) throws Exception
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
    public PathAndQuery addFragment(String fragment) throws Exception
    {
        if (this.separator=='#')
        {
            throw new Exception();
        }
        this.separator='#';
        this.sb.append(this.separator).append(fragment);
        return this;
    }
    public boolean containQueries()
    {
        return this.separator=='&';
    }
	public String toString()
	{
		return this.sb.toString();
	}
	public char getSeparator()
	{
	    return this.separator;
	}
}
