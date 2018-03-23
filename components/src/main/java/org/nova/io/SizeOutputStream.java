package org.nova.io;

import java.io.IOException;
import java.io.OutputStream;

public class SizeOutputStream extends OutputStream
{
	final private OutputStream outputStream;
	private long bytesStreamed;
	public SizeOutputStream(OutputStream outputStream)
	{
		this.outputStream=outputStream;
	}

	@Override
	public void write(int b) throws IOException
	{
		this.outputStream.write(b);
	}

	@Override
    public void write(byte b[], int off, int len) throws IOException 
	{
		this.bytesStreamed+=len;
		this.outputStream.write(b, off, len);
	}
	
	@Override
    public void flush() throws IOException
	{
		this.outputStream.flush();
	}

	@Override
    public void close() throws IOException
	{
		this.outputStream.close();
	}
	public long getBytesStreamed()
	{
		return this.bytesStreamed;
	}

}
