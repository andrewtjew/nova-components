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
package com.geneva.net;

import org.nova.tracing.Trace;

public class Context
{
	final long id;
	final ClientConnection clientConnection;
	final Trace trace;
	final private byte[] bytes;
	final private int type;
	private boolean responded=false;
//	int index=0;
	
	Context(Trace trace,ClientConnection clientConnection,long id,int type,byte[] requestContent)
	{
		this.trace=trace;
		this.clientConnection=clientConnection;
		this.id=id;
		this.type=type;
		this.bytes=requestContent;
	}
	/*
	public byte[] readBytes()
	{
		int length=Utils.bigEndianBytesToInt(this.bytes,this.index);
		byte[] bytes=new byte[length];
		System.arraycopy(this.bytes, this.index+4, bytes, 0, length);
		this.index+=length+4;
		return bytes;
	}
	public byte readByte()
	{
		return this.bytes[this.index++];
	}
	public int readInt()
	{
		int value=Utils.bigEndianBytesToInt(this.bytes,this.index);
		this.index+=4;
		return value;
	}
	public long readLong()
	{
		long value=Utils.bigEndianBytesToLong(this.bytes,this.index);
		this.index+=8;
		return value;
	}
	*/
	public ClientConnection getClientConnection()
	{
		return clientConnection;
	}

	public void respond(byte[] responseContent) throws Throwable
	{
		synchronized(this)
		{
			if (this.responded)
			{
				throw new Exception();
			}
			try
			{
				this.clientConnection.sendResponse(id, responseContent);
				responded=true;
				this.trace.close();
			}
			catch(Throwable t)
			{
				this.trace.close(t);
				throw t;
			}
		}
	}

	void responseWithError()
	{
		synchronized(this)
		{
			if (this.responded)
			{
				return;
			}
			try
			{
				this.clientConnection.sendErrorResponse(id);
				responded=true;
				this.trace.close();
			}
			catch(Throwable t)
			{
				this.trace.close(t);
			}
		}
	}
	public byte[] getBytes()
	{
		return bytes;
	}
	public int getType()
	{
		return type;
	}
	
}
