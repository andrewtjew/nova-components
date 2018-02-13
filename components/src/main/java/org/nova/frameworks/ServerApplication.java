package org.nova.frameworks;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jetty.server.Server;
import org.nova.collections.FileCache;
import org.nova.collections.FileCacheConfiguration;
import org.nova.concurrent.FutureScheduler;
import org.nova.concurrent.Synchronization;
import org.nova.concurrent.TimerScheduler;
import org.nova.configuration.Configuration;
import org.nova.core.Utils;
import org.nova.flow.SourceQueue;
import org.nova.html.TypeMappings;
import org.nova.html.elements.Element;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.operator.Menu;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.html.widgets.MenuBar;
import org.nova.html.widgets.templates.Template;
import org.nova.html.widgets.templates.TemplateManager;
import org.nova.http.server.JettyServerFactory;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.HtmlContentWriter;
import org.nova.http.server.HtmlContentWriter;
import org.nova.http.server.HttpServer;
import org.nova.http.server.HttpServerConfiguration;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.TextContentWriter;
import org.nova.logging.Level;
import org.nova.logging.LogDirectoryManager;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;
import org.nova.logging.SourceQueueLogger;
import org.nova.metrics.MeterStore;
import org.nova.metrics.SourceEventEventBoard;
import org.nova.operations.OperatorVariable;
import org.nova.operations.OperatorVariableManager;
import org.nova.operator.OperatorPages;
import org.nova.security.SecureFileVault;
import org.nova.security.UnsecureFileVault;
import org.nova.security.UnsecureVault;
import org.nova.security.Vault;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

import com.nova.disrupt.DisruptorManager;

public abstract class ServerApplication extends CoreEnvironmentApplication
{
	final private HttpServer publicServer;
	final private HttpServer privateServer;
	final private HttpServer operatorServer;
	final private OperatorVariableManager operatorVariableManager;
	final private FileCache fileCache;
	final private String baseDirectory;
	final private TypeMappings typeMappings;
	final private TemplateManager operationTemplateManager;
	final private DisruptorManager disruptorManager;
	private long startTime;
	final private MenuBar menuBar;
	final private String hostName;
	private Template template;
	@OperatorVariable
	boolean test;
	final private String localHostName;
	
	public ServerApplication(String name,CoreEnvironment coreEnvironment,HttpServer operatorServer) throws Throwable 
	{
	    super(name,coreEnvironment);

	    this.hostName=Utils.getLocalHostName();
		this.operatorServer=operatorServer;
		
		Configuration configuration=coreEnvironment.getConfiguration();
		
		//Discover base directory. This is important to know for troubleshooting errors with file paths, so we output this info and we put this in the configuration. 
        File baseDirectory=new File(".");
        this.baseDirectory=baseDirectory.getCanonicalPath();
        CoreEnvironment.SOURCE_EVENT_BOARD.set("Application.baseDirectory",this.baseDirectory);
        System.out.println("base directory: "+this.baseDirectory);

        this.test=configuration.getBooleanValue("System.test",false);
        this.disruptorManager=new DisruptorManager();

        //Do not keep these vault information in configuration.
        configuration.remove("System.vault.secureVaultFile");
        configuration.remove("System.vault.passwordFile");
        configuration.remove("System.vault.salt");

        this.localHostName=configuration.getValue("ServerApplication.localHostNameOverride",Utils.getLocalHostName());
        
        this.operatorVariableManager=new OperatorVariableManager();
		this.typeMappings=TypeMappings.DefaultTypeMappings();
		
        String operationTemplateDirectory=configuration.getValue("HttpServer.operator.templateDirectory","../resources/html/operator/");
        this.operationTemplateManager=new TemplateManager(this.getTraceManager(), operationTemplateDirectory);

        int operatorPort=this.operatorServer.getPorts()[0];

        //Private http server
        int privatePort=configuration.getIntegerValue("HttpServer.private.port",operatorPort+1);
        if (privatePort>0)
        {
            int threads=configuration.getIntegerValue("httpServer.private.threads",1000);
            this.privateServer=new HttpServer(this.getTraceManager(), getLogger("HttpServer.Operator"),this.isTest(),JettyServerFactory.createServer(threads, privatePort));
            this.privateServer.addContentDecoders(new GzipContentDecoder());
            this.privateServer.addContentEncoders(new GzipContentEncoder());
            this.privateServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
            this.privateServer.addContentWriters(new HtmlContentWriter(),new HtmlElementWriter(),new JSONContentWriter(),new AjaxQueryResultWriter());
        }
        else
        {
            this.privateServer=null;
        }
        
        //Public http server
        boolean https=configuration.getBooleanValue("HttpServer.public.https",true);
        boolean http=configuration.getBooleanValue("HttpServer.public.http",false);
        int ports=0;
        if (https)
        {
            ports++;
        }
        if (http)
        {
            ports++;
        }
        if (ports>0)
        {
            int threads=configuration.getIntegerValue("HttpServer.public.threads",100);
            HttpServerConfiguration publicServerConfiguration=getConfiguration().getNamespaceObject("HttpServer.public", HttpServerConfiguration.class);
            
            int publicHttpsPort=configuration.getIntegerValue("HttpServer.public.https.port",-1);
            int publicHttpPort=configuration.getIntegerValue("HttpServer.public.http.port",-1);

            Server[] servers=new Server[ports];
            int portIndex=0;
            if (https)
            {
                if (publicHttpsPort<0)
                {
                    publicHttpsPort=operatorPort+2;
                }
                Vault vault=coreEnvironment.getVault();
                String serverCertificatePassword=vault.get("KeyStore.serverCertificate.password");
                String clientCertificatePassword=vault.get("KeyStore.clientCertificate.password");
                String keyManagerPassword=vault.get("KeyManager.password");

                String serverCertificateKeyStorePath=configuration.getValue("HttpServer.serverCertificate.keyStorePath",null);
                String clientCertificateKeyStorePath=configuration.getValue("HttpServer.clientCertificate.keyStorePath",null);
                servers[portIndex]=JettyServerFactory.createHttpsServer(threads, publicHttpsPort, serverCertificateKeyStorePath, serverCertificatePassword,clientCertificateKeyStorePath,clientCertificatePassword,keyManagerPassword);
                portIndex++;
            }
            if (http)
            {
                if (publicHttpPort<0)
                {
                    if (https)
                    {
                        publicHttpPort=publicHttpsPort+1;
                    }
                    else
                    {
                        publicHttpPort=operatorPort+2;
                    }
                }
                servers[portIndex]=JettyServerFactory.createServer(threads, publicHttpPort);
            }
            this.publicServer=new HttpServer(this.getTraceManager(), this.getLogger("HttpServer"),isTest(),publicServerConfiguration, servers);
            
            this.publicServer.addContentDecoders(new GzipContentDecoder());
            this.publicServer.addContentEncoders(new GzipContentEncoder());
            this.publicServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
            this.publicServer.addContentWriters(new JSONContentWriter(),new HtmlContentWriter(),new HtmlElementWriter());
        }
        else
        {
            this.publicServer=null;
        }


        //File cache
        FileCacheConfiguration fileCacheConfiguration=configuration.getNamespaceObject("FileCache", FileCacheConfiguration.class);
		this.fileCache=new FileCache(fileCacheConfiguration);
		
        this.getOperatorVariableManager().register("HttpServer.operator", this.operatorServer);
        if (this.privateServer!=null)
        {
            this.getOperatorVariableManager().register("HttpServer.private", this.privateServer);
        }
        if (this.publicServer!=null)
        {
            this.getOperatorVariableManager().register("HttpServer.public", this.publicServer);
        }

        this.menuBar=new MenuBar();
        this.operatorServer.registerHandlers(new ServerApplicationPages(this));
        
        //Build template and start operator server so we can monitor the rest of the startup.
        this.template=OperatorPage.buildTemplate(this.menuBar,this.getName(),this.hostName); 
	}
	
