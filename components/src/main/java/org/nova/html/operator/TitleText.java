package org.nova.html.operator;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;
import org.nova.html.elements.TagElement;
import org.nova.html.tags.em;
import org.nova.html.tags.span;
import org.nova.html.tags.var;

public class TitleText extends Element
{
    final private String text;
    final private String title;

    public TitleText(String text,int maxDisplayLength)
    {
        if (text!=null)
        {
            if (text.length()<=maxDisplayLength)
            {
                this.text=text;
                this.title=null;
            }
            else
            {
                this.title=text;
                this.text=text.substring(0, maxDisplayLength)+"...";
            }
        }
        else
        {
            this.text=null;
            this.title=null;
        }
    }
    public TitleText(String title,String text)
    {
        this.title=title;
        this.text=text;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        if (text!=null)
        {
            if (this.title==null)
            {
                span element=new span();
                element.addInner(text);
                element.compose(composer);
            }
            else
            {
                span element=new span();
                element.title(title.replace("\"", "&quot;")).addInner(text);
                element.compose(composer);
            }
        }
    }
}
