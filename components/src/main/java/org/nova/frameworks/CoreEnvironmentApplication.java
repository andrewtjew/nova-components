package org.nova.frameworks;

import org.nova.concurrent.MultiTaskScheduler;
import org.nova.concurrent.TimerScheduler;
import org.nova.configuration.Configuration;
import org.nova.flow.SourceQueue;
import org.nova.logging.LogDirectoryManager;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;
import org.nova.metrics.MeterStore;
import org.nova.metrics.SourceEventEventBoard;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public abstract class CoreEnvironmentApplication
{
    final private CoreEnvironment coreEnvironment;
    final private String name;
    
    public CoreEnvironmentApplication(String name,CoreEnvironment coreEnvironment) throws Throwable 
    {
        this.name=name;
        this.coreEnvironment=coreEnvironment;
    }
    
    public LogDirectoryManager getLogDirectoryManager()
    {
        return this.coreEnvironment.getLogDirectoryManager();
    }
    public MeterStore getMeterStore()
    {
        return this.coreEnvironment.getMeterManager();
    }

    public MultiTaskScheduler getMultiTaskScheduler()
    {
        return this.coreEnvironment.getMultiTaskScheduler();
    }

    public TraceManager getTraceManager()
    {
        return this.coreEnvironment.getTraceManager();
    }

    public Configuration getConfiguration()
    {
        return this.coreEnvironment.getConfiguration();
    }

    public TimerScheduler getTimerScheduler()
    {
        return this.coreEnvironment.getTimerScheduler();
    }
    public Logger getLogger(String category) throws Throwable
    {
        return this.coreEnvironment.getLogger(category);
    }
    public Logger getLogger() 
    {
        return this.coreEnvironment.getLogger();
    }
    public SourceQueue<LogEntry> getLogQueue()
    {
        return this.coreEnvironment.getLogQueue();
    }
    public CoreEnvironment getCoreEnvironment()
    {
        return this.coreEnvironment;
    }
    public SourceEventEventBoard getSourceEventBoard()
    {
        return this.coreEnvironment.getSourceEventBoard();
    }
    public String getName()
    {
        return this.name;
    }
    abstract public void run(Trace parent) throws Throwable;
}
