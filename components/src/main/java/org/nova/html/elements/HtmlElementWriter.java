package org.nova.html.elements;

import java.io.OutputStream;
import java.net.InetAddress;

import org.nova.core.Utils;
import org.nova.html.widgets.templates.Template;
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
            element.write(outputStream);
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

