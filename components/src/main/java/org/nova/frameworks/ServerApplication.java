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
import org.nova.configuration.Configuration;
import org.nova.core.Utils;
import org.nova.html.TypeMappings;
import org.nova.html.elements.Element;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.operator.Menu;
import org.nova.html.operator.OperatorResultWriter;
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
import org.nova.operations.OperatorVariable;
import org.nova.operations.OperatorVariableManager;
import org.nova.operator.OperatorPages;
import org.nova.security.SecureFileVault;
import org.nova.security.UnsecureFileVault;
import org.nova.security.UnsecureVault;
import org.nova.security.Vault;

import com.nova.disrupt.DisruptorManager;

public class ServerApplication extends CoreApplication
{
	final private HttpServer publicServer;
	final private HttpServer privateServer;
	final private HttpServer operatorServer;
	final private OperatorVariableManager operatorVariableManager;
	final private FileCache fileCache;
	final private String baseDirectory;
	final private TypeMappings typeMappings;
	final private TemplateManager operationTemplateManager;
	final private OperatorResultWriter operatorResultWriter;
	final private DisruptorManager disruptorManager;
	final private Vault vault;
	private long startTime;
	final private String name;
	final private MenuBar menuBar;
	final private String hostName;
	private Template template;
	@OperatorVariable
	boolean debug;
	
	public ServerApplication(String name,Configuration configuration) throws Throwable 
	{
		super(configuration);
		this.name=name;
		this.hostName=Utils.getLocalHostName();
		
		//Discover base directory. This is important to know for troubleshooting errors with file paths, so we output this info and we put this in the configuration. 
        File baseDirectory=new File(".");
        this.baseDirectory=baseDirectory.getCanonicalPath();
        configuration.add("System.baseDirectory",this.baseDirectory,"The base directory");
        System.out.println("base directory: "+this.baseDirectory);

        this.debug=configuration.getBooleanValue("System.debug",false);
        this.disruptorManager=new DisruptorManager();

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
            String unsecureVaultFile=configuration.getValue("System.vault.unsecureVaultFile");
            if (unsecureVaultFile!=null)
            {
                this.vault=new UnsecureFileVault(unsecureVaultFile);
                this.getLogger().log(Level.FATAL,"Using UnsecureVault");
            }
            else
            {
                this.vault=new UnsecureVault();
            }
            printUnsecureVaultWarning(System.err);
        }
        //Do not keep these vault information in configuration.
        configuration.remove("System.vault.secureVaultFile");
        configuration.remove("System.vault.passwordFile");
        
        this.operatorVariableManager=new OperatorVariableManager();
		this.typeMappings=TypeMappings.DefaultTypeMappings();
		
        String operationTemplateDirectory=configuration.getValue("HttpServer.operator.templateDirectory","../resources/html/operator/");
        this.operationTemplateManager=new TemplateManager(this.getTraceManager(), operationTemplateDirectory);
        Menu menu=new Menu();
        String menuHtml=configuration.getValue("OperationResultWriter.template.main","./main.html");
        Template menuTemplate=this.operationTemplateManager.get(menuHtml);

        //Admin http server
        this.operatorResultWriter=new OperatorResultWriter(this.getName(),menu, menuTemplate);
		int threads=configuration.getIntegerValue("HttpServer.operator.threads",10);
        int operatorPort=configuration.getIntegerValue("HttpServer.operator.port",10051);
		this.operatorServer=new HttpServer(this.getTraceManager(), getLogger("HttpServer.Operator"), JettyServerFactory.createServer(threads, operatorPort));
        this.operatorServer.addContentDecoders(new GzipContentDecoder());
        this.operatorServer.addContentEncoders(new GzipContentEncoder());
        this.operatorServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
        this.operatorServer.addContentWriters(this.operatorResultWriter,new HtmlContentWriter(),new HtmlElementWriter(),new JSONContentWriter(),new AjaxQueryResultWriter());
        this.getMeterManager().register("HttpServer.operatorServer",this.operatorServer);
        System.out.println("admin endpoint: http://"+Utils.getLocalHostName()+":"+operatorPort);

