package org.nova.html.ext;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;

public class Text extends Element
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
    public void compose(Composer composer) throws Throwable
    {
        if (this.text!=null)
        {
            composer.getStringBuilder().append(this.text);
        }
    }
}
