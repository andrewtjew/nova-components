package org.nova.flow;

public interface Source<ITEM>
{
    public void send(ITEM item);
    public void flush();
}
