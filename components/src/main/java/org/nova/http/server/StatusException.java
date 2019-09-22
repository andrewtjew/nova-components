package org.nova.http.server;

import java.util.ArrayList;

import javax.servlet.http.Cookie;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.http.Header;

public class StatusException extends Exception
{
	/**
     * 
     */
    private static final long serialVersionUID = 8234425660729062731L;
    
    final int statusCode;
    ArrayList<Header> headers;
    ArrayList<Cookie> cookies;

	public StatusException(int statusCode)
	{
		this.statusCode=statusCode;
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
