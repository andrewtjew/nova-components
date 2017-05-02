package org.nova.sqldb;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;

import org.nova.core.Utils;
import org.nova.logging.Item;
import org.nova.logging.Logger;
import org.nova.security.UnsecureVault;
import org.nova.security.Vault;
import org.nova.sqldb.Param.Direction;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

import com.microsoft.sqlserver.jdbc.SQLServerCallableStatement;
import com.nova.disrupt.Disruptor;

public class SQLServerConnector extends Connector
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
	
    public SQLServerConnector(TraceManager traceManager, Logger logger,Disruptor disruptor,String user, String password, boolean connect, SqlServerConfiguration configuration)
            throws Throwable
    {
        this(traceManager,logger,disruptor,user,buildUnsecuredVault(password),"password",connect,configuration);
    }
    public SQLServerConnector(TraceManager traceManager, Logger logger,String user, String password, boolean connect, SqlServerConfiguration configuration)
            throws Throwable
    {
        this(traceManager,logger,null,user,password,connect,configuration);
    }
    public SQLServerConnector(TraceManager traceManager,Logger logger, String user, Vault vault,String passwordKey, boolean connect, SqlServerConfiguration configuration)
            throws Throwable
    {
        this(traceManager,logger,null,user,vault,passwordKey,connect,configuration);
    }
	public SQLServerConnector(TraceManager traceManager,Logger logger, Disruptor disruptor,String user, Vault vault,String passwordKey, boolean connect, SqlServerConfiguration configuration)
            throws Throwable
    {
        super(traceManager,logger,disruptor,configuration.maximumLeastRecentlyUsedCount);
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
	public String getName()
	{
		return this.name;
	}

    @SuppressWarnings("unchecked")
    public <RETURN_TYPE> CallResult<RETURN_TYPE> executeSqlServerCall(Trace parent, String traceCategoryOverride, Class<RETURN_TYPE> returnType,Param[] parameters,String name) throws Throwable
    {
        try (Accessor accessor=this.openAccessor(parent, traceCategoryOverride))
        {
            try (AccessorTraceContext context = new AccessorTraceContext(accessor, parent, traceCategoryOverride,name))
            {
                try
                {
                    StringBuilder sb=new StringBuilder();
                    sb.append('{');
                    int offset=1;
                    if ((returnType!=Void.class)&&(returnType!=void.class))
                    {
                        offset=2;
                        sb.append("?=");
                    }
                    sb.append("call ").append(name);
                    if (parameters.length>0)
                    {
                        sb.append('(');
                        for (int i = 0; i < parameters.length; i++)
                        {
                            if (i>0)
                            {
                                sb.append(',');
                            }
                            sb.append('?');
                        }
                        sb.append(')');
                    }
                    sb.append('}');
                    String call=sb.toString();
                    context.addLogItem(new Item("Call",call));
                    ArrayList<Integer> outIndexes=new ArrayList<>();
                    try (CallableStatement statement = accessor.connection.prepareCall(call))
                    {
                        SQLServerCallableStatement sqlServerStatement=(SQLServerCallableStatement)(statement);
                        if (offset==2)
                        {
                            int returnSqlType=Param.getSqlType(returnType);
                            statement.registerOutParameter(1,returnSqlType);
                        }
                        for (int i = 0; i < parameters.length; i++)
                        {
                            Param param=parameters[i];
                            if ((param.direction==Direction.IN)||(param.direction==Direction.IN_OUT))
                            {
                                if (param.sqlType!=microsoft.sql.Types.STRUCTURED)
                                {
                                    context.addLogItem(new Item("param"+i,param.inValue));
                                    statement.setObject(i + offset, parameters[i].inValue);
                                }
                                else
                                {
                                    SQLServerDataTableParameter dataTableParameter=(SQLServerDataTableParameter)parameters[i].inValue; 
                                    context.addLogItem(new Item("param"+i,"SQLServerDataTable"));
                                    sqlServerStatement.setStructured(i+offset,dataTableParameter.getName(),dataTableParameter.getDataTable());
                                }
                            }
                            if ((param.direction==Direction.OUT)||(param.direction==Direction.IN_OUT))
                            {
                                statement.registerOutParameter(i+offset, param.sqlType);
                                outIndexes.add(i);
                            }
                        }
                        this.callRate.increment();
                        boolean hasResultSet=statement.execute();
                        ArrayList<RowSet> rowSetList=new ArrayList<>();
                        ArrayList<Integer> updateCounts=new ArrayList<>();
                        RETURN_TYPE returnValue=null;
                        HashMap<Integer,Object> outValues=new HashMap<>();
                        for (;;)
                        {
                            if (hasResultSet)
                            {
                                try (ResultSet resultSet = statement.getResultSet())
                                {
                                    RowSet rowSet=Accessor.convert(resultSet);
                                    rowSetList.add(rowSet);
                                }
                                hasResultSet=statement.getMoreResults();
                            }
                            else
                            {
                                int updateCount=statement.getUpdateCount();
                                if (updateCount==-1)
                                {
                                    break;
                                }
                                updateCounts.add(updateCount);
                                hasResultSet=statement.getMoreResults();
                            }
                        }
                        if (offset==2)
                        {
                            returnValue=(RETURN_TYPE)statement.getObject(1);
                        }
                        for (int index:outIndexes)
                        {
                            outValues.put(index, statement.getObject(index+offset));
                        }
                        int[] updateCountArray=Utils.intArrayFromList(updateCounts);
                        RowSet[] rowSetArray=rowSetList.toArray(new RowSet[rowSetList.size()]);
                        context.logRowsUpdated(updateCountArray);
                        context.logRowsQueried(rowSetArray);
                        return new CallResult<RETURN_TYPE>(returnValue,outValues,rowSetArray,updateCountArray);
                    }
                }
                catch (Throwable ex)
                {
                    throw context.handleThrowable(ex);
                }
            }
        }
    }
	
	
}
