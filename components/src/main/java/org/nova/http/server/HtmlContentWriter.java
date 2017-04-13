package org.nova.http.server;

import java.io.OutputStream;

public class HtmlContentWriter extends ContentWriter<String>
{
    public HtmlContentWriter()
    {
    }
    
    @Override
    public String getMediaType()
    {
        return "text/html";
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
