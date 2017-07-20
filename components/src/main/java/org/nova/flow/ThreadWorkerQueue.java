package org.nova.flow;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;

import org.nova.concurrent.Synchronization;
import org.nova.metrics.CountMeter;
import org.nova.metrics.LevelMeter;
import org.nova.test.Testing;

public class ThreadWorkerQueue extends Node
{
	final private CountMeter droppedMeter;
	final private CountMeter stalledMeter;
    final private LevelMeter threadInUseMeter;
    final private LevelMeter waitingMeter;
    final private long stallWait;
    final private int stallSizeThreshold;
    final private int maxQueueSize;
    final private int id;

    final private Node receiver;
    final private Object lock;
    private Thread thread;

	private ArrayList<Packet> queue;
	
	private Throwable throwable;
	private boolean stop;
	
	public ThreadWorkerQueue(Node receiver,long stallWait,int stallSizeThreshold,int maxQueueSize,int id,CountMeter droppedMeter,CountMeter stalledMeter,LevelMeter threadInUseMeter,LevelMeter waitingMeter)
	{
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
                Testing.setTimeBase(System.currentTimeMillis());
				this.stop=false;
				this.thread=new Thread(()->
				{
					main();
				});
				this.thread.start();
			}
		}
	}
	public void stop() throws InterruptedException
	{
		synchronized(this.lock)
		{
			if (this.thread!=null)
			{
				this.stop=true;
				this.lock.notify();
				this.thread.join();
				this.thread=null;
			}
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
		try
		{
            Testing.oprintln("ThreadWorkerQueue="+this.id+":enter");
			for (;;)
			{
	            ArrayList<Packet> old=null;
				synchronized (this.lock)
				{
                    Testing.oprintln("ThreadWorkerQueue="+this.id+":start wait");
                    this.threadInUseMeter.decrement();
					Synchronization.waitForNoThrow(this.lock, ()->{return this.queue.size()>0||this.stop;});
                    this.threadInUseMeter.increment();
                    Testing.oprintln("ThreadWorkerQueue="+this.id+":end wait");
					if (this.stop)
					{
						return;
					}
					old=this.queue;
					this.queue=new ArrayList<>(old.size()*2);
				}
				for (Packet packet:old)
			    {
				    int size=packet.size();
				    if (size<0)
				    {
                        if (size==Packet.BEGIN_SEGMENT)
                        {
                            Testing.oprintln("ThreadWorkerQueue="+this.id+":receiver.beginSegment="+packet.get()[0]);
                            this.receiver.beginSegment((long)packet.get()[0]);
                        }
                        else if (size==Packet.END_SEGMENT)
                        {
                            Testing.oprintln("ThreadWorkerQueue="+this.id+":receiver.endSegment");
                            this.receiver.endSegment();
                        }
                        else if (size==Packet.FLUSH)
                        {
                            this.receiver.flush();
                            Testing.oprintln("ThreadWorkerQueue="+this.id+":receiver.flush");
                        }
				    }
			        else 
                    {
	                    this.waitingMeter.add(-size);
                        this.receiver.process(packet);
                    }
			    }
			}
		}
		catch (Throwable t)
		{
			synchronized (this.lock)
			{
				this.throwable=t;
			}			
		}
		finally
		{
		    this.threadInUseMeter.decrement();
		}
	}
	
	public Throwable getThrowable()
	{
        synchronized (this.lock)
        {
            return this.throwable;
        }           
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
            if (container.size()>0)
            {
                this.waitingMeter.add(container.size());
            }
            this.lock.notify();
            Testing.oprintln("ThreadWorkerQueue="+this.id+":notify, queue size="+this.queue.size());
        }
    }

    @Override
    public void beginSegment(long marker) throws Throwable
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
    public void endSegment() throws Throwable
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
