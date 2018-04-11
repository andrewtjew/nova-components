package org.nova.metrics;

import java.util.Random;

import org.nova.html.Chartjs.BarChart;
import org.nova.html.tags.var;

public class ThreadExecutionMeter
{
    final private Thread targetThread;
    final private long minimumSamplingIntervalNs;
    final private long maximumSamplingIntervalNs;
    final private int measurementsPerSample;
    final private long samplingDurationNs; 
    final private StackTraceNode root;
    final private Random random;
    final private boolean spinWait;
    boolean exit;
    private Thread samplingThread;
    final private int samplingThreadPriorityLevel; 
    
    public ThreadExecutionMeter(Thread targetThread,boolean spinWait,long minimumSamplingIntervalNs,long maximumSamplingIntervalNs,long samplingDurationNs,int measurementsPerSample,int samplingThreadPriorityLevel)
    {
        this.targetThread=targetThread;
        this.samplingDurationNs=samplingDurationNs;
        this.minimumSamplingIntervalNs=minimumSamplingIntervalNs;
        this.maximumSamplingIntervalNs=maximumSamplingIntervalNs;
        this.measurementsPerSample=measurementsPerSample;
        this.spinWait=spinWait;
        this.samplingThreadPriorityLevel=samplingThreadPriorityLevel;
        if (minimumSamplingIntervalNs>minimumSamplingIntervalNs)
        {
            this.random=new Random();
        }
        else
        {
            this.random=null;
        }
        this.root=new StackTraceNode(null);
        
    }
    public void start()
    {
        this.samplingThread=new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                main();
            }
        });
        this.samplingThread.start();
    }
    
    public boolean stop(long waitMs) throws InterruptedException
    {
        synchronized(this)
        {
            if (this.samplingThread==null)
            {
                return false;
            }
            if (this.samplingThread.isAlive())
            {
                this.exit=true;
                this.notify();
            }
        }
        this.samplingThread.join(waitMs);
        return this.samplingThread.isAlive()==false;
    }
    
    public ThreadExecutionSample sample()
    {
        synchronized(this)
        {
            return new ThreadExecutionSample(this.exit,this.root);
        }
    }
    
    
    private void main()
    {
        try
        {
            long base=System.nanoTime();
            long varianceNs=this.maximumSamplingIntervalNs-this.maximumSamplingIntervalNs;
            long start=System.nanoTime();
            Thread.currentThread().setPriority(this.samplingThreadPriorityLevel);
            if (this.spinWait)
            {
                for (;;)
                {
                    long sleepNs=this.minimumSamplingIntervalNs;
                    if (sleepNs>0)
                    {
                        if (varianceNs>0)
                        {
                            sleepNs+=varianceNs;
                        }
                        while (System.nanoTime()-start<sleepNs)
                        {
                            synchronized(this)
                            {
                                if (this.exit)
                                {
                                    return;
                                }
                            }
                        }
                    }
                    synchronized(this)
                    {
                        for (int i=0;i<this.measurementsPerSample;i++)
                        {
                            StackTraceElement[] stackTrace=this.targetThread.getStackTrace();
                            if (stackTrace.length==0)
                            {
                                return;
                            }
                            long end=System.nanoTime();
                            long durationNs=end-start;
                            start=end;
                            this.root.update(durationNs, stackTrace);
                        }
                    }
                    if (this.samplingDurationNs>0)
                    {
                        if (start-base>this.samplingDurationNs)
                        {
                            return;
                        }
                    }
                }
                
            }
            else
            {
                for (;;)
                {
                    long sleepNs=this.minimumSamplingIntervalNs;
                    synchronized(this)
                    {
                        if (sleepNs>0)
                        {
                            if (varianceNs>0)
                            {
                                sleepNs+=varianceNs;
                            }
                            long millis=sleepNs/1000000;
                            int nanos=(int)(sleepNs%1000000);
                            try
                            {
                                this.wait(millis,nanos);
                            }
                            catch (InterruptedException e)
                            {
                            }
                            if (this.exit)
                            {
                                return;
                            }
                        }
                        for (int i=0;i<this.measurementsPerSample;i++)
                        {
                            StackTraceElement[] stackTrace=this.targetThread.getStackTrace();
                            if (stackTrace.length==0)
                            {
                                return;
                            }
                            long end=System.nanoTime();
                            long durationNs=end-start;
                            start=end;
                            this.root.update(durationNs, stackTrace);
                        }
                    }
                    if (this.samplingDurationNs>0)
                    {
                        if (start-base>this.samplingDurationNs)
                        {
                            return;
                        }
                    }
                }
            }
        }
        finally
        {
            synchronized(this)
            {
                this.exit=true;
                this.notify();
            }
        }
    }
}
;