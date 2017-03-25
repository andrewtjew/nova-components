package com.geneva.net;

import java.io.IOException;

import org.nova.core.Utils;
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
