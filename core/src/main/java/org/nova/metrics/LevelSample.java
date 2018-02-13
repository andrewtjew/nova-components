package org.nova.metrics;

public class LevelSample
{
    final private long level;
    final private long maxLevel;
    final private long maxLevelInstantMs;
    final private long minLevel;
    final private long minLevelInstantMs;
    final private long createdMs;
    final private long baseBase;
    LevelSample(long level,long initial,long maxLevel,long maxLevelInstantMs,long minLevel,long minLevelInstantMs)
    {
        this.baseBase=initial;
        this.level=level;
        this.maxLevel=maxLevel;
        this.maxLevelInstantMs=maxLevelInstantMs;
        this.minLevel=minLevel;
        this.minLevelInstantMs=minLevelInstantMs;
        this.createdMs=System.currentTimeMillis();
    }
    public long getBaseLevel()
    {
        return baseBase;                                                                                                                                                                                                      
    }
    public long getLevel()
    {
        return level;
    }
    public long getMaxLevel()
    {
        return maxLevel;
    }
    public long getMaxLevelInstantMs()
    {
        return maxLevelInstantMs;
    }
    public long getCreatedMs()
    {
        return createdMs;
    }
    public long getMinLevel()
    {
        return minLevel;
    }
    public long getMinLevelInstantMs()
    {
        return minLevelInstantMs;
    }
    
    
}
