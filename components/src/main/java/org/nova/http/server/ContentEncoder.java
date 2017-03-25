package org.nova.http.server;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ContentEncoder
{
	public abstract String getCoding();
	public abstract EncoderContext open(HttpServletRequest request,HttpServletResponse response) throws Throwable;
}
