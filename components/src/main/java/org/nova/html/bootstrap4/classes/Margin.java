package org.nova.html.bootstrap4.classes;

public enum Margin
{
    ml_auto("ml-auto"), 
    mr_auto("mr-auto"), 
    ;
    private String value;

    Margin(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
