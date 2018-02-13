package org.nova.metrics;

import org.nova.core.Utils;
import org.nova.pathStore.AttributeValue;
import org.nova.pathStore.Store;

public class MeterAttributeValue extends AttributeValue<MeterAttribute,Object> 
{
    
    public MeterAttributeValue(String description,StackTraceElement[] stackTrace, String path,Object value) throws Exception
    {
        super(Store.toPathElements(path), new MeterAttribute(description,stackTrace), value);
    }
    @SuppressWarnings("unchecked")
    public <T> T getMeter()
    {
        return (T)get();
    }
    
    public CountMeter getCountMeter()
    {
        if (get() instanceof CountMeter)
        {
            return (CountMeter)get();
        }
        return null;
    }
    public RateMeter getRateMeter()
    {
        if (get() instanceof RateMeter)
        {
            return (RateMeter)get();
        }
        return null;
    }
    public LevelMeter getLevelMeter()
    {
        if (get() instanceof LevelMeter)
        {
            return (LevelMeter)get();
        }
        return null;
    }
    public LongValueMeter getLongValueMeter()
    {
        if (get() instanceof LongValueMeter)
        {
            return (LongValueMeter)get();
        }
        return null;
    }
    public RecentSourceEventMeter getRecentStateEventMeter()
    {
        if (get() instanceof RecentSourceEventMeter)
        {
            return (RecentSourceEventMeter)get();
        }
        return null;
    }
}
