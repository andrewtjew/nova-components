package org.nova.html.bootstrap4.classes;

public enum Font
{
    italic("italic"), 
    weight_bold("weight-bold"), 
    weight_light("weight-light"), 
    weight_normal("weight-normal"), 
    column_reverse("column-reverse"), 
    ;
    private String value;

    Font(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
