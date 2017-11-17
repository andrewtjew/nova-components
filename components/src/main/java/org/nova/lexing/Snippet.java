package org.nova.lexing;

public abstract class Snippet
{
    abstract public String getTarget();
    abstract public String getBuffer();
    abstract public int getTargetBufferPosition(); 
    abstract public int getTargetAbsolutePosition();
    
}
