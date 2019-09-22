package org.nova.html.remoting;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;

public class HtmlRemotingWriter extends ContentWriter<Result>
{
	public HtmlRemotingWriter() throws Exception
	{
	}
	
	@Override
	public String getMediaType()
	{
		return "application/json";
	}
	
	@Override
	public void write(Context context, OutputStream outputStream, Result script) throws Throwable
	{
        context.getHttpServletResponse().setContentType("application/json;charset=utf-8");
        String text=script.serialize();
        outputStream.write(text.getBytes(StandardCharsets.UTF_8));
        context.setResponseContentText(text);
	}

	@Override
	public void writeSchema(OutputStream outputStream, Class<?> contentType) throws Throwable
	{
	}

	@Override
	public void writeExample(OutputStream outputStream, Class<?> contentType) throws Throwable
	{
	}
}

