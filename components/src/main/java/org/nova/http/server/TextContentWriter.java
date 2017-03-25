package org.nova.http.server;

import java.io.OutputStream;

public class TextContentWriter extends ContentWriter<String>
{
	final private String mediaType;
	
	public TextContentWriter(String mediaType)
	{
		this.mediaType=mediaType;
	}
	
	@Override
	public String getMediaType()
	{
		return this.mediaType;
	}

	@Override
	public void write(Context context, OutputStream outputStream, String content) throws Throwable
	{
		outputStream.write(content.getBytes());
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
