package org.nova.logging;

import java.io.OutputStream;

import org.nova.flow.DataPacket;
import org.nova.flow.Node;
import org.nova.logging.Formatter;
import org.nova.logging.LogEntry;
import org.nova.metrics.RateMeter;


public abstract class OutputStreamWriter extends Node
{
	private Throwable throwable;
	private OutputStream outputStream;
	final private Formatter formatter;
	final private RateMeter rateMeter;
	
    protected OutputStreamWriter(Formatter formatter) throws Throwable
    {
        this(formatter,new RateMeter());
    }

    protected OutputStreamWriter(Formatter formatter,RateMeter rateMeter) throws Throwable
    {
        this.formatter=formatter;
        this.rateMeter=rateMeter;
    }

    public abstract OutputStream openOutputStream(long marker) throws Throwable;
    public abstract void closeOutputStream(OutputStream outputStream) throws Throwable;
	
    @Override
    public void beginSegment(long marker) throws Throwable
    {
        try
        {
            this.outputStream=openOutputStream(marker);
            this.rateMeter.add(this.formatter.outputBegin(outputStream));
        }
        catch (Throwable t)
        {
            synchronized(this)
            {
                this.throwable = t;
            }
            throw t;
        }
    }

    @Override
    public void endSegment() throws Throwable
    {
        synchronized(this)
        {
            try
            {
                this.rateMeter.add(this.formatter.outputEnd(this.outputStream));
                closeOutputStream(this.outputStream);
                this.outputStream = null;
                return;
            }
            catch (Throwable t)
            {
                this.throwable = t;
            }
            throw this.throwable;
        }
    }

    @Override
    public void process(DataPacket container) throws Throwable
    {
        synchronized(this)
        {
            if (this.throwable==null)
            {
                try
                {
                    for (int i = 0; i < container.size(); i++)
                    {
                        Object object = container.get()[i];
                        if ((object != null) && (object instanceof LogEntry))
                        {
                            this.rateMeter.add(this.formatter.output((LogEntry) object, this.outputStream));
                        }
                    }
                    return;
                }
                catch (Throwable t)
                {
                    this.throwable=t;
                }
            }
            throw throwable;
        }
    }
    
	@Override
	public void flush() throws Throwable
	{
		synchronized(this)
		{
		    if (this.throwable==null)
		    {
	            try
	            {
	                this.outputStream.flush();
	                return;
	            }
	            catch (Throwable t)
	            {
	                this.throwable=t;
	            }
		    }
		    throw this.throwable;
		}
	}

	public RateMeter getRateMeter()
	{
		return this.rateMeter;
	}
	
	public Throwable getThrowable()
	{
	    synchronized(this)
	    {
	        return this.throwable;
	    }
	}
}
