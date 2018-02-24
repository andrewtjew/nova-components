package org.nova.html.bootstrap4;

import org.nova.html.elements.GlobalEventTagElement;
import org.nova.html.elements.GlobalTagElement;
import org.nova.html.elements.InnerElement;
import org.nova.html.elements.TagElement;

public class ClassBuilder
{
    final StringBuilder sb;
    
    public ClassBuilder(Object class_,Object...fragments)
    {
        this.sb=new StringBuilder();
        add(class_,fragments);
    }
    public ClassBuilder()
    {
        this(null);
    }
    public ClassBuilder add(Object class_)
    {
        if (class_!=null)
        {
            if (sb.length()>0)
            {
                sb.append(' ');
            }
            sb.append(class_);
        }
        return this;
    }

    public ClassBuilder addIf(boolean test,Object class_)
    {
        if (test)
        {
            add(class_);
        }
        return this;
    }
    public ClassBuilder addIf(boolean test,Object class_,Object...fragments)
    {
        if (test)
        {
            add(class_,fragments);
        }
        return this;
    }
    public ClassBuilder add(Object class_,Object...fragments)
    {
        add(class_);
        addFragments(fragments);
        return this;
    }
    public ClassBuilder addFragments(Object...fragments)
    {
        for (Object fragment:fragments)
        {
            if (fragment!=null)
            {
                if (sb.length()>0)
                {
                    sb.append('-');
                }
                sb.append(fragment);
            }
        }
        return this;
    }
    
    public void addTo(GlobalTagElement<?> element)
    {
        element.class_(this.sb.toString());
    }
    
}
