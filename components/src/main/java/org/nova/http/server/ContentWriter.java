package org.nova.http.server;

import java.io.OutputStream;

public abstract class ContentWriter<CONTENT>
{
	abstract public String getMediaType();
	abstract public void write(Context context,OutputStream outputStream,CONTENT content) throws Throwable;
	abstract public void writeSchema(OutputStream outputStream,Class<?> contentType) throws Throwable;;
	abstract public void writeExample(OutputStream outputStream,Class<?> contentType) throws Throwable;;
	
}
