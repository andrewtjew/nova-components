package org.nova.logging;

import org.nova.flow.Node;
import org.nova.flow.Packet;
import org.nova.tracing.Trace;

public class NodeLogger extends Logger
{
    final private Node[] receivers;
    final private ThrowableEvents throwablesLog; 
    private long number;
    
    public NodeLogger(String category,Node...receivers)
    {
        super(category);
        this.receivers=receivers;
        this.throwablesLog=new ThrowableEvents(); 
                
    }

    @Override
    public void log(Trace trace, Level logLevel, String category, Throwable throwable, String message, Item[] items)
    {
        synchronized (this)
        {
            Packet packet=new Packet(1);
            packet.add(new LogEntry(this.number++,category,logLevel,System.currentTimeMillis(),throwable,trace,message,items));
            for (Node receiver:this.receivers)
            {
                try
                {
                    receiver.process(packet);
                }
                catch (Throwable t)
                {
                    this.throwablesLog.log(t);
                }
            }
        }
    }
    public ThrowableEvents getThrowablesLog()
    {
        synchronized(this)
        {
            return this.throwablesLog;
        }
    }

}
