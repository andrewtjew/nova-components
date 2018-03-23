package org.nova.flow;

import java.util.ArrayList;
import org.nova.concurrent.Synchronization;
import org.nova.logging.ThrowablesLog;
import org.nova.metrics.CountMeter;
import org.nova.metrics.LevelMeter;
import org.nova.test.Testing;

public class ThreadWorkerQueue extends Node
{
    final private static boolean TESTING=false;
	final private CountMeter droppedMeter;
	final private CountMeter stalledMeter;
    final private LevelMeter waitingMeter;
    final private int maxQueueSize;
    final private long stallWait;
    final private int stallSizeThreshold;
    final private Node receiver;
    final private Object lock;
    final private ThrowablesLog throwablesLog;
    private Thread thread;
    private ArrayList<Packet> queue;
    private boolean stop;

    final private LevelMeter threadInUseMeter;
    final private int id;
    

	
	public ThreadWorkerQueue(Node receiver,long stallWait,int stallSizeThreshold,int maxQueueSize,int id,CountMeter droppedMeter,CountMeter stalledMeter,LevelMeter threadInUseMeter,LevelMeter waitingMeter)
	{
	    this.throwablesLog=new ThrowablesLog();
		this.droppedMeter=droppedMeter;
		this.stalledMeter=stalledMeter;
		this.threadInUseMeter=threadInUseMeter;
		this.waitingMeter=waitingMeter;
		this.receiver=receiver;
		this.stallWait=stallWait;
		this.stallSizeThreshold=stallSizeThreshold;
		this.maxQueueSize=maxQueueSize;
		this.lock=new Object();
		this.queue=new ArrayList<>();
		this.id=id;
	}

	public void start()
	{
		synchronized(this.lock)
		{
			if (this.thread==null)
			{
				this.stop=false;
				this.thread=new Thread(()->
				{
					main();
				});
				this.thread.start();
			}
		}
	}
	public void stop() 
	{
	    Thread thread;
		synchronized(this.lock)
		{
		    if (this.thread==null)
		    {
		        return;
		    }
		    thread=this.thread;
			this.stop=true;
			this.lock.notify();
			this.thread=null;
		}
	    try
        {
            thread.join();
        }
        catch (InterruptedException e)
        {
        }
	}

    public void flush()
    {
        synchronized (this.lock)
        {
            if (this.stop==true)
            {
                return;
            }
            this.queue.add(Packet.FLUSH_PACKET);
            this.lock.notify();
        }
    }
    
	private void main() 
	{
        this.threadInUseMeter.increment();
        for (;;)
        {
    		try
    		{
    		    if (TESTING)
    		    {
    		        Testing.oprintln("ThreadWorkerQueue="+this.id+":enter");
    		    }
	            ArrayList<Packet> old=null;
				synchronized (this.lock)
				{
		            if (TESTING)
		            {
		                Testing.oprintln("ThreadWorkerQueue="+this.id+":start wait");
		            }
                    this.threadInUseMeter.decrement();
					Synchronization.waitForNoThrow(this.lock, ()->{return this.queue.size()>0||this.stop;});
                    this.threadInUseMeter.increment();
                    if (TESTING)
                    {
                        Testing.oprintln("ThreadWorkerQueue="+this.id+":end wait");
                    }
					if (this.stop)
					{
		                this.threadInUseMeter.decrement();
						return;
					}
					old=this.queue;
					this.queue=new ArrayList<>(old.size()*2);
				}
				for (Packet packet:old)
			    {
				    int size=packet.sizeOrType();
				    if (size<0)
				    {
                        if (size==Packet.BEGIN_SEGMENT)
                        {
                            if (TESTING)
                            {
                                Testing.oprintln("ThreadWorkerQueue="+this.id+":receiver.beginSegment="+packet.getSegmentObject(0));
                            }
                            this.receiver.beginGroup((long)packet.getSegmentObject(0));
                        }
                        else if (size==Packet.END_SEGMENT)
                        {
                            if (TESTING)
                            {
                                Testing.oprintln("ThreadWorkerQueue="+this.id+":receiver.endSegment");
                            }
                            this.receiver.endGroup();
                        }
                        else if (size==Packet.FLUSH)
                        {
                            this.receiver.flush();
                            if (TESTING)
                            {
                                Testing.oprintln("ThreadWorkerQueue="+this.id+":receiver.flush");
                            }
                        }
				    }
			        else 
                    {
	                    this.waitingMeter.add(-size);
                        this.receiver.process(packet);
                    }
			    }
			}
    		catch (Throwable t)
    		{
  			    this.throwablesLog.log(t);
    		}
        }
	}
	
	public ThrowablesLog getThrowablesLog()
	{
        return this.throwablesLog;
	}

    @Override
    public void process(Packet container) throws Throwable
    {
        synchronized (this.lock)
        {
            if (this.stop==true)
            {
                return;
            }
            long size=this.waitingMeter.getLevel();
            if (size>=this.maxQueueSize)
            {
                this.droppedMeter.increment();
                return;
            }
            if (size>=this.stallSizeThreshold)
            {
                try
                {
                    this.lock.wait(this.stallWait);
                }
                catch (InterruptedException e)
                {
                }
                this.stalledMeter.increment();
            }
            this.queue.add(container);
            if (container.sizeOrType()>0)
            {
                this.waitingMeter.add(container.sizeOrType());
            }
            this.lock.notify();
            if (TESTING)
            {
                Testing.oprintln("ThreadWorkerQueue="+this.id+":notify, queue size="+this.queue.size());
            }
        }
    }

    @Override
    public void beginGroup(long marker) throws Throwable
    {
        synchronized (this.lock)
        {
            int size=this.queue.size();
            if (size>=this.stallSizeThreshold)
            {
                try
                {
                    this.lock.wait(this.stallWait);
                }
                catch (InterruptedException e)
                {
                }
                this.stalledMeter.increment();
            }
            if (this.stop==true)
            {
                return;
            }
            this.queue.add(Packet.BeginSegment(marker));
            this.lock.notify();
        }
    }

    @Override
    public void endGroup() throws Throwable
    {
        synchronized (this.lock)
        {
            int size=this.queue.size();
            if (size>=this.stallSizeThreshold)
            {
                try
                {
                    this.lock.wait(this.stallWait);
                }
                catch (InterruptedException e)
                {
                }
                this.stalledMeter.increment();
            }
            if (this.stop==true)
            {
                return;
            }
            this.queue.add(Packet.END_SEGMENT_PACKET);
            this.lock.notify();
        }
    }
    public Node getReceiver()
    {
        return this.receiver;
    }
    
    public CountMeter getDroppedMeter()
    {
        return droppedMeter;
    }

    public CountMeter getStalledMeter()
    {
        return stalledMeter;
    }

    public LevelMeter getThreadInUseMeter()
    {
        return threadInUseMeter;
    }
    public LevelMeter getWaitingMeter()
    {
        return this.waitingMeter;
    }

    
}
