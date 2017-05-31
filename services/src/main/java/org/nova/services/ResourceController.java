package org.nova.services;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.client.HttpClient;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Server;
import org.nova.annotations.Description;
import org.nova.concurrent.Future;
import org.nova.concurrent.TimerTask;
import org.nova.configuration.Configuration;
import org.nova.configuration.ConfigurationItem;
import org.nova.core.Utils;
import org.nova.frameworks.ServerApplication;
import org.nova.html.Attribute;
import org.nova.html.HtmlWriter;
import org.nova.html.Selection;
import org.nova.html.TableList;
import org.nova.html.operator.Menu;
import org.nova.html.operator.OperatorResult;
import org.nova.html.operator.OperatorResultWriter;
import org.nova.html.tags.input_submit;
import org.nova.html.widgets.AjaxButton;
import org.nova.html.widgets.AjaxQueryResult;
import org.nova.html.widgets.AjaxQueryResultWriter;
import org.nova.http.Header;
import org.nova.http.client.HttpClientConfiguration;
import org.nova.http.client.HttpClientFactory;
import org.nova.http.client.PathAndQueryBuilder;
import org.nova.http.client.TextClient;
import org.nova.http.client.TextResponse;
import org.nova.http.server.CSharpClassWriter;
import org.nova.http.server.ContentDecoder;
import org.nova.http.server.ContentEncoder;
import org.nova.http.server.ContentReader;
import org.nova.http.server.ContentWriter;
import org.nova.http.server.Context;
import org.nova.http.server.Filter;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.HttpServer;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.JettyServerFactory;
import org.nova.http.server.ParameterInfo;
import org.nova.http.server.ParameterSource;
import org.nova.http.server.RequestHandler;
import org.nova.http.server.RequestHandlerNotFoundLogEntry;
import org.nova.http.server.RequestLogEntry;
import org.nova.http.server.Response;
import org.nova.http.server.HtmlContentWriter;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.DefaultValue;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.POST;
import org.nova.http.server.annotations.Path;
import org.nova.http.server.annotations.PathParam;
import org.nova.http.server.annotations.QueryParam;
import org.nova.logging.JSONBufferedLZ4Queue;
import org.nova.logging.LogDirectoryInfo;
import org.nova.logging.LogDirectoryManager;
import org.nova.metrics.AverageAndRate;
import org.nova.metrics.CategoryMeters;
import org.nova.metrics.CountAverageRateMeter;
import org.nova.metrics.CountAverageRateMeterBox;
import org.nova.metrics.CountMeter;
import org.nova.metrics.CountMeterBox;
import org.nova.metrics.LevelMeter;
import org.nova.metrics.LevelMeterBox;
import org.nova.metrics.RateMeter;
import org.nova.metrics.RateMeterBox;
import org.nova.operations.OperatorVariable;
import org.nova.security.Vault;
import org.nova.test.Testing;
import org.nova.testing.TestTraceClient;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceNode;
import org.nova.tracing.TraceStats;

import com.google.common.base.Strings;

@Description("Handlers for server operator pages")
@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({ JSONContentReader.class, JSONPatchContentReader.class })
@ContentWriters({OperatorResultWriter.class, HtmlContentWriter.class})

public class ResourceController
{
    public final ServerApplication serverApplication;
    private int cacheMaxAge = 300;
    private String cacheControlValue = "public";
//    private HashMap<String,Integer> 

    public ResourceController(ServerApplication serverApplication) throws Throwable
    {
        this.cacheMaxAge = serverApplication.getConfiguration().getIntegerValue("ResourceController.cache.maxAge", 300);
        this.cacheControlValue = serverApplication.getConfiguration().getValue("ResourceController.cache.controlValue", "public");
        this.serverApplication = serverApplication;
    }

    @GET
    @Path("/resources/{+}")
    public void resource(@PathParam(PathParam.AT_LEAST_ONE_SEGMENT) String file, Context context, Trace trace) throws Throwable
    {
        HttpServletResponse response = context.getHttpServletResponse();
        context.setHandled(true);
        byte[] bytes;
        try
        {
            bytes = this.serverApplication.getFileCache().get(trace, file);
            if (this.serverApplication.isDebug())
            {
                System.out.println("Resource:"+file);
            }
        }
        catch (Throwable t)
        {
            if (this.serverApplication.isDebug())
            {
                TestTraceClient.clientLog(file + " not found");
            }
            response.setStatus(HttpStatus.NOT_FOUND_404);
            return;
        }
        String contentType = this.serverApplication.getTypeMappings().getContentType(file);
        if (contentType != null)
        {
            response.setContentType(contentType);
        }
        response.setHeader("Cache-Control",
                (this.cacheControlValue == null || this.cacheControlValue.length() == 0) ? "max-age=" + this.cacheMaxAge : this.cacheControlValue + ",max-age=" + this.cacheMaxAge);
        response.setStatus(HttpStatus.OK_200);
        response.getOutputStream().write(bytes);
    }
}
