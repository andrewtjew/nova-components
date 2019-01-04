package org.nova.http.server;

public class RequestHandlerLatencyTracker extends LatencyTracker
{
    final private RequestHandler requestHandler;
    public RequestHandlerLatencyTracker(RequestHandler requestHandler)
    {
        super(new LatencyDescriptor(false,0,0,0,0));
        this.requestHandler=requestHandler;
    }
    
    public RequestHandler getRequestHandler()
    {
        return this.requestHandler;
    }
}