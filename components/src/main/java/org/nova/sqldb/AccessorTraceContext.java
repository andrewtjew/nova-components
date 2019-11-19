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
package org.nova.sqldb;

import org.nova.logging.Item;
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
    
    /*
    @Override
    public Throwable handleThrowable(Throwable throwable) 
    {
        try
        {
            this.accessor.connector.executeFailures.increment();
//            Connection connection=this.accessor.connection;
//            this.accessor.connection = null;
//            connection.close();
            return super.handleThrowable(throwable);
        }
        catch (Throwable t)
        {
            return super.handleThrowable(new MultiException(t,throwable));
        }
    }
    */
}
