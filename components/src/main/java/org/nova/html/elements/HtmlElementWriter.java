package org.nova.html.elements;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;

public class HtmlElementWriter extends ContentWriter<Element>
{
	public HtmlElementWriter() throws Exception
	{
	}
	
	@Override
	public String getMediaType()
	{
		return "text/html";
	}
	
	@Override
	public void write(Context context, OutputStream outputStream, Element element) throws Throwable
	{
        context.getHttpServletResponse().setContentType("text/html;charset=utf-8");
		if (element!=null)
		{
		    StringComposer composer=new StringComposer();
            element.build(composer);
            String text=composer.getStringBuilder().toString();
            outputStream.write(text.getBytes(StandardCharsets.UTF_8));
            context.setResponseContentText(text);
		}
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

