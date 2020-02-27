package org.nova.html.bootstrap4.ext;

import org.nova.html.bootstrap4.Button;
import org.nova.html.bootstrap4.ButtonDropdown;
import org.nova.html.bootstrap4.ButtonGroup;
import org.nova.html.bootstrap4.DropdownMenu;
import org.nova.html.bootstrap4.ToggleDropdownButton;
import org.nova.html.bootstrap4.classes.StyleColor;
import org.nova.html.ext.Content;

public class ButtonDropdownMenu extends ButtonGroup
{
    final private Button button;
    final private DropdownMenu menu;
    
    public ButtonDropdownMenu()
    {
        this.button=returnAddInner(new Button());
        this.menu=returnAddInner(new DropdownMenu(this.button()));
//        this.button.data("toggle","dropdown");
        this.menu.reference_parent(this.button);
        
    }

    public Button button()
    {
        return button;
    }


    public DropdownMenu menu()
    {
        return menu;
    }
    
    
}
