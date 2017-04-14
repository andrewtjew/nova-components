package org.nova.sqldb;

import java.sql.Connection;
import java.sql.DriverManager;

import org.nova.security.UnsecureVault;
import org.nova.security.Vault;
import org.nova.tracing.TraceManager;

public class SqlServerConnector extends Connector
{
    final private String host;
	final private String user;
    final private Vault passwordVault;
    final private String passwordKey;
	final private int port;
	final private String database;
	final private String name;

	private String buildConnectionString() throws Exception
	{
        return "jdbc:sqlserver://" + this.host + ":" + this.port+";databaseName="
                +this.database+";user="+user+";password="+this.passwordVault.get(this.passwordKey);	    
	}
	
	private static Vault buildUnsecuredVault(String password)
	{
        
      UnsecureVault unsecuredVault=new UnsecureVault();
      unsecuredVault.put("password", password);
      return unsecuredVault;
	}
	
	public SqlServerConnector(TraceManager traceManager, String user, String password, boolean connect, SqlServerConfiguration configuration)
			throws ClassNotFoundException
	{
		this(traceManager,user,buildUnsecuredVault(password),"password",connect,configuration);
		
	}

	public SqlServerConnector(TraceManager traceManager, String user, Vault vault,String passwordKey, boolean connect, SqlServerConfiguration configuration)
            throws ClassNotFoundException
    {
        super(traceManager,configuration.maximumLeastRecentlyUsedCount);
        this.passwordKey=passwordKey;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        this.user = user;
        this.passwordVault=vault;
        this.port=configuration.port;
        this.host=configuration.host;
        this.database=configuration.database;
        this.name = configuration.host + "/" + configuration.database;
        int poolSize = configuration.poolSize;
        long connectionKeepAlive = configuration.connectionKeepAlive;
        for (int i = 0; i < poolSize; i++)
        {
            Accessor accessor = new Accessor(this.pool, this, connectionKeepAlive);
            if (connect)
            {
                try
                {
                    accessor.activate();
                }
                catch (Throwable e)
                {
                    this.initialConnectionExceptions.increment();
                    connect = false;
                }
            }
            this.pool.add(accessor);
        }
    }

	@Override
	protected Connection createConnection() throws Throwable
	{
	    String connectionString=buildConnectionString();
		Connection connection = DriverManager.getConnection(connectionString);
		this.openConnectionSuccesses.increment();
		return connection;
	}

	@Override
	public String getName() throws Throwable
	{
		return this.name;
	}

}
