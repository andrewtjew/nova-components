package org.nova.metrics;

import java.util.TreeSet;

public class TopLongValues
{
    final TreeSet<Long> sorted;
    final int count;
    long total;
    
    public TopLongValues(int count)
    {
        this.count=count;
        this.sorted=new TreeSet<>();
    }
    
    public void update(long value)
    {
        if (this.count==0)
        {
            return;
        }
        if (this.sorted.size()<this.count)
        {
            this.sorted.add(value);
            return;
        }
        if (this.sorted.first().longValue()<value)
        {
            this.sorted.remove(this.sorted.first());
            this.sorted.add(value);
        }
        this.total+=value;
    }
    public boolean isInsideTop(long value)
    {
        if (this.sorted.size()==0)
        {
            return false;
        }
        return value>this.sorted.first().longValue();
            
    }
    public boolean isInTop(long value)
    {
        if (this.sorted.size()==0)
        {
            return false;
        }
        return value>=this.sorted.first().longValue();
            
    }
    public long getTotal()
    {
        return total;
    }
}
