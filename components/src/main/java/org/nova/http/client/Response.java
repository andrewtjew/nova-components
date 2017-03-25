package org.nova.http.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.nova.tracing.Trace;

public class Response implements AutoCloseable
{
	private HttpResponse response;
	private final Trace trace;
	Response(Trace trace,HttpResponse response)
	{
		this.response=response;
		this.trace=trace;
	}
	
	public int getStatusCode()
	{
		return this.response.getStatusLine().getStatusCode();
	}
	
	public String getHeader(String name)
	{
		return this.response.getFirstHeader(name).getValue();
	}

	
	@Override
	public void close() throws Exception
	{
		if (this.response!=null)
		{
			this.response.getEntity().getContent().close();
			this.trace.close();
			this.response=null;
		}
	}
	
	public InputStream getContentStream() throws UnsupportedOperationException, IOException
	{
		return this.response.getEntity().getContent();
	}
}
