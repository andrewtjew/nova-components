package org.nova.flow;

import org.nova.collections.RingBuffer;

public class CaptureBuffer extends Node
{
    final private Node receiver;
    private long spills;
    private RingBuffer<Object> buffer;
    private boolean allowSpills;
    private boolean capture;
    
    public CaptureBuffer(Node receiver)
    {
        this.receiver=receiver;
    }
    
    public void startCapture(RingBuffer<Object> buffer,boolean allowSpills)
    {
        synchronized (this)
        {
            this.capture=true;
            this.buffer=buffer;
            this.spills=0;
            this.allowSpills=allowSpills;
        }            
    }
    public void startCapture(int capacity,boolean allowSpills)
    {
        startCapture(new RingBuffer<Object>(new Object[capacity]),allowSpills);
    }
    public void stopCapture()
    {
        synchronized (this)
        {
            this.capture=false;
        }            
    }
    
    @Override
    public void process(Packet packet) throws Throwable
    {
        synchronized(this)
        {
            this.receiver.process(packet);
            if (this.capture==false)
            {
                return;
            }
            int size=packet.sizeOrType();
            if (this.allowSpills)
            {
                for (int i=0;i<size;i++)
                {
                    if (this.buffer.size()==this.buffer.getCapacity())
                    {
                        this.spills++;
                    }
                    this.buffer.add(packet.get(i));
                }
            }
            else
            {
                for (int i=0;i<size;i++)
                {
                    if (this.buffer.size()==this.buffer.getCapacity())
                    {
                        return;
                    }
                    this.buffer.add(packet.get(i));
                }
            }
        }
    }

    @Override
    public void flush() throws Throwable
    {
        this.receiver.flush();
    }

    @Override
    public void beginGroup(long groupIdentifier) throws Throwable
    {
        this.receiver.beginGroup(groupIdentifier);
    }

    @Override
    public void endGroup() throws Throwable
    {
        this.receiver.endGroup();
    }

    public Node getReceiver()
    {
        return this.receiver;
    }
    
    public boolean isCapture()
    {
        synchronized(this)
        {
            return this.capture;
        }
    }
    public long getSpills()
    {
        synchronized(this)
        {
            return this.spills;
        }
    }
    public RingBuffer<Object> getBuffer()
    {
        synchronized(this)
        {
            return this.buffer;
        }
    }
    public boolean isAllowSpills()
    {
        synchronized(this)
        {
            return this.allowSpills;
        }
    }

}
