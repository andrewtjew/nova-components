package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;
import org.nova.html.tags.span;
import org.nova.html.widgets.HtmlUtils;

public class NavbarToggler extends Element
{
    final private button_button button;
    final private String targetClass_; 
    public NavbarToggler()
    {
        this.button=new button_button();
        this.targetClass_=HtmlUtils.generateId(button);
        button.class_("navbar-toggler").data("toggle", "collapse").data("target", "."+targetClass_);
        button.addInner(new span().class_("navbar-toggler-icon"));
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.button);
    }

    String targetClass()
    {
        return this.targetClass_;
    }
}
