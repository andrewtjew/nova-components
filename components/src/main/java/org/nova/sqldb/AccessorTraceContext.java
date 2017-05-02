package org.nova.sqldb;

import java.sql.Connection;
import java.util.ArrayList;

import org.nova.core.MultiException;
import org.nova.logging.Item;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;

import com.nova.disrupt.DisruptorTraceContext;

public class AccessorTraceContext extends DisruptorTraceContext
{
    final Accessor accessor;
    
    public AccessorTraceContext(Accessor accessor,Trace parent,String traceCategoryOverride,String sql)
    {
        super(parent,accessor.connector.traceManager,accessor.connector.logger,accessor.connector.disruptor,traceCategoryOverride!=null?traceCategoryOverride:sql,accessor.connector.getName());
        this.accessor=accessor;
        addLogItem(new Item("sql",sql));
    }
    public int logRowsUpdated(int updated)
    {
        Connector connector=this.accessor.connector;
        connector.rowsUpdatedRate.add(updated);
        connector.updateRate.increment();
        addLogItem(new Item("rowsUpdated",updated));
        return updated;
    }
    public int[] logRowsUpdated(int[] updateCounts)
    {
        Connector connector=this.accessor.connector;
        for (int updated:updateCounts)
        {
            connector.rowsUpdatedRate.add(updated);
            addLogItem(new Item("rowsUpdated",updated));
        }
        connector.updateRate.increment();
        return updateCounts;
    }
    public int logRowsQueried(int queried)
    {
        Connector connector=this.accessor.connector;
        connector.rowsQueriedRate.add(queried);
        connector.queryRate.increment();
        addLogItem(new Item("rowsQueried",queried));
        return queried;
    }
    public void logRowsQueried(RowSet[] rowSets)
    {
        Connector connector=this.accessor.connector;
        for (RowSet rowSet:rowSets)
        {
            connector.rowsQueriedRate.add(rowSet.size());
            addLogItem(new Item("rowsQueried",rowSet.size()));
        }
        connector.queryRate.increment();
    }
    
    @Override
    public Exception handleThrowable(Throwable throwable) 
    {
        try
        {
            this.accessor.connector.executeFailures.increment();
            Connection connection=this.accessor.connection;
            this.accessor.connection = null;
            connection.close();
            return super.handleThrowable(new MultiException(throwable));
        }
        catch (Throwable t)
        {
            return super.handleThrowable(new MultiException(t,throwable));
        }
    }
}