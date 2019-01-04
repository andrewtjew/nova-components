package org.nova.html.elements;

import java.util.ArrayList;

import org.nova.html.ext.Text;

public class InnerElement<ELEMENT extends InnerElement<ELEMENT>> extends Element
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
    public ELEMENT addInner(Object object)
    {
        if (object!=null)
        {
            if (object instanceof Element)
            {
                return addInner((Element)object);
            }
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
    public void compose(Composer builder) throws Throwable
    {
        if (this.inners!=null)
        {
            for (Element inner:this.inners)
            {
                inner.compose(builder);
            }
        }
        else if (inner!=null)
        {
            inner.compose(builder);
        }
    }
    @Override
    public String toString()
    {
        try
        {
            StringComposer composer=new StringComposer();
            compose(composer);
            return composer.getStringBuilder().toString();
        }
        catch(Throwable t)
        {
            throw new RuntimeException(t);
        }
   }
}