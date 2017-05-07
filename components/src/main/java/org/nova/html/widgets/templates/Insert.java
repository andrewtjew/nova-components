package org.nova.html.widgets.templates;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.html.elements.Element;

public class Insert extends Element
{
    final private String key;
    public Insert(String key)
    {
        this.key=key;
    }
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        outputStream.write(("<insert key='"+key+"'>").getBytes(StandardCharsets.UTF_8));
    }
    public String getKey()
    {
        return this.key;
    }
    
}
