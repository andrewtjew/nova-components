package org.nova.html.objects.templates;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.nova.html.elements.Element;

public class Template extends Element 
{
    final Section[] sections;
    final ElementMap map;
    
    Template(Section[] sections)
    {
    	this.sections=sections;
    	this.map=new ElementMap();
    }
    
    public void insert(String key,Element element)
    {
        this.map.put(key, element);
    }

    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        for (Section section:this.sections)
        {
            section.write(outputStream, this.map);
        }
    }
    
}