	private void startServer(HttpServer server) throws Throwable
	{
		if (server!=null)
		{
			server.start();
		}
	}
	
	public void startServers() throws Throwable
	{
        startServer(this.privateServer);
        startServer(this.publicServer);
	}
	
	public void start() throws Throwable
	{
        try (Trace trace=new Trace(this.getTraceManager(),"postStart"))
        {
            preStart(trace);
        }

        this.startTime=System.currentTimeMillis();
        try (Trace trace=new Trace(this.getTraceManager(),"onStart"))
        {
            onStart(trace);
        }
        buildOperatorPageTemplate();
        startServers();

        try (Trace trace=new Trace(this.getTraceManager(),"postStart"))
        {
            postStart(trace);
        }
	}
	
	static enum Status
	{
	    RUNNING,
	    STOPPING,
	    STOPPED,
	}
	
	final private Object runLock=new Object();
	private Status status;
	
	public void run(Trace parent)
	{
        synchronized (runLock)
        {
            Synchronization.waitForNoThrow(parent,this.runLock, ()->{return this.status!=Status.RUNNING;});
            this.status=Status.STOPPED;
            this.runLock.notify();
        }
	}
	
	public void stop()
	{
	    synchronized(this.runLock)
        {
	        this.status=Status.STOPPING;
	        this.runLock.notify();
	        Synchronization.waitForNoThrow(this.runLock, ()->{return this.status==Status.STOPPING;});
        }
	}

	public DisruptorManager getDisruptorManager()
	{
	    return this.disruptorManager;
	}
	
    public void preStart(Trace trace) throws Throwable
    {
    }
    public abstract void onStart(Trace parent) throws Throwable;

    public void postStart(Trace trace) throws Throwable
    {
    }
    
    public HttpServer getPublicServer()
	{
		return publicServer;
	}

	public HttpServer getPrivateServer()
	{
		return privateServer;
	}

	public HttpServer getOperatorServer()
	{
		return operatorServer;
	}
	public OperatorVariableManager getOperatorVariableManager()
	{
		return operatorVariableManager;
	}

	public String getBaseDirectory()
	{
		return this.baseDirectory;
	}

	public TemplateManager getOperatorTemplateManager()
	{
	    return this.operationTemplateManager;
	}
	public FileCache getFileCache()
	{
		return this.fileCache;
	}

	public TypeMappings getTypeMappings()
	{
		return this.typeMappings;
	}

	public long getStartTime()
    {
        return this.startTime;
    }
	
	public Vault getVault()
	{
	    return this.getCoreEnvironment().getVault();
	}
	
    public MenuBar getMenuBar()
    {
        return this.menuBar;
    }

    public void buildOperatorPageTemplate() throws Throwable
    {
        this.template=OperatorPage.buildTemplate(this.menuBar,this.getName(),this.hostName); 
    }
    
    
    public OperatorPage buildOperatorPage(String title) throws Throwable
    {
        if (this.test)
        {
            Template template=new Template(OperatorPage.buildTemplate(this.menuBar, this.getName(), this.hostName));
            template.fill("title", title);
            template.fill("now", Utils.nowToLocalDateTimeString());
            return new OperatorPage(template);
        }
        else
        {
            Template template=new Template(this.template);
            template.fill("title", title);
            template.fill("now", Utils.nowToLocalDateTimeString());
            return new OperatorPage(template);
            
        }
    }

    public String getLocalHostName()
    {
        return this.localHostName;
    }
    
    public boolean isTest()
    {
        return this.test;
    }
    
    
}
