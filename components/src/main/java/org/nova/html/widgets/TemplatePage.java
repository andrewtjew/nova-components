package org.nova.html.widgets;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.elements.InnerElement;
import org.nova.html.elements.StringComposer;
import org.nova.html.tags.div;
import org.nova.html.templates.Template;

public abstract class TemplatePage extends Element
{
    final private Template template;
    final private Head head;
    final private InnerElement<?> content;
    
    protected abstract Template getStaticTemplate();
    
    public TemplatePage()
    {
        this.content=new div();
        this.head=new Head(); 
        this.template=getStaticTemplate().copy();
    }
    public Template getTemplate()
    {
        return this.template;
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
