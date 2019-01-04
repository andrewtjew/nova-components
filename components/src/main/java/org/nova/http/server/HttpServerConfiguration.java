package org.nova.http.server;

public class HttpServerConfiguration
{
    public int lastRequestLogEntryBufferSize=100;
    public int requestLastRequestLogEntryBufferSize=20;
    public int lastExceptionRequestLogEntryBufferSize=100;
    public int lastNotFoundBufferSize=100;
    public String categoryPrefix="HttpServer";
}
