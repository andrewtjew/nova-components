package org.nova.io;

import java.io.IOException;
import java.io.OutputStream;

public class NullOutputStream extends OutputStream
{
	private long bytesStreamed;
	public NullOutputStream()
	{
	}

	@Override
	public void write(int b) throws IOException
	{
	}

	@Override
    public void write(byte b[], int off, int len) throws IOException 
	{
		this.bytesStreamed+=len;
	}
	
	@Override
    public void flush() throws IOException
	{
	}

	@Override
    public void close() throws IOException
	{
	}
	public long getBytesStreamed()
	{
		return this.bytesStreamed;
	}

}
