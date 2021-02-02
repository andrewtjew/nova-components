/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package org.nova.frameworks;

import java.io.File;
import org.eclipse.jetty.server.Server;
import org.nova.collections.FileCache;
import org.nova.collections.FileCacheConfiguration;
import org.nova.concurrent.Synchronization;
import org.nova.configuration.Configuration;
import org.nova.html.ExtionsionToTypeMappings;
import org.nova.html.elements.Element;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.operator.MenuBar;
import org.nova.html.remote.RemoteResponseWriter;
import org.nova.html.remoting.HtmlRemotingWriter;
import org.nova.html.templating.Template;
import org.nova.http.server.JettyServerFactory;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.HtmlContentWriter;
import org.nova.http.server.HttpServer;
import org.nova.http.server.HttpServerConfiguration;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.operations.OperatorVariable;
import org.nova.operations.OperatorVariableManager;
import org.nova.security.Vault;
import org.nova.tracing.Trace;
import org.nova.utils.Utils;

import com.nova.disrupt.DisruptorManager;

@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({ JSONContentReader.class, JSONPatchContentReader.class })
@ContentWriters({HtmlElementWriter.class})
public abstract class ServerApplication extends CoreEnvironmentApplication
{
	final private HttpServer publicServer;
	final private HttpServer privateServer;
	final private HttpServer operatorServer;
	final private OperatorVariableManager operatorVariableManager;
	final private FileCache fileCache;
	final private String baseDirectory;
	final private ExtionsionToTypeMappings typeMappings;
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
        
        Element.HREF_LOCAL_DIRECTORY=configuration.getValue("Application.HREF_LOCAL_DIRECTORY",null);

        this.test=configuration.getBooleanValue("System.test",false);
        this.disruptorManager=new DisruptorManager();

        this.localHostName=configuration.getValue("ServerApplication.localHostNameOverride",Utils.getLocalHostName());
        
        this.operatorVariableManager=new OperatorVariableManager();
		this.typeMappings=ExtionsionToTypeMappings.fromDefault();

        int operatorPort=this.operatorServer.getPorts()[0];
 
