package org.nova.http.server;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nova.http.Header;
import org.nova.tracing.Trace;

public class RequestLogEntry
{
	//We should not keep any request related big objects so the GC can get rid of them. Anything we need to log must be a copy.
	final Trace trace;
	final String requestContentText;
	final String responseContentText;
	final RequestHandler requestHandler;
	final private String queryString;
    final String requestHeaders;
    final String responseHeaders;
	final int statusCode;
	final String remoteEndPoint;
	final String request;

	public RequestLogEntry(Trace trace,Context context,RequestHandler requestHandler,HttpServletRequest request,HttpServletResponse response)
	{
		this.trace=trace;
		this.requestContentText=context.getRequestContentText();
		String contentType=response.getContentType();
		if (contentType!=null)
		{
			switch (contentType)
			{
			case "text/css":
			case "text/javascript":
			case "text/html":
				this.responseContentText=null;
				break;
			
				default:
				this.responseContentText=context.getResponseContentText();
				break;
			}
		}
		else
		{
			this.responseContentText=null;
		}
		this.requestHandler=requestHandler;
		this.queryString=request.getQueryString();
		this.statusCode=response.getStatus();
		this.request=request.getMethod()+" "+request.getRequestURI();
		this.remoteEndPoint=request.getRemoteHost()+":"+request.getRemotePort();
        this.requestHeaders=WsUtils.getRequestHeaders(request);
        this.responseHeaders=WsUtils.getResponseHeaders(response);
	}

	public Trace getTrace()
	{
		return trace;
	}

	public String getRequestContentText()
	{
		return requestContentText;
	}

	public String getResponseContentText()
	{
		return responseContentText;
	}

	public RequestHandler getRequestHandler()
	{
		return requestHandler;
	}


	public int getStatusCode()
	{
		return statusCode;
	}
	public String getQueryString()
	{
		return queryString;
	}

	public String getRemoteEndPoint()
	{
		return remoteEndPoint;
	}
	public String getRequest()
	{
		return this.request;
	}

    public String getRequestHeaders()
    {
        return requestHeaders;
    }

    public String getResponseHeaders()
    {
        return responseHeaders;
    }
	

}
