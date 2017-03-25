package org.nova.logging;

public class LogDirectoryInfo
{
	long freeSpace;
	long totalSpace;
	long usableSpace;
	long directorySize;
	int fileCount;
	long oldestFileSize;
	String oldestFileName;
	long oldestFileDate;
	long newestFileSize;
	String newestFileName;
	long newestFileDate;
	Throwable throwable;
	public long getFreeSpace()
	{
		return freeSpace;
	}
	public long getTotalSpace()
	{
		return totalSpace;
	}
	public long getUsableSpace()
	{
		return usableSpace;
	}
	public long getDirectorySize()
	{
		return directorySize;
	}
	public int getFileCount()
	{
		return fileCount;
	}
	public long getOldestFileSize()
	{
		return oldestFileSize;
	}
	public String getOldestFileName()
	{
		return oldestFileName;
	}
	public long getOldestFileDate()
	{
		return oldestFileDate;
	}
	public long getNewestFileSize()
	{
		return newestFileSize;
	}
	public String getNewestFileName()
	{
		return newestFileName;
	}
	public long getNewestFileDate()
	{
		return newestFileDate;
	}
	public Throwable getThrowable()
	{
		return this.throwable;
	}
	
	
	
}
