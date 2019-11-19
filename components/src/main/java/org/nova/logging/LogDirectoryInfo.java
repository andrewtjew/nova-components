/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
