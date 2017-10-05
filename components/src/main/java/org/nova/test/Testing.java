package org.nova.test;

import java.util.concurrent.atomic.AtomicLong;

public class Testing
{
    public final static boolean ENABLED=false;
    public final static boolean PRINT=true;
    public final static boolean OPRINT=true;
	public final static boolean TEST_TRACE_SERVER_PRINT=false;

    public static void println(String text)
    {
        if (ENABLED&&PRINT)
        {
            System.out.println(text);
        }
    }
    public static void print(String text)
    {
        if (ENABLED&&PRINT)
        {
            System.out.print(text);
        }
    }
    public static void printf(String format,Object...args)
    {
        if (ENABLED&&PRINT)
        {
            System.out.printf(format,args);
        }
    }

    private static volatile long TIME_BASE=System.currentTimeMillis();
    private static AtomicLong LAST=new AtomicLong(System.currentTimeMillis());
    
    public static void oset(long base)
    {
        if (ENABLED&&OPRINT)
        {
            TIME_BASE=base;
            LAST.set(base);
        }
    }
    public static void oset()
    {
        if (ENABLED&&OPRINT)
        {
            oset(System.currentTimeMillis());
        }
    }
    
    public static void oprintln(String text)
    {
        if (ENABLED&&OPRINT)
        {
            long now=System.currentTimeMillis();
            long baseOffset=now-TIME_BASE;
            long lastOffset=now-LAST.getAndSet(now);
            System.out.println(baseOffset+":"+lastOffset+":"+text);
        }
    }
    public static void oprint(String text)
    {
        if (ENABLED&&OPRINT)
        {
            long now=System.currentTimeMillis();
            long baseOffset=now-TIME_BASE;
            long lastOffset=now-LAST.getAndSet(now);
            System.out.print(baseOffset+":"+lastOffset+":"+text);
        }
    }
    public static void oprintf(String format,Object...args)
    {
        if (ENABLED&&OPRINT)
        {
            long now=System.currentTimeMillis();
            long baseOffset=now-TIME_BASE;
            long lastOffset=now-LAST.getAndSet(now);
            System.out.printf(baseOffset+":"+lastOffset+":"+format,args);
        }
    }

}
