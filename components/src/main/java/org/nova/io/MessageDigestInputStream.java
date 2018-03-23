package org.nova.io;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

import org.nova.core.Utils;

public class MessageDigestInputStream extends InputStream
{
	private final InputStream inputStream;
	private final MessageDigest messageDigest;
	
	public MessageDigestInputStream(MessageDigest messageDigest,InputStream inputStream)
	{
		this.inputStream=inputStream;
		this.messageDigest=messageDigest;
	}

	@Override
	public int read() throws IOException
	{
		int read=this.inputStream.read();
        this.messageDigest.update(Utils.bigEndianIntToBytes(read));
		return read;
	}
    @Override
    public int read(byte b[], int off, int len) throws IOException {
		int read=this.inputStream.read(b,off,len);
		if (read>0)
		{
		    this.messageDigest.update(b, off, read);
		}
		return read;
    }

    @Override
    public void close() throws IOException 
    {
    	this.inputStream.close();
    }

}
