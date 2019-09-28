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
    private int cacheControlMaxAge = 300;
    private String cacheControlValue = "public";
    private final boolean cacheControl;
    
    public final static boolean TEST=true;

    public ResourceController(ServerApplication serverApplication) throws Throwable
    {
        this.cacheControl= serverApplication.getConfiguration().getBooleanValue("ResourceController.cacheControl", true);
        this.cacheControlMaxAge = serverApplication.getConfiguration().getIntegerValue("ResourceController.cacheControl.maxAgeS", 3600*24);
        this.cacheControlValue = serverApplication.getConfiguration().getValue("ResourceController.cacheControl.controlValue", "public");
        this.serverApplication = serverApplication;
    }

    @GET
    @Path("/resources/{+}")
    @Log(responseContent=false)
    public void resource(@PathParam(PathParam.AT_LEAST_ONE_SEGMENT) String file, Context context, Trace parent) throws Throwable
    {
        Trace trace=new Trace(parent,"ResourceController.resource");
        trace.setDetails(file);
        try
        {
            HttpServletResponse response = context.getHttpServletResponse();
            context.setCaptured(true);
            byte[] bytes;
            try
            {
                bytes = this.serverApplication.getFileCache().get(parent, file);
                if (TEST)
                {
                    Testing.printf("Resource:"+file);
                }
            }
            catch (Throwable t)
            {
                trace.close(t);
                if (TEST)
                {
                    Testing.printf("Resource: "+file + " not found");
                }
                response.setStatus(HttpStatus.NOT_FOUND_404);
                return;
            }
            String contentType = this.serverApplication.getContentTypeMappings().getContentType(file);
            if (contentType != null)
            {
                response.setContentType(contentType);
            }
//            response.setContentLength(bytes.length);
            if (this.cacheControl)
            {
                response.setHeader("Cache-Control",(this.cacheControlValue == null || this.cacheControlValue.length() == 0) ? "max-age=" + this.cacheControlMaxAge : this.cacheControlValue + ",max-age=" + this.cacheControlMaxAge);
            }
            response.setStatus(HttpStatus.OK_200);
            response.getOutputStream().write(bytes);
        }
        finally
        {
            trace.close();
        }
    }
}
