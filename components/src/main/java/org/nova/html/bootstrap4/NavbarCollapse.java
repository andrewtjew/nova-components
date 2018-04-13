package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;
import org.nova.html.tags.span;
import org.nova.html.widgets.HtmlUtils;

public class NavbarCollapse extends Element
{
    final private div div;
    final private String class_;
    private Integer width;
    
    public NavbarCollapse(NavbarToggler toggler)
    {
        this.class_=toggler.targetClass();
        this.div=new div();
    }
    
    public NavbarCollapse add(Element element)
    {
        this.div.addInner(element);
        return this;
    }
    
    public NavbarCollapse width(Integer value)
    {
        this.width=value;
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder("collapse navbar-collapse");
        cb.addIf(this.width!=null, "w",this.width);
        cb.add(class_);
        cb.applyTo(this.div);
        composer.render(this.div);
    }

}
