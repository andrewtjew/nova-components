package org.nova.lexing;

public abstract class Source
{
    public abstract char next() throws Throwable;
    public abstract void begin(int revert);
    public abstract Snippet end(int revert);
    public abstract String endContext();
    public abstract void beginContext();
    public abstract int getContextPosition();
}
