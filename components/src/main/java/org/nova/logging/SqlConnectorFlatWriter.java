package org.nova.logging;

import java.sql.Timestamp;

import org.nova.flow.Node;
import org.nova.flow.Packet;
import org.nova.metrics.RateMeter;
import org.nova.sqldb.Accessor;
import org.nova.sqldb.Connector;

public class SqlConnectorFlatWriter extends Node
{
    final private Connector connector;
    final private RateMeter rateMeter;
    private Throwable throwable;
    final private Formatter formatter;
    final private String categoryOverride;
    final private String insert;
    
    public SqlConnectorFlatWriter(Connector connector,String tableName,Formatter formatter,String categoryOverride)
    {
        this.insert="INSERT INTO "+tableName+" (Number,Created,LogLevel,Catetory,Text) VALUES(?,?,?,?,?)";
        this.connector=connector;
        this.rateMeter=new RateMeter();
        this.formatter=formatter;
        this.categoryOverride=categoryOverride==null?this.getClass().getSimpleName():categoryOverride;
    }
    public SqlConnectorFlatWriter(Connector connector)
    {
        this(connector,"logs",new JSONFormatter(),null);
    }
    
    private void write(LogEntry[] entries,int count) throws Exception, Throwable
    {
        Object[][] batchParameters=new Object[count][];
        for (int i=0;i<count;i++)
        {
            String text=this.formatter.format(entries[i]);
            batchParameters[i]=new Object[1];
            int index=0;
            LogEntry entry=entries[i];
            batchParameters[i][index++]=entry.getNumber();
            batchParameters[i][index++]=new Timestamp(entry.getCreated());
            batchParameters[i][index++]=entry.getLogLevel().toString();
            batchParameters[i][index++]=entry.getCategory();
            batchParameters[i][index++]=text;                
        }
        try (Accessor accessor=this.connector.openAccessor(null, this.categoryOverride))
        {
            accessor.executeBatchUpdate(null, this.categoryOverride, batchParameters, insert);
        }
    }
    
    @Override
    public void process(Packet packet) throws Throwable
    {
        synchronized(this)
        {
            if (this.throwable==null)
            {
                try
                {
                    int size=packet.sizeOrType();
                    if (size>0)
                    {
                        LogEntry[] entries=new LogEntry[size];
                        int count=0;
                        for (int i = 0; i < packet.sizeOrType(); i++)
                        {
                            Object object = packet.get()[i];
                            if ((object != null) && (object instanceof LogEntry))
                            {
                                entries[count++]=(LogEntry)object;
                            }
                        }
                        write(entries,count);
                    }
                    return;
                }
                catch (Throwable t)
                {
                    this.throwable=t;
                }
            }
            throw throwable;
        }
    }

    @Override
    public void flush() throws Throwable
    {
    }

    @Override
    public void beginGroup(long segmentIndex) throws Throwable
    {
    }

    @Override
    public void endGroup() throws Throwable
    {
    }

}
