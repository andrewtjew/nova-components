package org.nova.html.elements;

import java.io.OutputStream;

public abstract class Element
{
    abstract public void compose(Composer composer) throws Throwable;

}