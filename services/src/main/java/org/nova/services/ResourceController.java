package org.nova.services;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.annotations.Description;
import org.nova.frameworks.ServerApplication;
import org.nova.http.server.Context;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.Log;
import org.nova.http.server.annotations.Path;
import org.nova.http.server.annotations.PathParam;
import org.nova.test.Testing;
import org.nova.tracing.Trace;

@Description("Handlers for server operator pages")
@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({ JSONContentReader.class, JSONPatchContentReader.class })

public class ResourceController
{
    public final ServerApplication serverApplication;
    private int cacheMaxAge = 300;
    private String cacheControlValue = "public";
    public final static boolean TEST=true;

    public ResourceController(ServerApplication serverApplication) throws Throwable
    {
        this.cacheMaxAge = serverApplication.getConfiguration().getIntegerValue("ResourceController.cache.maxAgeS", 3600*24);
        this.cacheControlValue = serverApplication.getConfiguration().getValue("ResourceController.cache.controlValue", "public");
        this.serverApplication = serverApplication;
    }

    @GET
    @Path("/resources/{+}")
    @Log(responseContent=false)
    public void resource(@PathParam(PathParam.AT_LEAST_ONE_SEGMENT) String file, Context context, Trace trace) throws Throwable
    {
        HttpServletResponse response = context.getHttpServletResponse();
        context.setCaptured(true);
        byte[] bytes;
        try
        {
            bytes = this.serverApplication.getFileCache().get(trace, file);
            if (TEST)
            {
                Testing.printf("Resource:"+file);
            }
        }
        catch (Throwable t)
        {
            if (TEST)
            {
                Testing.printf("Resource: "+file + " not found");
            }
            System.out.println("Resource: "+file + " not found");
            response.setStatus(HttpStatus.NOT_FOUND_404);
            return;
        }
        String contentType = this.serverApplication.getTypeMappings().getContentType(file);
        if (contentType != null)
        {
            response.setContentType(contentType);
        }
        response.setContentLength(bytes.length);
        response.setHeader("Cache-Control",(this.cacheControlValue == null || this.cacheControlValue.length() == 0) ? "max-age=" + this.cacheMaxAge : this.cacheControlValue + ",max-age=" + this.cacheMaxAge);
        response.setStatus(HttpStatus.OK_200);
        response.getOutputStream().write(bytes);
    }
}
