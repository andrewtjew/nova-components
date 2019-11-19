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
package org.nova.testing;

import org.nova.http.client.JSONClient;
import org.nova.test.PrintMessage;
import org.nova.test.Testing;
import org.nova.tracing.TraceManager;
import org.nova.utils.Utils;

public class TestTraceClient
{
	public static String SERVER="http://localhost:9111";
	
	private static TestTraceClient instance;
	
	public static TestTraceClient getInstance() throws Throwable
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
	public TestTraceClient(String serverEndPoint) throws Throwable
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
				this.client.put(null, null, printUrl, new PrintMessage(category,source,text));
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
	
	
	public static void clientLog(String text)
	{
		if (Testing.ENABLED)
		{
			try
			{
	            TestTraceClient client=getInstance();
				Thread thread=Thread.currentThread();
				StackTraceElement element=thread.getStackTrace()[2];
				String category=element.getClassName();
				String source=thread.getName()+":"+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")";
				client.client.put(null, null, client.printUrl, new PrintMessage(category,source,text));
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void clientLog(String text,Throwable throwable)
	{
		if (Testing.ENABLED)
		{
			try
			{
	            TestTraceClient client=getInstance();
				Thread thread=Thread.currentThread();
				StackTraceElement element=thread.getStackTrace()[2];
				String category=element.getClassName();
				String source=thread.getName()+":"+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")";
				client.client.put(null, null, client.printUrl, new PrintMessage(category,text+":"+source,Utils.toString(throwable)));
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void testPrint(Throwable throwable)
	{
		if (Testing.ENABLED)
		{
			try
			{
	            TestTraceClient client=getInstance();
				Thread thread=Thread.currentThread();
				StackTraceElement element=thread.getStackTrace()[2];
				String category=element.getClassName();
				String source=thread.getName()+":"+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")";
				client.client.put(null, null, client.printUrl, new PrintMessage(category,source,Utils.toString(throwable)));
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void mark()
	{
		if (Testing.ENABLED)
		{
			try
			{
	            TestTraceClient client=getInstance();
				Thread thread=Thread.currentThread();
				StackTraceElement element=thread.getStackTrace()[2];
				String category=element.getClassName();
				String source=thread.getName()+":"+element.getMethodName()+"("+element.getFileName()+":"+element.getLineNumber()+")";
				
				client.client.put(null, null, client.printUrl, new PrintMessage(category,source,"MARK"));
			}
			catch (Throwable e)
			{
				e.printStackTrace();
			}
		}
	}
	
}
