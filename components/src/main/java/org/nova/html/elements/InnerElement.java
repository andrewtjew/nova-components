package org.nova.html.elements;

import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.nova.html.widgets.Text;

public abstract class InnerElement<ELEMENT extends InnerElement<ELEMENT>> extends Element
{
    protected Element inner=null;
    protected ArrayList<Element> inners=null; 

    @SuppressWarnings("unchecked")
    public ELEMENT addInner(Element element)
    {
        if (element==null)
        {
            return (ELEMENT)this;
        }
        if (this.inner==null)
        {
            this.inner=element;
            return (ELEMENT)this;
        }
        if (this.inners==null)
        {
            this.inners=new ArrayList<>(); 
            this.inners.add(this.inner);
        }
        this.inners.add(element);
        return (ELEMENT)this;
    }
    public ELEMENT setInner(Element element)
    {
        this.inners=null;
        this.inner=element;
        return (ELEMENT)this;
    }
    public ELEMENT addInners(Element...elements)
    {
        for (Element element:elements)
        {
            addInner(element);
        }
        return (ELEMENT)this;
    }
    public ELEMENT addInner(String text)
    {
        if (text!=null)
        {
            return addInner(new Text(text));
        }
        return (ELEMENT)this;
    }
    public ELEMENT addInner(Object object)
    {
        if (object!=null)
        {
            return addInner(new Text(object.toString()));
        }
        return (ELEMENT)this;
    }
    public <RETURN extends Element> RETURN returnAddInner(RETURN element)
    {
        addInner(element);
        return element;
    }

    @Override
    public void build(Builder builder) throws Throwable
    {
        if (this.inners!=null)
        {
            for (Element inner:this.inners)
            {
                inner.build(builder);
            }
        }
        else if (inner!=null)
        {
            inner.build(builder);
        }
    }
    @Override
    public String toString()
    {
        if (this.inners!=null)
        {
            StringBuilder sb=new StringBuilder();
            {
                for (Element inner:this.inners)
                {
                    sb.append(inner.toString());
                }
            }
            return sb.toString();
        }
        else if (inner!=null)
        {
            return inner.toString();
        }
        return "";
    }
}