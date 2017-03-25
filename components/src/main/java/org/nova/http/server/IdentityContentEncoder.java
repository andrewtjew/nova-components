package org.nova.http.server;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nova.io.SizeOutputStream;

public class IdentityContentEncoder extends ContentEncoder
{
	static class Context extends EncoderContext
	{
		final private SizeOutputStream outputStream;
		
		Context(OutputStream outputStream)
		{
			this.outputStream=new SizeOutputStream(outputStream);
		}
		@Override
		public OutputStream getOutputStream() throws Throwable
		{
			return this.outputStream;
		}

		@Override
		public void close() throws Exception
		{
			this.outputStream.close();
		}
		@Override
		public long getUncompressedContentSize() throws Throwable
		{
			return outputStream.getContentSize();
		}
		@Override
		public long getCompressedContentSize() throws Throwable
		{
			return outputStream.getContentSize();
		}
		
	}
	
	@Override
	public String getCoding()
	{
		return "";
	}

	@Override
	public EncoderContext open(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		return new Context(response.getOutputStream());
	}

}
