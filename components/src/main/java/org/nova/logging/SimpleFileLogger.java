package org.nova.logging;

import java.util.ArrayList;

import org.nova.core.MultiException;
import org.nova.flow.Distributor;
import org.nova.flow.DataPacket;
import org.nova.flow.Node;
import org.nova.flow.SourceQueue;
import org.nova.flow.SourceQueueConfiguration;
import org.nova.flow.ThreadWorkerQueue;
import org.nova.logging.Formatter;
import org.nova.logging.LogDirectoryManager;
import org.nova.logging.LogEntry;
import org.nova.metrics.CountMeter;
import org.nova.metrics.LevelMeter;
import org.nova.metrics.RateMeter;

/* Note
 * A problem of this implementation is that file name ordering is not guaranteed. It relies that file operations are fast enough
 * compared to the logging-in-a-file.
 */

public class SimpleFileLogger extends SourceQueue<LogEntry> 
{
	private static Distributor connect(LogDirectoryManager logDirectoryManager,JSONBufferedLZ4QueueConfiguration configuration) throws Throwable
	{
        CountMeter threadWorkerQueueDroppedMeter=new CountMeter();
        CountMeter threadWorkerQueueStalledMeter=new CountMeter();
        LevelMeter threadWorkerQueueInUseMeter=new LevelMeter();
        LevelMeter threadWorkerQueueWaitingMeter=new LevelMeter();
        RateMeter writeRateMeter=new RateMeter();
        int threads=configuration.writerThreads;
        ThreadWorkerQueue[] queues=new ThreadWorkerQueue[threads];
        JSONFormatter logWriter=new JSONFormatter();

        int maxQueueSize=configuration.writerMaxQueueSize;
        if (maxQueueSize==0)
        {
            maxQueueSize=configuration.writerSegmentSize*threads*2;
        }
        int stallSizeThreshold=configuration.writerStallSizeThreshold;
        if (stallSizeThreshold==0)
        {
            stallSizeThreshold=maxQueueSize-configuration.writerSegmentSize;
        }
        
        for (int i=0;i<threads;i++)
        {
            BufferedLZ4FileWriter writer=new BufferedLZ4FileWriter(logDirectoryManager, configuration.writerBufferInitialCapacity,logWriter,writeRateMeter);
            ThreadWorkerQueue queue=new ThreadWorkerQueue(writer, configuration.writerStallWait,stallSizeThreshold,maxQueueSize,i,threadWorkerQueueStalledMeter,threadWorkerQueueDroppedMeter,threadWorkerQueueInUseMeter,threadWorkerQueueWaitingMeter);
            queue.start();
            queues[i]=queue;
        }
        return new Distributor(null, configuration.writerSegmentSize, false,queues);
	}
	
    final private CountMeter threadWorkerQueueDroppedMeter;
    final private CountMeter threadWorkerQueueStalledMeter;
    final private LevelMeter threadWorkerQueueInUseMeter;
    final private LevelMeter threadWorkerQueueWaitingMeter;
    final private RateMeter writeRateMeter;
    final private BufferedLZ4FileWriter[] writers;
    final private ThreadWorkerQueue[] queues;

    public SimpleFileLogger(LogDirectoryManager logDirectoryManager, JSONBufferedLZ4QueueConfiguration configuration) throws Throwable
	{
		super(connect(logDirectoryManager,configuration),configuration);
        this.writers=new BufferedLZ4FileWriter[configuration.writerThreads];
        this.queues=new ThreadWorkerQueue[configuration.writerThreads];
		Node[] receivers=((Distributor)this.getReceiver()).getReceivers();
		for (int i=0;i<this.writers.length;i++)
		{
		    this.queues[i]=(ThreadWorkerQueue)receivers[i];
		    this.writers[i]=(BufferedLZ4FileWriter)(this.queues[i].getReceiver());
		}
		this.writeRateMeter=this.writers[0].getRateMeter();
		this.threadWorkerQueueDroppedMeter=this.queues[0].getDroppedMeter();
		this.threadWorkerQueueStalledMeter=this.queues[0].getStalledMeter();
		this.threadWorkerQueueInUseMeter=this.queues[0].getThreadInUseMeter();
		this.threadWorkerQueueWaitingMeter=this.queues[0].getWaitingMeter();
		
		this.start();
	}
	
	public Throwable getThrowable()
	{
	    ArrayList<Throwable> throwables=new ArrayList<>();
        for (BufferedLZ4FileWriter writer:this.writers)
        {
            Throwable throwable=writer.getThrowable();
            if (throwable!=null)
            {
                throwables.add(throwable);
            }
        }
        if (throwables.size()>0)
        {
            return new MultiException(throwables);
        }
        return null;
	}

    public CountMeter getThreadWorkerQueueDroppedMeter()
    {
        return threadWorkerQueueDroppedMeter;
    }

    public CountMeter getThreadWorkerQueueStalledMeter()
    {
        return threadWorkerQueueStalledMeter;
    }

    public LevelMeter getThreadWorkerQueueInUseMeter()
    {
        return threadWorkerQueueInUseMeter;
    }

    public LevelMeter getThreadWorkerQueueWaitingMeter()
    {
        return threadWorkerQueueWaitingMeter;
    }

    public RateMeter getWriteRateMeter()
    {
        return writeRateMeter;
    }
}
