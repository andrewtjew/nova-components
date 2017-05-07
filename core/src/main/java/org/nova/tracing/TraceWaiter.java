package org.nova.tracing;

public class TraceWaiter implements AutoCloseable
{
    final private Trace trace;
    public TraceWaiter(Trace trace) throws Exception
    {
        if (trace.beginWait()!=true)
        {
            throw new Exception();
        }
        this.trace=trace;
    }

    @Override
    public void close() throws Exception
    {
        this.trace.endWait();
    }
    
}
