package org.nova.testing;

import org.nova.core.Utils;
import org.nova.http.client.JSONClient;
import org.nova.test.PrintMessage;
import org.nova.test.Testing;
import org.nova.tracing.TraceManager;

public class TestTraceClient
{
	public static String SERVER="http://localhost:9111";
	
	private static TestTraceClient instance;
	
	public static TestTraceClient getInstance()
	{
		synchronized(TestTraceClient.class)
		{
			if (instance==null)
			{
				instance=new TestTraceClient(SERVER);
			}
			return instance;
		}
	}
	
	private final JSONClient client; 
	private final String printUrl;
	public TestTraceClient(String serverEndPoint)
	{
		this.client=new JSONClient(new TraceManager(), null,serverEndPoint);
		this.printUrl="/print/message";	
	}

	public void log(String text)
	{
		if (Testing.ENABLED)
		{
			try
			{
				Thread thread=Thread.currentThread();
				StackTraceElement element=thread.getStackTrace()[2];
				String category=element.getClassName();
				String source=thread.getName()+":"+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")";
				this.client.PUT(null, null, printUrl, new PrintMessage(category,source,text));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public static void clientLog(String text)
	{
		if (Testing.ENABLED)
		{
			TestTraceClient client=getInstance();
			try
			{
				Thread thread=Thread.currentThread();
				StackTraceElement element=thread.getStackTrace()[2];
				String category=element.getClassName();
				String source=thread.getName()+":"+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")";
				client.client.PUT(null, null, client.printUrl, new PrintMessage(category,source,text));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void clientLog(String text,Throwable throwable)
	{
		if (Testing.ENABLED)
		{
			TestTraceClient client=getInstance();
			try
			{
				Thread thread=Thread.currentThread();
				StackTraceElement element=thread.getStackTrace()[2];
				String category=element.getClassName();
				String source=thread.getName()+":"+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")";
				client.client.PUT(null, null, client.printUrl, new PrintMessage(category,text+":"+source,Utils.toString(throwable)));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void testPrint(Throwable throwable)
	{
		if (Testing.ENABLED)
		{
			TestTraceClient client=getInstance();
			try
			{
				Thread thread=Thread.currentThread();
				StackTraceElement element=thread.getStackTrace()[2];
				String category=element.getClassName();
				String source=thread.getName()+":"+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")";
				client.client.PUT(null, null, client.printUrl, new PrintMessage(category,source,Utils.toString(throwable)));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void mark()
	{
		if (Testing.ENABLED)
		{
			TestTraceClient client=getInstance();
			try
			{
				Thread thread=Thread.currentThread();
				StackTraceElement element=thread.getStackTrace()[2];
				String category=element.getClassName();
				String source=thread.getName()+":"+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")";
				
				client.client.PUT(null, null, client.printUrl, new PrintMessage(category,source,"MARK"));
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
