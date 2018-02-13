package org.nova.frameworks;

import java.io.File;
import java.io.PrintStream;
import java.util.HashMap;

import org.nova.concurrent.FutureScheduler;
import org.nova.concurrent.TimerScheduler;
import org.nova.configuration.Configuration;
import org.nova.core.Utils;
import org.nova.flow.SourceQueue;
import org.nova.flow.SourceQueueConfiguration;
import org.nova.logging.ConsoleWriter;
import org.nova.logging.HighPerformanceLogger;
import org.nova.logging.HighPerformanceConfiguration;
import org.nova.logging.JSONFormatter;
import org.nova.logging.Level;
import org.nova.logging.LogDirectoryManager;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;
import org.nova.logging.SimpleFileWriter;
import org.nova.logging.SourceQueueLogger;
import org.nova.metrics.MeterStore;
import org.nova.metrics.SourceEventEventBoard;
import org.nova.security.SecureFileVault;
import org.nova.security.UnsecureFileVault;
import org.nova.security.UnsecureVault;
import org.nova.security.Vault;
import org.nova.tracing.TraceManager;

public class CoreEnvironment
{
	final private TimerScheduler timerScheduler;
	final private FutureScheduler futureScheduler;
	final private TraceManager traceManager;
	final private Configuration configuration;
	final private MeterStore meterStore;
	final private Logger logger;
	final HashMap<String,Logger> loggers;
	final SourceQueue<LogEntry> logSourceQueue;
	final private LogDirectoryManager logDirectoryManager;
	final private int logCategoryBufferSize;
    final private Vault vault;
    final public static SourceEventEventBoard SOURCE_EVENT_BOARD=new SourceEventEventBoard();
	
	public CoreEnvironment(Configuration configuration) throws Throwable
	{
        this.configuration=configuration;
        String directory=configuration.getValue("Environment.Logger.logDirectory","logs");
		long maxFiles=configuration.getIntegerValue("Environment.Logger.logDirectory.maxFiles",0);
		long reserve=configuration.getLongValue("Environment.Logger.logDirectory.reserveSpace",100_000_000_000L);
		long maxDirectorySize=configuration.getLongValue("Environment.Logger.logDirectory.maxDirectorySize",10_000_000_000L);
		int maxMakeSpaceRetries=configuration.getIntegerValue("Environment.Logger.logDirectory.maxMakeSpaceRetries",10);
		this.logCategoryBufferSize=configuration.getIntegerValue("Environment.Logger.logCategoryBufferSize",10);

        this.meterStore=new MeterStore();

		this.logDirectoryManager=new LogDirectoryManager(directory, maxMakeSpaceRetries, maxFiles, maxDirectorySize, reserve);
		String loggerType=configuration.getValue("Environment.Logger.class","JSONBufferedLZ4Queue");
		switch (loggerType)
		{
            case "SimpleFileWriter":
                this.logSourceQueue=new SourceQueue<LogEntry>(new SimpleFileWriter(this.logDirectoryManager,new JSONFormatter()),new SourceQueueConfiguration());
                break;

            case "JSONBufferedLZ4Queue":
                this.logSourceQueue=new HighPerformanceLogger(logDirectoryManager, new HighPerformanceConfiguration());
                break;
                
		    default:
		        this.logSourceQueue=new SourceQueue<LogEntry>(new ConsoleWriter(new JSONFormatter(),true),new SourceQueueConfiguration());
		        break;
		}
		this.logSourceQueue.start();
		this.loggers=new HashMap<>();
		Logger traceLogger=this.getLogger("tracing");
	
		this.traceManager=new TraceManager(traceLogger);
		this.futureScheduler=new FutureScheduler(traceManager,configuration.getIntegerValue("Environment.FutureScheduler.threads",1000));
		this.timerScheduler=new TimerScheduler(traceManager, this.getLogger());
		this.timerScheduler.start();
		this.logger=getLogger("application");

		//Setting up the vault
        String secureVaultFile=configuration.getValue("Environment.Vault.secureVaultFile",null);
        if (secureVaultFile!=null)
        {
            String password=null;
            String passwordFile=configuration.getValue("Application.commentFile",null);
            if (passwordFile!=null)
            {
                password=Utils.readTextFile(passwordFile).trim();
            }
            else 
            {
                if (System.console()==null)
                {
                    System.err.println("No console available to enter vault password");
                    System.exit(1);
                }
                password=new String(System.console().readPassword("Enter vault password:"));
            }
            String salt=configuration.getValue("Environment.Vault.salt");
            this.vault=new SecureFileVault(password, salt, secureVaultFile);
            if (passwordFile!=null)
            {
                if ("false".equalsIgnoreCase(this.vault.get("DeletePasswordFile")))
                {
                }
                else
                {
                    try
                    {
                        if (new File(passwordFile).delete()==false)
                        {
                            System.err.println("Unable to delete password file.");
                            System.exit(1);
                        }
                    }
                    catch (Throwable t)
                    {
                        System.err.println("Unable to delete password file.");
                        t.printStackTrace(System.err);
                        System.exit(1);
                    }
                }
            }
        }
        else
        {
            String unsecureVaultFile=configuration.getValue("Environment.Vault.unsecureVaultFile");
            if (unsecureVaultFile!=null)
            {
                this.vault=new UnsecureFileVault(unsecureVaultFile);
                this.getLogger().log(Level.CRITICAL,"Using UnsecureVault");
            }
            else
            {
                this.vault=new UnsecureVault();
            }
            printUnsecureVaultWarning(System.err);
        }
	}
	
    private void printUnsecureVaultWarning(PrintStream stream)
    {
        stream.println("**************************************");
        stream.println("**   WARNING: Using UnsecureVault   **");
        stream.println("**   DO NOT USE IN PRODUCTION!!!!   **");
        stream.println("**************************************");
    }
    
	public MeterStore getMeterManager()
	{
		return meterStore;
	}
	
	public SourceEventEventBoard getSourceEventBoard()
	{
	    return this.SOURCE_EVENT_BOARD;
	}
	

	public FutureScheduler getFutureScheduler()
	{
		return futureScheduler;
	}

	public TraceManager getTraceManager()
	{
		return traceManager;
	}

	public Configuration getConfiguration()
	{
		return configuration;
	}

	public TimerScheduler getTimerScheduler()
	{
		return timerScheduler;
	}
	public Logger getLogger(String category) throws Throwable
	{
		synchronized (this.loggers)
		{
		    Logger logger=this.loggers.get(category);
			if (logger==null)
			{
				logger=new SourceQueueLogger(this.logCategoryBufferSize,category, this.logSourceQueue);
				this.loggers.put(category, logger);
			}
			return logger;
		}
	}
	public Logger getLogger() 
	{
		return this.logger;
	}
	public Logger[] getLoggers()
	{
	    synchronized (this.loggers)
	    {
	        return this.loggers.values().toArray(new Logger[this.loggers.size()]);
	    }
	}
	public SourceQueue<LogEntry> getLogQueue()
	{
	    return this.logSourceQueue;
	}
	public LogDirectoryManager getLogDirectoryManager()
	{
		return this.logDirectoryManager;
	}
	public Vault getVault()
	{
	    return this.vault;
	}
	public void stop() 
	{
	    this.logSourceQueue.stop();
	    this.timerScheduler.stop();
	}
}
