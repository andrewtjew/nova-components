package org.nova.html.bootstrap4.classes;

public enum DropdownPosition
{
    left("left"), 
    up("up"), 
    down("down"), 
    right("right"),
    ;
    
    private String value;

    DropdownPosition(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
