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

//TODO: logging
public class JSONClient
{
	final private TraceManager traceManager;
	final private Logger logger;
	final private HttpClient client;
	final private String endPoint;
	final private ArrayList<Header> headers;
	final private String contentType;  
	final private String patchType;  
	final private Disruptor disruptor;
    final private TimerTask timerTask;
	
	public JSONClient(TraceManager traceManager,Logger logger,TimerScheduler scheduler,long idleConnectionTimeoutMs,Disruptor disruptor,String endPoint,HttpClient client,String contentType,String patchType,Header...headers) throws Throwable
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
		this.contentType=contentType;
		this.patchType=patchType;
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
	
	
    public JSONClient(TraceManager traceManager,Logger logger,TimerScheduler scheduler,long idleConnectionTimeoutMs,String endPoint,HttpClient client) throws Throwable
    {
        this(traceManager,logger,scheduler,idleConnectionTimeoutMs,null,endPoint,client,"application/json","application/merge-patch+json");
    }
    public JSONClient(TraceManager traceManager,Logger logger,String endPoint,HttpClient client) throws Throwable
    {
        this(traceManager,logger,null,0,endPoint,client);
    }
    public JSONClient(TraceManager traceManager,Logger logger,String endPoint) throws Throwable
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
	
	private void logHeaders(DisruptorTraceContext context,org.apache.http.Header[] headers)
	{
        for (org.apache.http.Header header:headers)
        {
            context.addLogItem(new Item("requestHeader:"+header.getName(),header.getValue()));
        }
	}
	private DisruptorTraceContext createDisruptorConext(Trace parent,String traceCategoryOverride,String pathAndQuery)
	{
	    return new DisruptorTraceContext(parent, this.traceManager, this.logger, this.disruptor, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery,this.endPoint);
	}
    public <TYPE> JSONResponse<TYPE> get(Trace parent,String traceCategoryOverride,String pathAndQuery,Class<TYPE> responseContentType) throws Throwable
    {
        return get(parent,traceCategoryOverride,pathAndQuery,responseContentType);
    }	
    private <TYPE> JSONResponse<TYPE> processResponse(HttpResponse response,DisruptorTraceContext context,Class<TYPE> responseContentType) throws Throwable
    {
       String json=processResponse(response, context);
       int statusCode=response.getStatusLine().getStatusCode();
       if (statusCode>=300)
       {
           return new JSONResponse<TYPE>(statusCode, null);
       }
       return new JSONResponse<TYPE>(statusCode,ObjectMapper.read(json, responseContentType));
    }

