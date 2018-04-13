package org.nova.html.widgets;

import java.util.Stack;

import org.nova.json.ObjectMapper;

public class ObjectBuilder
{
    final StringBuilder sb;
    final Stack<Boolean> commas;
    private boolean comma;
    public ObjectBuilder()
    {
        this.sb=new StringBuilder();
        this.commas=new Stack<>();
        this.comma=false;
    }
    
    private void handleComma()
    {
        if (this.comma==false)
        {
            this.comma=true;
            return;
        }
        this.sb.append(',');
    }
    private void addName(String name)
    {
        sb.append('"'+name+"\":");
    }
    public void add(String name,Object value) throws Throwable
    {
        handleComma();
        addName(name);
        if (value==null)
        {
            sb.append("null");
            return;
        }
        Class<?> type=value.getClass();
        if (type.isPrimitive())
        {
            sb.append(value);
        }
        else
        {
            sb.append(ObjectMapper.write(value));
        }
    }
    public void addRawString(String name,String value) throws Throwable
    {
        handleComma();
        addName(name);
        if (value==null)
        {
            sb.append("null");
            return;
        }
        sb.append(value);
    }
    public void addIfNotNull(String name,Object value) throws Throwable
    {
        if (value==null)
        {
            return;
        }
        handleComma();
        addName(name);
        Class<?> type=value.getClass();
        if (type.isPrimitive())
        {
            sb.append(value);
        }
        else
        {
            sb.append(ObjectMapper.write(value));
        }
    }
    
    public void begin(String name)
    {
        handleComma();
        addName(name);
        sb.append('{');
        this.commas.push(this.comma);
        this.comma=false;
    }
    public void end()
    {
        sb.append('}');
        this.comma=this.commas.pop();
    }
    
    @Override
    public String toString()
    {
        return '{'+this.sb.toString()+'}';
    }
}
