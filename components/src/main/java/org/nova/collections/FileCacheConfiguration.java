package org.nova.collections;

public class FileCacheConfiguration
{
    public String sharedDirectory="../resources/";
    public String localDirectory="./resources/";
    public long maxAge=Long.MAX_VALUE;
    public long maxSize=Long.MAX_VALUE;
    public int capacity=10000;
}
