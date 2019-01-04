package org.nova.io;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;

import org.nova.utils.TypeUtils;

public class MessageDigestOutputStream extends OutputStream
{
	final private OutputStream outputStream;
	final private MessageDigest message;
	
	public MessageDigestOutputStream(MessageDigest messageDigest,OutputStream outputStream)
	{
		this.outputStream=outputStream;
		this.message=messageDigest;
	}

	@Override
	public void write(int b) throws IOException
	{
		this.outputStream.write(b);
		this.message.update(TypeUtils.bigEndianIntToBytes(b));
	}

	@Override
    public void write(byte b[], int off, int len) throws IOException 
	{
		this.outputStream.write(b, off, len);
		this.message.update(b, off, len);
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

}
