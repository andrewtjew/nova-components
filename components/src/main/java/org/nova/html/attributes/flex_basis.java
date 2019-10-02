package org.nova.html.attributes;

public enum flex_basis
{
    auto("auto"),
    initial("initial"),
    inherit("inherit")
    ;
    final String value;
    flex_basis(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
