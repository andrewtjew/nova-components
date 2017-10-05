package org.nova.metrics;

public class LevelSample
{
    final private long level;
    final private long maxLevel;
    final private long maxLevelInstantMs;
    final private long createdMs;
    LevelSample(long level,long maxLevel,long maxLevelInstantMs)
    {
        this.level=level;
        this.maxLevel=maxLevel;
        this.maxLevelInstantMs=maxLevelInstantMs;
        this.createdMs=System.currentTimeMillis();
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
    
}
