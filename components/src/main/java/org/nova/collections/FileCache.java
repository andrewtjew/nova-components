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
		super(configuration.capacity,configuration.maxAgeMs,configuration.maxSize);
        this.sharedDirectory=new File(Utils.toNativePath(configuration.sharedDirectory)).getCanonicalPath();
        this.localDirectory=new File(Utils.toNativePath(configuration.localDirectory)).getCanonicalPath();
	}
	
	public void preload(Trace trace) throws Throwable
	{
		File directory=new File(sharedDirectory);
		for (String file:directory.list())
		{
			get(trace,file);
		}
	}
    
	static byte[] readFile(File file) throws Exception
    {
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
            throw new Exception("File too big. Filename="+file.getCanonicalPath());
        }
        try (FileInputStream stream=new FileInputStream(file))
        {
            byte[] bytes=new byte[(int)file.length()];
            stream.read(bytes);
            return bytes;
        }
    }
    	
	@Override
	protected ValueSize<byte[]> load(Trace trace, String filePath) throws Throwable
	{
        File file=new File(Utils.toNativePath(this.localDirectory+File.separator+filePath));
        if (file.getCanonicalPath().startsWith(this.localDirectory))
        {
            byte[] bytes=readFile(file);
            if (bytes!=null)
            {
                return new ValueSize<byte[]>(bytes,bytes.length);
            }
        }
        file=new File(Utils.toNativePath(this.sharedDirectory+File.separator+filePath));
        if (file.getCanonicalPath().startsWith(this.sharedDirectory))
        {
            byte[] bytes=readFile(file);
            if (bytes==null)
            {
                throw new Exception("File not found: "+filePath);
            }
            return new ValueSize<byte[]>(bytes,bytes.length);
        }
        throw new Exception("Invalid file: "+filePath);
	}
	
}
