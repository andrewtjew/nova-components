package org.nova.metrics;

public class BooleanStateSample
{
    final private long lastChangeMs;
    final private long lastUpdateMs;
    final private long lastNoChangeUpdates;
    final private boolean state;
    final private long changeUpdates;
    final private long noChangeUpdates;
    
    BooleanStateSample(long lastChangeMs,long lastUpdateMs,long lastNoChangeUpdates,boolean state,long changeUpdates,long noChangeUpdates)
    {
        this.lastChangeMs=lastChangeMs;
        this.lastUpdateMs=lastUpdateMs;
        this.lastNoChangeUpdates=lastNoChangeUpdates;
        this.state=state;
        this.changeUpdates=changeUpdates;
        this.noChangeUpdates=noChangeUpdates;
    }

    public long getLastChangeMs()
    {
        return lastChangeMs;
    }

    public long getLastUpdateMs()
    {
        return lastUpdateMs;
    }

    public long getLastNoChangeUpdates()
    {
        return lastNoChangeUpdates;
    }

    public boolean getState()
    {
        return state;
    }

    public long getChangeUpdates()
    {
        return changeUpdates;
    }

    public long getNoChangeUpdates()
    {
        return noChangeUpdates;
    }
    
}
