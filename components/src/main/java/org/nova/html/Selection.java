package org.nova.html;

import org.nova.frameworks.InteropTarget;

public class Selection
{
    private HtmlWriter writer;
    public Selection(String idAndName,Attribute...attributes)
    {
        this.writer=new HtmlWriter();
        this.writer.begin_select(idAndName,attributes);
        
    }
    
    public void option(Object option)
    {
        this.writer.option(option);
    }
    
    public void options(Enum<?>[] options)
    {
        for (Enum<?> option:options)
        {
            this.writer.option(option);
        }
    }
    
    public String toString()
    {
        this.writer.end_select();
        String result=this.writer.toString();
        this.writer=null;
        return result;
    }
    
    
}
