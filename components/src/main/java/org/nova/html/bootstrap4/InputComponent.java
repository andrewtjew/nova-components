package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.TagElement;

public abstract class InputComponent<ELEMENT extends InputComponent<ELEMENT>> extends StyleComponent<ELEMENT> 
{
    private String name; 
    protected InputComponent(String componentClass,String type)
    {
        super("input",componentClass);
        attr("type",type);
    }
    protected InputComponent(String tag,String componentClass,String type)
    {
        super(tag,componentClass);
        attr("type",type);
    }

    public ELEMENT autofocus()
    {
        return attr("autofocus");
    }
    public ELEMENT autofocus(boolean autofocus)
    {
        if (autofocus)
        {
            return attr("autofocus");
        }
        return (ELEMENT)this;
    }
    public ELEMENT disabled()
    {
        return attr("disabled");
    }
    public ELEMENT disabled(boolean disabled)
    {
        if (disabled)
        {
            return attr("disabled");
        }
        return (ELEMENT)this;
    }
    public ELEMENT form(String form_id)
    {
        return attr("form",form_id);
    }
    public ELEMENT form(TagElement<?> element)
    {
        return attr("form",element.id());
    }
    
    public ELEMENT readonly()
    {
        return attr("readonly");
    }
    public ELEMENT readonly(boolean readonly)
    {
        if (readonly)
        {
            attr("readonly");
        }
        return (ELEMENT)this;
    }
    public ELEMENT name(String name)
    {
        this.name=name;
        return (ELEMENT)this;
    }
    public String name()
    {
        if (this.name==null)
        {
            this.name="_"+this.hashCode();
            return this.name;
        }
        return this.name;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        attr("name",this.name);
        super.compose(composer);
    }
    
}
