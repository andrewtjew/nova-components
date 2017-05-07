package org.nova.collections;

import java.io.File;
import java.io.FileInputStream;

import org.nova.collections.ContentCache;
import org.nova.core.Utils;
import org.nova.tracing.Trace;

public class FileCache extends ContentCache<String,byte[]>
{
    final private String sharedDirectory;
    final private String localDirectory;

	public FileCache(FileCacheConfiguration configuration) throws Exception
	{
		super(configuration.capacity,configuration.maxAge,configuration.maxSize);
        this.sharedDirectory=Utils.toNativePath(configuration.sharedDirectory);
        this.localDirectory=Utils.toNativePath(configuration.localDirectory);
	}
	
	public void preload(Trace trace) throws Throwable
	{
		File directory=new File(sharedDirectory);
		for (String file:directory.list())
		{
			get(trace,file);
		}
	}
    
	static byte[] readFile(String fileName) throws Exception
    {
        fileName=Utils.toNativePath(fileName);
        File file=new File(fileName);
        if (file.exists()==false)
        {
            return null;
        }
        if (file.isDirectory()==true)
        {
            return null;
        }
        long length=file.length();
        if (length>Integer.MAX_VALUE)
        {
            throw new Exception("File too big. Filename="+fileName);
        }
        try (FileInputStream stream=new FileInputStream(file))
        {
            byte[] bytes=new byte[(int)file.length()];
            stream.read(bytes);
            return bytes;
        }
    }
    	
	@Override
	protected ValueSize<byte[]> load(Trace trace, String key) throws Throwable
	{
        byte[] bytes=readFile(this.localDirectory+key);
        if (bytes==null)
        {
            bytes=readFile(this.sharedDirectory+key);
        }
        if (bytes==null)
        {
            throw new Exception("File not found: "+key);
        }
		return new ValueSize<byte[]>(bytes,bytes.length);
	}
	
}
