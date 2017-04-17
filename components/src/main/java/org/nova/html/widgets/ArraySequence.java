package org.nova.html.widgets;

import java.io.OutputStream;

import org.nova.html.elements.Element;

public class ArraySequence extends Element
{
    final Element[] elements;
    public ArraySequence(int size)
    {
        this.elements=new Element[size];
    }
    public void set(int index,Element element)
    {
        this.elements[index]=element;
    }
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        for (Element element:this.elements)
        {
            if (element!=null)
            {
                element.write(outputStream);
            }
        }
    }

    @Override
    public String toString()
    {
        StringBuilder sb=new StringBuilder();
        for (Element element:this.elements)
        {
            if (element!=null)
            {
                sb.append(element);
            }
        }
        return sb.toString();      
    }
    
}
