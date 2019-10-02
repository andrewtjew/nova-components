package org.nova.html.attributes;

public enum flex_wrap
{
    wrap("wrap"),
    nowrap("nowrap"),
    wrap_reverse("wrap-reverse"),
    inherit("inherit"),
    ;
    final String value;
    flex_wrap(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
