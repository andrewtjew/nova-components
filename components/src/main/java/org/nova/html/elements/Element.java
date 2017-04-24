package org.nova.html.elements;

import java.io.OutputStream;

public abstract class Element
{
    abstract public void write(OutputStream outputStream) throws Throwable;

}