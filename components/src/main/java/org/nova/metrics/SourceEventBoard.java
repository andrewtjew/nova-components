package org.nova.metrics;

import java.util.HashMap;
import java.util.Map;

import org.nova.metrics.RecentSourceEventMeter;

public class SourceEventBoard
{
    final private HashMap<String,RecentSourceEventMeter> map;
    final private int bufferSize;
    
    public SourceEventBoard(int bufferSize)
    {
        this.map=new HashMap<>();
        this.bufferSize=bufferSize;
    }
    public SourceEventBoard()
    {
        this(3);
    }

    private RecentSourceEventMeter getMeter(String path,String meterDescription)
    {
        synchronized(this)
        {
            RecentSourceEventMeter meter=this.map.get(path);
            if (meter==null)
            {
                meter=new RecentSourceEventMeter(this.bufferSize);
                this.map.put(path, meter);
            }
            return meter;
        }
    }
    
    public void set(String path,Object value)
    {
        getMeter(path,null).update(value,1);
    }
    public Map<String,RecentSourceEventMeter> getSnapshot()
    {
        synchronized(this)
        {
            return new HashMap<String,RecentSourceEventMeter>(this.map);
        }        
    }
    
}