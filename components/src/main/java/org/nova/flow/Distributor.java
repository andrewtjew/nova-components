package org.nova.flow;

public class Distributor extends Node
{
    final private Node[] receivers;
    final private Object lock;
    private int index;
    private int sizePerReceiver;
    private boolean strictSize;
    private int currentRoundSize;
    private long lastMarker;

    public Distributor(Object lock, int sizePerReceiver, boolean strictSize, Node[] receivers) throws Throwable
    {
        this.receivers = receivers;
        this.lock = lock;
        this.sizePerReceiver = sizePerReceiver;
        this.strictSize = strictSize;
    }

    private void switchReceiver() throws Throwable
    {
        this.receivers[this.index].endSegment();
        this.index=(this.index+1)%this.receivers.length;
        long marker=System.currentTimeMillis();
        if (marker<this.lastMarker)
        {
            marker=this.lastMarker+1;
        }
        this.lastMarker=marker;
        this.receivers[this.index].beginSegment(marker);
        this.currentRoundSize = 0;
    }

    @Override
    public void flush() throws Throwable
    {
        if (this.lock != null)
        {
            synchronized (this.lock)
            {
                this.receivers[this.index].flush();
            }
        }
        else
        {
            this.receivers[this.index].flush();
        }
    }

    private void _send(Packet container) throws Throwable
    {
        if ((this.strictSize==false)||(this.currentRoundSize + container.sizeOrType() <= this.sizePerReceiver))
        {
            this.receivers[this.index].process(container);
            this.currentRoundSize += container.sizeOrType();
            if (this.currentRoundSize >= this.sizePerReceiver)
            {
                switchReceiver();
            }
            return;
        }
        Object[] array=container.get();
        int sent = 0;
        while (sent<container.sizeOrType())
        {
            int receiverContainerSize=container.sizeOrType()-sent-this.currentRoundSize;
            if (receiverContainerSize>=this.sizePerReceiver-this.currentRoundSize)
            {
                receiverContainerSize=this.sizePerReceiver-this.currentRoundSize;
            }
            Packet packet=new Packet(receiverContainerSize);
            for (int i=sent;i<sent+receiverContainerSize;i++)
            {
                packet.add(array[i]);
            }
            this.receivers[this.index].process(packet);
            this.currentRoundSize+=receiverContainerSize;
            if (this.currentRoundSize >= this.sizePerReceiver)
            {
                switchReceiver();
            }
            sent+=receiverContainerSize;
        }
    }
    @Override
    public void process(Packet container) throws Throwable
    {
        if (this.lock != null)
        {
            synchronized (this.lock)
            {
                _send(container);
            }
        }
        else
        {
            _send(container);
        }
    }


    private void _endSegment() throws Throwable
    {
        this.receivers[this.index].endSegment();
    }

    @Override
    public void endSegment() throws Throwable
    {
        if (this.lock != null)
        {
            synchronized (this.lock)
            {
                _endSegment();
            }
        }
        else
        {
            _endSegment();
        }
    }

    private void _beginSegment(long marker) throws Throwable
    {
        if (this.lastMarker > marker)
        {
            marker = this.lastMarker + 1;
        }
        this.lastMarker = marker;
        this.index=(this.index+1)%this.receivers.length;
        this.receivers[this.index].beginSegment(marker);
    }

    @Override
    public void beginSegment(long marker) throws Throwable
    {
        if (this.lock != null)
        {
            synchronized (this.lock)
            {
                _beginSegment(marker);
            }
        }
        else
        {
            _beginSegment(marker);
        }
    }

    public Node[] getReceivers()
    {
        return this.receivers;
    }
}
