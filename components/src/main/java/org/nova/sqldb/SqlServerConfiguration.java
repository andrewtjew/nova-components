package org.nova.sqldb;

public class SqlServerConfiguration
{
	public SqlServerConfiguration(String host,int port,String database,int poolSize,long connectionKeepAliveMs,long maximumLeastRecentlyUsedCount)
	{
		this.host=host;
		this.port=port;
		this.database=database;
		this.poolSize=poolSize;
		this.connectionKeepAliveMs=connectionKeepAliveMs;
		this.maximumLeastRecentlyUsedCount=maximumLeastRecentlyUsedCount;
	}
	public SqlServerConfiguration(String host,String database)
	{
		this(host,1433,database,10,10000,1000000);
	}

	public boolean connectImmediately=true;
	public String host;
    public String database;
	public int port=1433;
	public int poolSize=10;
	public long connectionKeepAliveMs=10000;
	public long maximumLeastRecentlyUsedCount=1000000;
}
