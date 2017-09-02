package org.nova.logging;

import java.util.List;

import org.nova.collections.RingBuffer;
import org.nova.flow.SourceQueue;
import org.nova.logging.Item;
import org.nova.logging.Level;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;
import org.nova.metrics.CountMeter;
import org.nova.metrics.RateMeter;
import org.nova.tracing.Trace;

public class SourceQueueLogger extends Logger
{
    private final SourceQueue<LogEntry> logQueue;
    private boolean active;
    private CountMeter logFailures;
    private Throwable logFailureThrowable;
    private long number;
    private RateMeter rateMeter;
    private RingBuffer<LogEntry> buffer;
    
    public SourceQueueLogger(int bufferSize,String category,SourceQueue<LogEntry> logQueue)
    {
        super(category);
        this.logQueue=logQueue;
        this.number=0;
        this.logFailures=new CountMeter();
        this.active=true;
        this.rateMeter=new RateMeter();
        if (bufferSize>0)
        {
            this.buffer=new RingBuffer<LogEntry>(new LogEntry[bufferSize]);
        }
        else
        {
            this.buffer=null;
        }
        
    }

    @Override
    public void log(Trace trace,Level logLevel,String category,Throwable throwable,String message,Item[] items)
    {
        synchronized(this)
        {
            LogEntry entry=new LogEntry(this.number++,category,logLevel,System.currentTimeMillis(),throwable,trace,message,items);
            if (this.buffer!=null)
            {
                this.buffer.add(entry);
            }
            if (this.active==false)
            {
                return;
            }
            try
            {
                this.logQueue.send(entry);
                this.rateMeter.increment();
            }
            catch (Throwable t)
            {
                logFailureThrowable=t;
                this.logFailures.increment();
            }
        }
    }
    public void setActive(boolean active)
    {
        synchronized(this)
        {
            this.active=active;
        }
    }
    public boolean isActive()
    {
        synchronized(this)
        {
            return this.active;
        }
    }
    public CountMeter getLogFailures()
    {
        return this.logFailures;
    }
    public Throwable getLogFailureThrowable()
    {
        return this.logFailureThrowable;
    }
    public SourceQueue<LogEntry> getSourceQueue()
    {
        return this.logQueue;
    }
    public List<LogEntry> getLastLogEntries()
    {
        synchronized(this)
        {
            if (this.buffer==null)
            {
                return null;
            }
            return this.buffer.getSnapshot();
        }
    }
    public RateMeter getRateMeter()
    {
        return this.rateMeter;
    }

}
