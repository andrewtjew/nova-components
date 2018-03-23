package org.nova.logging;

import java.util.ArrayList;

import org.nova.tracing.Trace;

public abstract class Logger
{
    final private String category;
    
    public Logger(String category)
    {
        this.category=category;
    }
    
    public void log(Throwable throwable,String message,Item...items)
    {
        log(null,throwable!=null?Level.EXCEPTION:Level.NORMAL,this.category,throwable,message,items);
    }
	public void log(Throwable throwable)
	{
		log(throwable,null);
	}
	
	public void log(Level logLevel,String message,Item...items)
	{
		log(null,logLevel,this.category,null,message,items);
	}
    public void log(String message,Item...items)
    {
        log(Level.NORMAL,message,items);
    }

    public void log(Trace trace,String message,Item...items)
    {
        log(trace,trace.getThrowable()!=null?Level.EXCEPTION:Level.NORMAL,this.category,null,message,items);
    }
    public void log(Trace trace,Item...items)
    {
        log(trace,null,items);
    }
	
    public String getCategory()
	{
	    return this.category;
	}
	abstract public void log(Trace trace,Level logLevel,String category,Throwable throwable,String message,Item[] items);
	
	public static Item[] toArray(ArrayList<Item> items)
	{
	    return items.toArray(new Item[items.size()]);
	}
}
