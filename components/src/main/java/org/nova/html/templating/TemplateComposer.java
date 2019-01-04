package org.nova.html.templating;

import java.util.ArrayList;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.ext.Text;

public class TemplateComposer extends Composer
{
    final ArrayList<Element> elements;
    private StringBuilder sb;
    
    TemplateComposer()
    {
        this.elements=new ArrayList<>();
        this.sb=new StringBuilder();
    }
    
    
    @Override
    public StringBuilder getStringBuilder()
    {
        return this.sb;
    }
    
    
    void mark(InsertMarker marker)
    {
        if (this.sb.length()>0)
        {
            this.elements.add(new Text(this.sb.toString()));
            this.sb=new StringBuilder();
        }
        this.elements.add(marker);
    }
    

}
