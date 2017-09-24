package org.nova.logging;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.flow.Node;
import org.nova.flow.Packet;
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
	
    public void write(String text) throws IOException
    {
        byte[] bytes=text.getBytes(StandardCharsets.UTF_8);
        this.outputStream.write(bytes);
        this.rateMeter.add(bytes.length);
    }
    
    @Override
    public void beginGroup(long groupIdentifier) throws Throwable
    {
        try
        {
            this.outputStream=openOutputStream(groupIdentifier);
            write(this.formatter.formatBegin());
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
    public void endGroup() throws Throwable
    {
        synchronized(this)
        {
            try
            {
                write(this.formatter.formatEnd());
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
    public void process(Packet container) throws Throwable
    {
        synchronized(this)
        {
            if (this.throwable==null)
            {
                try
                {
                    for (int i = 0; i < container.sizeOrType(); i++)
                    {
                        Object object = container.get()[i];
                        if ((object != null) && (object instanceof LogEntry))
                        {
                            write(this.formatter.format((LogEntry)object));
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
