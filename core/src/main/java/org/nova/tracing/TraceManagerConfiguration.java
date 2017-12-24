package org.nova.tracing;

public class TraceManagerConfiguration
{
	public int maximumActives=2000;
    public int lastTraceBufferSize=2000;
    public int watchListLastTraceBufferSize=2000;
	public boolean enableLastTraceWatching=false;
	public int lastExceptionBufferSize=2000;
	public boolean logTraces=true;
	public boolean logExceptionTraces=true;
    public boolean captureCreateStackTrace=false;
    public boolean captureCloseStackTrace=false;
	public int logSlowTraceDurationMs=-1; //disabled
	
	
	public TraceManagerConfiguration()
	{
	}

	/*
	static public TraceManagerConfiguration NormalApplicationConfiguration()
	{
		TraceManagerConfiguration configuration=new TraceManagerConfiguration();
		configuration.maximumActives=10000;
		configuration.enableLastTraceWatching=false;
		configuration.lastExceptionBufferSize=100;
        configuration.lastTraceBufferSize=100;
        configuration.watchListLastTraceBufferSize=100;
		configuration.logExceptionTraces=true;
		configuration.logTraces=false;
        configuration.captureCreateStackTrace=true;
        configuration.captureCloseStackTrace=false;
        configuration.logSlowTraceDurationMs=-1000;
		return configuration;
	}
	*/
}
