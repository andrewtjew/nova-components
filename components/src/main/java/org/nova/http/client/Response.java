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
package org.nova.http.client;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.nova.tracing.Trace;

public class Response implements AutoCloseable
{
	private HttpResponse response;
	private final Trace trace;
	Response(Trace trace,HttpResponse response)
	{
		this.response=response;
		this.trace=trace;
	}
	
	public int getStatusCode()
	{
		return this.response.getStatusLine().getStatusCode();
	}
	
	public String getHeader(String name)
	{
		return this.response.getFirstHeader(name).getValue();
	}

	
	@Override
	public void close() throws Exception
	{
		if (this.response!=null)
		{
			this.response.getEntity().getContent().close();
			this.trace.close();
			this.response=null;
		}
	}
	
	public InputStream getContentStream() throws UnsupportedOperationException, IOException
	{
		return this.response.getEntity().getContent();
	}
}
