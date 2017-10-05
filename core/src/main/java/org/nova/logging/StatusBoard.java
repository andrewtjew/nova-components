package org.nova.logging;

import java.util.HashMap;

public class StatusBoard
{
    final private HashMap<String,StatusEntry> map;
    
    public StatusBoard()
    {
        this.map=new HashMap<>();
    }

    private static String getSource(StackTraceElement element)
    {
        return element.getFileName()+"."+element.getLineNumber();
    }

    public void set(String name,String value)
    {
        synchronized(this)
        {
            int count=1;
            StatusEntry existing=this.map.get(name);
            if (existing!=null)
            {
                count+=existing.getCount();
            }
            this.map.put(name,new StatusEntry(name,getSource(Thread.currentThread().getStackTrace()[2]),value,count));
        }
    }
    public StatusEntry getStatusEntry(String name)
    {
        synchronized(this)
        {
            return map.get(name);
        }        
    }
    public StatusEntry[] getStatusEntries()
    {
        synchronized(this)
        {
            return map.values().toArray(new StatusEntry[map.size()]);
        }        
    }
    
}
