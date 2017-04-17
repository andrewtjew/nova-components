package org.nova.html.elements;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import org.nova.html.widgets.Text;

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
    public ELEMENT inner(String text)
    {
        if (text!=null)
        {
            this.inner=new Text(text);
        }
        return (ELEMENT)this;
    }
    public ELEMENT inner(Object object)
    {
        if (object!=null)
        {
            this.inner=new Text(object.toString());
        }
        return (ELEMENT)this;
    }
    @SuppressWarnings("unchecked")
    protected ELEMENT attr(String name,Object value)
    {
        if (value!=null)
        {
            sb.append(' ').append(name).append("=\"").append(value).append('"');
        }
        return (ELEMENT) this;
    }
    protected ELEMENT attr(String name)
    {
        sb.append(' ').append(name);
        return (ELEMENT) this;
    }
    static byte[] CLOSE=new byte[]{'>'};
    static byte[] END_OPEN=new byte[]{'<','/'};
    static byte[] INLINE_CLOSE=new byte[]{' ','/','>'};
    
    @Override
    public void write(OutputStream outputStream) throws Throwable
    {
        outputStream.write(this.sb.toString().getBytes(StandardCharsets.UTF_8));
        outputStream.write(CLOSE);
        if (inner!=null)
        {
            inner.write(outputStream);
        }
        
        outputStream.write(END_OPEN);
        outputStream.write(this.tag.getBytes(StandardCharsets.UTF_8));
        outputStream.write(CLOSE);
    }
    @Override
    public String toString()
    {
        if (inner==null)
        {
            return this.sb+"></"+this.tag+">";
        }
        return this.sb+">"+inner.toString()+"</"+this.tag+">";
    }
}