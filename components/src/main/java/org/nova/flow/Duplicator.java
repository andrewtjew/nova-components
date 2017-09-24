package org.nova.flow;

import java.util.List;

public class Duplicator extends Node
{
	final private Node[] receivers;
	final private Object lock;
	public Duplicator(Object lock,Node...receivers)
	{
		this.receivers=receivers;
		this.lock=lock;
	}
	private void _flush() throws Throwable
	{
		for (Node receiver:this.receivers)
		{
			receiver.flush();
		}
	}
	@Override
	public void flush() throws Throwable
	{
		if (this.lock!=null)
		{
			synchronized (this.lock)
			{
				_flush();
			}
		}
		else
		{
			_flush();
		}
	}
	private void _endSegment() throws Throwable
	{
        for (Node receiver:this.receivers)
		{
			receiver.endGroup();
		}
	}
	@Override
	public void endGroup() throws Throwable
	{
		if (this.lock!=null)
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
    private void _send(Packet container) throws Throwable
    {
        for (Node receiver:this.receivers)
        {
            receiver.process(container);
        }
    }
    @Override
    public void process(Packet container) throws Throwable
    {
        if (this.lock!=null)
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

    private void _beginGroup(long groupSequenceNumber) throws Throwable
    {
        for (Node receiver:this.receivers)
        {
            receiver.beginGroup(groupSequenceNumber);
        }
    }
    @Override
    public void beginGroup(long marker) throws Throwable
    {
        if (this.lock!=null)
        {
            synchronized (this.lock)
            {
                _beginGroup(marker);
            }
        }
        else
        {
            _beginGroup(marker);
        }
    }
}
