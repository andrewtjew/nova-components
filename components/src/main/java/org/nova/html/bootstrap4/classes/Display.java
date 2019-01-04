package org.nova.html.bootstrap4.classes;

public enum Display
{
    none("none"), 
    inline("inline"), 
    inline_block("inline-block"), 
    block("block"), 
    table("table"), 
    table_cell("table-cell"), 
    table_row("table-row"), 
    flex("flex"), 
    inline_flex("inline-flex"), 
    ;
    private String value;

    Display(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}
