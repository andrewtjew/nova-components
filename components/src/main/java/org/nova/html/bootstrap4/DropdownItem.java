package org.nova.html.bootstrap4;


public class DropdownItem extends StyleComponent<DropdownItem>
{

    public DropdownItem(String label,String URL)
    {
        super("a", "dropdown-item");
        attr("href",URL);
        addInner(label);
    }
    
}
