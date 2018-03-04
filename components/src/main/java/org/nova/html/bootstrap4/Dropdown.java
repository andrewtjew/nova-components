package org.nova.html.bootstrap4;

import org.nova.html.tags.li;
import org.nova.html.bootstrap4.classes.NavbarState;
import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.properties.Style;
import org.nova.html.properties.float_;
import org.nova.html.properties.list_style;
import org.nova.html.properties.position;
import org.nova.html.tags.a;
import org.nova.html.tags.button_button;
import org.nova.html.tags.div;

public class Dropdown extends Element
{
    final private div div;
    final private div dropDownDiv;
    public Dropdown(String label)
    {
        this.div=new div().class_("dropdown");
        this.div.returnAddInner(new button_button()).class_("btn btn-primary dropdown-toggle").attr("data-toggle","dropdown").addInner(label);
        this.dropDownDiv=this.div.returnAddInner(new div()).class_("dropdown-menu");
    }
    
    public Dropdown add(String label,String href)
    {
        return add(new a().addInner(label).href(href));
    }

    public Dropdown addDivider()
    {
        this.dropDownDiv.returnAddInner((new div()).class_("dropdown-divider"));
        return this;
    }
    
    public Dropdown add(a option)
    {
        this.dropDownDiv.addInner(option.class_("dropdown-item"));
        return this;
    }

    @Override
    public void compose(Composer composer) throws Throwable
    {
        composer.render(this.div);
        
    }
}
