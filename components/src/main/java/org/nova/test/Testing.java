package org.nova.test;

public class Testing
{
    public static boolean ENABLED=false;
    public static boolean PRINT=true;
	public static boolean TEST_TRACE_SERVER_PRINT=false;

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

    public static volatile long TIME_BASE;
    
    public static void setTimeBase(long base)
    {
        if (ENABLED&&PRINT)
        {
            TIME_BASE=base;
        }
    }
    
    public static void oprintln(String text)
    {
        if (ENABLED&&PRINT)
        {
            long offset=System.currentTimeMillis()-TIME_BASE;
            System.out.println(offset+":"+text);
        }
    }
    public static void oprint(String text)
    {
        if (ENABLED&&PRINT)
        {
            long offset=System.currentTimeMillis()-TIME_BASE;
            System.out.print(offset+":"+text);
        }
    }
    public static void oprintf(String format,Object...args)
    {
        if (ENABLED&&PRINT)
        {
            long offset=System.currentTimeMillis()-TIME_BASE;
            System.out.printf(offset+":"+format,args);
        }
    }

}
