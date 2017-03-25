package org.nova.http.server;

import java.io.InputStream;

public abstract class DecoderContext implements AutoCloseable
{
	abstract public InputStream getInputStream() throws Throwable;
	abstract public long getUncompressedContentSize() throws Throwable;
	abstract public long getCompressedContentSize() throws Throwable;
}
