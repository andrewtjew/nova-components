package org.nova.html.elements;

public class ButtonElement<ELEMENT extends ButtonElement<ELEMENT>> extends GlobalEventTagElement<ELEMENT>
{
    protected ButtonElement()
    {
        super("button");
    }

    public ELEMENT autofocus()
    {
        return attr("autofocus");
    }
    public ELEMENT disabled()
    {
        return attr("disabled");
    }
    @SuppressWarnings("unchecked")
    public ELEMENT disabled(boolean disabled)
    {
        if (disabled)
        {
            attr("disabled");
        }
        return (ELEMENT)this;
    }
    public ELEMENT form(String form_id)
    {
        return attr("form",form_id);
    }
    public ELEMENT name(String text)
    {
        return attr("name",text);
    }
    public ELEMENT value(String text)
    {
        return attr("value",text);
    }

}
