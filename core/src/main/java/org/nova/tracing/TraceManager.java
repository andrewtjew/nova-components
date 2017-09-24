package org.nova.tracing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;

import org.nova.annotations.Description;
import org.nova.logging.Logger;
import org.nova.metrics.CountMeter;
import org.nova.metrics.ValueRateMeter;
import org.nova.metrics.ValueRateSample;
import org.nova.operations.OperatorVariable;

// Don't track Trace in Thread Local Store. The problem is figuring out after closing a Trace which trace to make current
public class TraceManager
{
	final private HashMap<Long,Trace> traces;
	final private HashMap<String,ValueRateMeter> meters;
	final private HashMap<String,TraceNode> traceRoots;
	final private TraceBuffer lastExceptions;
	final private TraceBuffer lastTraces;
	final private HashSet<String> watchList;
	
	private Logger logger;
	private long number;
	final private int maximumActives;

	@Description("The number of times maximum actives is exceeded. Traces exceeding the maximum are not tracked.")
	final private CountMeter maximumExceeded=new CountMeter();
	
    @OperatorVariable(description="Capture the stackTrace when trace is closed. WARNING: Application performance may drop.")
    private boolean captureCreateStackTrace;

    @OperatorVariable(description="Capture the stackTrace when trace is closed. WARNING: Application performance may drop.")
    private boolean captureCloseStackTrace;
	@OperatorVariable(description="Log traces. WARNING: Application performance may drop. Logger may become stressed.")
	private boolean logTraces;
	@OperatorVariable(description="Log exception traces. WARNING: Logger may become stressed.")
	private boolean logExceptionTraces;
	@OperatorVariable(description="Enable trace graph. WARNING: Application performance may drop.")
	private boolean enableTraceGraph;
	@OperatorVariable(description="Enable trace stats.")
	private boolean enableTraceStats;
	@OperatorVariable(description="Enable last exceptions to be captured.")
	private boolean enableLastExceptions;
	@OperatorVariable(description="Enable last traces in category watchlist to be captured. WARNING: Application performance may drop.")
	private boolean enableWatchListLastTraces;

    @OperatorVariable(description="Enable all last traces to be captured. WARNING: Application performance may drop.")
    private boolean enableLastTraces;
    
    @OperatorVariable(description="Log traces with greater duration in milliseconds. A negative value disables this form of logging. WARNING: Application performance may drop for small durations.")
    private long logTracesWithGreaterDuration;
	
	public TraceManager(Logger logger,TraceManagerConfiguration configuration)
	{
		this.traces=new HashMap<>();
		this.meters=new HashMap<>();
		this.traceRoots=new HashMap<>();
		if (configuration.lastExceptionBufferSize>0)
		{
			this.lastExceptions=new TraceBuffer(configuration.lastExceptionBufferSize);
		}
		else
		{
			this.lastExceptions=null;
		}
		if (configuration.lastTraceBufferSize>0)
		{
			this.lastTraces=new TraceBuffer(configuration.lastTraceBufferSize);
			this.watchList=new HashSet<>();
		}
		else
		{
			this.lastTraces=null;
			this.watchList=null;
		}
		this.enableLastExceptions=configuration.enableLastExceptions;
		this.logExceptionTraces=configuration.logExceptionTraces;
		this.logTraces=configuration.logTraces;
		this.enableTraceGraph=configuration.enableTraceGraph;
		this.enableTraceStats=configuration.enableTraceStats;
		this.enableLastTraces=configuration.enableLastTraces;
		this.enableWatchListLastTraces=configuration.enableWatchListLastTraces;
        this.captureCreateStackTrace=configuration.captureCreateStackTrace;
        this.captureCloseStackTrace=configuration.captureCloseStackTrace;
		this.maximumActives=configuration.maximumActives;
		this.logTracesWithGreaterDuration=configuration.logSlowTraceDurationMs;
		this.logger=logger;
	}
	public TraceManager(Logger logger)
	{
//        this(logger, new TraceManagerConfiguration());
        this(logger, TraceManagerConfiguration.NormalApplicationConfiguration());
	}
	public TraceManager()
	{
		this(null);
	}
	
	
	TraceSettings open(Trace trace)
	{
		synchronized(this.traces)
		{
			long number=this.number++;
			if (this.traces.size()<this.maximumActives)
			{
				this.traces.put(number, trace);
			}
			else
			{
				this.maximumExceeded.increment();
			}
			return new TraceSettings(number,this.captureCreateStackTrace,this.captureCloseStackTrace);
		}
	}
	
