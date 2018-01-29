package org.nova.http.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nova.io.SizeInputStream;

public class GzipContentDecoder extends ContentDecoder
{
	static class Context extends DecoderContext
	{
		final private SizeInputStream uncompressedInputStream;
		final private SizeInputStream compressedInputStream;
		final private GZIPInputStream gzipInputStream;
		final private int contentLength;
		
		Context(InputStream inputStream,int contentLength) throws IOException
		{
			this.compressedInputStream=new SizeInputStream(inputStream);
			this.gzipInputStream=new GZIPInputStream(this.compressedInputStream);
			this.uncompressedInputStream=new SizeInputStream(gzipInputStream);
			this.contentLength=contentLength;
		}
		
		@Override
		public InputStream getInputStream() throws Throwable
		{
			return this.uncompressedInputStream;
		}

		@Override
		public void close() throws Exception
		{
			this.uncompressedInputStream.close();
		}

		@Override
		public long getUncompressedContentSize() throws Throwable
		{
			return this.uncompressedInputStream.getContentSize();
		}

		@Override
		public long getCompressedContentSize() throws Throwable
		{
			return this.compressedInputStream.getContentSize();
		}

        @Override
        public int getContentLength() throws Throwable
        {
            return this.contentLength;
        }
	}
	
	@Override
	public String getCoding()
	{
		return "gzip";
	}

	@Override
	public DecoderContext open(HttpServletRequest request,HttpServletResponse response) throws IOException
	{
		return new Context(request.getInputStream(),request.getContentLength());
	}

}
