package org.nova.http.client;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.nova.concurrent.FutureScheduler;
import org.nova.concurrent.TimerScheduler;
import org.nova.concurrent.TimerTask;
import org.nova.concurrent.TimerTask.TimeBase;
import org.nova.core.Utils;
import org.nova.html.tags.table;
import org.nova.http.Header;
import org.nova.json.ObjectMapper;
import org.nova.logging.Item;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

import com.amazonaws.util.IOUtils;
import com.nova.disrupt.Disruptor;
import com.nova.disrupt.DisruptorTraceContext;

public class Client
{
	final private TraceManager traceManager;
	final private Logger logger;
	final private HttpClient client;
	final private String endPoint;
	final private ArrayList<Header> headers;
	final private Disruptor disruptor;
    final private TimerTask timerTask;
	
	public Client(TraceManager traceManager,Logger logger,TimerScheduler scheduler,long idleConnectionTimeoutMs,Disruptor disruptor,String endPoint,HttpClient client,String patchType,Header...headers) throws Throwable
	{
		this.traceManager=traceManager;
		this.disruptor=disruptor;
		this.logger=logger;
		this.endPoint=endPoint;
		this.client=client;
		this.headers=new ArrayList<>();
		for (Header header:headers)
		{
		    setHeader(header);
		}
		if (scheduler!=null)
		{
		    this.timerTask=scheduler.schedule("JSONClient.closeIdleConnections",TimeBase.FREE,idleConnectionTimeoutMs,idleConnectionTimeoutMs,(trace,task)->{closeIdleConnections(trace);});
		}
		else
		{
		    this.timerTask=null;
		}
	}
	
	public void closeIdleConnections(Trace parent)
	{
        HttpClientUtils.closeQuietly(this.client);
	}
	
	
    public Client(TraceManager traceManager,Logger logger,TimerScheduler scheduler,long idleConnectionTimeoutMs,String endPoint,HttpClient client) throws Throwable
    {
        this(traceManager,logger,scheduler,idleConnectionTimeoutMs,null,endPoint,client,"application/merge-patch+json");
    }
    public Client(TraceManager traceManager,Logger logger,String endPoint,HttpClient client) throws Throwable
    {
        this(traceManager,logger,null,0,endPoint,client);
    }
    public Client(TraceManager traceManager,Logger logger,String endPoint) throws Throwable
    {
        this(traceManager,logger,endPoint,HttpClientFactory.createClient());
    }
    public void setHeader(Header header)
    {
        for (int i=0;i<this.headers.size();i++)
        {
            if (Utils.equals(this.headers.get(i).getName(), header.getName()))
            {
                this.headers.remove(i);
                break;
            }
        }
        this.headers.add(header);
    }
	
    public String getEndPoint()
    {
        return this.endPoint;
    }
    
    public void close()
    {
        synchronized (this)
        {
            if (this.timerTask!=null)
            {
                this.timerTask.cancel();
            }
        }
        HttpClientUtils.closeQuietly(this.client);
    }

	protected void logHeaders(DisruptorTraceContext context,org.apache.http.Header[] headers)
	{
        for (org.apache.http.Header header:headers)
        {
            context.addLogItem(new Item("requestHeader:"+header.getName(),header.getValue()));
        }
	}
	protected DisruptorTraceContext createDisruptorContext(Trace parent,String traceCategory)
	{
	    return new DisruptorTraceContext(parent, this.traceManager, this.logger, this.disruptor, traceCategory,this.endPoint);
	}
	
    private String processResponse(HttpResponse response,DisruptorTraceContext context) throws Throwable
    {
        context.addLogItem(new Item("statusCode",response.getStatusLine().getStatusCode()));
        for (org.apache.http.Header header:response.getAllHeaders())
        {
            context.addLogItem(new Item("responseHeader:"+header.getName(),header.getValue()));
        }
        String responseContent=Utils.readString(response.getEntity().getContent());
        if (responseContent.length()>0)
        {
            context.addLogItem(new Item("response",responseContent));
        }
        return responseContent;
    }
    
    public int get(Trace parent,String traceCategoryOverride,String pathAndQuery) throws Throwable
    {
        return get(parent,traceCategoryOverride,pathAndQuery,null);
    }
    
    public int get(Trace parent,String traceCategoryOverride,String pathAndQuery,String acceptContentType) throws Throwable
    {
        return get(parent,traceCategoryOverride,pathAndQuery,acceptContentType,new Header[0]);
    }
    
    public int get(Trace parent,String traceCategoryOverride,String pathAndQuery,String acceptContentType,Header...headers) throws Throwable
    {
        try (DisruptorTraceContext context=new DisruptorTraceContext(parent, this.traceManager, this.logger, this.disruptor, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery,this.endPoint))
        {
            try 
            {
                HttpGet get=new HttpGet(this.endPoint+pathAndQuery);
                context.addLogItem(new Item("endPoint",this.endPoint));
                context.addLogItem(new Item("pathAndQuery",pathAndQuery));
    
                if (this.headers!=null)
                {
                    for (Header header:this.headers)
                    {
                        get.setHeader(header.getName(),header.getValue());
                    }
                }
                for (Header header:headers)
                {
                    get.setHeader(header.getName(),header.getValue());
                }
                if (acceptContentType!=null)
                {
                    get.setHeader("Accept",acceptContentType);
                }
                logHeaders(context,get.getAllHeaders());
    
                context.beginWait();
                HttpResponse response=this.client.execute(get);
                context.endWait();
                try
                {
                    processResponse(response, context);
                    return response.getStatusLine().getStatusCode();
                }
                finally
                {
                    response.getEntity().getContent().close();
                }
            }       
            catch (Throwable t)
            {
                throw context.handleThrowable(t);
            }
        }
    }
    public int get(Trace parent,String traceCategoryOverride,OutputStream outputStream,String pathAndQuery) throws Throwable
    {
        return get(parent,traceCategoryOverride,outputStream,pathAndQuery,null);
    }

