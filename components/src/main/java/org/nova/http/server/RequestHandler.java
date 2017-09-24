package org.nova.http.server;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.nova.metrics.ValueRateMeter;
import org.nova.metrics.ValueRateSample;
import org.nova.metrics.RateMeter;

import java.util.SortedMap;
import java.util.TreeMap;

public class RequestHandler
{
	final private Object object;
	final private Method method;
	final private Filter[] filters;
	final private ParameterInfo[] parameterInfos;
	final private String path;
	final private Map<String,ContentReader<?>> contentReaders;
	final private Map<String,ContentWriter<?>> contentWriters;
	final private Map<String,ContentEncoder> contentEncoders;
	final private Map<String,ContentDecoder> contentDecoders;
	final private String key;
	final private String httpMethod;
	final private boolean public_;
    final private boolean log;
    final private boolean logRequestHeaders;
    final private boolean logRequestContent;
    final private boolean logResponseHeaders;
    final private boolean logResponseContent;
    final private boolean logLastRequestsInMemory;
	
	
	final private HashMap<Integer,ValueRateMeter> meters;
	final private ValueRateMeter requestUncompressedContentSizeMeter;
	final private ValueRateMeter responseUncompressedContentSizeMeter;
	final private ValueRateMeter requestCompressedContentSizeMeter;
	final private ValueRateMeter responseCompressedContentSizeMeter;
	
	RequestHandler(Object object,Method method,String httpMethod,String path,Filter[] filters,ParameterInfo[] parameterInfos,	Map<String,ContentDecoder> contentDecoders,Map<String,ContentEncoder> contentEncoders,Map<String,ContentReader<?>> contentReaders,Map<String,ContentWriter<?>> contentWriters,boolean log,boolean logRequestHeaders,boolean logRequestContent,boolean logResponseHeaders,boolean logResponseContent,boolean logLastRequestsInMemory,boolean public_)
	{
		this.object=object;
		this.method=method;
		this.filters=filters;
		this.parameterInfos=parameterInfos;
		this.path=path;
		this.contentReaders=contentReaders;
		this.contentWriters=contentWriters;
		this.contentEncoders=contentEncoders;
		this.contentDecoders=contentDecoders;
		this.httpMethod=httpMethod;
		this.key=httpMethod+" "+path;
		this.public_=public_;
		this.meters=new HashMap<>();
		this.requestUncompressedContentSizeMeter=new ValueRateMeter();
		this.responseUncompressedContentSizeMeter=new ValueRateMeter();
		this.requestCompressedContentSizeMeter=new ValueRateMeter();
		this.responseCompressedContentSizeMeter=new ValueRateMeter();
		
		this.log=log;
		this.logRequestHeaders=logRequestHeaders;
		this.logRequestContent=logRequestContent;
        this.logResponseHeaders=logResponseHeaders;
        this.logResponseContent=logResponseContent;
        this.logLastRequestsInMemory=logLastRequestsInMemory;
	}

	public Object getObject()
	{
		return object;
	}

	public Method getMethod()
	{
		return method;
	}
	
	public String getHttpMethod()
	{
		return this.httpMethod;
	}

	public Filter[] getFilters()
	{
		return filters;
	}

	public ParameterInfo[] getParameterInfos()
	{
		return parameterInfos;
	}

	public String getKey()
	{
		return key;
	}

	
	public String getPath()
	{
		return path;
	}

	public Map<String, ContentReader<?>> getContentReaders()
	{
		return contentReaders;
	}

	public Map<String, ContentWriter<?>> getContentWriters()
	{
		return contentWriters;
	}

	public Map<String, ContentEncoder> getContentEncoders()
	{
		return contentEncoders;
	}

	public Map<String, ContentDecoder> getContentDecoders()
	{
		return contentDecoders;
	}

	public boolean isPublic()
	{
		return this.public_;
	}
	
	public void update(Context context,int statusCode,long duration,long requestUncompressedContentSize,long responseUncompressedContentSize,long requestCompressedContentSize,long responseCompressedContentSize)
	{
		synchronized (this)
		{
			ValueRateMeter meter=this.meters.get(statusCode);
			if (meter==null)
			{
				meter=new ValueRateMeter();
				this.meters.put(statusCode, meter);
			}
			meter.update(duration);
		}
		this.requestUncompressedContentSizeMeter.update(requestUncompressedContentSize);
		this.responseUncompressedContentSizeMeter.update(responseUncompressedContentSize);
		this.requestCompressedContentSizeMeter.update(requestCompressedContentSize);
		this.responseCompressedContentSizeMeter.update(responseCompressedContentSize);
	}

	public Map<Integer,ValueRateSample> sampleStatusMeters()
	{
		synchronized (this)
		{
			TreeMap<Integer,ValueRateSample> results =new TreeMap<>();
			for (Entry<Integer, ValueRateMeter> entry:this.meters.entrySet())
			{
			    results.put(entry.getKey(), entry.getValue().sample());
			}
			return results;
		}		
	}

	public ValueRateMeter getRequestUncompressedContentSizeMeter()
	{
		return requestUncompressedContentSizeMeter;
	}

	public ValueRateMeter getResponseUncompressedContentSizeMeter()
	{
		return responseUncompressedContentSizeMeter;
	}

	public ValueRateMeter getRequestCompressedContentSizeMeter()
	{
		return requestCompressedContentSizeMeter;
	}

	public ValueRateMeter getResponseCompressedContentSizeMeter()
	{
		return responseCompressedContentSizeMeter;
	}

    public boolean isLog()
    {
        return log;
    }

    public boolean isLogRequestHeaders()
    {
        return logRequestHeaders;
    }

    public boolean isLogRequestContent()
    {
        return logRequestContent;
    }

    public boolean isLogResponseHeaders()
    {
        return logResponseHeaders;
    }

    public boolean isLogResponseContent()
    {
        return logResponseContent;
    }

    public boolean isLogLastRequestsInMemory()
    {
        return logLastRequestsInMemory;
    }
    
    
	
}
