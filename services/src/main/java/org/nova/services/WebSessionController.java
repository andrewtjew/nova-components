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

@Filters(SessionFilter.class)
public class WebSessionController<SERVICE extends ServerApplication> extends WebController<SERVICE>
{
	public WebSessionController(SERVICE service) 
	{
		super(service);
	}
}
