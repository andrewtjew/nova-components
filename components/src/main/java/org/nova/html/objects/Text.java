package org.nova.html.objects;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.html.elements.Element;

public class Text extends Element
{
    final private String text;
    public Text(String text)
    {
        super();
        this.text=text;
    }

    @Override
    public String toString()
    {
        return this.text;
    }
    
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        outputStream.write(this.text.getBytes(StandardCharsets.UTF_8));
    }
}
