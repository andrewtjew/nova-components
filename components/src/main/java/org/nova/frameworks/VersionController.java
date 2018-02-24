package org.nova.frameworks;

import java.util.HashMap;
import java.util.Map.Entry;

import org.nebula.sqlserver.ConnectorAndMigrationConfiguration;
import org.nebula.sqlserver.DatabaseUpdatePermissions;
import org.nebula.sqlserver.DatabaseUpdater;
import org.nova.annotations.Description;
import org.nova.configuration.Configuration;
import org.nova.html.elements.Element;
import org.nova.html.operator.TitleText;
import org.nova.html.tags.em;
import org.nova.html.widgets.DataTable;
import org.nova.html.widgets.NameValueList;
import org.nova.html.widgets.Row;
import org.nova.http.client.PathAndQueryBuilder;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.JSONContentWriter;
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

@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({ JSONContentReader.class})
@ContentWriters({JSONContentWriter.class})
public class VersionController
{
    final private long version;
    
    public VersionController(long version) throws Exception
    {
        this.version=version;
    }

    @GET
    @Path("/version")
    @ContentWriters({JSONContentWriter.class})
    public Long version()
    {
        return this.version;
    }

}
