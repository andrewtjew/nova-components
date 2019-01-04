package org.nova.html.widgets;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Stack;

import org.nova.annotations.Alias;
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
            sb.append(ObjectMapper.writeObjectToString(value));
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
            sb.append(ObjectMapper.writeObjectToString(value));
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
    
    public void add(Object object) throws Throwable
    {
        if (object==null)
        {
            return;
        }
        Class<?> c=object.getClass();
        
        for (Field field : c.getDeclaredFields())
        {
            int modifiers = field.getModifiers();
            if (Modifier.isTransient(modifiers))
            {
                continue;
            }
            if (Modifier.isStatic(modifiers))
            {
                continue;
            }
            Alias alias=(Alias)field.getAnnotation(Alias.class);
            if (alias==null)
            {
                addIfNotNull(field.getName(),field.get(object));
            }
            else
            {
                addIfNotNull(alias.value(),field.get(object));
            }
        }
    }
    
    
    @Override
    public String toString()
    {
        return '{'+this.sb.toString()+'}';
    }
}
