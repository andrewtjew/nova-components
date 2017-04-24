package org.nova.html.elements;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.nova.html.widgets.Text;

public class TagElement<ELEMENT extends TagElement<ELEMENT>> extends InnerElement<ELEMENT>
{
    final private StringBuilder sb;
    final private String tag;
    private Element inner=null;
    private ArrayList<Element> inners=null; 
    
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
        super.write(outputStream);
        outputStream.write(END_OPEN);
        outputStream.write(this.tag.getBytes(StandardCharsets.UTF_8));
        outputStream.write(CLOSE);
    }
    @Override
    public String toString()
    {
        StringBuilder out=new StringBuilder(this.sb.toString());
        out.append('>');
        out.append(super.toString());
        out.append("</").append(this.tag).append('>');
        return out.toString();
    }
}
