package org.nova.frameworks;

import java.util.HashMap;
import java.util.Map.Entry;

import org.nebula.sqlserver.ConnectorAndMigrationConfiguration;
import org.nebula.sqlserver.DatabaseUpdatePermissions;
import org.nebula.sqlserver.DatabaseUpdater;
import org.nova.annotations.Description;
import org.nova.configuration.Configuration;
import org.nova.core.Utils;
import org.nova.html.elements.Element;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.tags.em;
import org.nova.html.tags.hr;
import org.nova.html.tags.p;
import org.nova.html.tags.textarea;
import org.nova.html.widgets.DataTable;
import org.nova.html.widgets.NameValueList;
import org.nova.html.widgets.Row;
import org.nova.html.widgets.TitleText;
import org.nova.http.client.PathAndQueryBuilder;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.HtmlContentWriter;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.DefaultValue;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.POST;
import org.nova.http.server.annotations.Path;
import org.nova.http.server.annotations.QueryParam;
import org.nova.metrics.CountMeter;
import org.nova.metrics.LevelMeter;
import org.nova.metrics.LevelSample;
import org.nova.metrics.LongValueMeter;
import org.nova.metrics.LongValueSample;
import org.nova.metrics.RateMeter;
import org.nova.metrics.RateSample;
import org.nova.sqldb.Accessor;
import org.nova.sqldb.Connector;
import org.nova.tracing.Trace;

@Description("Handlers for server operator pages")
@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({ JSONContentReader.class})
@ContentWriters({HtmlElementWriter.class})
public class ConnectorController
{
    final private HashMap<String,Connector> connectors;
    final private ServerApplication application;
    
    public ConnectorController(ServerApplication application) throws Exception
    {
        this.connectors=new HashMap<>();
        this.application=application;
        this.application.getMenuBar().add("/operator/connectors/view", "Connectors","View All");
        this.application.getMenuBar().add("/operator/connector/pools/view", "Connectors","View Pools");
        this.application.getMenuBar().add("/operator/connector/stackTraces/view", "Connectors","View Active StackTraces");
        this.application.getOperatorServer().registerHandlers(this);
    }


    public void track(String name,Connector connector) throws Throwable
    {
        synchronized(this)
        {
            if (this.connectors.containsKey(name))
            {
                throw new Exception("Provide unique names. Clashing name="+name);
            }
            this.connectors.put(name, connector);
        }
        this.application.getMeterStore().addMeters("/connectors/"+name,connector);
    }
    public void track(Connector connector) throws Throwable
    {
        track(connector.getName(),connector);
    }
    
    public Connector initializeConnector(Trace parent,String configurationNameFragment) throws Throwable
    {
        Configuration configuration=this.application.getConfiguration();
        DatabaseUpdatePermissions permisssions=configuration.getJSONObject("Application.Database.MigrationPermission."+configurationNameFragment,new DatabaseUpdatePermissions(),DatabaseUpdatePermissions.class);
        DatabaseUpdater migrator=new DatabaseUpdater(this.application.getCoreEnvironment(),permisssions);
        ConnectorAndMigrationConfiguration connectorAndMigrationconfiguration=configuration.getJSONObject("Application.Database.ConnectorAndMigrationConfiguration."+configurationNameFragment, ConnectorAndMigrationConfiguration.class);
        if (connectorAndMigrationconfiguration==null)
        {
            return null;
        }
        Connector connector=migrator.connectAndMigrate(parent, this.application.getCoreEnvironment(), connectorAndMigrationconfiguration);
        connector.setCaptureActivateStackTrace(connectorAndMigrationconfiguration.captureActivateStackTrace);
        track(configurationNameFragment,connector);
        return connector;
    }    


    @GET
    @Path("/operator/connectors/view")
    public Element viewAll(@QueryParam("minimalResetDurationS") @DefaultValue("10.0") double minimalResetDurationS) throws Throwable
    {
        OperatorPage page=this.application.buildOperatorPage("View Connector Stats");
        DataTable table=page.content().returnAddInner(new DataTable(page.head()));
        table.lengthMenu(-1,40,60,100);
        Row row=new Row();
        row.add("Name");
        row.add(new TitleText("Row updated rate (per second)","Updated"));
        row.add(new TitleText("Rows updated","Count"));
        row.add(new TitleText("Update rate (per second)","Updates"));
        row.add(new TitleText("Update count","Count"));

        row.add(new TitleText("Rows queried rate (per second)","Queried"));
        row.add(new TitleText("Rows queried","Count"));
        row.add(new TitleText("Query rate (per second)","Queries"));
        row.add(new TitleText("Query count","Count"));

        row.add(new TitleText("Call rate (per second)","Calls"));
        row.add(new TitleText("Call count","Count"));

        row.add(new TitleText("Execute failures","Fails"));
        table.setHeadRow(row);
        

        synchronized(this)
        {
            for (Entry<String, Connector> entry:this.connectors.entrySet())
            {
                Connector connector=entry.getValue();
                row=new Row();
                row.add(entry.getKey());
                write(row,connector.getRowsUpdatedRate(),minimalResetDurationS);
                write(row,connector.getUpdateRate(),minimalResetDurationS);
                write(row,connector.getRowsQueriedRate(),minimalResetDurationS);
                write(row,connector.getQueryRate(),minimalResetDurationS);
                write(row,connector.getCallRate(),minimalResetDurationS);
                row.add(connector.getExecuteFailures().getCount());
                table.addBodyRow(row);
            }
        }

        return page;
    }
    
    private void write(NameValueList list,String name,CountMeter meter)
    {
        list.add(name, meter.getCount());
    }

