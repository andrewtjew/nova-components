package org.nova.collections;

import java.io.File;

import org.nova.collections.ContentCache;
import org.nova.core.Utils;
import org.nova.tracing.Trace;

public class FileCache extends ContentCache<String,byte[]>
{
	final private String cacheDirectory;

	public FileCache(FileCacheConfiguration configuration) throws Exception
	{
		super(configuration.capacity,configuration.maxAge,configuration.maxSize);
		this.cacheDirectory=Utils.toNativePath(configuration.cacheDirectory);
	}
	
	public void preload(Trace trace) throws Throwable
	{
		File directory=new File(cacheDirectory);
		for (String file:directory.list())
		{
			get(trace,file);
		}
	}
	
	@Override
	protected ValueSize<byte[]> load(Trace trace, String key) throws Throwable
	{
		byte[] bytes=Utils.readFile(this.cacheDirectory+key);
		return new ValueSize<byte[]>(bytes,bytes.length);
	}
	
}
