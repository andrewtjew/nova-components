package org.nova.html.elements;

public class TagElement<ELEMENT extends TagElement<ELEMENT>> extends InnerElement<ELEMENT>
{
    private String id;
    final private StringBuilder sb;
    final private String tag;
    final private boolean noEndTag;
    final private StringBuilder classBuilder;
    
    public TagElement(String tag,boolean noEndTag)
    {
        this.tag=tag;
        this.sb=new StringBuilder();
        this.noEndTag=noEndTag;
        this.classBuilder=new StringBuilder();
    }
    public TagElement(String tag)
    {
        this(tag,false);
    }
    public ELEMENT addClass(String class_)
    {
        if (class_!=null)
        {
            if (this.classBuilder.length()>0)
            {
                this.classBuilder.append(' ');
            }
            this.classBuilder.append(class_);
        }
        return (ELEMENT) this;
    }
    public ELEMENT id(String value)
    {
        if (value!=null)
        {
            this.id=value;
        }
        else
        {
//            id();
        }
        return (ELEMENT) this;
    }
    /*
    public ELEMENT autoid()
    {
        this.id="_"+this.hashCode();
        return (ELEMENT) this;
    }
    */
    public String id()
    {
        if (this.id==null)
        {
            this.id="_"+this.hashCode();
            return this.id;
        }
        return this.id;
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

    public String class_()
    {
        return this.classBuilder.toString();
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        if (this.classBuilder.length()>0)
        {
            attr("class",this.classBuilder.toString());
        }
        attr("id",this.id);
        
        StringBuilder composerStringBuilder=composer.getStringBuilder();
        composerStringBuilder.append('<').append(this.tag);
        composerStringBuilder.append(this.sb.toString());
        composerStringBuilder.append('>');
        if (this.noEndTag==false)
        {
            super.compose(composer);
            composerStringBuilder=composer.getStringBuilder();
            composerStringBuilder.append("</").append(this.tag).append('>');
        }
    }
}
