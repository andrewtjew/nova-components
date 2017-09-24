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
    public void beginGroup(long groupIdentifier) throws Throwable
    {
        this.receiver.beginGroup(groupIdentifier);
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.beginGroup(groupIdentifier);
            }
        }
    }

    @Override
    public void endGroup() throws Throwable
    {
        this.receiver.endGroup();
        synchronized(this)
        {
            if (this.tap!=null)
            {
                this.tap.endGroup();
            }
        }
    }

}
