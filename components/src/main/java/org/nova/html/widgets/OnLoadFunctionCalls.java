package org.nova.html.widgets;

import org.nova.html.tags.script;

public class OnLoadFunctionCalls 
{
    final private StringBuilder sb;
    public OnLoadFunctionCalls()
    {
        this.sb=new StringBuilder();
    }
    public void add(String functionCall)
    {
        sb.append(functionCall);
    }
    public void addTo(Head head,String key)
    {
        if (sb.length()>0)
        {
            head.add(key, new script().addInner(key+"=function(e){"+sb.toString()+"}"));
        }
    }
    public void addTo(Head head)
    {
        addTo(head,"window.onload");
    }
}
