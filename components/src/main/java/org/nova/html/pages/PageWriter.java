package org.nova.html.pages;

import java.io.OutputStream;

import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;

public class PageWriter extends ContentWriter<Page>
{
	final private ElementMap parameters;
	public PageWriter(ElementMap parameter)
	{
		this.parameters=parameter;
	}
	@Override
	public String getMediaType()
	{
		return "text/html";
	}

	@Override
	public void write(Context context, OutputStream outputStream, Page page) throws Throwable
	{
        context.getHttpServletResponse().setContentType("text/html;charset=utf-8");
		page.write(outputStream,this.parameters);
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
