/*******************************************************************************
 * Copyright (C) 2016-2019 Kat Fung Tjew
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
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
