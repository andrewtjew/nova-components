package org.nova.sqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.format.DateTimeFormatter;

import org.nova.logging.Logger;
import org.nova.security.UnsecureVault;
import org.nova.security.Vault;
import org.nova.tracing.TraceManager;

import com.nova.disrupt.Disruptor;

public class MySqlConnector extends Connector
{
	final private String name;
    final private String host;
    final private int port;
    final private String user;
    final private Vault passwordVault;
    final private String passwordKey;
    final private String schema;
	
	
	public static DateTimeFormatter DATETIME_FORMATTER=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
    private String buildConnectionString()
    {
        return "jdbc:mysql://"+this.host+":"+this.port+"/"+this.schema+"?rewriteBatchedStatements=true";
    }
    
    private static Vault buildUnsecuredVault(String password)
    {
        
      UnsecureVault unsecuredVault=new UnsecureVault();
      unsecuredVault.put("password", password);
      return unsecuredVault;
    }

    public MySqlConnector(TraceManager traceManager,Logger logger, Disruptor disruptor,String user, String password, boolean connect, MySqlConfiguration configuration)
            throws Throwable
    {
        this(traceManager,logger,disruptor,user,"password",buildUnsecuredVault(password),connect,configuration);
        
    }
    public MySqlConnector(TraceManager traceManager,Logger logger, String user, String password, boolean connect, MySqlConfiguration configuration)
            throws Throwable
    {
        this(traceManager,logger,null,user,password,connect,configuration);
    }
    public MySqlConnector(TraceManager traceManager,Logger logger,String user,String passwordKey,Vault vault,boolean connect,MySqlConfiguration configuration) throws Throwable 
    {
        this(traceManager,logger,null,user,passwordKey,vault,connect,configuration);
    }    	
	public MySqlConnector(TraceManager traceManager,Logger logger,Disruptor disruptor,String user,String passwordKey,Vault vault,boolean connect,MySqlConfiguration configuration) throws Throwable 
	{
		super(traceManager,logger,disruptor,configuration.maximumRecentlyUsedCount);
		Class.forName("com.mysql.jdbc.Driver");
		this.user=user;
		this.schema=configuration.schema;
		this.port=configuration.port;
		this.name=configuration.host+"/"+configuration.schema;
		this.passwordVault=vault;
		this.passwordKey=passwordKey;
		this.host=configuration.host;
		int poolSize=configuration.poolSize;
		long connectionKeepAlive=configuration.connectionKeepAlive;
		Accessor[] accessors=new Accessor[poolSize];
		for (int i=0;i<poolSize;i++)
		{
			Accessor accessor=new Accessor(this.pool, this, connectionKeepAlive);
			if (connect)
			{
				try
				{
					accessor.activate();
				}
				catch (Throwable e)
				{
					this.initialConnectionExceptions.increment();
					connect=false;
				}
			}
			accessors[i]=accessor;
		}
		this.pool.initialize(accessors);
	}

	@Override
	protected Connection createConnection() throws Throwable
	{
		Connection connection=DriverManager.getConnection(this.buildConnectionString(),user,this.passwordVault.get(this.passwordKey));
		this.openConnectionSuccesses.increment();
		return connection;
	}

	@Override
	public String getName() 
	{
		return this.name;
	}

}
