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
	public boolean captureStackTrace=true;
	public int logSlowTraceDurationMs=1000*30; //30 seconds
	
	
	public TraceManagerConfiguration()
	{
	}

	static public TraceManagerConfiguration getPerformanceConfiguration(int bufferSize)
	{
		TraceManagerConfiguration configuration=new TraceManagerConfiguration();
		configuration.maximumActives=10000;
		configuration.enableLastExceptions=true;
		configuration.enableLastTraces=false;
		configuration.enableTraceGraph=false;
		configuration.enableTraceStats=true;
		configuration.enableWatchListLastTraces=false;
		configuration.lastExceptionBufferSize=bufferSize;
		configuration.lastTraceBufferSize=bufferSize;
		configuration.logExceptionTraces=false;
		configuration.logTraces=false;
		configuration.captureStackTrace=false;
		return configuration;
	}
}
