package org.nova.logging;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.nova.collections.RingBuffer;
import org.nova.tracing.Trace;

public class MemoryLogger extends Logger
{
    final private AtomicLong number;
    final private RingBuffer<LogEntry> buffer;

    public MemoryLogger(String category,int capacity)
    {
        super(category);
        this.number=new AtomicLong();
        this.buffer=new RingBuffer<>(new LogEntry[capacity]);
    }

    @Override
    public void log(Trace trace, Level logLevel, String category, Throwable throwable, String message, Item[] items)
    {
        long number=this.number.getAndIncrement();
        long now=System.currentTimeMillis();
        LogEntry entry=new LogEntry(number, category, logLevel, now, throwable, trace, message, items);
        synchronized(this)
        {
            this.buffer.add(entry);
        }
        
    }

    public List<LogEntry> getSnapshot()
    {
        synchronized(this)
        {
            return this.buffer.getSnapshot();
        }
    }
}
