package org.nova.html.enums;

public enum name
{
    application_name("application-name"),
    author("author"),
    description("description"),
    generator("generator"),
    keywords("keywords"),
    viewport("viewport"),
    ;
    private String value;
    name(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }
}
