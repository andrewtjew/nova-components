package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.Fixed;
import org.nova.html.bootstrap4.classes.Placement;
import org.nova.html.bootstrap4.classes.Responsiveness;
import org.nova.html.bootstrap4.classes.ThemeColor;
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
    private ThemeColor backgroundColor;
    private ThemeColor color;
    private Fixed fixed;
    
    public Navbar()
    {
        this.nav=new nav();
    }
    
    public Navbar responsiveness(Responsiveness value)
    {
        this.responsiveness=value;
        return this;
    }
    
    public Navbar color(ThemeColor value)
    {
        this.color=value;
        return this;
    }
    
    public Navbar fixed(Fixed value)
    {
        this.fixed=value;
        return this;
    }
    
    public Navbar backgroundColor(ThemeColor value)
    {
        this.backgroundColor=value;
        return this;
    }
    
    public Navbar add(NavbarCollapse element)
    {
        this.nav.addInner(element);
        return this;
    }

    public Navbar add(String text)
    {
        span span=new span().class_("navbar-text").addInner(text);
        this.nav.addInner(span);
        return this;
    }

    public Navbar add(Brand element)
    {
        this.nav.addInner(element);
        return this;
    }

    public Navbar add(NavbarToggler element)
    {
        this.nav.addInner(element);
        return this;
    }
    
    public Navbar add(NavbarList element)
    {
        this.nav.addInner(element);
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder navClass=new ClassBuilder("navbar");
        navClass.addIf(this.responsiveness!=null,"navbar-expand",this.responsiveness);
        navClass.addIf(this.backgroundColor!=null,"bg",this.backgroundColor);
        navClass.addIf(this.color!=null,"navbar",this.color);
        navClass.addIf(this.fixed!=null,"fixed",this.fixed);
        navClass.applyTo(this.nav);

        div top=new div().addInner(this.nav);
        composer.render(top);
    }


    
}
