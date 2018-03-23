package org.nova.concurrent;

import java.util.HashMap;

import org.nova.metrics.LevelMeter;

public class SingleWaiterBoard
{
    private final HashMap<String,Message> messages;
    
    private final LevelMeter actives;
    
    public SingleWaiterBoard()
    {
        this.messages=new HashMap<>();
        this.actives=new LevelMeter();
        
    }
    
    public boolean post(String name,Object content,long lifeTimeMs)
    {
        Message message;
        synchronized(this)
        {
            message=this.messages.get(name);
            if (message!=null)
            {
                return false;
            }
            message=new Message(content);
            this.messages.put(name, message);
            this.actives.increment();
            this.notifyAll();
        }
        synchronized(message)
        {
            final Message m=message;
            if (Synchronization.waitForNoThrow(message, ()->{return m.receivedMs!=null;},lifeTimeMs)==false)
            {
                synchronized(this)
                {
                    if (message.receivedMs!=null)
                    {
                        return true;
                    }
                    this.messages.remove(name);
                    this.actives.decrement();
                    return false;
                }                
            }
            return true;
        }
    }

    public Message waitForMessage(String name,long waitMs)
    {
        Message message;
        synchronized (this)
        {
            if (Synchronization.waitForNoThrow(this, ()->{return this.messages.containsKey(name);},waitMs)==false)
            {
                return null;
            }                
            message=this.messages.remove(name);
            this.actives.decrement();
        }
        synchronized(message)
        {
            message.receivedMs=System.currentTimeMillis();
            message.notify();
        }
        return message;
    }
    
}
