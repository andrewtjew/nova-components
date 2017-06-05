package org.nova.http.server;

import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.Filters;
import org.nova.http.server.annotations.Log;
import org.nova.http.server.annotations.Path;

public class Annotations
{
	Path path=null;
	ContentWriters contentWriters = null;
	ContentReaders contentReaders = null;
	ContentEncoders contentEncoders=null;
	ContentDecoders contentDecoders=null;
	Filters filters = null;
	Log log=null;
	
    Annotations()
    {
        
    }
	Annotations(Annotations that)
	{
	    this.path=that.path;
	    this.contentWriters = that.contentWriters;
	    this.contentReaders = that.contentReaders;
	    this.contentEncoders=that.contentEncoders;
	    this.contentDecoders=that.contentDecoders;
	    this.filters = that.filters;
	    this.log=that.log;
	}

}