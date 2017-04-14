package org.nova.html.enums;

public enum crossorigin
{
    anonymous("anonymous"),
    use_credentials("use-credentials"),
    ;
    private String value;
    crossorigin(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }}
