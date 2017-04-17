package org.nova.html.widgets;

import java.io.OutputStream;
import java.util.ArrayList;

import org.nova.html.elements.Element;

public class ListSequence extends Element
{
    ArrayList<Element> elements;
    
    public ListSequence()
    {
        this.elements=new ArrayList<>();
    }
    
    public ListSequence add(Element element)
    {
        if (element!=null)
        {
            this.elements.add(element);
        }
        return this;
    }
    public ListSequence add(String text)
    {
        this.elements.add(new Text(text));
        return this;
    }
    
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        for (Element element:this.elements)
        {
            element.write(outputStream);
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        for (Element element:this.elements)
        {
            sb.append(element);
        }
        return sb.toString();
    }

}
