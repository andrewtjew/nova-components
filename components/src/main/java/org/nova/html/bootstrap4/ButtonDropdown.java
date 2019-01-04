package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;
import org.nova.html.tags.div;

public class ButtonDropdown extends ButtonComponent<ButtonDropdown>
{
    final private Item menu;
    private boolean split;
    private boolean menuRight;
    
    public ButtonDropdown(String label)
    {
        super("button");
        attr("type","button");
        attr("data-toggle","dropdown");
        addInner(label);
        this.menu=new Item();
    }
    
    public ButtonDropdown addToMenu(String label,String href)
    {
        this.menu.addInner(new a().addClass("dropdown-item").href(href).addInner(label));
        return this;
    }
    public ButtonDropdown addToMenu(Element element)
    {
        this.menu.addInner(element);
        return this;
    }

    public ButtonDropdown addDivider()
    {
        this.menu.addInner(new div().addClass("dropdown-divider"));
        return this;
    }
    public ButtonDropdown menuRight(boolean value)
    {
        this.menuRight=value;
        return this;
    }

    public ButtonDropdown split()
    {
        this.split=true;
        return this;
    }
    
    @Override
    public void compose(Composer composer) throws Throwable
    {
        this.menu.addClass("dropdown-menu");
        if (this.menuRight)
        {
            this.menu.addClass(this.menuRight, "dropdown-menu-right");
        }
        
        if (this.split)
        {
            div group=new div().addClass("btn-group");
            group.addInner(new Element()
            {
                @Override
                public void compose(Composer composer) throws Throwable
                {
                    attr("data-toggle","dropdown");
                    composeThis(composer);
                    Button splitter=new Button().attr("data-toggle","dropdown").attr("aria-haspopup",true).attr("aria-expanded",false);
                    splitter.addClass(class_());
                    splitter.addClass("dropdown-toggle");
                    splitter.addClass("dropdown-toggle-split");
                    splitter.compose(composer);
                    menu.compose(composer);
                }
            });
            group.compose(composer);
        }
        else
        {
            addClass("dropdown-toggle");
            super.compose(composer);
            this.menu.compose(composer);
        }
        
    }

}
