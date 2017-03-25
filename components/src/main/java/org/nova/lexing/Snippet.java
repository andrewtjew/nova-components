package org.nova.lexing;

public abstract class Snippet
{
    abstract public String getTarget();
    abstract public String getContext();
    abstract public int getContextPosition();
    abstract public int getAbsolutePosition();
    
}
