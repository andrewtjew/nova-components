package org.nova.html.elements;

public class TagElement<ELEMENT extends TagElement<ELEMENT>> extends InnerElement<ELEMENT>
{
    final private StringBuilder sb;
    final private String tag;
    
    public TagElement(String tag)
    {
        this.tag=tag;
        this.sb=new StringBuilder();
        this.sb.append("<"+tag);
    }
    @SuppressWarnings("unchecked")
    public ELEMENT attr(String name,Object value)
    {
        return attr(name,value,'"');
    }
    @SuppressWarnings("unchecked")
    public ELEMENT attr(String name,Object value,char bracketChar)
    {
        if (value!=null)
        {
            sb.append(' ').append(name).append("=").append(bracketChar).append(value).append(bracketChar);
        }
        return (ELEMENT) this;
    }

    @SuppressWarnings("unchecked")
    public ELEMENT attr(String name)
    {
        sb.append(' ').append(name);
        return (ELEMENT) this;
    }
    @Override
    public void compose(Composer composer) throws Throwable
    {
        StringBuilder composerStringBuilder=composer.getStringBuilder();
        composerStringBuilder.append(this.sb.toString());
        composerStringBuilder.append('>');
        super.compose(composer);
        composerStringBuilder=composer.getStringBuilder();
        composerStringBuilder.append("</").append(this.tag).append('>');
    }
}
