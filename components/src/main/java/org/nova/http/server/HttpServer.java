package org.nova.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.nova.annotations.Description;
import org.nova.collections.RingBuffer;
import org.nova.http.Cookie;
import org.nova.http.Header;
import org.nova.metrics.RateMeter;
import org.nova.operations.OperatorVariable;
import org.nova.test.Testing;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;
import org.omg.CORBA.ServerRequest;

import com.google.common.base.Strings;

public class HttpServer 
{
	final private RequestHandlerMap requestHandlerMap;
	final private TraceManager traceManager;
	final private IdentityContentDecoder identityContentDecoder;
	final private IdentityContentEncoder identityContentEncoder;
	@Description("Overall request rate.")
	final private RateMeter requestRateMeter;
	final private String categoryPrefix;
	final private Server[] servers;
	final private RingBuffer<RequestLogEntry> lastRequestsLogEntries;
	final private RingBuffer<RequestLogEntry> lastExceptionRequestsLogEntries;
	final private RingBuffer<RequestHandlerNotFoundLogEntry> lastRequestHandlerNotFoundLogEntries;
	private Transformers transformers;
	
	@OperatorVariable()
	private boolean debug;
	private int preferredPort;
	private AbstractHandler handler;
	
	public HttpServer(String categoryPrefix,TraceManager traceManager, int lastRequestLogEntryBufferSize,int lastExceptionRequestLogEntryBufferSize,int lastNotFoundBufferSize,int preferredPort,Server[] servers) throws Exception
	{
		this.categoryPrefix=categoryPrefix+"@";
		this.requestHandlerMap = new RequestHandlerMap();
		this.traceManager = traceManager;
		this.servers = servers;
		this.requestRateMeter = new RateMeter();
		this.identityContentDecoder = new IdentityContentDecoder();
		this.identityContentEncoder = new IdentityContentEncoder();
		this.debug=true;
		this.lastRequestsLogEntries=new RingBuffer<>(new RequestLogEntry[lastRequestLogEntryBufferSize]);
		this.lastExceptionRequestsLogEntries=new RingBuffer<>(new RequestLogEntry[lastExceptionRequestLogEntryBufferSize]);
		this.lastRequestHandlerNotFoundLogEntries=new RingBuffer<>(new RequestHandlerNotFoundLogEntry[lastNotFoundBufferSize]);
		this.transformers=new Transformers();
        this.preferredPort=preferredPort;
	}
	public HttpServer(String categoryPrefix,TraceManager traceManager, Server server) throws Exception
	{
		this(categoryPrefix,traceManager,100,100,100,((ServerConnector)server.getConnectors()[0]).getPort(),new Server[]{server});
	}

	public HttpServer(TraceManager traceManager, Server server) throws Exception
	{
		this("HttpServer",traceManager, server);
	}

	public void start() throws Exception
	{
		this.handler=new AbstractHandler()
		{
			@Override
			public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
			{
				handleRequest(target, baseRequest, request, response);
			}
		};
		for (Server server:this.servers)
		{
			server.setHandler(this.handler);
			server.start();
		}
	}
	
	public void setTransformers(Transformers transformers)
	{
	    this.transformers=transformers;
	}

	public void addContentReaders(ContentReader<?>...contentReaders)
    {
	    this.transformers.add(contentReaders);
    }
    public void addContentWriters(ContentWriter<?>...contentWriters)
    {
        this.transformers.add(contentWriters);
    }
    public void addContentEncoders(ContentEncoder...contentEncoders)
    {
        this.transformers.add(contentEncoders);
    }
    public void addContentDecoders(ContentDecoder...contentDecoders)
    {
        this.transformers.add(contentDecoders);
    }
    public void addFilters(Filter...filters)
    {
        this.transformers.add(filters);
    }

    public int getPreferredPort()
    {
        return this.preferredPort;
    }
	public Transformers getTransformers()
	{
	    return this.transformers;
	}
	
	public void register(Object object) throws Exception
	{
		register(null, object);
	}

	public void register(String root, Object object) throws Exception
	{
		this.requestHandlerMap.register(root, object, this.transformers);
	}

	public void register(String root, Object object, Method method) throws Exception
	{
		this.requestHandlerMap.register(root, object, method, this.transformers);
	}

	public RequestHandler[] getRequestHandlers()
	{
		return this.requestHandlerMap.getRequestHandlers();
	}
	public RequestHandler getRequestHandler(String key)
	{
		return this.requestHandlerMap.getRequestHandler(key);
	}
	public void handleRequest(String string, Request request, HttpServletRequest servletRequest, HttpServletResponse servletResponse)
			throws IOException, ServletException
	{
		try
		{
			this.requestRateMeter.increment();
			decode(servletRequest, servletResponse);
		}
		catch (Throwable t)
		{
			t.printStackTrace();
		}
		finally
		{
			request.setHandled(true);
		}
	}