    private void write(Row row,RateMeter meter,double minimalResetDurationS)
    {
        RateSample sample=meter.sample(minimalResetDurationS);
        row.add(String.format("%.3f",sample.getWeightedRate()));
        row.add(sample.getSamples());
    }

    private void writeMax(Row row,LevelMeter meter)
    {
        LevelSample sample=meter.sample();
        row.add(sample.getLevel());
        row.add(sample.getMaxLevel());
        if (sample.getMaxLevel()>sample.getBaseLevel())
        {
            row.add(Utils.millisToLocalDateTime(sample.getMaxLevelInstantMs()));
        }
        else
        {
            row.add("");
        }
    }
    private void writeMin(Row row,LevelMeter meter)
    {
        LevelSample sample=meter.sample();
        row.add(sample.getLevel());
        row.add(sample.getBaseLevel());
        row.add(sample.getMinLevel());
        if (sample.getMinLevel()<sample.getBaseLevel())
        {
            row.add(Utils.millisToLocalDateTime(sample.getMinLevelInstantMs()));
        }
        else
        {
            row.add("");
        }
    }

    private void write(Row row,LongValueMeter meter,long used)
    {
        LongValueSample sample=meter.sample();
        row.add(sample.getTotalCount());
        if (used>0)
        {
            double percentage=(sample.getTotalCount()*100.0)/used;
            row.add(String.format("%.3f",percentage));
        }
        else
        {
            row.add("");
        }
        row.add(String.format("%.3f",sample.getWeightedAverage(0)/1.0e6));
        row.add(String.format("%.3f",sample.getWeightedStandardDeviation(0)/1.0e6));
        if (sample.getMax()>Long.MIN_VALUE)
        {
            row.add(String.format("%.3f",sample.getMax()/1.0e6));
        }
        else
        {
            row.add("");
        }
            
        row.add(Utils.millisToLocalDateTime(sample.getMaxInstantMs()));
    }

    @GET
    @Path("/operator/connector/pools/view")
    public Element viewUsage(@DefaultValue("1.0") @QueryParam("minimalResetDurationS") double minimalResetDurationS) throws Throwable
    {
        OperatorPage page=this.application.buildOperatorPage("View Connector Pools");
        DataTable table=page.content().returnAddInner(new DataTable(page.head()));
        table.lengthMenu(-1,40,60,100);
        Row row=new Row();
        row.add("Name");
        row.add(new TitleText("Accessors available in pool","Available"));
        row.add(new TitleText("Accessor pool size","Size"));
        row.add(new TitleText("Minimum accessors available","Min"));
        row.add(new TitleText("Instant when minimum acccessors occurred","Min available Instant"));
        row.add(new TitleText("Threads waiting for accessors","Waiting"));
        row.add(new TitleText("Maximum threads waiting for accessors","Max"));
        row.add(new TitleText("Instant when maximum threads are waiting for accessors","Max Waiting Instant"));
        row.add(new TitleText("Number of times connector is used","Used"));
        row.add(new TitleText("Connector use rate (per second)","Rate"));
        row.add(new TitleText("Number of times when waiting for accessor occurs","Waits"));
        row.add(new TitleText("Percentage times waits occurred","%"));
        row.add(new TitleText("Average wait for accessor (ms)","Ave Wait"));
        row.add(new TitleText("Standard deviation wait for accessor (ms)","StdDev"));
        row.add(new TitleText("Maximum wait for accessor (ms)","Max"));
        row.add(new TitleText("Instant when maximum wait occurs","Max Duration Instant"));
        table.setHeadRow(row);

        synchronized(this)
        {
            for (Entry<String, Connector> entry:this.connectors.entrySet())
            {
                Connector connector=entry.getValue();
                row=new Row();
                row.add(entry.getKey());

                writeMin(row,connector.getAccessorsAvailableMeter());
                writeMax(row,connector.getWaitingForAcessorsMeter());
                long used=connector.getUseMeter().getTotalCount();
                row.add(used);
                RateSample useSample=connector.getUseMeter().sample(minimalResetDurationS);
                row.add(String.format("%.3f", useSample.getWeightedRate()));
                
                write(row,connector.getAcessorsWaitNsMeter(),used);
                
                table.addBodyRow(row);
            }
        }

        return page;
    }
    @GET
    @Path("/operator/connector/stackTraces/view")
    public Element viewStackTraces() throws Throwable
    {
        OperatorPage page=this.application.buildOperatorPage("View Connector Active Stack Traces");
        
        synchronized(this)
        {
            for (Entry<String, Connector> entry:this.connectors.entrySet())
            {
                Connector connector=entry.getValue();
                for (Accessor accessor:connector.getSnapshotOfAccessorsInUse())
                {
                    String stackTrace=Utils.toString(accessor.getActivateThread().getStackTrace());
                    page.content().addInner("Trace:"+accessor.getParent().getCategory());
                    page.content().addInner(", Thread:"+accessor.getActivateThread().getName());
                    page.content().addInner(new p());
                    page.content().addInner("Current Stack Trace:");
                    page.content().returnAddInner(new textarea()).rows(15).addInner(stackTrace).style("width:100%;");
                    StackTraceElement[] stackTraceElements=accessor.getActivateStackTrace();
                    if (stackTraceElements!=null)
                    {
                        page.content().addInner("Activate Stack Trace:");
                        page.content().returnAddInner(new textarea()).rows(15).addInner(Utils.toString(stackTraceElements)).style("width:100%;");
                    }
                    page.content().addInner(new hr());
                }
            }
        }

        return page;
    }
}
