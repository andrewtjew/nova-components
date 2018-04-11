package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;
import org.nova.html.tags.span;
import org.nova.html.widgets.HtmlUtils;

public class Toggler extends Element
{
    final private div div;
    final private button_button button;
    public Toggler(Element element)
    {
        this.button=new button_button();
        String id=HtmlUtils.generateId(button);
        button.class_("navbar-toggler").data("toggle", "collapse").data("target", "#"+id);
        button.addInner(new span().class_("navbar-toggler-icon"));
        this.div=new div().class_("collapse navbar-collapse").id(id);
        this.div.addInner(element);
    }
    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.button);
        composer.render(this.div);
    }

}
