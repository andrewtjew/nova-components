package com.geneva.net;

import org.nova.core.Utils;

public class FixedLengthByteArrayWriter
{
	final private byte[] bytes;
	private int index;
	
	public FixedLengthByteArrayWriter(int length)
	{
		this.bytes=new byte[length];
	}
	
	public void write(byte value) throws Exception
	{
		this.bytes[this.index++]=value;
	}
	public void write(long value) throws Exception
	{
		Utils.bigEndianLongToBytes(value,this.bytes,this.index);
		this.index+=8;
	}
	public void write(int value) throws Exception
	{
		Utils.bigEndianIntToBytes(value,this.bytes,this.index);
		this.index+=4;
	}

	public void write(byte[] bytes,int offset,int length) 
	{
		if (bytes==null)
		{
			Utils.bigEndianIntToBytes(-1,this.bytes,this.index);
			this.index+=4;
		}
		else
		{
			Utils.bigEndianIntToBytes(length,this.bytes,this.index);
			System.arraycopy(bytes, offset, this.bytes, this.index+4,length);
			this.index+=length+4;
		}
	}
	public void write(byte[] bytes)
	{
		write(bytes,0,bytes.length);
	}

	public byte[] getBytes() throws Exception
	{
		if (this.index!=this.bytes.length)
		{
			throw new Exception();
		}
		return this.bytes;
	}
	
}
