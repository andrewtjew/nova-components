package org.nova.sqldb;

public class SqlServerConfiguration
{
	public SqlServerConfiguration(String host,int port,String database,int poolSize,long connectionKeepAlive,long maximumLeastRecentlyUsedCount)
	{
		this.host=host;
		this.port=port;
		this.database=database;
		this.poolSize=poolSize;
		this.connectionKeepAlive=connectionKeepAlive;
	}
	public SqlServerConfiguration(String host,String database)
	{
		this(host,1433,database,10,10000,1000000);
	}
	
	String host;
	int port;
	String database;
	int poolSize;
	long connectionKeepAlive;
	long maximumLeastRecentlyUsedCount;
}