	void close(Trace trace)
	{
		boolean enableTraceGraph;
		boolean enableTraceStats;
		boolean logTraces;
		boolean logExceptionTraces;
		boolean enableLastExceptions;
		boolean enableWatchListLastTraces;
		boolean enableLastTraces;
		long logTracesWithGreaterDuration;
		synchronized(this.traces)
		{
			this.traces.remove(trace.getNumber());
			enableTraceStats=this.enableTraceStats;
			enableTraceGraph=this.enableTraceGraph;
			logExceptionTraces=this.logExceptionTraces;
			enableLastExceptions=this.enableLastExceptions;
			logTraces=this.logTraces;
			enableWatchListLastTraces=this.enableWatchListLastTraces;
			enableLastTraces=this.enableLastTraces;
			logTracesWithGreaterDuration=this.logTracesWithGreaterDuration;
		}
		if (enableLastTraces)
		{
			if (this.lastTraces!=null)
			{
				this.lastTraces.add(trace);
			}			
		} 
		else if (enableWatchListLastTraces)
		{
			if (this.lastTraces!=null)
			{
				if (this.watchList.contains(trace.getCategory()))
				{
					this.lastTraces.add(trace);
				}
			}			
		}
		
		if (enableLastExceptions)
		{
			if (this.lastExceptions!=null)
			{
				if (trace.getThrowable()!=null)
				{
					synchronized (this.lastExceptions)
					{
						this.lastExceptions.add(trace);
					}
				}
			}
		}
		
		if (enableTraceStats)
		{
			if (this.enableTraceStats)
			{
			    synchronized (traces)
			    {
    				ValueRateMeter meter=this.meters.get(trace.getCategory());
    				if (meter==null)
    				{
    				    meter=new ValueRateMeter();
    					this.meters.put(trace.getCategory(), meter);
    				}
    				meter.update(trace.getDurationNs());
			    }
			}
			
		}
		if (enableTraceGraph)
		{
			int count=1;
			for (Trace t=trace.getParent();t!=null;t=t.getParent())
			{
				count++;
			}
            Trace[] traces=new Trace[count];
			int index=traces.length-1;
			for (Trace t=trace;t!=null;t=t.getParent())
			{
				traces[index]=t;
				index--;
			}
            TraceNode node;
			synchronized(this.traceRoots)
			{
				Trace rootTrace=traces[0];
				node=this.traceRoots.get(rootTrace.getCategory());
				if (node==null)
				{
					node=new TraceNode();
					this.traceRoots.put(rootTrace.getCategory(),node);
				}
				for (int i=1;i<traces.length;i++)
				{
					Trace childTrace=traces[i];
					TraceNode childNode;
					if (node.childTraces==null)
					{
						node.childTraces=new HashMap<>();
						childNode=new TraceNode();
						node.childTraces.put(childTrace.getCategory(), childNode);
					}
					else
					{
						childNode=node.childTraces.get(childTrace.getCategory());
						if (childNode==null)
						{
							childNode=new TraceNode();
							node.childTraces.put(childTrace.getCategory(), childNode);
						}
					}
					node=childNode;
				}
			}
            node.update(trace);
		}

		if (logTraces||logExceptionTraces||((logTracesWithGreaterDuration>=0)&&(trace.getDurationNs()>=logTracesWithGreaterDuration*1000000)))
		{
			if (this.logger!=null)
			{
				this.logger.log(trace);
			}
		}
	}
	
