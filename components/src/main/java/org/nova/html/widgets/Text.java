package org.nova.html.widgets;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.html.elements.Builder;
import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;

public class Text extends InnerElement<Text>
{
    final private String text;
    public Text(String text)
    {
        super();
        this.text=text;
    }
    public Text(Object object)
    {
        super();
        if (object!=null)
        {
            this.text=object.toString();
        }
        else
        {
            text=null;
        }
    }

    @Override
    public String toString()
    {
        return this.text;
    }
    
    @Override
    public void build(Builder builder) throws Throwable
    {
        if (this.text!=null)
        {
            builder.getOutputStream().write(this.text.getBytes(StandardCharsets.UTF_8));
        }
    }
}
