package org.nova.html.elements;

import java.io.OutputStream;

public abstract class Element
{
    abstract public void build(Builder builder) throws Throwable;

}