        //Private http server
        int privatePort=configuration.getIntegerValue("HttpServer.private.port",operatorPort+1);
        if (privatePort>0)
        {
            threads=configuration.getIntegerValue("httpServer.private.threads",1000);
            this.privateServer=new HttpServer(this.getTraceManager(), getLogger("HttpServer.Operator"),JettyServerFactory.createServer(threads, privatePort));
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
        HttpServerConfiguration publicServerConfiguration=getConfiguration().getConfiguration("HttpServer.public", HttpServerConfiguration.class);
        int publicPort=configuration.getIntegerValue("HttpServer.public.port",-1);
        if (publicPort<0)
        {
            publicPort=operatorPort+2;
        }
        if (publicPort>0)
        {
            threads=configuration.getIntegerValue("HttpServer.public.threads",100);
            boolean useTestPort=isDebug();
            Server[] servers=new Server[useTestPort?2:1];
            if (useTestPort)
            {
                servers[1]=JettyServerFactory.createServer(threads, publicPort+1);
            }
            boolean https=configuration.getBooleanValue("HttpServer.public.https",true);
            if (https)
            {
                String serverCertificatePassword=this.vault.get("KeyStore.serverCertificate.password");
                String clientCertificatePassword=this.vault.get("KeyStore.clientCertificate.password");
                String keyManagerPassword=this.vault.get("KeyManager.password");

                String serverCertificateKeyStorePath=configuration.getValue("HttpServer.serverCertificate.keyStorePath",null);
                String clientCertificateKeyStorePath=configuration.getValue("HttpServer.clientCertificate.keyStorePath",null);
                servers[0]=JettyServerFactory.createHttpsServer(threads, publicPort, serverCertificateKeyStorePath, serverCertificatePassword,clientCertificateKeyStorePath,clientCertificatePassword,keyManagerPassword);
            }
            else
            {
                servers[0]=JettyServerFactory.createServer(threads, publicPort);
            }
            this.publicServer=new HttpServer(this.getTraceManager(), this.getLogger("HttpServer"),publicServerConfiguration, servers);
            
            this.publicServer.addContentDecoders(new GzipContentDecoder());
            this.publicServer.addContentEncoders(new GzipContentEncoder());
            this.publicServer.addContentReaders(new JSONContentReader(),new JSONPatchContentReader());
            this.publicServer.addContentWriters(new JSONContentWriter(),new HtmlContentWriter(),new HtmlElementWriter());
        }
        else
        {
            this.publicServer=null;
        }
        
        FileCacheConfiguration fileCacheConfiguration=configuration.getConfiguration("FileCache", FileCacheConfiguration.class);
		this.fileCache=new FileCache(fileCacheConfiguration);
		
        this.getOperatorVariableManager().register("HttpServer.operator", this.operatorServer);
        this.getOperatorVariableManager().register("HttpServer.public", this.publicServer);
        this.getOperatorVariableManager().register("HttpServer.private", this.privateServer);

        this.menuBar=new MenuBar();
        this.template=OperatorPage.buildTemplate(this.menuBar,this.name,this.hostName);
        this.operatorServer.register(new ServerOperatorPages(this));
        this.operatorServer.register(new OperatorPages(this.operatorVariableManager, this.getMenuBar()));
        startServer(this.operatorServer);
	}
	
	private void printUnsecureVaultWarning(PrintStream stream)
	{
        stream.println("**************************************");
        stream.println("**   WARNING: Using UnsecureVault   **");
        stream.println("**   DO NOT USE IN PRODUCTION!!!!   **");
        stream.println("**************************************");
	}
	
	private void startServer(HttpServer server) throws Throwable
	{
		if (server!=null)
		{
			server.start();
		}
	}
	
	public void runForever() throws Throwable
	{
        this.startTime=System.currentTimeMillis();
        onStart();
        startServer(this.privateServer);
        startServer(this.publicServer);
		System.out.println("Running forever!");
		super.runForever();
	}

	public DisruptorManager getDisruptorManager()
	{
	    return this.disruptorManager;
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
	    return this.vault;
	}
	
    public OperatorResultWriter getOperatorResultWriter()
    {
        return this.operatorResultWriter;
    }
    
    public MenuBar getMenuBar()
    {
        return this.menuBar;
    }
    
    public OperatorPage buildOperatorPage(String title) throws Throwable
    {
        if (this.debug)
        {
            Template template=new Template(OperatorPage.buildTemplate(this.menuBar, this.name, this.hostName));
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
    
	public String getName()
	{
	    return this.name;
	}
	public boolean isDebug()
	{
	    return this.debug;
	}
}
