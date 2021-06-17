/*******************************************************************************
 * Copyright (C) 2017-2019 Kat Fung Tjew
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
package com.nova.control;

import java.util.ArrayList;
import org.nova.core.MultiException;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;

public abstract class ReturnRetry<RETURN,THROWABLE extends Throwable> 
{
    public abstract RETURN retry(Trace parent) throws THROWABLE;
    
    public static <RETURN,THROWABLE extends Throwable> RETURN invoke(Trace parent,int attempts,long sleep,ReturnRetry<RETURN,THROWABLE> retry) throws THROWABLE
    {
        THROWABLE throwable=null;
        for (int i=0;i<attempts;i++)
        {
            try (Trace trace=new Trace(parent,"Retry:"+parent.getCategory()))
            {
                trace.setDetails("RetryIndex="+i);
                try 
                {
                    return retry.retry(trace);
                }
                catch (Throwable t)
                {
                    trace.close(t);
                    throwable=(THROWABLE)t;
                }
                if (sleep>0)
                {
                    try
                    {
                        Thread.sleep(sleep);
                    }
                    catch (InterruptedException e)
                    {
                    }
                }
            }
        }
        throw throwable;
    }

    public static <RETURN,THROWABLE extends Throwable> RETURN invoke(Trace parent,int attempts,ReturnRetry<RETURN,THROWABLE> retry) throws THROWABLE
    {
        return invoke(parent,attempts,0,retry);
    }
}