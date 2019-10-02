package org.nova.html.attributes;

public enum flex_direction
{
    row("row"),
    row_reverse("row-reverse"),
    column("column"),
    column_reverse("column-reverse"),
        ;
    final String value;
    flex_direction(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
