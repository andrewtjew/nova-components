package org.nova.http.server;

import java.util.HashMap;
import java.util.Random;

import org.nova.tracing.Trace;
import org.nova.tracing.TraceWaiter;
import org.nova.utils.MathUtils;

public class LatencyFilter extends Filter
{
    final private HashMap<String,RequestHandlerLatencyTracker> trackers;
    private boolean enabled;
    private LatencyDescriptor overrideLatencyDescriptor;
    final private Random random;
    
    public LatencyFilter()
    {
        this.trackers=new HashMap<>();
        this.random=new Random();
    }
    
    public synchronized void setEnabled(boolean enabled)
    {
        this.enabled=enabled; 
    }
    
    public synchronized boolean isEnabled()
    {
        return this.enabled;
    }
    
    public synchronized void setOverrideLatencyDescriptor(LatencyDescriptor overrideLatencyDescriptor)
    {
        this.overrideLatencyDescriptor=overrideLatencyDescriptor;
    }
    
    public synchronized LatencyDescriptor getOverrideLatencyDescriptor()
    {
        return this.overrideLatencyDescriptor;
    }
    
//    public synchronized void setLatency(String key,LatencyDescriptor latencyDescriptor)
//    {
//        RequestHandlerLatencyTracker tracker=this.trackers.get(key);
//        if (tracker!=null)
//        {
//            tracker.setLatencyDescriptor(latencyDescriptor);
//        }
//    }
    
    public RequestHandlerLatencyTracker getRequestHandlerLatencyTracker(String key)
    {
        return this.trackers.get(key);
    }
    
    @Override
    public Response<?> executeNext(Trace trace, Context context, FilterChain filterChain) throws Throwable
    {
        long beforeLatencyMs=0;
        long afterLatencyMs=0;
        synchronized(this)
        {
            if (this.enabled)
            {
                RequestHandlerLatencyTracker tracker=this.trackers.get(context.getRequestHandler().getKey());
                if (tracker!=null)
                {
                    LatencyDescriptor descriptor=(this.overrideLatencyDescriptor!=null)&&(this.overrideLatencyDescriptor.isEnabled())?this.overrideLatencyDescriptor:tracker.getLatencyDescriptor();
                    if ((descriptor!=null)&&(descriptor.isEnabled()))
                    {
                        beforeLatencyMs=descriptor.getBeforeExecuteMinimumMs();
                        long varianceMs=descriptor.getBeforeExecuteMaximumMs()-descriptor.getBeforeExecuteMinimumMs();
                        if (varianceMs>0)
                        {
                            beforeLatencyMs+=MathUtils.mod(this.random.nextLong(), varianceMs);
                        }
                        afterLatencyMs=descriptor.getAfterExecuteMinimumMs();
                        varianceMs=descriptor.getAfterExecuteMaximumMs()-descriptor.getAfterExecuteMinimumMs();
                        if (varianceMs>0)
                        {
                            afterLatencyMs+=MathUtils.mod(this.random.nextLong(), varianceMs);
                        }
                        tracker.update(beforeLatencyMs, afterLatencyMs);
                    }
                }
            }
        }
        if (beforeLatencyMs>0)
        {
            try (TraceWaiter waiter=new TraceWaiter(trace))
            {
                Thread.sleep(beforeLatencyMs);
            }
        }
        Response<?> response=filterChain.next(trace, context);
        if (afterLatencyMs>0)
        {
            try (TraceWaiter waiter=new TraceWaiter(trace))
            {
                Thread.sleep(afterLatencyMs);
            }
        }
        return response;
    }

    @Override
    public void onRegister(RequestHandler requestHandler)
    {
        this.trackers.put(requestHandler.getKey(), new RequestHandlerLatencyTracker(requestHandler));
    }
    
    synchronized public RequestHandlerLatencyTracker[] getSnapshot()
    {
        return this.trackers.values().toArray(new RequestHandlerLatencyTracker[this.trackers.size()]);
    }
    
}
