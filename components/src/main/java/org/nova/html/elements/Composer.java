package org.nova.html.elements;

import java.io.OutputStream;


public abstract class Composer
{
//    public abstract OutputStream getOutputStream();
    public abstract StringBuilder getStringBuilder();
    public void render(Element element) throws Throwable
    {
        element.compose(this);
    }
}
