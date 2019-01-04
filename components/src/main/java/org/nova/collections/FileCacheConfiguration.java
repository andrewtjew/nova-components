package org.nova.collections;

public class FileCacheConfiguration
{
    public String sharedDirectory="../resources/client";
    public String localDirectory="./resources/client";
    public long maxAgeMs=Long.MAX_VALUE;
    public long maxSize=Long.MAX_VALUE;
    public int capacity=10000;
}
