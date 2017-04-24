package org.nova.http.server;

import java.util.ArrayList;

import javax.servlet.http.Cookie;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.core.KeyValue;
import org.nova.core.NameValue;
import org.nova.http.Header;

public class Response<CONTENT>
{
	final private CONTENT content;
	final private int statusCode;
    ArrayList<Header> headers;
    ArrayList<Cookie> cookies;

	public Response(int statusCode,CONTENT content)
	{
		this.statusCode=statusCode;
		this.content=content;
	}
	public Response()
	{
		this(HttpStatus.OK_200,null);
	}

	public Response(int statusCode)
	{
		this(statusCode,null);
	}
	public Response(CONTENT content)
	{
		this(HttpStatus.OK_200,content);
	}

	public CONTENT getContent()
	{
		return content;
	}

	public int getStatusCode()
	{
		return statusCode;
	}
    public void addHeader(String name,String value)
    {
        addHeader(new Header(name,value));
    }
    public void addHeader(Header header)
    {
        if (this.headers==null)
        {
            this.headers=new ArrayList<>();
        }
        this.headers.add(header);
    }
    public void addCookie(String name,String value)
    {
        if (this.cookies==null)
        {
            this.cookies=new ArrayList<>();
        }
        this.cookies.add(new Cookie(name,value));
    }
    public void addCookie(Cookie cookie)
    {
        if (this.cookies==null)
        {
            this.cookies=new ArrayList<>();
        }
        this.cookies.add(cookie);
    }
	

}
