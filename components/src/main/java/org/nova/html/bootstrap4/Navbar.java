package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.BackgroundColor;
import org.nova.html.bootstrap4.classes.Margin;
import org.nova.html.bootstrap4.classes.NavbarColor;
import org.nova.html.bootstrap4.classes.Responsiveness;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.nav;
import org.nova.html.tags.span;
import org.nova.html.widgets.HtmlUtils;
import org.nova.html.tags.a;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;

public class Navbar extends Element
{
    final private nav nav;
    private Responsiveness responsiveness;
    private BackgroundColor backgroundColor;
    private NavbarColor color;
    
    public Navbar()
    {
        this.nav=new nav();
    }
    
    public Navbar responsiveness(Responsiveness value)
    {
        this.responsiveness=value;
        return this;
    }
    
    public Navbar color(NavbarColor value)
    {
        this.color=value;
        return this;
    }
    
    public Navbar backgroundColor(BackgroundColor value)
    {
        this.backgroundColor=value;
        return this;
    }
    
    public Navbar add(Element element)
    {
        this.nav.addInner(element);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder navClass=new ClassBuilder("navbar");
        navClass.addIf(this.responsiveness!=null,"navbar-expand",this.responsiveness);
        navClass.add(this.backgroundColor);
        navClass.add(this.color);
        navClass.applyTo(this.nav);

        div top=new div().addInner(this.nav);
        composer.render(top);
    }


    
}