	public Trace[] getActiveSnapshot()
	{
		synchronized (traces)
		{
			return this.traces.values().toArray(new Trace[this.traces.size()]);
		}
	}
	public TraceSample[] sampleTraceMeters()
	{
		synchronized (traces)
		{
			try
			{
			    ArrayList<TraceSample> results=new ArrayList<>(this.meters.size());
			    for (Entry<String, ValueRateMeter> entry:this.meters.entrySet())
			    {
			        ValueRateSample result=entry.getValue().sample();
			        if (result.getCount()>0)
			        {
			            results.add(new TraceSample(entry.getKey(),result));
			        }
			    }
			    return results.toArray(new TraceSample[results.size()]);
			}
			finally
			{
				this.meters.clear();
			}
		}
	}
	public Map<String,TraceNode> getTraceRootSnapshot()
	{
		HashMap<String,TraceNode> traceRoots=new HashMap<>();
		synchronized (this.traceRoots)
		{
		    traceRoots.putAll(this.traceRoots);
		}
		return traceRoots;
		
	}
	public boolean isCaptureCreateStackTrace()
	{
		synchronized(this.traces)
		{
			return this.captureCreateStackTrace;
		}
	}
	public void setCaptureCreateStackTrace(boolean captureCreateStackTrace)
	{
		synchronized(this.traces)
		{
			this.captureCreateStackTrace=captureCreateStackTrace;
		}
	}
    public boolean isCaptureCloseStackTrace()
    {
        synchronized(this.traces)
        {
            return this.captureCloseStackTrace;
        }
    }
    public void setCaptureCloseStackTrace(boolean captureCloseStackTrace)
    {
        synchronized(this.traces)
        {
            this.captureCloseStackTrace=captureCloseStackTrace;
        }
    }
	public boolean isLogTraces()
	{
		synchronized(this.traces)
		{
			return logTraces;
		}
	}
	public void setLogTraces(boolean logTraces)
	{
		synchronized(this.traces)
		{
			this.logTraces = logTraces;
		}
	}
	public boolean isLogExceptionTraces()
	{
		synchronized(this.traces)
		{
			return logExceptionTraces;
		}
	}
	public void setLogExceptionTraces(boolean logExceptionTraces)
	{
		synchronized(this.traces)
		{
			this.logExceptionTraces = logExceptionTraces;
		}
	}
	public boolean isEnableTraceGraph()
	{
		synchronized(this.traces)
		{
			return enableTraceGraph;
		}
	}
	public void setEnableTraceGraph(boolean enableTraceGraph)
	{
		synchronized(this.traces)
		{
			this.enableTraceGraph = enableTraceGraph;
		}
	}
	public boolean isEnableTraceStats()
	{
		synchronized(this.traces)
		{
			return enableTraceStats;
		}
	}
	public void setEnableTraceStats(boolean enableTraceStats)
	{
		synchronized(this.traces)
		{
			this.enableTraceStats = enableTraceStats;
		}
	}
	public boolean isEnableLastExceptions()
	{
		synchronized(this.traces)
		{
			return enableLastExceptions;
		}
	}
	public void setEnableLastExceptions(boolean enableLastExceptions)
	{
		synchronized(this.traces)
		{
			this.enableLastExceptions = enableLastExceptions;
		}
	}
	public boolean isEnableWatchListLastTraces()
	{
		synchronized(this.traces)
		{
			return enableWatchListLastTraces;
		}
	}
	public void setEnableWatchListLastTraces(boolean enableWatchListLastTraces)
	{
		synchronized(this.traces)
		{
			this.enableWatchListLastTraces = enableWatchListLastTraces;
		}
	}
	public boolean isEnableLastTraces()
	{
		synchronized(this.traces)
		{
			return enableLastTraces;
		}
	}
	public void setEnableLastTraces(boolean enableLastTraces)
	{
		synchronized(this.traces)
		{
			this.enableLastTraces = enableLastTraces;
		}
	}
	
	public void setLogTracesWithGreaterDuration(long durationMs)
	{
	    synchronized(this.traces)
	    {
	        this.logTracesWithGreaterDuration=durationMs;
	    }
	}
	public long getLogTracesWithGreaterDuration()
	{
        synchronized(this.traces)
        {
            return this.logTracesWithGreaterDuration;
        }
	}
	
	public Trace[] getLastExceptionTraces()
	{
		if (this.lastExceptions!=null)
		{
			synchronized(this.lastExceptions)
			{
				return this.lastExceptions.getSnapshot();
			}
		}
		return new Trace[0];
	}
	
	public Trace[] getLastTraces()
	{
		if (this.lastTraces!=null)
		{
			synchronized(this.lastTraces)
			{
			    return this.lastTraces.getSnapshot();
			}
		}
		return new Trace[0];
	}
	
	
}
