package org.nova.http.client;

public class HttpClientConfiguration
{
    public int maxPerRoute=10;
    public int maxConnections=10;
    public int socketTimeoutMs=60*1000;
    public int connectTimeoutMs=65*1000;
    public int connectionRequestTimeoutMs=70*1000;
    public boolean expectContinueEnabled=false;

}
