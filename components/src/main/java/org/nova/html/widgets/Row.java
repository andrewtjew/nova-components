package org.nova.html.widgets;

import java.io.OutputStream;

import org.nova.html.elements.Element;
import org.nova.html.tags.td;
import org.nova.html.tags.tr;

public class Row extends Element
{
    final ListSequence sequence;
    final tr tr;
    public Row()
    {
        this.sequence=new ListSequence();
        this.tr=new tr().inner(this.sequence);
    }
    
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        this.tr.write(outputStream);
    }

    @Override
    public String toString()
    {
        return this.tr.toString();
    }
    public Row td(td data)
    {
        this.sequence.add(data);
        return this;
    }
    public Row td(Object object)
    {
        this.sequence.add(new td().inner(object));
        return this;
    }
}
