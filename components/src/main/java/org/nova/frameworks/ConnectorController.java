package org.nova.frameworks;

import java.util.HashMap;
import java.util.Map.Entry;

import org.nebula.sqlserver.ConnectorAndMigrationConfiguration;
import org.nebula.sqlserver.DatabaseUpdatePermissions;
import org.nebula.sqlserver.DatabaseUpdater;
import org.nova.annotations.Description;
import org.nova.configuration.Configuration;
import org.nova.html.elements.Element;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.tags.em;
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
import org.nova.metrics.RateMeter;
import org.nova.metrics.RateSample;
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
        this.application.getMenuBar().add("/operator/connector/viewAll", "Connectors","View All");
        this.application.getOperatorServer().registerHandlers(this);
    }


    public void track(String name,Connector connector) throws Throwable
    {
        synchronized(this)
        {
            this.connectors.put(name, connector);
        }
    }
    public void track(Connector connector) throws Throwable
    {
        track(connector.getName(),connector);
    }
    
    private void write(NameValueList list,String name,CountMeter meter)
    {
        list.add(name, meter.getCount());
    }

    private void write(Row row,RateMeter meter,double minimalResetDurationS)
    {
        RateSample sample=meter.sample(minimalResetDurationS);
        row.add(String.format("%.3f",sample.getWeightedRate()));
        row.add(sample.getCount());
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
        track(connector);
        return connector;
    }    


    @GET
    @Path("/operator/connector/viewAll")
    public Element viewAll(@QueryParam("minimalResetDurationS") @DefaultValue("10.0") double minimalResetDurationS) throws Throwable
    {
        OperatorPage page=this.application.buildOperatorPage("View All Connectors");
        DataTable table=page.content().returnAddInner(new DataTable(page.head()));
        table.lengthMenu(-1,20,40,60);
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

}
