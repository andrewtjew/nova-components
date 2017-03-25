package org.nova.http.server;

import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.Filters;
import org.nova.http.server.annotations.Path;

public class ClassAnnotations
{
	Path path=null;
	ContentWriters contentWriters = null;
	ContentReaders contentReaders = null;
	ContentEncoders contentEncoders=null;
	ContentDecoders contentDecoders=null;
	Filters filters = null;
	
	public void setPath(Path path)
	{
		this.path = path;
	}
	public void setWriters(ContentWriters writers)
	{
		this.contentWriters = writers;
	}
	public void setReaders(ContentReaders readers)
	{
		this.contentReaders = readers;
	}
	public void setContentEncoders(ContentEncoders contentEncoders)
	{
		this.contentEncoders = contentEncoders;
	}
	public void setContentDecoders(ContentDecoders contentDecoders)
	{
		this.contentDecoders = contentDecoders;
	}
	public void setFilters(Filters filters)
	{
		this.filters = filters;
	}
}