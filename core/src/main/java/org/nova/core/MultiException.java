package org.nova.core;

import java.util.ArrayList;
import java.util.List;

public class MultiException extends Exception
{
	private static final long serialVersionUID = 2803345797507011758L;
	private static final String CLASSNAME=MultiException.class.getSimpleName();
	
	static Throwable chain(Throwable[] throwables)
	{
		ArrayList<StackTraceElement> list=new ArrayList<>();
		for (int j=0;j<throwables.length;j++)
		{
			Throwable t=throwables[j];			
			StackTraceElement[] elements=t.getStackTrace();
			for (int i=0;i<elements.length;i++)
			{
				list.add(elements[i]);
			}
			list.add(new StackTraceElement("index",Integer.toString(j),CLASSNAME+".java",1));
		}
		Exception e=new Exception();
		e.setStackTrace(list.toArray(new StackTraceElement[list.size()]));
		return e;
	}
	
	public MultiException(String message,List<Throwable> throwables)
	{
		this(message,throwables.toArray(new Throwable[throwables.size()]));
	}
	public MultiException(List<Throwable> throwables)
	{
		this(throwables.toArray(new Throwable[throwables.size()]));
	}
	public MultiException(String message,Throwable[] throwables)
	{
		super(message,chain(throwables));
	}
	public MultiException(Throwable...throwables)
	{
		super(chain(throwables));
	}
}
