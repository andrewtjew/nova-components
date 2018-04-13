package org.nova.html.bootstrap4.classes;

public enum Placement
{
    ml_auto("ml-auto"), 
    mr_auto("mr-auto"), 
    mx_auto("mx-auto"), 
    abs_center_x("abs_center_x"), 
    mr("mr"), 
    ml("ml"), 
    ;
    private String value;

    Placement(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
