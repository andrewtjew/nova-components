package org.nova.metrics;

import org.nova.collections.RingBuffer;

public class LastEventOccuranceMeter
{
    final private RingBuffer<Long> instants;
    
    public LastEventOccuranceMeter(int numberOfSamples)
    {
        this.instants=new RingBuffer<Long>(new Long[numberOfSamples]);
    }
    
    public void recordEventInstant()
    {
        synchronized(this)
        {
            this.instants.add(System.nanoTime());
        }
    }
    
    public boolean eventsOccurWithin(int numberOfEvents,double lastTimespan)
    {
        synchronized(this)
        {
            long now=System.nanoTime();
            if (this.instants.size()<numberOfEvents)
            {
                return false;
            }
            try
            {
                long oldest=this.instants.getFromStart(0);
                long span=now-oldest;
                return span<=lastTimespan*1e9;
            }
            catch (Throwable t)
            {
                //Should never occur.
                throw new RuntimeException(t);
            }
            
        }
    }
    public boolean allEventsOccurWithinLast(double lastTimespan)
    {
        return eventsOccurWithin(this.instants.getCapacity(),lastTimespan);
    }
    
    
}
