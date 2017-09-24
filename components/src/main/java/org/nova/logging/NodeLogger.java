package org.nova.logging;

import org.nova.flow.Node;
import org.nova.flow.Packet;
import org.nova.tracing.Trace;

public class NodeLogger extends Logger
{
    final private Node receiver;
    private Throwable exception; 
    private long number;
    
    public NodeLogger(Node receiver,String category)
    {
        super(category);
        this.receiver=receiver;
    }

    @Override
    public void log(Trace trace, Level logLevel, String category, Throwable throwable, String message, Item[] items)
    {
        synchronized (this)
        {
            Packet packet=new Packet(1);
            packet.add(new LogEntry(this.number++,category,logLevel,System.currentTimeMillis(),throwable,trace,message,items));
            try
            {
                this.receiver.process(packet);
            }
            catch (Throwable t)
            {
                if (this.exception==null)
                {
                    this.exception=t;
                }
            }
        }
    }
    public Throwable getException()
    {
        synchronized(this)
        {
            return this.exception;
        }
    }

}