	private ContentReader<?> findContentReader(String contentType, RequestHandler handler)
	{
		if ((contentType == null) || (contentType.length() == 0))
		{
			return null;
		}
		contentType = org.nova.core.Utils.split(contentType, ';')[0];

		ContentReader<?> reader = handler.getContentReaders().get(contentType);
		if (reader != null)
		{
			return reader;
		}
		reader = handler.getContentReaders().get(WsUtils.toAnySubMediaType(contentType));
		if (reader != null)
		{
			return reader;
		}
		return handler.getContentReaders().get("*/*");
	}

	private ContentWriter<?> findContentWriter(String accept, RequestHandler handler)
	{
		Map<String, ContentWriter<?>> map = handler.getContentWriters();
		if (accept != null)
		{
			List<ValueQ> list = ValueQ.sort(accept);
			for (ValueQ item : list)
			{
				ContentWriter<?> writer = map.get(item.value);
				if (writer != null)
				{
					return writer;
				}
			}
			for (ValueQ item : list)
			{
				String anySubMediaType=WsUtils.toAnySubMediaType(item.value);
				ContentWriter<?> writer = map.get(anySubMediaType);
				if (writer != null)
				{
					return writer;
				}
			}
			return null;
		}
		else
		{
			return map.get("*/*"); 
		}
	}

	private ContentDecoder getContentDecoder(String contentEncoding, RequestHandler handler) throws AbnormalException
	{
		if (contentEncoding == null)
		{
			return this.identityContentDecoder;
		}
		ContentDecoder decoder = handler.getContentDecoders().get(contentEncoding);
		if (decoder != null)
		{
			return decoder;
		}
		throw new AbnormalException(Abnormal.NO_DECODER);
	}

	private ContentEncoder getContentEncoder(String acceptEncoding, RequestHandler handler) throws AbnormalException
	{
		if (acceptEncoding == null)
		{
			return this.identityContentEncoder;
		}
		List<ValueQ> list = ValueQ.sort(acceptEncoding);
		for (ValueQ item : list)
		{
			ContentEncoder encoder = handler.getContentEncoders().get(item.value);
			if (encoder != null)
			{
				return encoder;
			}
		}
		return this.identityContentEncoder;
	}

