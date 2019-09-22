package org.nova.http.server;

public class StatusCodeResponse extends Response<Void>
{
	public StatusCodeResponse(int statusCode)
	{
		super(statusCode,null);
	}
}
