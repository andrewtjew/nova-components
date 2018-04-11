package org.nova.html.bootstrap4;

import org.nova.html.bootstrap4.classes.ButtonSize;
import org.nova.html.bootstrap4.classes.ButtonState;
import org.nova.html.bootstrap4.classes.ButtonStyle;
import org.nova.html.bootstrap4.classes.DropdownPosition;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;
import org.nova.html.widgets.HtmlUtils;

public class ButtonDropdown extends Element
{
    final private div menu;
    private boolean outline=false;
    private ButtonStyle buttonStyle;
    private ButtonSize buttonSize;
    final private String label;
//    private DropdownPosition dropdownPosition;
    private boolean split;
    
    public ButtonDropdown(String label)
    {
        this.label=label;
        this.menu=new div().class_("dropdown-menu");//.attr("aria-labelledby",id);
    }
    
    public ButtonDropdown add(String label,String href)
    {
        this.menu.addInner(new a().class_("dropdown-item").href(href).addInner(label));
        return this;
    }
    public ButtonDropdown add(Element element)
    {
        this.menu.addInner(element);
        return this;
    }
/*
    public ButtonDropdown dropdownPosition(DropdownPosition value)
    {
        this.dropdownPosition=value;
        return this;
    }
  */  
    public ButtonDropdown addDivider()
    {
        this.menu.addInner(new div().class_("dropdown-divider"));
        return this;
    }
    public ButtonDropdown buttonSize(ButtonSize value)
    {
        this.buttonSize=value;
        return this;
    }
    public ButtonDropdown buttonStyle(ButtonStyle value)
    {
        this.buttonStyle=value;
        return this;
    }
    
    public ButtonDropdown outline(boolean value)
    {
        this.outline=value;
        return this;
    }

    public ButtonDropdown split(boolean value)
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
        cb.addFragments(this.buttonStyle);
        cb.addIf(this.buttonSize!=null,"btn",this.buttonSize);

        if (split)
        {
            button_button button=new button_button().addInner(label).attr("data-toggle","dropdown");//.attr("aria-haspopup",true).attr("aria-expanded",false);
            cb.applyTo(button);
            button_button splitter=new button_button().attr("data-toggle","dropdown");//.attr("aria-haspopup",true).attr("aria-expanded",false);
            cb.add("dropdown-toggle");
            cb.add("dropdown-toggle-split");
            cb.applyTo(splitter);
            composer.render(button);
            composer.render(splitter);
        }
        else
        {
            button_button button=new button_button().addInner(label).attr("data-toggle","dropdown");//.attr("aria-haspopup",true).attr("aria-expanded",false);
            cb.add("dropdown-toggle");
            cb.applyTo(button);
            composer.render(button);
        }
        
        composer.render(this.menu);
    }

}
