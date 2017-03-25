package org.nova.http.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nova.io.SizeOutputStream;

public class GzipContentEncoder extends ContentEncoder
{
	static class Context extends EncoderContext
	{
		final private SizeOutputStream uncompressedOutputStream;
		final private SizeOutputStream compressedOutputStream;
		final private GZIPOutputStream gzipOutputStream;		
		Context(OutputStream outputStream) throws IOException
		{
			this.compressedOutputStream=new SizeOutputStream(outputStream);
			this.gzipOutputStream=new GZIPOutputStream(this.compressedOutputStream);
			this.uncompressedOutputStream=new SizeOutputStream(this.gzipOutputStream);
		}

		@Override
		public void close() throws Exception
		{
			this.uncompressedOutputStream.close();
		}

		@Override
		public OutputStream getOutputStream() throws Throwable
		{
			return this.uncompressedOutputStream;
		}
		
		@Override
		public long getUncompressedContentSize()
		{
			return this.uncompressedOutputStream.getContentSize();
		}

		@Override
		public long getCompressedContentSize() throws Throwable
		{
			return this.compressedOutputStream.getContentSize();
		}
	}
	@Override
	public String getCoding()
	{
		return "gzip";
	}

	@Override
	public EncoderContext open(HttpServletRequest request, HttpServletResponse response) throws Throwable
	{
		response.setHeader("Content-Encoding", getCoding());
		return new Context(response.getOutputStream());
	}

}
