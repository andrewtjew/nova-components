package org.nova.html.attributes;

public enum input_type
{
    button("button"),
    checkbox("checkbox"),
    color("color"),
    date("date"),
    datetime_local("datetime-local"),
    email("email"),
    file("file"),
    hidden("hidden"),
    image("image"),
    month("month"),
    number("number"),
    password("password"),
    radio("radio"),
    range("range"),
    reset("reset"),
    search("search"),
    submit("submit"),
    tel("tel"),
    text("text"),
    time("time"),
    url("url"),
    week("week"),
        ;
    final String value;
    input_type(String value)
    {
        this.value=value;
    }
    @Override
    public String toString()
    {
        return this.value;
    }
}
