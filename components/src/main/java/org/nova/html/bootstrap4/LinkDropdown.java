package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.Size;
import org.nova.html.bootstrap4.classes.ThemeColor;
import org.nova.html.bootstrap4.classes.ButtonState;
import org.nova.html.bootstrap4.classes.DropdownPosition;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;
import org.nova.html.widgets.HtmlUtils;

public class LinkDropdown extends Element
{
    final private div menu;
    private boolean outline=false;
    private ThemeColor color;
    private ThemeColor textColor;
    private Size buttonSize;
    final private String label;
    private boolean split;
    private boolean menuRight;
    
    public LinkDropdown(String label)
    {
        this.label=label;
        this.menu=new div();
    }
    
    public LinkDropdown add(String label,String href)
    {
        this.menu.addInner(new a().class_("dropdown-item").href(href).addInner(label));
        return this;
    }
    public LinkDropdown add(Element element)
    {
        this.menu.addInner(element);
        return this;
    }

    public LinkDropdown addDivider()
    {
        this.menu.addInner(new div().class_("dropdown-divider"));
        return this;
    }
    public LinkDropdown size(Size value)
    {
        this.buttonSize=value;
        return this;
    }
    
    
    public LinkDropdown color(ThemeColor value)
    {
        this.color=value;
        return this;
    }
    public LinkDropdown textColor(ThemeColor value)
    {
        this.textColor=value;
        return this;
    }
    
    public LinkDropdown menuRight(boolean value)
    {
        this.menuRight=value;
        return this;
    }
    
    public LinkDropdown outline(boolean value)
    {
        this.outline=value;
        return this;
    }

    public LinkDropdown split(boolean value)
    {
        this.split=value;
        return this;
    }

    
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        ClassBuilder cb=new ClassBuilder("btn");
        cb.add("btn");
        cb.addFragmentsIf(this.outline,"outline");
        cb.addFragments(this.color);
        cb.addIf(this.buttonSize!=null,"btn",this.buttonSize);
        cb.addIf(this.textColor!=null, "text",this.textColor);
        if (split)
        {
            div div=new div().class_("btn-group");
            
            a button=new a().addInner(label).attr("data-toggle","dropdown");//.attr("aria-haspopup",true).attr("aria-expanded",false);
            cb.applyTo(button);

            button_button splitter=new button_button().attr("data-toggle","dropdown").attr("aria-haspopup",true).attr("aria-expanded",false);
            cb.add("dropdown-toggle");
            cb.add("dropdown-toggle-split");
            cb.applyTo(splitter);

            cb=new ClassBuilder("dropdown-menu");
            cb.addIf(this.menuRight, "dropdown-menu-right");
            cb.applyTo(this.menu);
            
            div.addInner(button);
            div.addInner(splitter);
            div.addInner(this.menu);
            composer.render(div);
        }
        else
        {
            button_button button=new button_button().addInner(label).attr("data-toggle","dropdown");//.attr("aria-haspopup",true).attr("aria-expanded",false);
            cb.add("dropdown-toggle");
            cb.applyTo(button);
            composer.render(button);
            cb=new ClassBuilder("dropdown-menu");
            cb.addIf(this.menuRight, "dropdown-menu-right");
            cb.applyTo(this.menu);
            composer.render(this.menu);
        }
    }

}
