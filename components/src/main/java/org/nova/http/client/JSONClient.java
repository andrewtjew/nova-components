package org.nova.http.client;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.nova.core.Utils;
import org.nova.http.Header;
import org.nova.json.ObjectMapper;
import org.nova.logging.Item;
import org.nova.logging.Level;
import org.nova.logging.LogEntry;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

//TODO: logging
public class JSONClient
{
	final private TraceManager traceManager;
	final private Logger logger;
	final private HttpClient client;
	final private String endPoint;
	final private Header[] headers;
	final private String contentType;  
	final private String patchType;  
	
	public JSONClient(TraceManager traceManager,Logger logger,String endPoint,HttpClient client,String contentType,String patchType,Header...headers)
	{
		this.traceManager=traceManager;
		this.logger=logger;
		this.endPoint=endPoint;
		this.client=client;
		this.headers=headers;
		this.contentType=contentType;
		this.patchType=patchType;
	}
    public JSONClient(TraceManager traceManager,Logger logger,String endPoint,HttpClient client)
    {
        this(traceManager,logger,endPoint,client,"application/json","application/merge-patch+json");
    }

	public JSONClient(TraceManager traceManager,Logger logger,String endPoint)
	{
		this(traceManager,logger,endPoint,HttpClientFactory.createDefaultClient(),"application/json","application/merge-patch+json");
	}
	
	public <TYPE> JSONResponse<TYPE> get(Trace parent,String traceCategoryOverride,String pathAndQuery,Class<TYPE> responseContentType) throws Throwable
	{
        ArrayList<Item> items=new ArrayList<>();
        Trace trace=new Trace(this.traceManager, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery);
        try 
        {
			HttpGet get=new HttpGet(this.endPoint+pathAndQuery);
            items.add(new Item("endPoint",this.endPoint));
            items.add(new Item("pathAndQuery",pathAndQuery));

			if (this.headers!=null)
			{
				for (Header header:this.headers)
				{
					get.setHeader(header.getName(),header.getValue());
				}
			}
            get.setHeader("Accept",this.contentType);
            for (org.apache.http.Header header:get.getAllHeaders())
            {
                items.add(new Item("requestHeader:"+header.getName(),header.getValue()));
            }

            trace.beginWait();
            HttpResponse response=this.client.execute(get);
            trace.endWait();
			try
			{
                String json=processResponse(response, items);
				int statusCode=response.getStatusLine().getStatusCode();
				if (statusCode>=300)
				{
					return new JSONResponse<TYPE>(statusCode, null);
				}
				return new JSONResponse<TYPE>(statusCode,ObjectMapper.read(json, responseContentType));
			}
			finally
			{
				response.getEntity().getContent().close();
			}
		}		
        catch (Throwable t)
        {
            trace.close(t);
            throw t;
        }
        finally
        {
            trace.close();
            this.logger.log(trace,Level.NORMAL,"JSONClient:get",Logger.toArray(items));
        }
	}
    public <TYPE> JSONResponse<TYPE> get(Trace parent,String traceCategoryOverride,String pathAndQuery) throws Throwable
    {
        ArrayList<Item> items=new ArrayList<>();
        Trace trace=new Trace(this.traceManager, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery);
        try 
        {
            HttpGet get=new HttpGet(this.endPoint+pathAndQuery);
            items.add(new Item("endPoint",this.endPoint));
            items.add(new Item("pathAndQuery",pathAndQuery));

            if (this.headers!=null)
            {
                for (Header header:this.headers)
                {
                    get.setHeader(header.getName(),header.getValue());
                }
            }
            get.setHeader("Accept",this.contentType);
            for (org.apache.http.Header header:get.getAllHeaders())
            {
                items.add(new Item("requestHeader:"+header.getName(),header.getValue()));
            }

            trace.beginWait();
            HttpResponse response=this.client.execute(get);
            trace.endWait();
            try
            {
                processResponse(response, items);
                int statusCode=response.getStatusLine().getStatusCode();
                return new JSONResponse<TYPE>(statusCode, null);
            }
            finally
            {
                response.getEntity().getContent().close();
            }
        }       
        catch (Throwable t)
        {
            trace.close(t);
            throw t;
        }
        finally
        {
            trace.close();
            this.logger.log(trace,Level.NORMAL,"JSONClient:get",Logger.toArray(items));
        }
    }

