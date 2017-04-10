package org.nova.html.elements;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

class TagElement<ELEMENT extends TagElement<ELEMENT>> extends Element
{
    final private StringBuilder sb;
    final private String tag;
    private Element inner=null;
    
    TagElement()
    {
        this.sb=null;
        this.tag=null;
    }
    TagElement(String tag)
    {
        this.tag=tag;
        this.sb=new StringBuilder();
        this.sb.append("<"+tag);
    }
    @SuppressWarnings("unchecked")
    public ELEMENT inner(Element element)
    {
        this.inner=element;
        return (ELEMENT)this;
    }
    @SuppressWarnings("unchecked")
    protected ELEMENT attr(String name,Object value)
    {
        sb.append(' ').append(name).append("=\"").append(value).append('"');
        return (ELEMENT) this;
    }
    protected ELEMENT attr(String name)
    {
        sb.append(' ').append(name);
        return (ELEMENT) this;
    }
    static byte[] CLOSE=new byte[]{'>'};
    static byte[] END_OPEN=new byte[]{'<','/'};
    
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        outputStream.write(this.sb.toString().getBytes(StandardCharsets.UTF_8));
        if (inner==null)
        {
            outputStream.write(CLOSE);
            return;
        }
        inner.write(outputStream);
        outputStream.write(END_OPEN);
        outputStream.write(this.tag.getBytes(StandardCharsets.UTF_8));
        outputStream.write(CLOSE);
    }
    @Override
    public String toString()
    {
        if (inner==null)
        {
            return this.sb.toString()+">";
        }
        return this.sb+">"+inner.toString()+"</"+this.tag+">";
    }
}
