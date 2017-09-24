package org.nova.html.elements;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.nova.html.widgets.Text;

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
    protected ELEMENT attr(String name,Object value)
    {
        if (value!=null)
        {
            sb.append(' ').append(name).append("=\"").append(value).append('"');
        }
        return (ELEMENT) this;
    }
    @SuppressWarnings("unchecked")
    protected ELEMENT attr(String name)
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
