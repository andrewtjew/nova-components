package org.nova.sqldb;

public class MySqlConfiguration
{
	public MySqlConfiguration(String host,int port,String schema,int poolSize,long connectionKeepAlive,long maximumRecentlyUsedCount)
	{
		this.host=host;
		this.port=port;
		this.schema=schema;
		this.poolSize=poolSize;
		this.connectionKeepAlive=connectionKeepAlive;
		this.maximumRecentlyUsedCount=maximumRecentlyUsedCount;
	}
    public MySqlConfiguration(String host,int port,String schema,int poolSize,long connectionKeepAlive)
    {
        this(host,port,schema,poolSize,10000,1000000);
    }
	public MySqlConfiguration(String host,String schema)
	{
		this(host,3306,schema,10,10000,1000000);
	}
    public MySqlConfiguration(String schema)
    {
        this("localhost",schema);
    }
	
	String host="localhost";
	int port=3306;
	String schema;
	int poolSize=2;
	long connectionKeepAlive=10000;
	long maximumRecentlyUsedCount=1000000;
}
