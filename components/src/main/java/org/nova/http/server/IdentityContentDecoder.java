package org.nova.http.server;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.nova.io.SizeInputStream;

public class IdentityContentDecoder extends ContentDecoder
{
	static class Context extends DecoderContext
	{
		final SizeInputStream inputStream;
		final private int contentLength; 
		Context(InputStream inputStream,Integer contentLength)
		{
			this.inputStream=new SizeInputStream(inputStream);
			this.contentLength=contentLength;
		}
		@Override
		public InputStream getInputStream() throws Throwable
		{
			return this.inputStream;
		}
		@Override
		public void close() throws Exception
		{
			this.inputStream.close();
		}
		@Override
		public long getUncompressedContentSize() throws Throwable
		{
			return this.inputStream.getContentSize();
		}
		@Override
		public long getCompressedContentSize() throws Throwable
		{
			return this.inputStream.getContentSize();
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
		return "";
	}

	@Override
	public DecoderContext open(HttpServletRequest request,HttpServletResponse response) throws Throwable
	{
		return new Context(request.getInputStream(),request.getContentLength());
	}

}
