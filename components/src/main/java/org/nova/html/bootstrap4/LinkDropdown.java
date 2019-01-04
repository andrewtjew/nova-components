package org.nova.html.bootstrap4;

import org.nova.html.elements.Composer;
import org.nova.html.elements.Element;
import org.nova.html.tags.a;
import org.nova.html.tags.div;

public class LinkDropdown extends ButtonComponent<LinkDropdown>
{
    final private Item menu;
    private boolean split;
    private boolean usehref;
    private boolean menuRight;
    
    public LinkDropdown(String label)
    {
        this(label,null);
    }
    public LinkDropdown(String label,String href)
    {
        super("a");
        this.usehref=href!=null;
        attr("href",href);
        addInner(label);
        this.menu=new Item();
    }

    public Item menu()
    {
        return this.menu;
    }
    
    public LinkDropdown addToMenu(String label,String href)
    {
//        this.menu.addInner(new LinkButton(href).addInner(label).outline().color(StyleColor.primary));
        this.menu.addInner(new a().addClass("dropdown-item").href(href).addInner(label));
        return this;
    }
    public LinkDropdown addToMenu(Element element)
    {
        this.menu.addInner(element);
        return this;
    }

    public LinkDropdown addDivider()
    {
        this.menu.addInner(new div().addClass("dropdown-divider"));
        return this;
    }
    public LinkDropdown menuRight(boolean value)
    {
        this.menuRight=value;
        return this;
    }
    
    public LinkDropdown split()
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
            this.menu.addClass("dropdown-menu-right");
        }
        
        if (this.split)
        {
            div group=new div().addClass("btn-group");
            group.addInner(new Element()
            {
                @Override
                public void compose(Composer composer) throws Throwable
                {
                    if (usehref==false)
                    {
                        attr("data-toggle","dropdown");
                    }
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
            attr("data-toggle","dropdown");
            addClass("dropdown-toggle");
            super.compose(composer);
            this.menu.compose(composer);
        }
        
    }

}
