package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Button;
import org.nova.html.bootstrap4.ButtonDropdown;
import org.nova.html.bootstrap4.ButtonGroup;
import org.nova.html.bootstrap4.DropdownMenu;
import org.nova.html.bootstrap4.ToggleDropdownButton;
import org.nova.html.bootstrap4.classes.StyleColor;

public class Dropdown extends ButtonGroup
{
    final private ToggleDropdownButton toggler;
    final private DropdownMenu menu;
    
    public Dropdown(boolean buttonDropDown)
    {
        addClass("dropdown");
        this.toggler=returnAddInner(new ToggleDropdownButton());
        this.menu=returnAddInner(new DropdownMenu(this.toggler()));
    }


    public ToggleDropdownButton toggler()
    {
        return toggler;
    }

    public DropdownMenu menu()
    {
        return menu;
    }
    
    public Dropdown color(StyleColor color)
    {
        this.toggler().color(color);
        return this;
    }
    
    
    
}
