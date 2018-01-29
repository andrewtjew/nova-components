package org.nova.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;

import org.nova.core.Utils;
import org.nova.json.ObjectExample;
import org.nova.json.ObjectMapper;
import org.nova.json.SchemaWriter;

import com.amazonaws.services.simpleemail.model.Content;

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
		String contentText=Utils.readString(inputStream, StandardCharsets.UTF_8);
		context.setRequestContentText(contentText);
		return ObjectMapper.read(contentText,contentType);
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