	public int delete(Trace parent,String traceCategoryOverride,String pathAndQuery) throws Throwable
	{
        ArrayList<Item> items=new ArrayList<>();
        Trace trace=new Trace(this.traceManager, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery);
        try 
        {
			HttpDelete delete=new HttpDelete(this.endPoint+pathAndQuery);
            items.add(new Item("endPoint",this.endPoint));
            items.add(new Item("pathAndQuery",pathAndQuery));

            if (this.headers!=null)
			{
				for (Header header:this.headers)
				{
					delete.setHeader(header.getName(),header.getValue());
				}
			}
            for (org.apache.http.Header header:delete.getAllHeaders())
            {
                items.add(new Item("requestHeader:"+header.getName(),header.getValue()));
            }

            trace.beginWait();
            HttpResponse response=this.client.execute(delete);
            trace.endWait();
			try
			{
			    processResponse(response, items);
				return response.getStatusLine().getStatusCode();
			}
			finally
			{
				response.getEntity().getContent().close();
			}
		}		
        catch (Throwable t)
        {
            trace.close(t);
            throw t;
        }
        finally
        {
            trace.close();
            this.logger.log(trace,Level.NORMAL,"JSONClient:delete",Logger.toArray(items));
        }
	}

	public int put(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content) throws Throwable
	{
        ArrayList<Item> items=new ArrayList<>();
        Trace trace=new Trace(this.traceManager, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery);
        try 
		{
			HttpPut put=new HttpPut(this.endPoint+pathAndQuery);
            items.add(new Item("endPoint",this.endPoint));
            items.add(new Item("pathAndQuery",pathAndQuery));

			if (content!=null)
			{
                String jsonContent=ObjectMapper.write(content);
                items.add(new Item("request",jsonContent));
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
            put.setHeader("Accept",this.contentType);
            put.setHeader("Content-Type",this.contentType);
            for (org.apache.http.Header header:put.getAllHeaders())
            {
                items.add(new Item("requestHeader:"+header.getName(),header.getValue()));
            }

            trace.beginWait();
            HttpResponse response=this.client.execute(put);
            trace.endWait();
			try
			{
                processResponse(response, items);
                return response.getStatusLine().getStatusCode();
			}
			finally
			{
				response.getEntity().getContent().close();
			}
		}		
        catch (Throwable t)
        {
            trace.close(t);
            throw t;
        }
        finally
        {
            trace.close();
            this.logger.log(trace,Level.NORMAL,"JSONClient:put",Logger.toArray(items));
        }
	}

	public int patch(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content) throws Throwable
	{
        ArrayList<Item> items=new ArrayList<>();
        Trace trace=new Trace(this.traceManager, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery);
        try 
        {
            HttpPatch patch=new HttpPatch(this.endPoint+pathAndQuery);
            items.add(new Item("endPoint",this.endPoint));
            items.add(new Item("pathAndQuery",pathAndQuery));

            if (content!=null)
            {
                String jsonContent=ObjectMapper.write(content);
                items.add(new Item("request",jsonContent));
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
            patch.setHeader("Accept",this.contentType);
            patch.setHeader("Content-Type",this.patchType);
            for (org.apache.http.Header header:patch.getAllHeaders())
            {
                items.add(new Item("requestHeader:"+header.getName(),header.getValue()));
            }
            
            trace.beginWait();
            HttpResponse response=this.client.execute(patch);
            trace.endWait();
            try
            {
                processResponse(response, items);
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
            trace.close(t);
            throw t;
        }
        finally
        {
            trace.close();
            this.logger.log(trace,Level.NORMAL,"JSONClient:patch",Logger.toArray(items));
        }
	}

	public <TYPE> JSONResponse<TYPE> post(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content,Class<TYPE> responseContentType) throws Throwable
	{
	    ArrayList<Item> items=new ArrayList<>();
	    Trace trace=new Trace(this.traceManager, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery);
		try 
		{
			HttpPost post=new HttpPost(this.endPoint+pathAndQuery);
            items.add(new Item("endPoint",this.endPoint));
            items.add(new Item("pathAndQuery",pathAndQuery));
			if (content!=null)
			{
			    String jsonContent=ObjectMapper.write(content);
                items.add(new Item("request",jsonContent));
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
			post.setHeader("Accept",this.contentType);
			post.setHeader("Content-Type",this.contentType);
            for (org.apache.http.Header header:post.getAllHeaders())
            {
                items.add(new Item("requestHeader:"+header.getName(),header.getValue()));
            }
            trace.beginWait();
			HttpResponse response=this.client.execute(post);
			trace.endWait();
			try
			{
                String json=processResponse(response,items);
                int statusCode=response.getStatusLine().getStatusCode();
				if (statusCode>=300)
				{
					return new JSONResponse<TYPE>(statusCode, null);
				}
				return new JSONResponse<TYPE>(statusCode,ObjectMapper.read(json, responseContentType));
			}
			finally
			{
	            response.getEntity().getContent().close();
			}
	    }
		catch (Throwable t)
		{
            trace.close(t);
            throw t;
		}
        finally
        {
            trace.close();
            this.logger.log(trace,Level.NORMAL,"JSONClient:post",Logger.toArray(items));
        }
	}
    public <TYPE> JSONResponse<TYPE> post(Trace parent,String traceCategoryOverride,String pathAndQuery,Class<TYPE> responseContentType) throws Throwable
    {
        return post(parent,traceCategoryOverride,pathAndQuery,null,responseContentType);
    }
    public <TYPE> JSONResponse<TYPE> post(Trace parent,String traceCategoryOverride,String pathAndQuery) throws Throwable
    {
        return post(parent,traceCategoryOverride,pathAndQuery,null);
    }	
    public <TYPE> JSONResponse<TYPE> post(Trace parent,String traceCategoryOverride,String pathAndQuery,Object content) throws Throwable
    {
        ArrayList<Item> items=new ArrayList<>();
        Trace trace=new Trace(this.traceManager, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery);
        try 
        {
            HttpPost post=new HttpPost(this.endPoint+pathAndQuery);
            items.add(new Item("endPoint",this.endPoint));
            items.add(new Item("pathAndQuery",pathAndQuery));
            if (content!=null)
            {
                String jsonContent=ObjectMapper.write(content);
                items.add(new Item("request",jsonContent));
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
            post.setHeader("Accept",this.contentType);
            post.setHeader("Content-Type",this.contentType);
            for (org.apache.http.Header header:post.getAllHeaders())
            {
                items.add(new Item("requestHeader:"+header.getName(),header.getValue()));
            }
            trace.beginWait();
            HttpResponse response=this.client.execute(post);
            trace.endWait();
            try
            {
                processResponse(response,items);
                int statusCode=response.getStatusLine().getStatusCode();
                return new JSONResponse<TYPE>(statusCode,null);
            }
            finally
            {
                response.getEntity().getContent().close();
            }
        }
        catch (Throwable t)
        {
            trace.close(t);
            throw t;
        }
        finally
        {
            trace.close();
            this.logger.log(trace,Level.NORMAL,"JSONClient:post",Logger.toArray(items));
        }
    }
    
	private String processResponse(HttpResponse response,ArrayList<Item> items) throws Throwable
	{
        for (org.apache.http.Header header:response.getAllHeaders())
        {
            items.add(new Item("responseHeader:"+header.getName(),header.getValue()));
        }
        String responseContent=Utils.readString(response.getEntity().getContent());
        if (responseContent.length()>0)
        {
            items.add(new Item("response",responseContent));
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
}
