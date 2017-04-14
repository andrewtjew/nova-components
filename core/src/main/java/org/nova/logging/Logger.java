package org.nova.logging;

import java.util.ArrayList;

import org.nova.metrics.CountMeter;
import org.nova.tracing.Trace;

public abstract class Logger
{
    final private String category;
    
    public Logger(String category)
    {
        this.category=category;
    }
    
	public void log(Trace trace,Throwable throwable,String message,Item...items)
	{
		log(trace,Level.EXCEPTION,this.category,throwable,message,items);
	}
	public void log(Throwable throwable,String message,Item...items)
	{
		log(null,Level.EXCEPTION,this.category,throwable,message,items);
	}
	public void log(Throwable throwable)
	{
		log(null,Level.EXCEPTION,this.category,throwable,null,null);
	}
	public void log(Trace trace,Throwable throwable)
	{
		log(trace,Level.EXCEPTION,this.category,throwable,null,null);
	}
	public void log(Trace trace,Throwable throwable,String message)
	{
		log(trace,Level.EXCEPTION,this.category,throwable,message,null);
	}
	public void log(String message,Item...items)
	{
		log(null,Level.NORMAL,this.category,null,message,items);
	}
	public void log(Level logLevel,String message,Item...items)
	{
		log(null,logLevel,this.category,null,message,items);
	}
	public void log(Trace trace,Level logLevel,String message,Item...items)
	{
		log(trace,logLevel,this.category,null,message,items);
	}
	public void log(Trace trace)
	{
		log(trace,Level.NORMAL,this.category,null,null,null);
	}
	public void log(Trace trace,Level logLevel)
	{
		log(trace,logLevel,this.category,null,null,null);
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
