package org.nova.http.server;

class RequestHandlerWithParameters
{
	final RequestHandler requestHandler;
	final String[] parameters;
	RequestHandlerWithParameters(RequestHandler requestHandler,String[] parameters)
	{
		this.requestHandler=requestHandler;
		this.parameters=parameters;
	}
}
