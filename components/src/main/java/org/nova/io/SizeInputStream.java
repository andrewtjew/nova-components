package org.nova.io;

import java.io.IOException;
import java.io.InputStream;

public class SizeInputStream extends InputStream
{
	private final InputStream inputStream;
	private long bytesStreamed;
	
	public SizeInputStream(InputStream inputStream)
	{
		this.inputStream=inputStream;
	}

	@Override
	public int read() throws IOException
	{
		int read=this.inputStream.read();
		this.bytesStreamed++;
		return read;
	}
    @Override
    public int read(byte b[], int off, int len) throws IOException {
		int read=this.inputStream.read(b,off,len);
		if (read>0)
		{
		    this.bytesStreamed+=read;
		}
		return read;
    }
    @Override
    public long skip(long n) throws IOException 
    {
    	long skipped=this.inputStream.skip(n);
        this.bytesStreamed+=skipped;
        return skipped;
    }
    @Override
    public int available() throws IOException 
    {
        return this.inputStream.available();
    }

    @Override
    public void close() throws IOException 
    {
    	this.inputStream.close();
    }
    @Override
    public synchronized void mark(int readlimit) 
    {
    	this.inputStream.mark(readlimit);
    }
    @Override
    public synchronized void reset() throws IOException 
    {
        this.inputStream.reset();
    }
    @Override
    public boolean markSupported() 
    {
        return this.inputStream.markSupported();
    }
    public long getBytesStreamed()
    {
    	return this.bytesStreamed;
    }

}
