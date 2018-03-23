package org.nova.html.elements;


public abstract class Composer
{
    public abstract StringBuilder getStringBuilder();
    public void render(Element element) throws Throwable
    {
        element.compose(this);
    }
}
