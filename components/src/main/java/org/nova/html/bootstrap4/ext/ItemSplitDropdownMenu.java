package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Button;
import org.nova.html.bootstrap4.ButtonDropdown;
import org.nova.html.bootstrap4.ButtonGroup;
import org.nova.html.bootstrap4.DropdownMenu;
import org.nova.html.bootstrap4.Item;
import org.nova.html.bootstrap4.ToggleDropdownButton;
import org.nova.html.bootstrap4.classes.StyleColor;

public class ItemSplitDropdownMenu extends ButtonGroup
{
    final private Item item;
    final private ToggleDropdownButton toggler;
    final private DropdownMenu menu;
    
    public ItemSplitDropdownMenu()
    {
        addClass("dropdown");
        this.item=returnAddInner(new Item());
        this.toggler=returnAddInner(new ToggleDropdownButton());
        this.menu=returnAddInner(new DropdownMenu(this.toggler()));
    }
    

    public Item item()
    {
        return item;
    }

    public ToggleDropdownButton toggler()
    {
        return toggler;
    }

    public DropdownMenu menu()
    {
        return menu;
    }
    
    public ItemSplitDropdownMenu color(StyleColor color)
    {
        this.item().bg(color);
        this.toggler().color(color);
        return this;
    }
    
    
    
}
