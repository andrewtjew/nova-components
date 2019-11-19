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
package com.nova.disrupt;

import java.util.ArrayList;

import org.nova.logging.Item;
import org.nova.logging.Logger;
import org.nova.tracing.Trace;
import org.nova.tracing.TraceManager;

public class DisruptorTraceContext implements AutoCloseable
{
    private Trace trace;
    final private String logMessage;
    final private Logger logger;
    final private Disruptor disruptor;
    final private ArrayList<Item> logItems;
    private boolean cancel;
    
    public DisruptorTraceContext(Trace parent,TraceManager traceManager,Logger logger,Disruptor disruptor,String traceCategory,String logMessage)
    {
        this.logItems=new ArrayList<>();
        this.trace=new Trace(traceManager, parent, traceCategory);
        this.logger=logger;
        this.disruptor=disruptor;
        this.logMessage=logMessage;
        if (disruptor!=null)
        {
            try
            {
                Item item=disruptor.disruptBegin(this.trace);
                if (item!=null)
                {
                    this.logItems.add(item);
                }
            }
            catch (RuntimeException ex)
            {
                this.trace.close(ex);
                throw ex;
            }
        }
    }
    public void beginWait()
    {
        this.trace.beginWait();
    }
    public void endWait()
    {
        this.trace.endWait();
    }
    public Throwable handleThrowable(Throwable throwable)
    {
//        this.throwable=throwable;
        return throwable;
    }
    public void cancel()
    {
        this.cancel=true;
    }
    public void addLogItem(Item item)
    {
        this.logItems.add(item);
    }

    @Override
    public void close() throws Exception
    {
        /*
        if (this.trace==null)
        {
            return;
        }
        if (this.throwable==null)
        {
            if (this.disruptor!=null)
            {
                try
                {
                    Item item=this.disruptor.disruptEnd(this.trace);
                    if (item!=null)
                    {
                        this.logItems.add(item);
                    }
                }
                catch (Throwable t)
                {
                    handleThrowable(t);
                }
            }
        }
        this.trace.close(this.throwable);
        this.logger.log(trace,this.logMessage,Logger.toArray(this.logItems));
        this.trace=null;
        if (this.throwable!=null)
        {
            if (this.throwable instanceof Exception)
            {
                throw (Exception)this.throwable;
            }
            throw new Exception(this.throwable);
        }
        */
        if (this.trace==null)
        {
            return;
        }
        Throwable throwable=null;
        try
        {
            if (this.cancel==false)
            {
                if (this.disruptor!=null)
                {
                    try
                    {
                        Item item=this.disruptor.disruptEnd(this.trace);
                        if (item!=null)
                        {
                            this.logItems.add(item);
                        }
                    }
                    catch (Throwable t)
                    {
                        throwable=t;
                    }
                }
            }
        }
        finally
        {
            this.trace.close(throwable);
            if (this.logger!=null)
            {
                this.logger.log(trace,this.logMessage,Logger.toArray(this.logItems));
            }
            this.trace=null;
        }
    }

}
