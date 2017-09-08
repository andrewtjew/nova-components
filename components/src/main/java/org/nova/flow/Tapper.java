package org.nova.flow;

public class Tapper extends Node
{
    final private Node receiver;
    private Node tap;
    
    
    public Tapper(Node receiver,Node tap)
    {
        this.receiver=receiver;
        this.tap=tap;
    }
    public Tapper(Node receiver)
    {
        this(receiver,null);
    }
    
    public Node getReceiver()
    {
        return this.receiver;
    }
    
    public void setTap(Node tap)
    {
        synchronized(this)
        {
            this.tap=tap;
        }
    }
    public Node getTap()
    {
        synchronized(this)
        {
            return this.tap;
        }
    }

    
    @Override
    public void process(Packet packet) throws Throwable
    {
        this.receiver.process(packet);
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.process(packet);
            }
        }
    }

    @Override
    public void flush() throws Throwable
    {
        this.receiver.flush();
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.flush();
            }
        }
    }

    @Override
    public void beginSegment(long segmentIndex) throws Throwable
    {
        this.receiver.beginSegment(segmentIndex);
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.beginSegment(segmentIndex);
            }
        }
    }

    @Override
    public void endSegment() throws Throwable
    {
        this.receiver.endSegment();
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.endSegment();
            }
        }
    }

}
