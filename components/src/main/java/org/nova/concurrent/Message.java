package org.nova.concurrent;

public class Message
{
    final long createdMs;
    final Object content;
    Long receivedMs;
    Message(Object content)
    {
        this.content=content;
        this.createdMs=System.currentTimeMillis();
    }
    public Object getContent()
    {
        return this.content;
    }
    public long getCreatedMs()
    {
        return this.createdMs;
    }
}
