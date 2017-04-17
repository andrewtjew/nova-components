package org.nova.collections;

import java.io.File;

import org.nova.collections.ContentCache;
import org.nova.core.Utils;
import org.nova.tracing.Trace;

public class FileCache extends ContentCache<String,byte[]>
{
	final private String baseDirectory;

	public FileCache(String baseDirectory,int capacity,long maxAge,long maxSize) throws Exception
	{
		super(capacity,maxAge,maxSize);
		this.baseDirectory=Utils.toNativePath(baseDirectory);
	}
	
	public void preload(Trace trace) throws Throwable
	{
		File directory=new File(baseDirectory);
		for (String file:directory.list())
		{
			get(trace,file);
		}
	}
	
	@Override
	protected ValueSize<byte[]> load(Trace trace, String key) throws Throwable
	{
		byte[] bytes=Utils.readFile(this.baseDirectory+key);
		return new ValueSize<byte[]>(bytes,bytes.length);
	}
	
}