    public <TYPE> JSONResponse<TYPE> get(Trace parent,String traceCategoryOverride,String pathAndQuery,Class<TYPE> responseContentType,Header...headers) throws Throwable
    {
        try (DisruptorTraceContext context=createDisruptorConext(parent, traceCategoryOverride, pathAndQuery))
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
                get.setHeader("Accept",this.contentType);
                logHeaders(context,get.getAllHeaders());
    
                context.beginWait();
                HttpResponse response=this.client.execute(get);
                context.endWait();
                try
                {
                    return processResponse(response, context,responseContentType);
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
	
    public int get(Trace parent,String traceCategoryOverride,String pathAndQuery) throws Throwable
    {
        return get(parent,traceCategoryOverride,pathAndQuery,new Header[0]);
    }
    
    public int get(Trace parent,String traceCategoryOverride,String pathAndQuery,Header...headers) throws Throwable
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
                get.setHeader("Accept",this.contentType);
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
    public int delete(Trace parent,String traceCategoryOverride,String pathAndQuery) throws Throwable
    {
        return delete(parent,traceCategoryOverride,pathAndQuery,new Header[0]);
    }
	public int delete(Trace parent,String traceCategoryOverride,String pathAndQuery,Header...headers) throws Throwable
	{
        try (DisruptorTraceContext context=createDisruptorConext(parent, traceCategoryOverride, pathAndQuery))
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
    public int put(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content) throws Throwable
    {
        return put(parent,traceCategoryOverride,pathAndQuery,content,new Header[0]);
    }
	public int put(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content,Header...headers) throws Throwable
	{
        try (DisruptorTraceContext context=createDisruptorConext(parent, traceCategoryOverride, pathAndQuery))
        {
            try 
    		{
    			HttpPut put=new HttpPut(this.endPoint+pathAndQuery);
                context.addLogItem(new Item("endPoint",this.endPoint));
                context.addLogItem(new Item("pathAndQuery",pathAndQuery));
    
    			if (content!=null)
    			{
                    String jsonContent=ObjectMapper.write(content);
                    context.addLogItem(new Item("request",jsonContent));
                    StringEntity entity=new StringEntity(jsonContent);
    				put.setEntity(entity);
    			}
                if (this.headers!=null)
                {
                    for (Header header:this.headers)
                    {
                        put.setHeader(header.getName(),header.getValue());
                    }
                }
                for (Header header:headers)
                {
                    put.setHeader(header.getName(),header.getValue());
                }
                put.setHeader("Accept",this.contentType);
                put.setHeader("Content-Type",this.contentType);
                logHeaders(context,put.getAllHeaders());
    
                context.beginWait();
                HttpResponse response=this.client.execute(put);
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
    
	public int patch(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content) throws Throwable
    {
	    return patch(parent,traceCategoryOverride,pathAndQuery,content,new Header[0]);
    }
	
	public int patch(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content,Header...headers) throws Throwable
	{
        try (DisruptorTraceContext context=createDisruptorConext(parent, traceCategoryOverride, pathAndQuery))
        {
            try 
            {
                HttpPatch patch=new HttpPatch(this.endPoint+pathAndQuery);
                context.addLogItem(new Item("endPoint",this.endPoint));
                context.addLogItem(new Item("pathAndQuery",pathAndQuery));
    
                if (content!=null)
                {
                    String jsonContent=ObjectMapper.write(content);
                    context.addLogItem(new Item("request",jsonContent));
                    StringEntity entity=new StringEntity(jsonContent);
                    patch.setEntity(entity);
                }
                if (this.headers!=null)
                {
                    for (Header header:this.headers)
                    {
                        patch.setHeader(header.getName(),header.getValue());
                    }
                }
                for (Header header:headers)
                {
                    patch.setHeader(header.getName(),header.getValue());
                }
                patch.setHeader("Accept",this.contentType);
                patch.setHeader("Content-Type",this.patchType);
                logHeaders(context,patch.getAllHeaders());
                
                context.beginWait();
                HttpResponse response=this.client.execute(patch);
                context.endWait();
                try
                {
                    processResponse(response, context);
                    int statusCode=response.getStatusLine().getStatusCode();
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
	public <TYPE> JSONResponse<TYPE> post(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content,Class<TYPE> responseContentType,Header...headers) throws Throwable
	{
        try (DisruptorTraceContext context=createDisruptorConext(parent, traceCategoryOverride, pathAndQuery))
        {
    		try 
    		{
    			HttpPost post=new HttpPost(this.endPoint+pathAndQuery);
                context.addLogItem(new Item("endPoint",this.endPoint));
                context.addLogItem(new Item("pathAndQuery",pathAndQuery));
    			if (content!=null)
    			{
    			    String jsonContent=ObjectMapper.write(content);
                    context.addLogItem(new Item("request",jsonContent));
    				StringEntity entity=new StringEntity(jsonContent);
    				post.setEntity(entity);
    			}
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
    			post.setHeader("Accept",this.contentType);
    			post.setHeader("Content-Type",this.contentType);
                logHeaders(context,post.getAllHeaders());
                context.beginWait();
    			HttpResponse response=this.client.execute(post);
    			context.endWait();
    			try
    			{
                    return processResponse(response,context,responseContentType);
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
    public <TYPE> JSONResponse<TYPE> post(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content,Class<TYPE> responseContentType) throws Throwable
    {
        return post(parent,traceCategoryOverride,pathAndQuery,content,responseContentType,new Header[0]);
    }
    public <TYPE> JSONResponse<TYPE> post(Trace parent,String traceCategoryOverride,String pathAndQuery,Class<TYPE> responseContentType) throws Throwable
    {
        return post(parent,traceCategoryOverride,pathAndQuery,null,responseContentType);
    }
    public int post(Trace parent,String traceCategoryOverride,String pathAndQuery) throws Throwable
    {
        return post(parent,traceCategoryOverride,pathAndQuery,null,new Header[0]);
    }
    public int post(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content) throws Throwable
    {
        return post(parent,traceCategoryOverride,pathAndQuery,content,new Header[0]);
    }
    public int post(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content,Header...headers) throws Throwable
    {
        try (DisruptorTraceContext context=createDisruptorConext(parent, traceCategoryOverride, pathAndQuery))
        {
            try 
            {
                HttpPost post=new HttpPost(this.endPoint+pathAndQuery);
                context.addLogItem(new Item("endPoint",this.endPoint));
                context.addLogItem(new Item("pathAndQuery",pathAndQuery));
                if (content!=null)
                {
                    String jsonContent=ObjectMapper.write(content);
                    context.addLogItem(new Item("request",jsonContent));
                    StringEntity entity=new StringEntity(jsonContent);
                    post.setEntity(entity);
                }
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
                post.setHeader("Accept",this.contentType);
                post.setHeader("Content-Type",this.contentType);
                logHeaders(context,post.getAllHeaders());
                context.beginWait();
                HttpResponse response=this.client.execute(post);
                context.endWait();
                try
                {
                    processResponse(response,context);
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
    
	private String processResponse(HttpResponse response,DisruptorTraceContext context) throws Throwable
	{
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
	
	/*
    public TextResponse get(Trace parent,String traceCategoryOverride,String pathAndQuery) throws Exception
    {
        try (Trace trace=new Trace(this.traceManager, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery))
        {
            HttpGet get=new HttpGet(this.endPoint+pathAndQuery);
            get.setHeader("Accept",this.contentType);
            if (this.headers!=null)
            {
                for (Header header:this.headers)
                {
                    get.setHeader(header.getName(),header.getValue());
                }
            }
            HttpResponse response=this.client.execute(get);
            try
            {
                int statusCode=response.getStatusLine().getStatusCode();
                if (statusCode>=300)
                {
                    return new TextResponse(statusCode, null,null);
                }
                org.apache.http.Header[] responseHeaders=response.getAllHeaders();
                Header[] textResponseHeaders=new Header[responseHeaders.length];
                for (int i=0;i<responseHeaders.length;i++)
                {
                    textResponseHeaders[i]=new Header(responseHeaders[i].getName(),responseHeaders[i].getValue());
                }
                return new TextResponse(statusCode,Utils.readString(response.getEntity().getContent()),textResponseHeaders);
            }
            finally
            {
                response.getEntity().getContent().close();
            }
        }       
    }

	public TextResponse post(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content) throws Exception
	{
		try (Trace trace=new Trace(this.traceManager, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery))
		{
			HttpPost post=new HttpPost(this.endPoint+pathAndQuery);
			if (content!=null)
			{
				StringEntity entity=new StringEntity(ObjectMapper.write(content));
				post.setEntity(entity);
			}
			if (this.headers!=null)
			{
				for (Header header:this.headers)
				{
					post.setHeader(header.getName(),header.getValue());
				}
			}
			post.setHeader("Accept",this.contentType);
			post.setHeader("Content-Type",this.contentType);
			HttpResponse response=this.client.execute(post);
			try
			{
				int statusCode=response.getStatusLine().getStatusCode();
				if (statusCode>=300)
				{
					return new TextResponse(statusCode, null,null);
				}
                org.apache.http.Header[] responseHeaders=response.getAllHeaders();
                Header[] textResponseHeaders=new Header[responseHeaders.length];
                for (int i=0;i<responseHeaders.length;i++)
                {
                    textResponseHeaders[i]=new Header(responseHeaders[i].getName(),responseHeaders[i].getValue());
                }
                return new TextResponse(statusCode,Utils.readString(response.getEntity().getContent()),textResponseHeaders);
			}
			finally
			{
				response.getEntity().getContent().close();
			}
		}		
	}
	*/
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

	public int get(Trace parent,String traceCategoryOverride,OutputStream outputStream,String pathAndQuery,Header...headers) throws Throwable
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
                get.setHeader("Accept",this.contentType);
                logHeaders(context,get.getAllHeaders());
    
                context.beginWait();
                HttpResponse response=this.client.execute(get);
                context.endWait();
                try
                {
                    IOUtils.copy(response.getEntity().getContent(), outputStream);
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
}
