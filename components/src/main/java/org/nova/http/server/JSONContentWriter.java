package org.nova.http.server;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.json.ObjectExample;
import org.nova.json.ObjectMapper;
import org.nova.json.SchemaWriter;

public class JSONContentWriter extends ContentWriter<Object>
{
	@Override
	public String getMediaType()
	{
		return "application/json";
	}

	@Override
	public void write(Context context, OutputStream outputStream, Object content) throws Throwable
	{
		String contentText=ObjectMapper.writeObjectToString(content);
		outputStream.write(contentText.getBytes(StandardCharsets.UTF_8));
		context.setResponseContentText(contentText);
	}

	@Override
	public void writeSchema(OutputStream outputStream, Class<?> contentType) throws Throwable
	{
		SchemaWriter.writeSchema(outputStream, contentType);		
	}

	@Override
	public void writeExample(OutputStream outputStream, Class<?> contentType) throws Throwable
	{
		ObjectExample.write(outputStream, contentType);
	}

}
