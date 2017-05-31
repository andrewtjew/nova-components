package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;
import org.nova.html.elements.StringComposer;
import org.nova.html.tags.div;
import org.nova.html.widgets.templates.Template;

public abstract class BasicApplicationPage extends Element
{
    final private Template template;
    final private Head head;
    final private InnerElement<?> content;
    
    protected abstract Template getTemplate();
    
    public BasicApplicationPage(Element title)
    {
        this.template=getTemplate().copy();
        this.content=new div();
        this.head=new Head(); 
        template.fill("head", head);
        template.fill("content", content);
        template.fill("title", title);
    }
    public Template getPageTemplate()
    {
        return this.template;
    }
    public BasicApplicationPage(String title)
    {
        this(new Text(title));
    }

    public InnerElement<?> content()
    {
        return this.content;
    }
    public Head head()
    {
        return this.head;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        this.template.compose(composer);
    }
}
