package org.nova.scan;

public abstract class Source
{
    public abstract char next() throws Throwable;
    public abstract void begin(int revert);
    public abstract void revert();
    public abstract void end(int revert);
    public abstract Snippet endAndGetSnippet(int revert);
    public abstract String endContext();
    public abstract void beginContext();
    public abstract int getContextPosition();
}
