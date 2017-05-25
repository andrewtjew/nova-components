package org.nova.html.elements;

import org.nova.html.enums.dir;
import org.nova.html.enums.dropzone;

public class GlobalTagElement<ELEMENT extends TagElement<ELEMENT>> extends TagElement<ELEMENT>
{

    GlobalTagElement(String tag)
    {
        super(tag);
    }

    public ELEMENT accesskey(String value)
    {
        return attr("accesskey",value);
    }

    public ELEMENT class_(String value)
    {
        return attr("class",value);
    }

    public ELEMENT contenteditable(boolean value)
    {
        return attr("contenteditable",value);
    }

    public ELEMENT contextmenu(String value)
    {
        return attr("contextmenu",value);
    }
    
    public ELEMENT datea(String custom,String value)
    {
        return attr("data-"+custom,value);
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
    public ELEMENT id(String value)
    {
        return attr("id",value);
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
