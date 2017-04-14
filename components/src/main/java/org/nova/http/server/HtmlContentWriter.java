package org.nova.http.server;

import java.io.OutputStream;

import org.nova.html.HtmlWriter;


public class HtmlContentWriter extends ContentWriter<HtmlWriter>
{
	@Override
	public String getMediaType()
	{
		return "text/html";
	}

	@Override
	public void write(Context context, OutputStream outputStream, HtmlWriter content) throws Throwable
	{
//		outputStream.write("<html>".getBytes());
		if (content!=null)
		{
			outputStream.write(content.toString().getBytes());
		}
//		outputStream.write("</html>".getBytes());
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
