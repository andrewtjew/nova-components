package org.nova.core;

import java.util.List;

public class MultiException extends Exception
{
    private static final long serialVersionUID = 2803345797507011758L;
	private static final String CLASSNAME=MultiException.class.getSimpleName();
	final private Throwable[] throwables;
	
    static Throwable chain(Throwable[] throwables)
    {
        if (throwables.length==0)
        {
            return null;
        }
        LinkException[] nodeExceptions=new LinkException[throwables.length];
        for (int i=0;i<throwables.length;i++)
        {
            nodeExceptions[i]=new LinkException(i, throwables[i]);
        }
        
        for (int i=0;i<nodeExceptions.length-1;i++)
        {
            Throwable throwable=throwables[i];
            while (throwable.getCause()!=null)
            {
                throwable=throwable.getCause();
            }
            throwable.initCause(nodeExceptions[i+1]);
        }
        return nodeExceptions[0];
    }

	//First is top of stack
	public MultiException(String message,List<Throwable> throwables)
	{
		this(message,throwables.toArray(new Throwable[throwables.size()]));
	}
	public MultiException(List<Throwable> throwables)
	{
		this(throwables.toArray(new Throwable[throwables.size()]));
	}
	public MultiException(Throwable... throwables)
	{
	    this(null,throwables);
	}
	public MultiException(String message,Throwable...throwables)
	{
		super(message,chain(throwables));
		this.throwables=throwables;
	}
	public Throwable[] getThrowables()
	{
	    return this.throwables;
	}
}