        { //Private
            boolean https=configuration.getBooleanValue("HttpServer.private.https",false);
            boolean http=configuration.getBooleanValue("HttpServer.private.http",true);
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
                int threads=configuration.getIntegerValue("HttpServer.private.threads",20);
                HttpServerConfiguration privateServerConfiguration=getConfiguration().getNamespaceObject("HttpServer.private", HttpServerConfiguration.class);
                
                int httpsPort=configuration.getIntegerValue("HttpServer.private.https.port",-1);
                int httpPort=configuration.getIntegerValue("HttpServer.private.http.port",-1);
    
                Server[] servers=new Server[ports];
                int portIndex=0;
                if (https)
                {
                    if (httpsPort<0)
                    {
                        httpsPort=operatorPort+2;
                    }
                    Vault vault=coreEnvironment.getVault();
                    String serverCertificatePassword=vault.get("KeyStore.private.serverCertificate.password");
                    String clientCertificatePassword=vault.get("KeyStore.private.clientCertificate.password");
                    String keyManagerPassword=vault.get("KeyManager.password");
    
                    String serverCertificateKeyStorePath=configuration.getValue("HttpServer.private.serverCertificate.keyStorePath",null);
                    String clientCertificateKeyStorePath=configuration.getValue("HttpServer.private.clientCertificate.keyStorePath",null);
                    servers[portIndex]=JettyServerFactory.createHttpsServer(threads, httpsPort, serverCertificateKeyStorePath, serverCertificatePassword,clientCertificateKeyStorePath,clientCertificatePassword,keyManagerPassword);
                    portIndex++;
                }
                if (http)
                {
                    if (httpPort<0)
                    {
                        httpPort=operatorPort+1;
                    }
                    servers[portIndex]=JettyServerFactory.createServer(threads, httpPort);
                }
                this.privateServer=new HttpServer(this.getTraceManager(), this.getLogger("HttpServer"),isTest(),privateServerConfiguration, servers);
                
                this.privateServer.addContentDecoders(new GzipContentDecoder());
                this.privateServer.addContentEncoders(new GzipContentEncoder());
                this.privateServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
                this.privateServer.addContentWriters(new JSONContentWriter(),new HtmlContentWriter(),new HtmlElementWriter(),new HtmlRemotingWriter(),new RemoteResponseWriter());
            }
            else
            {
                this.privateServer=null;
            }
        }
        
        { //Public http server
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
                
                int httpsPort=configuration.getIntegerValue("HttpServer.public.https.port",-1);
                int httpPort=configuration.getIntegerValue("HttpServer.public.http.port",-1);
    
                Server[] servers=new Server[ports];
                int portIndex=0;
                
                if (https)
                {
                    if (httpsPort<0)
                    {
                        httpsPort=operatorPort+4;
                    }
                    Vault vault=coreEnvironment.getVault();
                    String serverCertificatePassword=vault.get("KeyStore.public.serverCertificate.password");
                    String clientCertificatePassword=vault.get("KeyStore.public.clientCertificate.password");
                    String keyManagerPassword=vault.get("KeyManager.password");
    
                    String serverCertificateKeyStorePath=configuration.getValue("HttpServer.public.serverCertificate.keyStorePath",null);
                    String clientCertificateKeyStorePath=configuration.getValue("HttpServer.public.clientCertificate.keyStorePath",null);
                    servers[portIndex]=JettyServerFactory.createHttpsServer(threads, httpsPort, serverCertificateKeyStorePath, serverCertificatePassword,clientCertificateKeyStorePath,clientCertificatePassword,keyManagerPassword);
                    portIndex++;
                }
                if (http)
                {
                    if (httpPort<0)
                    {
                        httpPort=operatorPort+3;
                    }
                    servers[portIndex]=JettyServerFactory.createServer(threads, httpPort);
                }
                this.publicServer=new HttpServer(this.getTraceManager(), this.getLogger("HttpServer"),isTest(),publicServerConfiguration, servers);
                
                this.publicServer.addContentDecoders(new GzipContentDecoder());
                this.publicServer.addContentEncoders(new GzipContentEncoder());
                this.publicServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
                this.publicServer.addContentWriters(new JSONContentWriter(),new HtmlContentWriter(),new HtmlElementWriter(),new HtmlRemotingWriter(),new RemoteResponseWriter());
            }
            else
            {
                this.publicServer=null;
            }
        }


        //File cache
        FileCacheConfiguration fileCacheConfiguration=configuration.getNamespaceObject("FileCache", FileCacheConfiguration.class);
		this.fileCache=new FileCache(fileCacheConfiguration);
		configuration.add("Classes.FileCache.sharedDirectory", this.fileCache.getSharedDirectory());
		configuration.add("Classes.FileCache.localDirectory", this.fileCache.getLocalDirectory());
		
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
        String namespace=configuration.getValue("ServerApplication.APIDefinitionNamespace","");
        this.operatorServer.registerHandlers(new ServerApplicationPages(this,namespace));
        
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
	    STOPPED,
	}
	
	final private Object runLock=new Object();
	private Status status;
	
	
	public void stop() throws Throwable
	{
	    synchronized(this.runLock)
        {
            this.onStop();
	        this.status=Status.STOPPED;
	        this.runLock.notify();
        }
	}
	
	public void waitForStop()
	{
        synchronized(this.runLock)
        {
            Synchronization.waitForNoThrow(this.runLock, ()->{return this.status==Status.STOPPED;});
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
    public abstract void onStop() throws Throwable;

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

	public FileCache getFileCache()
	{
		return this.fileCache;
	}

	public ExtionsionToTypeMappings getContentTypeMappings()
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
        Template template=this.test?OperatorPage.buildTemplate(this.menuBar, this.getName(), this.hostName):this.template;
        OperatorPage page=new OperatorPage(template);
        page.fill("title", title);
        page.fill("now", Utils.nowToLocalDateTimeString());
        return page;
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
