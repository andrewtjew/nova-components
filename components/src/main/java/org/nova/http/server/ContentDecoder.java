package org.nova.http.server;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ContentDecoder
{
	abstract public String getCoding();
	abstract public DecoderContext open(HttpServletRequest request,HttpServletResponse response) throws Throwable;
}
