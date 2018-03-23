package org.nova.http.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Context
{
	private RequestHandler requestHandler;
	private HttpServletRequest httpServletRequest;
	private HttpServletResponse httpServletResponse;
	private ContentReader<?> contentReader;
	private ContentWriter<?> contentWriter;
	private Object state;
	private boolean captured=false;
	
	private String responseContentText;
	private String requestContentText;
	
	Context(RequestHandler requestHandler,HttpServletRequest servletRequest,HttpServletResponse servletResponse)
	{
		this.requestHandler=requestHandler;
		this.httpServletRequest=servletRequest;
		this.httpServletResponse=servletResponse;
	}
	public RequestHandler getRequestHandler()
	{
		return requestHandler;
	}
	public void setRequestHandler(RequestHandler requestHandler)
	{
		this.requestHandler = requestHandler;
	}
	public HttpServletRequest getHttpServletRequest()
	{
		return httpServletRequest;
	}
	public void setHttpServletRequest(HttpServletRequest httpServletRequest)
	{
		this.httpServletRequest = httpServletRequest;
	}
	public HttpServletResponse getHttpServletResponse()
	{
		return httpServletResponse;
	}
	public void setHttpServletResponse(HttpServletResponse httpServletResponse)
	{
		this.httpServletResponse = httpServletResponse;
	}
	public ContentReader<?> getContentReader()
	{
		return contentReader;
	}
	public void setContentReader(ContentReader<?> contentReader)
	{
		this.contentReader = contentReader;
	}
	public ContentWriter<?> getContentWriter()
	{
		return contentWriter;
	}
	public void setContentWriter(ContentWriter<?> contentWriter)
	{
		this.contentWriter = contentWriter;
	}
	public Object getState()
	{
		return state;
	}
	public void setState(Object state)
	{
		this.state = state;
	}
	public String getResponseContentText()
	{
		return responseContentText;
	}
	public void setResponseContentText(String responseContentText)
	{
		this.responseContentText=responseContentText;
	}
	public String getRequestContentText()
	{
		return requestContentText;
	}
	public void setRequestContentText(String requestContentText)
	{
		this.requestContentText=requestContentText;
	}
	public boolean isCaptured()
	{
		return captured;
	}
    public void setCaptured(boolean captured)
    {
        this.captured = captured;
    }
	public void capture()
	{
		this.captured = true;
	}
}
