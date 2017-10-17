package org.nova.flow;

public class Distributor extends Node
{
    final private Node[] receivers;
    final private Object lock;
    private int index;
    private int sizePerReceiver;
    private boolean strictSize;
    private int currentRoundSize;
    private long lastGroupIdentifier;

    public Distributor(Object lock, int sizePerReceiver, boolean strictSize, Node[] receivers) throws Throwable
    {
        this.receivers = receivers;
        this.lock = lock;
        this.sizePerReceiver = sizePerReceiver;
        this.strictSize = strictSize;
    }

    private void switchReceiver() throws Throwable
    {
        this.receivers[this.index].endGroup();
        this.index=(this.index+1)%this.receivers.length;
        long now=System.currentTimeMillis();
        if (now<this.lastGroupIdentifier)
        {
            now=this.lastGroupIdentifier+1;
        }
        this.lastGroupIdentifier=now;
        this.receivers[this.index].beginGroup(now);
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
                packet.add(container.get(i));
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


    private void _endGroup() throws Throwable
    {
        this.receivers[this.index].endGroup();
    }

    @Override
    public void endGroup() throws Throwable
    {
        if (this.lock != null)
        {
            synchronized (this.lock)
            {
                _endGroup();
            }
        }
        else
        {
            _endGroup();
        }
    }

    private void _beginGroup(long groupIdentifier) throws Throwable
    {
        if (this.lastGroupIdentifier > groupIdentifier)
        {
            groupIdentifier = this.lastGroupIdentifier + 1;
        }
        this.lastGroupIdentifier = groupIdentifier;
        this.index=(this.index+1)%this.receivers.length;
        this.receivers[this.index].beginGroup(groupIdentifier);
    }

    @Override
    public void beginGroup(long groupSequenceNumber) throws Throwable
    {
        if (this.lock != null)
        {
            synchronized (this.lock)
            {
                _beginGroup(groupSequenceNumber);
            }
        }
        else
        {
            _beginGroup(groupSequenceNumber);
        }
    }

    public Node[] getReceivers()
    {
        return this.receivers;
    }
}
