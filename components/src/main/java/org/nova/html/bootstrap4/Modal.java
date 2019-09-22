package org.nova.html.bootstrap4;

public class Modal extends StyleComponent<Modal>
{
    public Modal()
    {
        super("div","modal");
    }
    
    public Modal fade()
    {
        addClass("fade");
        return this;
    }

    public Modal backdrop(boolean value)
    {
        attr("data-backdrop",value);
        return this;
    }
    public Modal backdrop()
    {
        return backdrop(true);
    }
    public Modal backdrop_static()
    {
        attr("data-backdrop","static");
        return this;
    }
    public Modal keyboard(boolean value)
    {
        attr("data-keyboard",value);
        return this;
    }
    public Modal keyboard()
    {
        return keyboard(true);
    }

    public Modal show(boolean value)
    {
        attr("data-show",value);
        return this;
    }
    public Modal show()
    {
        return show(true);
    }
    public Modal focus(boolean value)
    {
        attr("data-focus",value);
        return this;
    }
    public Modal focus()
    {
        return focus(true);
    }
}
