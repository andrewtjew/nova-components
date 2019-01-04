package org.nova.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.json.ObjectExample;
import org.nova.json.ObjectMapper;
import org.nova.json.SchemaWriter;
import org.nova.utils.FileUtils;

public class JSONContentReader extends ContentReader<Object>
{
	@Override
	public String getMediaType()
	{
		return "application/json";
	}

	int er=0;
	@Override
	public Object read(Context context, int contentLength,InputStream inputStream,Class<?> contentType) throws Throwable
	{
		String contentText=FileUtils.readString(inputStream, StandardCharsets.UTF_8);
		context.setRequestContentText(contentText);
		return ObjectMapper.readObject(contentText,contentType);
	}

	@Override
	public void writeSchema(OutputStream outputStream, Class<?> contentType) throws IOException
	{
		SchemaWriter.writeSchema(outputStream, contentType);
	}
		

	@Override
	public void writeExample(OutputStream outputStream, Class<?> contentType) throws Throwable
	{
		ObjectExample.write(outputStream, contentType);
	}

}
