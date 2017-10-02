package org.nova.frameworks;

import org.nova.metrics.TopDoubleValues;
import org.nova.metrics.TopLongValues;
import org.nova.metrics.TraceSample;

public class SlowTraceSampleDetector
{
    final private TopLongValues topCounts;
    final private TopDoubleValues topAverageDurations;
    final private TopLongValues topTotalDurations;
    final private TopDoubleValues topMaxDurationOutliers;
    final private TopDoubleValues topAverageWaits;
    final private TopLongValues topTotalWaits;
    final private TopDoubleValues topWaitOutliers;
    
    public SlowTraceSampleDetector(int totalSamples)
    {
        int topCount=5;
        if (totalSamples<25)
        {
            topCount=totalSamples/5;
        }
        
        this.topCounts=new TopLongValues(topCount);
        this.topAverageDurations=new TopDoubleValues(topCount);
        this.topTotalDurations=new TopLongValues(topCount);
        this.topMaxDurationOutliers=new TopDoubleValues(topCount);

        this.topAverageWaits=new TopDoubleValues(topCount);
        this.topTotalWaits=new TopLongValues(topCount);
        this.topWaitOutliers=new TopDoubleValues(topCount);
    }
    
    public void update(TraceSample sample)
    {
        this.topCounts.update(sample.getCount());
        this.topAverageDurations.update(sample.getAverageDurationNs());
        this.topTotalDurations.update(sample.getTotalDurationNs());
        double out=0;
        if (sample.getStandardDeviationDurationNs()>0)
        {
            out=(sample.getMaxDurationNs()-sample.getAverageDurationNs())/sample.getStandardDeviationDurationNs();
        }
        this.topMaxDurationOutliers.update(out);
        
        
        this.topAverageWaits.update(sample.getAverageWaitNs());
        this.topTotalWaits.update(sample.getTotalWaitNs());
        out=0;
        if (sample.getStandardDeviationWaitNs()>0)
        {
            out=(sample.getMaxWaitNs()-sample.getAverageWaitNs())/sample.getStandardDeviationWaitNs();
        }
        this.topWaitOutliers.update(out);
    }
    public boolean isCountInsideTop(TraceSample sample)
    {
        return this.topCounts.isInsideTop(sample.getCount());
    }
    public boolean isAverageDurationInsideTop(TraceSample sample)
    {
        return this.topAverageDurations.isInsideTop(sample.getAverageDurationNs());
    }
    public boolean isTotalDurationInsideTop(TraceSample sample)
    {
        return this.topTotalDurations.isInsideTop(sample.getTotalDurationNs());
    }
    public boolean isMaxDurationAnOutlier(TraceSample sample)
    {
        double out=0;
        if (sample.getStandardDeviationDurationNs()>0)
        {
            out=(sample.getMaxDurationNs()-sample.getAverageDurationNs())/sample.getStandardDeviationDurationNs();
        }
        return this.topMaxDurationOutliers.isInsideTop(out);
    }
    public boolean isAverageWaitInsideTop(TraceSample sample)
    {
        return this.topAverageWaits.isInsideTop(sample.getAverageWaitNs());
    }
    public boolean isTotalWaitInsideTop(TraceSample sample)
    {
        return this.topTotalWaits.isInsideTop(sample.getTotalWaitNs());
    }
    public boolean isMaxWaitAnOutlier(TraceSample sample)
    {
        double out=0;
        if (sample.getStandardDeviationWaitNs()>0)
        {
            out=(sample.getMaxWaitNs()-sample.getAverageWaitNs())/sample.getStandardDeviationWaitNs();
        }
        return this.topWaitOutliers.isInsideTop(out);
    }
    public double getTotalDurationRatio(TraceSample sample)
    {
        return (double)sample.getTotalDurationNs()/this.topTotalDurations.getTotal();
    }
    public double getTotalWaitRatio(TraceSample sample)
    {
        return (double)sample.getTotalWaitNs()/this.topTotalWaits.getTotal();
    }
}
