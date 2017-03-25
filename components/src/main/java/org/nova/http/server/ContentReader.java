package org.nova.http.server;

import java.io.InputStream;
import java.io.OutputStream;

public abstract class ContentReader<CONTENT>
{
	abstract public String getMediaType();
	abstract public CONTENT read(Context context,InputStream inputStream,Class<?> contentType) throws Throwable;
	abstract public void writeSchema(OutputStream outputStream,Class<?> contentType) throws Throwable;;
	abstract public void writeExample(OutputStream outputStream,Class<?> contentType) throws Throwable;;
}
