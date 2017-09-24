package org.nova.tracing;

public class TraceManagerConfiguration
{
	public int maximumActives=10000;
	public int lastTraceBufferSize=100;
	public boolean enableLastTraces=true;
	public boolean enableWatchListLastTraces=true;
	public int lastExceptionBufferSize=100;
	public boolean enableLastExceptions=true;
	public boolean enableTraceStats=true;
	public boolean enableTraceGraph=true;
	public boolean logTraces=true;
	public boolean logExceptionTraces=true;
    public boolean captureCreateStackTrace=true;
    public boolean captureCloseStackTrace=true;
	public int logSlowTraceDurationMs=1000*30; //30 seconds
	
	
	public TraceManagerConfiguration()
	{
	}

	static public TraceManagerConfiguration NormalApplicationConfiguration()
	{
		TraceManagerConfiguration configuration=new TraceManagerConfiguration();
		configuration.maximumActives=10000;
		configuration.enableLastExceptions=true;
		configuration.enableLastTraces=true;
		configuration.enableTraceGraph=true;
		configuration.enableTraceStats=true;
		configuration.enableWatchListLastTraces=false;
		configuration.lastExceptionBufferSize=100;
		configuration.lastTraceBufferSize=100;
		configuration.logExceptionTraces=true;
		configuration.logTraces=false;
        configuration.captureCreateStackTrace=false;
        configuration.captureCloseStackTrace=false;
		return configuration;
	}
}
