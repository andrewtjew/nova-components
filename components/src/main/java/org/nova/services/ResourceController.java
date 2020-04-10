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
package org.nova.services;
import java.io.File;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.nova.annotations.Description;
import org.nova.frameworks.ServerApplication;
import org.nova.html.elements.Element;
import org.nova.http.client.BinaryResponse;
import org.nova.http.client.HttpClientFactory;
import org.nova.http.client.JSONClient;
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
import org.nova.utils.FileUtils;

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
            String external=null;
            if (Element.HREF_LOCAL_DIRECTORY!=null)
            {
                int colon=file.indexOf(":");
                if (colon>0)
                {
                    int slash=file.lastIndexOf('/',colon);
                    external=file.substring(slash+1);
                    String root=file.substring(0,slash+1);
                    file=root+external.substring(colon-slash+2);
                    
                }
            }
            
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
                if (external!=null)
                {
                    int colon=external.indexOf(':');
                    int firstSlash=external.indexOf('/',colon+3);
//                    int secondSlash=external.indexOf('/',firstSlash);
//                    int lastSlash=external.lastIndexOf('/');
                    
                    //String protocol=external.substring(0,colon);
                    //String path=external.substring(colon+2,lastSlash);
                    //String fileExt=external.substring(lastSlash);
                    String endPoint=external.substring(0,firstSlash);
                    String pathAndQuery=external.substring(firstSlash);
                    
                    JSONClient client=new JSONClient(this.serverApplication.getTraceManager(),this.serverApplication.getLogger(),endPoint);
                    BinaryResponse binaryResponse=client.getBinary(parent, null, pathAndQuery);
                    
                    
                    String fileName=this.serverApplication.getFileCache().getLocalDirectory()+"/"+file;
                    String dirs=FileUtils.toNativePath(fileName.substring(0,fileName.lastIndexOf('/')));
                    new File(dirs).mkdirs();
                    bytes=binaryResponse.get();
                    FileUtils.writeBinaryFile(fileName, bytes);
                    
                    
                }
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