	private void decode(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws Throwable
	{
		try (Trace trace = new Trace(this.traceManager, "HttpServer.handle"))
		{
			String URI = servletRequest.getRequestURI();
			String method = servletRequest.getMethod();
			RequestHandlerWithParameters requestHandlerWithParameters = this.requestHandlerMap.resolve(method, URI);
			if (requestHandlerWithParameters == null)
			{
				if (Testing.ENABLED)
				{
					System.out.println(method+" "+URI+": No Handler");
					servletResponse.getWriter().println("404 - NOT FOUND: "+URI);
				}	
				servletResponse.setStatus(HttpStatus.NOT_FOUND_404);
				synchronized (this.lastRequestHandlerNotFoundLogEntries)
				{
					this.lastRequestHandlerNotFoundLogEntries.add(new RequestHandlerNotFoundLogEntry(trace,servletRequest));
				}
				return;
			}
			if (requestHandlerWithParameters.requestHandler.isPublic()==true)
			{
				//TODO: prevent access
			}
			handle(trace, servletRequest, servletResponse, requestHandlerWithParameters);
		}
	}

	private void handle(Trace parent, HttpServletRequest servletRequest, HttpServletResponse servletResponse, RequestHandlerWithParameters requestHandlerWithParameters) throws Throwable
	{
		long responseUncompressedContentSize=0;
		long requestUncompressedContentSize=0;
		long responseCompressedContentSize=0;
		long requestCompressedContentSize=0;
		Context context = new Context(requestHandlerWithParameters.requestHandler, servletRequest, servletResponse);
		RequestHandler handler = requestHandlerWithParameters.requestHandler;
		Trace trace = new Trace(traceManager,parent,this.categoryPrefix+ handler.getKey());
		try
		{
		    
		    //??? how does the following need to work???
			DecoderContext decoderContext = null;
			if ("application/x-www-form-urlencoded".equals(servletRequest.getParameter("Content-Type")))
			{
				decoderContext = getContentDecoder(servletRequest.getHeader("Content-Encoding"), handler).open(servletRequest, servletResponse);
			}
			else
			{
			    ContentDecoder contentDecoder = getContentDecoder(servletRequest.getHeader("Content-Encoding"), handler);
			    decoderContext = contentDecoder.open(servletRequest, servletResponse);
			}
			
//			Object attribute=servletRequest.getAttribute("javax.servlet.request.X509Certificate");
			
			try 
			{
				FilterChain chain = new FilterChain(requestHandlerWithParameters, decoderContext);
				context.setContentReader(findContentReader(servletRequest.getContentType(), handler));
				context.setContentWriter(findContentWriter(servletRequest.getHeader("Accept"), handler));

				Response<?> response = chain.next(trace, context);
				if (context.isHandled()==false)
				{
					if (response != null)
					{
						if (response.headers != null)
						{
							for (Header header : response.headers)
							{
								servletResponse.setHeader(header.getName(), header.getValue());
							}
						}
						if (response.cookies!=null)
						{
						    for (Cookie cookie:response.cookies)
						    {
						        servletResponse.addCookie(new javax.servlet.http.Cookie(cookie.getName(),cookie.getValue()));
						    }
						}
						servletResponse.setStatus(response.getStatusCode());
						ContentWriter<?> writer = context.getContentWriter();
						if (writer != null)
						{
                            if (servletResponse.getContentType()==null)
                            {
                                servletResponse.setContentType(writer.getMediaType());
                            }
							ContentEncoder contentEncoder = getContentEncoder(servletRequest.getHeader("Accept-Encoding"), handler);
							if (Strings.isNullOrEmpty(contentEncoder.getCoding())==false)
							{
							    servletResponse.setHeader("Content-Encoding", contentEncoder.getCoding());
							}
							EncoderContext encoderContext = contentEncoder.open(servletRequest, servletResponse);
							try 
							{
								Method write = writer.getClass().getMethod("write", Context.class, OutputStream.class, Object.class);
								Object content = response.getContent();
								write.invoke(writer, context, encoderContext.getOutputStream(), content);
	    
							}
							finally
							{
								encoderContext.close();
								responseUncompressedContentSize=encoderContext.getUncompressedContentSize();
								responseCompressedContentSize=encoderContext.getCompressedContentSize();
							}
							
						}
						else if (response.getContent() != null)
						{
							if (Testing.ENABLED)
							{
								System.out.println("NO_WRITER: Accept="+servletRequest.getHeader("Accept"));
							}
							throw new AbnormalException(Abnormal.NO_WRITER);
						}
					}
				}
			}
			finally
			{
				if (decoderContext!=null)
				{
					requestUncompressedContentSize=decoderContext.getUncompressedContentSize();
					requestCompressedContentSize=decoderContext.getCompressedContentSize();
					decoderContext.close();
				}
			}
		}
		catch (Throwable e)
		{
			trace.close(e);
			servletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR_500);
			if (this.debug)
			{
				e.printStackTrace();
			}
		}
		finally
		{
			trace.close();
			handler.update(context,servletResponse.getStatus(), trace.getDurationNs(),requestUncompressedContentSize,responseUncompressedContentSize,requestCompressedContentSize,responseCompressedContentSize);
			RequestLogEntry entry=new RequestLogEntry(trace,context,handler,servletRequest,servletResponse);
			if (trace.getThrowable()==null)
			{
				synchronized (this.lastRequestsLogEntries)
				{
					this.lastRequestsLogEntries.add(entry);
				}
			}
			else
			{
				synchronized (this.lastExceptionRequestsLogEntries)
				{
					this.lastExceptionRequestsLogEntries.add(entry);
				}
			}
		}
	}

	public RequestLogEntry[] getLastRequestLogEntries()
	{
		RequestLogEntry[] entries=new RequestLogEntry[this.lastRequestsLogEntries.size()];
		synchronized (this.lastRequestsLogEntries)
		{
			
			this.lastRequestsLogEntries.getSnapshot(entries);
		}
		return entries;
	}
	
	public RequestHandlerNotFoundLogEntry[] getRequestHandlerNotFoundLogEntries()
	{
		RequestHandlerNotFoundLogEntry[] entries=new RequestHandlerNotFoundLogEntry[this.lastRequestHandlerNotFoundLogEntries.size()];
		synchronized (this.lastRequestsLogEntries)
		{
			
			this.lastRequestHandlerNotFoundLogEntries.getSnapshot(entries);
		}
		return entries;
	}
	
	public RequestLogEntry[] getLastExceptionRequestLogEntries()
	{
		RequestLogEntry[] entries=new RequestLogEntry[this.lastExceptionRequestsLogEntries.size()];
		synchronized (this.lastRequestsLogEntries)
		{
			
			this.lastExceptionRequestsLogEntries.getSnapshot(entries);
		}
		return entries;
	}
	public RateMeter getRequestRateMeter()
	{
		return this.requestRateMeter;
	}
	
}
