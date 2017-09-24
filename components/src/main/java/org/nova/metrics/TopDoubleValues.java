package org.nova.metrics;

import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

public class TopDoubleValues
{
    final TreeSet<Double> sorted;
    final int count;
    public TopDoubleValues(int count)
    {
        this.count=count;
        this.sorted=new TreeSet<>();
    }
    
    public void update(double value)
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
        if (this.sorted.first()<value)
        {
            this.sorted.remove(this.sorted.first());
            this.sorted.add(value);
        }
    }
    public boolean isInTop(double value)
    {
        if (this.sorted.size()==0)
        {
            return false;
        }
        return value>=this.sorted.first();
            
    }
}
