package org.nova.html.elements;

import org.nova.html.attributes.Style;
import org.nova.html.enums.dir;
import org.nova.html.enums.dropzone;

public class GlobalTagElement<ELEMENT extends TagElement<ELEMENT>> extends TagElement<ELEMENT>
{
    public GlobalTagElement(String tag)
    {
        super(tag);
    }
    public GlobalTagElement(String tag,boolean noEndTag)
    {
        super(tag,noEndTag);
    }

    public ELEMENT accesskey(String value)
    {
        return attr("accesskey",value);
    }

    public ELEMENT contenteditable(boolean value)
    {
        return attr("contenteditable",value);
    }

    public ELEMENT contextmenu(String value)
    {
        return attr("contextmenu",value);
    }
    
    public ELEMENT data(String attribute,String value)
    {
        return attr("data-"+attribute,value);
    }
    public ELEMENT dir(dir dir)
    {
        return attr("dir",dir.toString());
    }
    public ELEMENT dra(String value)
    {
        return attr("contextmenu",value);
    }
    public ELEMENT draggable(boolean value)
    {
        return attr("draggable",value);
    }
    public ELEMENT dropzone(dropzone dropzone)
    {
        return attr("dropzone",dropzone.toString());
    }
    public ELEMENT hidden(boolean value)
    {
        if (value==true)
        {
            return attr("hidden");
        }
        return (ELEMENT)this;
    }
    public ELEMENT hidden()
    {
        return attr("hidden");
    }
    public ELEMENT lang(String value)
    {
        return attr("lang",value);
    }
    public ELEMENT spellcheck(boolean value)
    {
        return attr("spellcheck",value);
    }
    public ELEMENT style(String value)
    {
        return attr("style",value);
    }
    public ELEMENT style(Style value)
    {
        return attr("style",value.toString());
    }
    public ELEMENT tabindex(int value)
    {
        return attr("tabindex",value);
    }
    public ELEMENT title(String value)
    {
        return attr("title",value);
    }
    public ELEMENT translate(boolean value)
    {
        return attr("translate",value);
    }
}
