package org.nova.parsing.scan;

public abstract class Snippet
{
    abstract public String getTarget();
    abstract public String getBuffer();
    abstract public int getTargetBufferPosition(); 
    abstract public int getTargetAbsolutePosition();
    
}
