package org.nova.html.bootstrap4.classes;

public enum Flex
{
    row("row"), 
    grow("grow"), 
    row_reverse("row-reverse"), 
    column("column"), 
    column_reverse("column-reverse"), 
    ;
    private String value;

    Flex(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
