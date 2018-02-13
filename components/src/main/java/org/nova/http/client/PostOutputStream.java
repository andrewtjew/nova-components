package org.nova.http.client;

import java.io.IOException;
import java.io.OutputStream;

public class PostOutputStream extends OutputStream
{
	final private OutputStream outputStream;
	private long contentSize;
	public PostOutputStream(OutputStream outputStream)
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
		this.contentSize+=len;
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
	public long getContentSize()
	{
		return this.contentSize;
	}

}
