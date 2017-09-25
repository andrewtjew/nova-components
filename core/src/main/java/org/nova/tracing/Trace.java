package org.nova.tracing;

public class Trace implements AutoCloseable
{
	private TraceManager traceManager;
	final private String category;
	final private long created;
	final private Trace parent;
	final long start;
	private long duration;
	private long wait;
	private long waitStart;
	private Throwable throwable;
	private String details;
	private Object state;
	boolean waiting;
	private Thread thread;
	private String toLink;
	private String fromLink;
	TraceNode traceNode;
	final private StackTraceElement[] createStackTrace;
	private StackTraceElement[] closeStackTrace;
	private TraceContext context;
	
	public Trace(TraceManager traceManager,Trace parent,String category,String details,boolean waiting)
	{
		this.thread=Thread.currentThread();
		this.traceManager=traceManager;
		this.parent=parent;
		this.category=category;
		this.details=details;
		this.created=System.currentTimeMillis();
		this.waitStart=this.start=System.nanoTime();
		this.waiting=waiting;
		this.context=this.traceManager.open(this);
		if (this.context.captureCreateStack)
		{
			this.createStackTrace=this.thread.getStackTrace();
		}
		else
		{
			this.createStackTrace=null;
		}
		if (this.context.enableTraceGraph)
		{
		    this.traceNode=traceManager.getTraceNode(category, parent);
		}
	}
	public Trace(TraceManager traceManager,Trace parent,String category,boolean waiting)
	{
		this(traceManager,parent,category,null,waiting);
	}
	
	public Trace(TraceManager traceManager,String category,boolean waiting)
	{
		this(traceManager,null,category,null,waiting);
	}
	public Trace(TraceManager traceManager,Trace parent,String category)
	{
		this(traceManager,parent,category,false);
	}
    public Trace(Trace parent,String category,boolean waiting)
    {
        this(parent.traceManager,parent,category,waiting);
    }
    public Trace(Trace parent,String category)
    {
        this(parent,category,false);
    }
	public Trace(TraceManager traceManager,String category)
	{
		this(traceManager,null,category,null,false);
	}

	public Trace(TraceManager traceManager,String category,String details,boolean waiting)
	{
		this(traceManager,null,category,details,waiting);
	}
	public Trace(TraceManager traceManager,Trace parent,String category,String details)
	{
		this(traceManager,parent,category,details,false);
	}
	public Trace(TraceManager traceManager,String category,String details)
	{
		this(traceManager,null,category,details,false);
	}
	
	public String getCategory()
	{
		return category;
	}
	

	public long getNumber()
	{
		return this.context.number;
	}
	
	public boolean isClosed()
	{
		synchronized (this)
		{
			return this.traceManager==null;
		}
	}
	public long getCreated()
	{
		return this.created;
	}
	public String getDetails()
	{
		synchronized(this)
		{
			return this.details;
		}
	}
	public void setDetails(String details)
	{
		synchronized(this)
		{
			this.details=details;
		}
	}
	
	public Object getState()
	{
		synchronized(this)
		{
			return state;
		}
	}
	public void setState(Object state)
	{
		synchronized(this)
		{
			this.state = state;
		}
	}
	public void close(Throwable t)
	{
		synchronized(this)
		{
		    if ((t!=null)&&(this.throwable==null))
		    {
		        this.throwable=t;
		    }
			if (this.traceManager!=null)
			{
				if (this.context.captureCloseStack)
				{
					this.closeStackTrace=this.thread.getStackTrace();
				}
				this.traceManager.close(this);
				if (this.waiting)
				{
					this.wait+=System.nanoTime()-this.waitStart;
				}
				else
				{
					this.duration=System.nanoTime()-this.start;
				}
				this.traceManager=null;
				if (this.context.valueRateMeter!=null)
				{
				    this.context.valueRateMeter.update(System.nanoTime()-this.start);
				}
			}
		}
	}
    @Override
    public void close() 
    {
        close(null);
    }
	public boolean isWaiting()
	{
		synchronized (this)
		{
			return this.waiting;
		}
	}
	public boolean beginWait()
	{
		synchronized(this)
		{
			if (this.waiting)
			{
				return false;
			}
			this.waiting=true;
			this.waitStart=System.nanoTime();
			return true;
		}		
	}
	public boolean endWait()
	{
		synchronized(this)
		{
			if (this.waiting==false)
			{
				return false;
			}
			this.wait+=System.nanoTime()-this.waitStart;
			this.waiting=false;
			return true;
		}		
	}

	public long getDurationNs()
	{
		synchronized(this)
		{
			if (this.traceManager!=null)
			{
				return System.nanoTime()-this.start;
			}
			return this.duration;
		}
	}
	public long getActiveNs()
	{
		synchronized(this)
		{
			if (this.traceManager!=null)
			{
				if (this.waiting)
				{
					return this.waitStart-this.start-this.wait;
				}
				return System.nanoTime()-this.start-this.wait;
			}
			return this.duration;
		}
	}
	public long getWaitNs()
	{
		synchronized(this)
		{
			if (this.traceManager!=null)
			{
				if (this.waiting)
				{
					return System.nanoTime()-this.waitStart+this.wait;
				}
			}
			return this.wait;
		}
	}
	public double getWaitS()
	{
		return ((double)getWaitNs())/1.0e9;
	}
    public double getActiveS()
    {
        return ((double)getActiveS())/1.0e9;
    }
    public double getDurationS()
    {
        return ((double)getDurationNs())/1.0e9;
    }
	
	public Throwable getThrowable()
	{
		synchronized(this)
		{
			return this.throwable;
		}
	}
	public Trace getParent()
	{
		return this.parent;
	}
	public Thread getThread()
	{
		synchronized (this)
		{
			return this.thread;
		}
	}
	public String getFromLink()
	{
		synchronized (this)
		{
			return this.fromLink;
		}
	}
	public String getToLink()
	{
		synchronized (this)
		{
			return this.toLink;
		}
	}
	public void setFromLink(String fromLink)
	{
		synchronized (this)
		{
			this.toLink=fromLink;
		}
	}
	public void setToLink(String toLink)
	{
		synchronized (this)
		{
			this.toLink=toLink;
		}
	}
	public StackTraceElement[] getCreateStackTrace()
	{
		synchronized (this)
		{
			return this.createStackTrace;
		}
	}
	public StackTraceElement[] getCloseStackTrace()
	{
		synchronized (this)
		{
			return this.closeStackTrace;
		}
	}
	
	@SuppressWarnings("resource")
    public Trace getRoot()
	{
		Trace trace=this;
		while (trace.getParent()!=null)
		{
		    trace=trace.getParent();
		}
		return trace;
	}
	
	public Trace find(String cateogory)
	{
	    for (Trace trace=this;trace!=null;trace=trace.getParent())
	    {
	        if ((category==null)&&(trace.getCategory()==null))
	        {
	            return trace;
	        }
	        else if (category.equals(trace.getCategory()))
	        {
	            return trace;
	        }
	    }	
	    return null;
	}
}
