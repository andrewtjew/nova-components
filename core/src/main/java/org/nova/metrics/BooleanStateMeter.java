package org.nova.metrics;

public class BooleanStateMeter
{
    private long lastChangeMs;
    private long lastUpdateMs;
    private boolean state;
    private long changeUpdates;
    private long noChangeUpdates;
    private long lastNoChangeUpdates;
    
    public BooleanStateMeter(boolean state)
    {
        synchronized(this)
        {
            this.lastUpdateMs=this.lastChangeMs=System.currentTimeMillis();
            this.state=state;
        }
    }
    
    public void update(boolean state)
    {
        synchronized(this)
        {
            this.lastUpdateMs=System.currentTimeMillis();
            if (state!=this.state)
            {
                this.changeUpdates++;
                this.lastChangeMs=this.lastUpdateMs;
                this.state=state;
            }
            else
            {
                this.lastNoChangeUpdates++;
            }
        }
    }

    public BooleanStateSample sample()
    {
        synchronized(this)
        {
            return new BooleanStateSample(this.lastChangeMs,this.lastUpdateMs,this.lastNoChangeUpdates,this.state,this.changeUpdates,this.noChangeUpdates);
        }
    }
    
    
}
