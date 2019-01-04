package org.nova.html.elements;


public abstract class Composer
{
    public abstract StringBuilder getStringBuilder();
    public void compose(Element element) throws Throwable
    {
        if (element!=null)
        {
            element.compose(this);
        }
    }
}
