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

import java.util.Random;

import org.nova.logging.Item;
import org.nova.tracing.Trace;

public class Disruptor
{
    public static volatile boolean ENABLED;
    
    final private Random random;
    final private DisruptProfile beginDisruptProfile;
    final private DisruptProfile endDisruptProfile;
    
    public Disruptor(DisruptProfile beginDisruptProfile,DisruptProfile endDisruptProfile)
    {
        this.beginDisruptProfile=beginDisruptProfile;
        this.endDisruptProfile=endDisruptProfile;
        this.random=new Random();
    }
    
    private Item executeProfile(Trace trace,DisruptProfile profile)
    {
        if (profile==null)
        {
            return null;
        }
        if (profile.exceptionProbabilityRange>0)
        {
            if (this.random.nextInt(profile.exceptionProbabilityRange)<profile.exceptionProbability)
            {
                throw new RuntimeException();
            }
        }
        if (profile.maxDelay>0)
        {
            long delay=profile.minDelay;
            long range=profile.maxDelay-profile.minDelay;
            if (range>0)
            {
                delay+=this.random.nextLong()%range;
            }
            boolean activateWaiting=false;
            if (trace!=null)
            {
                activateWaiting=trace.isWaiting()==false;
                if (activateWaiting)
                {
                    trace.beginWait();
                }
            }
            try
            {
                Thread.sleep(delay);
                return new Item("DisruptorDelay",delay);
            }
            catch (InterruptedException e)
            {
                //don't care
            }
            finally
            {
                if (trace!=null)
                {
                    if (activateWaiting)
                    {
                        trace.endWait();
                    }
                }
            }
        }
        return null;
    }
    
    
    public Item disruptBegin(Trace trace) throws RuntimeException
    {
        if (ENABLED==false)
        {
            return null;
        }
        return executeProfile(trace,this.beginDisruptProfile);
    }
    public Item disruptEnd(Trace trace) throws RuntimeException
    {
        if (ENABLED==false)
        {
            return null;
        }
        return executeProfile(trace,this.endDisruptProfile);
    }
}
