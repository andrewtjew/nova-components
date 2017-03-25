package org.nova.http.server;

import java.io.OutputStream;

public abstract class EncoderContext implements AutoCloseable
{
	public abstract OutputStream getOutputStream() throws Throwable;
	public abstract long getUncompressedContentSize() throws Throwable;
	public abstract long getCompressedContentSize() throws Throwable;
}
