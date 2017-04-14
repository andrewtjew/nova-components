package org.nova.html.objects;

import java.io.OutputStream;
import java.util.ArrayList;

import org.nova.html.elements.Element;

public class Sequence extends Element
{
    ArrayList<Element> elements;
    
    public Sequence()
    {
        this.elements=new ArrayList<>();
    }
    
    public void add(Element element)
    {
        this.elements.add(element);
    }
    
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        for (Element element:this.elements)
        {
            element.write(outputStream);
        }
    }

}
