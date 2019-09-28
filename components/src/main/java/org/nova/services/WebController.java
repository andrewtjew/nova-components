package org.nova.services;

import org.nova.frameworks.ServerApplication;
import org.nova.html.elements.HtmlElementWriter;
import org.nova.html.remoting.HtmlRemotingWriter;
import org.nova.http.server.GzipContentDecoder;
import org.nova.http.server.GzipContentEncoder;
import org.nova.http.server.JSONContentReader;
import org.nova.http.server.JSONContentWriter;
import org.nova.http.server.JSONPatchContentReader;
import org.nova.http.server.annotations.ContentDecoders;
import org.nova.http.server.annotations.ContentEncoders;
import org.nova.http.server.annotations.ContentReaders;
import org.nova.http.server.annotations.ContentWriters;
import org.nova.http.server.annotations.Filters;

@ContentDecoders(GzipContentDecoder.class)
@ContentEncoders(GzipContentEncoder.class)
@ContentReaders({JSONContentReader.class,JSONPatchContentReader.class})
@ContentWriters({JSONContentWriter.class,HtmlRemotingWriter.class,HtmlElementWriter.class})
public class WebController<SERVICE extends ServerApplication>
{
	final protected SERVICE service;
	public WebController(SERVICE service)
	{
		this.service=service;
	}
	public SERVICE getService()
	{
		return this.service;
	}
}
