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
import org.nova.configuration.Configuration;
import org.nova.core.Utils;
import org.nova.html.objects.AjaxQueryContentWriter;
import org.nova.html.pages.Page;
import org.nova.html.pages.PageManager;
import org.nova.html.pages.PageWriter;
import org.nova.html.pages.TypeMappings;
import org.nova.html.pages.operations.Menu;
import org.nova.html.pages.operations.OperationContentWriter;
import org.nova.http.server.JettyServerFactory;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.HttpServer;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.TextContentWriter;
import org.nova.logging.Level;
import org.nova.operations.OperatorPages;
import org.nova.operations.OperatorVariableManager;
import org.nova.security.SecureFileVault;
import org.nova.security.UnsecureFileVault;
import org.nova.security.Vault;

public class ServerApplication extends CoreApplication
{
	final private HttpServer publicServer;
	final private HttpServer privateServer;
	final private HttpServer operatorServer;
	final private OperatorVariableManager operatorVariableManager;
	final private FileCache fileCache;
	final private String baseDirectory;
	final private TypeMappings typeMappings;
	final private PageManager pageManager;
	final private OperationContentWriter pageContentWriter;
	final private Vault vault;
	private long startTime;
	final private String name;
	
	
	
	public ServerApplication(String name,Configuration configuration) throws Throwable 
	{
		super(configuration);
		this.name=name;
		
		//Discover base directory. This is important to know for troubleshooting errors with file paths, so we output this info and we put this in the configuration. 
        File baseDirectory=new File(".");
        this.baseDirectory=baseDirectory.getCanonicalPath();
        configuration.add("System.baseDirectory",this.baseDirectory,"The base directory");
        System.out.println("base directory: "+this.baseDirectory);

        //Setting up the vault
        String secureVaultFile=configuration.getValue("System.vault.secureVaultFile",null);
        if (secureVaultFile!=null)
        {
            String password=null;
            String passwordFile=configuration.getValue("System.vault.passwordFile",null);
            if (passwordFile!=null)
            {
                password=Utils.readTextFile(passwordFile).trim();
                if (new File(passwordFile).delete()==false)
                {
                    System.err.println("Unable to delete password file.");
                    System.exit(1);
                }
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
            String salt=name;
            this.vault=new SecureFileVault(password, salt, secureVaultFile);
        }
        else
        {
            String unsecureVaultFile=configuration.getValue("System.vault.unsecureVaultFile","./resources/UnsecureVault.cnf");
            this.vault=new UnsecureFileVault(unsecureVaultFile);
            printUnsecureVaultWarning(System.err);
            this.getLogger().log(Level.FATAL,"Using UnsecureVault");
        }
        //Do not keep these vault information in configuration.
        configuration.remove("System.vault.secureVaultFile");
        configuration.remove("System.vault.passwordFile");
        
        this.operatorVariableManager=new OperatorVariableManager();
		this.typeMappings=TypeMappings.DefaultTypeMappings();
		
        String pageDirectory=configuration.getValue("HttpServer.operator.pageDirectory","./resources/pages/");
        this.pageManager=new PageManager(this.getTraceManager(), pageDirectory);
        Menu menu=new Menu();
        String menuHtml=configuration.getValue("PageContentWriter.operator.main","operator/main.html");
        Page menuPage=this.pageManager.get(menuHtml,null);

        //Admin http server
        this.pageContentWriter=new OperationContentWriter(menu, menuPage);
		int threads=configuration.getIntegerValue("HttpServer.operator.threads",10);
        int operatorPort=configuration.getIntegerValue("HttpServer.operator.port",10079);
		this.operatorServer=new HttpServer(this.getTraceManager(), JettyServerFactory.createServer(threads, operatorPort));
        this.operatorServer.addContentDecoders(new GzipContentDecoder());
        this.operatorServer.addContentEncoders(new GzipContentEncoder());
        this.operatorServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
        
        this.operatorServer.addContentWriters(this.pageContentWriter,new TextContentWriter("text"),new JSONContentWriter(),new AjaxQueryContentWriter());
        this.getMeterManager().register("HttpServer.operatorServer",this.operatorServer);
        System.out.println("admin endpoint: http://"+Utils.getLocalHostName()+":"+operatorPort);

        //Private http server
        int privatePort=configuration.getIntegerValue("HttpServer.private.port",operatorPort+1);
        if (privatePort>0)
        {
            threads=configuration.getIntegerValue("httpServer.private.threads",1000);
            this.privateServer=new HttpServer(this.getTraceManager(), JettyServerFactory.createServer(threads, privatePort));
            this.privateServer.addContentDecoders(new GzipContentDecoder());
            this.privateServer.addContentEncoders(new GzipContentEncoder());
            this.privateServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
            this.privateServer.addContentWriters(new JSONContentWriter());
        }
        else
        {
            this.privateServer=null;
        }
        
        //Public http server
        int publicPort=configuration.getIntegerValue("HttpServer.public.port",operatorPort+2);
        if (publicPort>0)
        {
            threads=configuration.getIntegerValue("HttpServer.public.threads",1000);
            boolean https=configuration.getBooleanValue("httpServer.public.https",false);
            if (https)
            {
                String keyStorePassword=this.vault.get("Store.keyStore.password");
                String trustStorePassword=this.vault.get("Store.trustStore.password");
                String keyManagerPassword=this.vault.get("Manager.keyManagerPassword");

                String keyStorePath=configuration.getValue("httpServer.public.keyStorePath",null);
                String trustStorePath=configuration.getValue("httpServer.public.trustStorePath",null);
                Server server=JettyServerFactory.createHttpsServer(threads, publicPort, keyStorePath, keyStorePassword,trustStorePath,trustStorePassword,keyManagerPassword);
                this.publicServer=new HttpServer(this.getTraceManager(), server);
            }
            else
            {
                this.publicServer=new HttpServer(this.getTraceManager(), JettyServerFactory.createServer(threads, publicPort));
            }
            this.publicServer.addContentDecoders(new GzipContentDecoder());
            this.publicServer.addContentEncoders(new GzipContentEncoder());
            this.publicServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
            this.publicServer.addContentWriters(new JSONContentWriter());
        }
        else
        {
            this.publicServer=null;
        }
        
		String fileCacheDirectory=configuration.getValue("FileCache.baseDirectory","./resources/pages/");
		long fileCacheMaxAge=configuration.getLongValue("FileCache.maxAge",Long.MAX_VALUE);
		long fileCacheMaxSize=configuration.getLongValue("FileCache.maxSize",Long.MAX_VALUE);
		int fileCacheCapacity=configuration.getIntegerValue("FileCache.capacity",10000);
		this.fileCache=new FileCache(fileCacheDirectory,fileCacheCapacity,fileCacheMaxAge,fileCacheMaxSize);
		
        this.getOperatorVariableManager().register("HttpServer.operator", this.operatorServer);
        this.getOperatorVariableManager().register("HttpServer.Public", this.publicServer);
        this.getOperatorVariableManager().register("HttpServer.Private", this.privateServer);
	}
	
	private void printUnsecureVaultWarning(PrintStream stream)
	{
        stream.println("**************************************");
        stream.println("**   WARNING: Using UnsecureVault   **");
        stream.println("**   DO NOT USE IN PRODUCTION!!!!   **");
        stream.println("**************************************");
	}
	
	private void startServer(HttpServer server) throws Exception
	{
		if (server!=null)
		{
			server.start();
		}
	}
	
	public void runForever() throws Throwable
	{
        this.startTime=System.currentTimeMillis();
        this.operatorServer.register(new ServerOperatorPages(this));
        this.operatorServer.register(new OperatorPages(this.operatorVariableManager, this.getOperationContentWriter().getMenu()));
        onStart();
        startServer(this.operatorServer);
        startServer(this.privateServer);
        startServer(this.publicServer);
		System.out.println("Running forever!");
		super.runForever();
	}

    public void onStart() throws Throwable
    {
        //for sub classes to overrride
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

	public PageManager getPageManager()
	{
	    return this.pageManager;
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
	    return this.vault;
	}
	
	public OperationContentWriter getOperationContentWriter()
	{
	    return this.pageContentWriter;
	}
	public String getName()
	{
	    return this.name;
	}
}
