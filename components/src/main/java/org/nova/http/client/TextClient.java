package org.nova.http.client;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.nova.core.Utils;
import org.nova.http.Header;
import org.nova.json.ObjectMapper;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class TextClient
{
	final private TraceManager trace;
	final private Logger logger;
	final private HttpClient client;
	final private String endPoint;
	final private Header[] headers;
	
	public TextClient(TraceManager trace,Logger logger,String endPoint,HttpClient client,Header...headers)
	{
		this.trace=trace;
		this.logger=logger;
		this.endPoint=endPoint;
		this.client=client;
		this.headers=headers;
	}

	static private HttpClient newDefaultClient()
	{
		RequestConfig config=RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
		HttpClients.custom().setDefaultRequestConfig(config);
		PoolingHttpClientConnectionManager connectionManager=new PoolingHttpClientConnectionManager();
		connectionManager.setDefaultMaxPerRoute(10);
		connectionManager.setMaxTotal(10);
		return HttpClients.custom().setConnectionManager(connectionManager).build();
	}
	
	public TextClient(TraceManager traceManager,Logger logger,String endPoint)
	{
		this(traceManager,logger,endPoint,newDefaultClient());
	}

	public TextResponse GET(Trace parent,String traceCategoryOverride,String pathAndQuery,Header...headers) throws Exception
	{
		try (Trace trace=new Trace(this.trace, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery))
		{
			HttpGet get=new HttpGet(this.endPoint+pathAndQuery);
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
			HttpResponse response=this.client.execute(get);
			try
			{
				int statusCode=response.getStatusLine().getStatusCode();
				org.apache.http.Header contentType=response.getEntity().getContentType();
				String text=Utils.readString(response.getEntity().getContent());
				org.apache.http.Header[] responseHeaders=response.getAllHeaders();
				Header[] textResponseHeaders=new Header[responseHeaders.length];
				for (int i=0;i<responseHeaders.length;i++)
				{
				    textResponseHeaders[i]=new Header(responseHeaders[i].getName(),responseHeaders[i].getValue());
				}
				return new TextResponse(statusCode,text,textResponseHeaders);
			}
			finally
			{
				response.getEntity().getContent().close();
			}
		}		
	}
    
	public TextResponse POST(Trace parent,String traceCategoryOverride,String pathAndQuery,String content,Header...headers) throws Exception
    {
        try (Trace trace=new Trace(this.trace, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery))
        {
            HttpPost post=new HttpPost(this.endPoint+pathAndQuery);
            if (content!=null)
            {
                StringEntity entity=new StringEntity(content);
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
            HttpResponse response=this.client.execute(post);
            try
            {
                int statusCode=response.getStatusLine().getStatusCode();
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

	public int PUT(Trace parent,String traceCategoryOverride,String pathAndQuery,String content,Header...headers) throws Exception
    {
        try (Trace trace=new Trace(this.trace, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery))
        {
            HttpPut put=new HttpPut(this.endPoint+pathAndQuery);
            if (content!=null)
            {
                StringEntity entity=new StringEntity(content);
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
            HttpResponse response=this.client.execute(put);
            try
            {
                return response.getStatusLine().getStatusCode();
            }
            finally
            {
                response.getEntity().getContent().close();
            }
        }       
    }

	public int PATCH(Trace parent,String traceCategoryOverride,String pathAndQuery,String content,Header...headers) throws Exception
    {
        try (Trace trace=new Trace(this.trace, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery))
        {
            HttpPatch patch=new HttpPatch(this.endPoint+pathAndQuery);
            if (content!=null)
            {
                StringEntity entity=new StringEntity(content);
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
            HttpResponse response=this.client.execute(patch);
            try
            {
                return response.getStatusLine().getStatusCode();
            }
            finally
            {
                response.getEntity().getContent().close();
            }
        }       
    }
    public int DELETE(Trace parent,String traceCategoryOverride,String pathAndQuery,Header...headers) throws Exception
    {
        try (Trace trace=new Trace(this.trace, parent, traceCategoryOverride!=null?traceCategoryOverride:pathAndQuery))
        {
            HttpDelete delete=new HttpDelete(this.endPoint+pathAndQuery);
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
            HttpResponse response=this.client.execute(delete);
            try
            {
                return response.getStatusLine().getStatusCode();
            }
            finally
            {
                response.getEntity().getContent().close();
            }
        }       
    }
            
			
}