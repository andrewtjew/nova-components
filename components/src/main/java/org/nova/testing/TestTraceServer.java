package org.nova.testing;

import java.util.LinkedList;

import org.nova.core.Utils;
import org.nova.html.HtmlWriter;
import org.nova.http.server.JettyServerFactory;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.HtmlContentWriter;
import org.nova.http.server.HttpServer;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.Transformers;
import org.nova.http.server.annotations.ContentParam;
import org.nova.http.server.annotations.DefaultValue;
import org.nova.http.server.annotations.GET;
import org.nova.http.server.annotations.PUT;
import org.nova.http.server.annotations.Path;
import org.nova.http.server.annotations.QueryParam;
import org.nova.logging.Logger;
import org.nova.logging.LogUtils;
import org.nova.test.PrintMessage;
import org.nova.test.Testing;
import org.nova.tracing.TraceManager;

public class TestTraceServer
{
	final private HttpServer server;
	final private LinkedList<ReceivedDebugMessage> messages;
	final private int maximumMessages;
	
	public TestTraceServer(int maximumMessages,int threads,int port) throws Throwable
	{
		this.maximumMessages=maximumMessages;
		this.messages=new LinkedList<>();
		this.server=new HttpServer(new TraceManager(), LogUtils.createConsoleLogger(),false,JettyServerFactory.createServer(threads,port));

		Transformers transformers=new Transformers();
        transformers.add(new GzipContentDecoder());
        transformers.add(new JSONContentReader());
        transformers.add(new JSONContentWriter());
        transformers.add(new HtmlContentWriter());
		this.server.setTransformers(transformers);
		this.server.registerHandlers(this);
		this.server.start();
		System.out.println("TestTraceServer started: port="+port);
	}

	public TestTraceServer() throws Throwable
	{
		this(10000,10,9111);
	}

	
	static class ReceivedDebugMessage
	{
		final private PrintMessage message;
		final long received;
		public ReceivedDebugMessage(PrintMessage debugMessage)
		{
			this.message=debugMessage;
			this.received=System.currentTimeMillis();
		}
	}

	@GET
	@Path("/print/test")
	public HtmlWriter test() throws Exception
	{
		System.out.println("TestTraceServer");
		TestTraceClient.clientLog("hello");
		HtmlWriter writer=new HtmlWriter();
		writer.text("hello");
		return writer;
	}
	
	@PUT
	@Path("/print/message")
	public void message(@ContentParam PrintMessage debugMessage)
	{
		if ((Testing.ENABLED)&&(Testing.TEST_TRACE_SERVER_PRINT))
		{
			System.out.println(debugMessage.source+":"+debugMessage.text);
		}
		synchronized (this.messages)
		{
			this.messages.add(new ReceivedDebugMessage(debugMessage));
			while (this.messages.size()>this.maximumMessages)
			{
				this.messages.remove();
			}
		}
	}
	
	@GET
	@Path("/")
	public HtmlWriter getMessages()
	{
		return getMessages("","");
	}

	@GET
	@Path("/print/messages")
	public HtmlWriter getMessages(@DefaultValue("") @QueryParam("hostFilters") String hostFilters,@DefaultValue("") @QueryParam("categoryFilters") String categoryFilters)
	{
		HtmlWriter writer=new HtmlWriter();
		writer.begin_sortableTable(1);
		writer.tr(writer.inner().th("Received").th("Created").th("Host").th("Category").th("Source").th("Text"));
		synchronized (this.messages)
		{
			for (ReceivedDebugMessage item:this.messages)
			{
				if (((hostFilters.length()==0)||(hostFilters.equals(item.message.host)))
					&&((categoryFilters.length()==0)||(categoryFilters.equals(item.message.category))))
				{
					writer.tr(writer.inner()
							.td(Utils.millisToLocalDateTimeString(item.received))
							.td(Utils.millisToLocalDateTimeString(item.message.created))
							.td(writer.inner().a(item.message.host,"/print/messages?hostFilters="+item.message.host))
							.td(writer.inner().a(item.message.category,"/print/messages?categoryFilters="+item.message.category))
							.td(item.message.source)
							.td(item.message.text)
							);
				}
			}
		}
		writer.end_table();
		return writer;
	}
}
