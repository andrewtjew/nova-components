package org.nova.html.widgets;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.html.elements.Element;

public class ObjectContent extends Element
{
    Object object;
    
    public ObjectContent(Object object)
    {
        this.object=object;
    }
    
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        if (this.object!=null)
        {
            outputStream.write(this.object.toString().getBytes(StandardCharsets.UTF_8));
        }
    }

}
