package org.nova.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.core.Utils;
import org.nova.json.ObjectExample;
import org.nova.json.ObjectMapper;
import org.nova.json.SchemaWriter;

public class JSONPatchContentReader extends ContentReader<Object>
{
	@Override
	public String getMediaType()
	{
		return "application/merge-patch+json";
	}

	@Override
	public Object read(Context context, InputStream inputStream,Class<?> contentType) throws Exception
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
