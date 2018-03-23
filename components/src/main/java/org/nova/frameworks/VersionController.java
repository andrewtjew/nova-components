package org.nova.frameworks;

import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.Path;

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