    public int get(Trace parent,String traceCategoryOverride,OutputStream outputStream,String pathAndQuery,String acceptContentType) throws Throwable
    {
        return get(parent,traceCategoryOverride,outputStream,pathAndQuery,acceptContentType,new Header[0]);
    }

    private String getCategory(String traceCategoryOverride,String pathAndQuery)
    {
        return traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery;
    }
    
    public int get(Trace parent,String traceCategoryOverride,OutputStream outputStream,String pathAndQuery,String acceptContentType,Header...headers) throws Throwable
    {
        try (DisruptorTraceContext context=new DisruptorTraceContext(parent, this.traceManager, this.logger, this.disruptor, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery,this.endPoint))
        {
            try 
            {
                HttpGet get=new HttpGet(this.endPoint+pathAndQuery);
                context.addLogItem(new Item("endPoint",this.endPoint));
                context.addLogItem(new Item("pathAndQuery",pathAndQuery));
    
                if (this.headers!=null)
                {
                    for (Header header:this.headers)
                    {
                        get.setHeader(header.getName(),header.getValue());
                    }
                }
                for (Header header:headers)
                {
                    get.setHeader(header.getName(),header.getValue());
                }
                if (acceptContentType!=null)
                {
                    get.setHeader("Accept",acceptContentType);
                }
                logHeaders(context,get.getAllHeaders());
    
                context.beginWait();
                HttpResponse response=this.client.execute(get);
                context.endWait();
                try
                {
                    IOUtils.copy(response.getEntity().getContent(), outputStream);
                    int statusCode=response.getStatusLine().getStatusCode();
                    context.addLogItem(new Item("statusCode",statusCode));
                    return statusCode;
                }
                finally
                {
                    response.getEntity().getContent().close();
                }
            }       
            catch (Throwable t)
            {
                throw context.handleThrowable(t);
            }
        }
    }
    
    public int delete(Trace parent,String traceCategoryOverride,String pathAndQuery) throws Throwable
    {
        return delete(parent,traceCategoryOverride,pathAndQuery,new Header[0]);
    }

    public int delete(Trace parent,String traceCategoryOverride,String pathAndQuery,Header...headers) throws Throwable
	{
        try (DisruptorTraceContext context=createDisruptorContext(parent, getCategory(traceCategoryOverride,pathAndQuery)))
        {
            try 
            {
    			HttpDelete delete=new HttpDelete(this.endPoint+pathAndQuery);
                context.addLogItem(new Item("endPoint",this.endPoint));
                context.addLogItem(new Item("pathAndQuery",pathAndQuery));
    
                if (this.headers!=null)
    			{
    				for (Header header:this.headers)
    				{
    					delete.setHeader(header.getName(),header.getValue());
    				}
    			}
                for (Header header:headers)
                {
                    delete.setHeader(header.getName(),header.getValue());
                }
                logHeaders(context,delete.getAllHeaders());
                
                context.beginWait();
                HttpResponse response=this.client.execute(delete);
                context.endWait();
    			try
    			{
    			    processResponse(response, context);
    				return response.getStatusLine().getStatusCode();
    			}
    			finally
    			{
    				response.getEntity().getContent().close();
    			}
    		}		
            catch (Throwable t)
            {
                throw context.handleThrowable(t);
            }
        }
	}
    
    public PostContext openPost(FutureScheduler scheduler,Trace parent,String traceCategoryOverride,String pathAndQuery,String contentType,String acceptContentType,Header...headers) throws Throwable
    {
        String traceCategory=getCategory(traceCategoryOverride, pathAndQuery);
        DisruptorTraceContext context=createDisruptorContext(parent, traceCategory);
        try
        {
            try 
            {
                HttpPost post=new HttpPost(this.endPoint+pathAndQuery);
                context.addLogItem(new Item("endPoint",this.endPoint));
                context.addLogItem(new Item("pathAndQuery",pathAndQuery));
                if (this.headers!=null)
                {
                    for (Header header:this.headers)
                    {
                        post.setHeader(header.getName(),header.getValue());
                    }
                }
                for (Header header:headers)
                {
                    post.setHeader(header.getName(),header.getValue());
                }
                if (acceptContentType!=null)
                {
                    post.setHeader("Accept",acceptContentType);
                }
                if (contentType!=null)
                {
                    post.setHeader("Content-Type",contentType);
                }
                
                logHeaders(context,post.getAllHeaders());
                PostContext postContext=new PostContext(parent,traceCategory,this.client,scheduler,post,context);
                context=null;
                return postContext;
            }
            catch (Throwable t)
            {
                throw context.handleThrowable(t);
            }
        }
        finally
        {
            if (context!=null)
            {
                context.close();
            }
        }
    }
    
}
