package org.nova.http.client;

public class HttpClientConfiguration
{
    public int maxPerRoute=10;
    public int maxConnections=10;
    public int socketTimeoutMs=30*1000;
    public int connectTimeoutMs=30*1000;
    public int connectionRequestTimeoutMs=30*1000;
    public boolean expectContinueEnabled=false;

}
