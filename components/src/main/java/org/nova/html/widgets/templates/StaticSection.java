package org.nova.html.widgets.templates;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

class StaticSection extends Section
{
    final private byte[] bytes;
    StaticSection(String text)
    {
        this(text.getBytes(StandardCharsets.UTF_8));
    }
    StaticSection(byte[] bytes)
    {
        this.bytes=bytes;
    }
    @Override
    public void write(OutputStream outputStream,ElementMap map) throws Throwable
    {
        outputStream.write(this.bytes);
    }
    @Override 
    public String toString()
    {
        return new String(this.bytes,StandardCharsets.UTF_8);
    }
}
