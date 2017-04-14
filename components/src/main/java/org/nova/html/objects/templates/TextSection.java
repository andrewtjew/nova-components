package org.nova.html.objects.templates;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

class TextSection extends Section
{
    final private byte[] bytes;
    TextSection(String text)
    {
        this.bytes=text.getBytes(StandardCharsets.UTF_8);
    }
    @Override
    public void write(OutputStream outputStream,ElementMap map) throws Throwable
    {
        outputStream.write(this.bytes);
    }
}
