package org.nova.logging;

import org.nova.flow.SourceQueue;
import org.nova.logging.Item;
import org.nova.logging.Level;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;
import org.nova.metrics.CountMeter;
import org.nova.tracing.Trace;

public class SourceQueueLogger extends Logger
{
    private final SourceQueue<LogEntry> logQueue;
    private boolean active;
    private CountMeter logFailures;
    private Throwable logFailureThrowable;
    private long number;
    
    public SourceQueueLogger(String category,SourceQueue<LogEntry> logQueue)
    {
        super(category);
        this.logQueue=logQueue;
        this.number=0;
        this.logFailures=new CountMeter();
        this.active=true;
    }

    @Override
    public void log(Trace trace,Level logLevel,String category,Throwable throwable,String message,Item[] items)
    {
        synchronized(this)
        {
            if (this.active==false)
            {
                return;
            }
            try
            {
                this.logQueue.send(new LogEntry(this.number++,category,logLevel,System.currentTimeMillis(),throwable,trace,message,items));
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
    public Throwable getLogFailureThrowable()
    {
        return this.logFailureThrowable;
    }
    public SourceQueue<LogEntry> getSourceQueue()
    {
        return this.logQueue;
                
    }

}
