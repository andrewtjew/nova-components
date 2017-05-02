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
    final private DisruptProfile disruptProfile;
    
    public Disruptor(DisruptProfile beginDisruptProfile,DisruptProfile endDisruptProfile,DisruptProfile disruptProfile)
    {
        this.beginDisruptProfile=beginDisruptProfile;
        this.endDisruptProfile=endDisruptProfile;
        this.disruptProfile=disruptProfile;
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
        trace.beginWait();
        try
        {
            
        }
        finally
        {
            trace.endWait();
        }
        return null;
    }
    
    public Item disrupt(Trace trace) throws RuntimeException
    {
        if (ENABLED==false)
        {
            return null;
        }
        trace.beginWait();
        try
        {
            
        }
        finally
        {
            trace.endWait();
        }
        return null;
    }
}
