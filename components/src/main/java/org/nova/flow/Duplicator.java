package org.nova.flow;

import java.util.List;

public class Duplicator extends Receiver
{
	final private Receiver[] receivers;
	final private Object lock;
	public Duplicator(Object lock,Receiver...receivers)
	{
		this.receivers=receivers;
		this.lock=lock;
	}
	private void _flush() throws Throwable
	{
		for (Receiver receiver:this.receivers)
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
        for (Receiver receiver:this.receivers)
		{
			receiver.endSegment();
		}
	}
	@Override
	public void endSegment() throws Throwable
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
        for (Receiver receiver:this.receivers)
        {
            receiver.send(container);
        }
    }
    @Override
    public void send(Packet container) throws Throwable
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

    private void _beginSegment(long marker) throws Throwable
    {
        for (Receiver receiver:this.receivers)
        {
            receiver.beginSegment(marker);
        }
    }
    @Override
    public void beginSegment(long marker) throws Throwable
    {
        if (this.lock!=null)
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
}
