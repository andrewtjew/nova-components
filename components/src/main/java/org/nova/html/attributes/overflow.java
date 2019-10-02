package org.nova.html.attributes;

public enum overflow
{
    overflow("overflow"),
    hidden("hidden"),
    auto("auto"),
    visible("visible"),
    initial("initial"),
    inherit("inherit"),
        ;
    final String value;
    overflow(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
