package org.nova.html.enums;

public enum enctype
{
    application("application/x-www-form-urlencoded"),
    multipart("multipart/form-data"),
    text("text/plain"),
    ;
    private String value;
    enctype(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }
}
