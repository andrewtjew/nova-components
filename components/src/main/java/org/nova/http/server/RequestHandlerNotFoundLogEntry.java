package org.nova.http.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nova.tracing.Trace;

public class RequestHandlerNotFoundLogEntry
{
	final private String requestHeaders;
	final private String URI;
	final private String method;
	final private String queryString;
	final private Trace trace;
	final private String remoteEndPoint;
	public RequestHandlerNotFoundLogEntry(Trace trace,HttpServletRequest request)
	{
		this.trace=trace;
		this.requestHeaders=WsUtils.getRequestHeaders(request);
		this.method=request.getMethod();
		this.URI=request.getRequestURI();
		this.queryString=request.getQueryString();
		this.remoteEndPoint=request.getRemoteHost()+":"+request.getRemotePort();
	}
	public String getRequestHeaders()
	{
		return requestHeaders;
	}
	public String getURI()
	{
		return URI;
	}
	public String getMethod()
	{
		return method;
	}
	public String getQueryString()
	{
		return queryString;
	}
	public Trace getTrace()
	{
		return trace;
	}
	public String getRemoteEndPoint()
	{
		return remoteEndPoint;
	}
	
}
