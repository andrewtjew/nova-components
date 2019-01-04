package org.nova.utils;

public class MathUtils
{
    public static int mod(int value,int mod)
    {
        int result=value%mod;
        return result<0?result+mod:result;
    }
    public static long mod(long value,long mod)
    {
        long result=value%mod;
        return result<0?result+mod:result;
    }
}



