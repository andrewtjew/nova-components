package org.nova.frameworks;

import java.io.File;
import java.io.PrintStream;

import org.eclipse.jetty.server.Server;
import org.nova.collections.FileCache;
import org.nova.collections.FileCacheConfiguration;
import org.nova.concurrent.FutureScheduler;
import org.nova.concurrent.TimerScheduler;
import org.nova.configuration.Configuration;
import org.nova.core.Utils;
import org.nova.flow.SourceQueue;
import org.nova.html.TypeMappings;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.html.widgets.MenuBar;
import org.nova.html.widgets.templates.Template;
import org.nova.html.widgets.templates.TemplateManager;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.HtmlContentWriter;
import org.nova.http.server.HttpServer;
import org.nova.http.server.HttpServerConfiguration;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.JettyServerFactory;
import org.nova.logging.Level;
import org.nova.logging.LogDirectoryManager;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;
import org.nova.metrics.MeterStore;
import org.nova.metrics.SourceEventEventBoard;
import org.nova.operations.OperatorVariable;
import org.nova.operations.OperatorVariableManager;
import org.nova.security.SecureFileVault;
import org.nova.security.UnsecureFileVault;
import org.nova.security.UnsecureVault;
import org.nova.security.Vault;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

import com.nova.disrupt.DisruptorManager;

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

    public FutureScheduler getFutureScheduler()
    {
        return this.coreEnvironment.getFutureScheduler();
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
