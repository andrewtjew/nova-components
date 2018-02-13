package org.nova.logging;

import java.util.concurrent.atomic.AtomicLong;

import org.nova.tracing.Trace;

public class ConsoleLogger extends Logger
{
    final private Formatter formatter;
    final private AtomicLong number;
    public ConsoleLogger(String category,Formatter formatter)
    {
        super(category);
        this.formatter=formatter;
        this.number=new AtomicLong();
       
    }

    @Override
    public void log(Trace trace, Level logLevel, String category, Throwable throwable, String message, Item[] items)
    {
        try
        {
            String text=this.formatter.format(new LogEntry(this.number.getAndIncrement(),category,logLevel,System.currentTimeMillis(),throwable,trace,message,items));
            System.out.println(text);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

}
