package org.nova.html.enums;

public enum sandbox
{
    allow_forms("allow-forms"),
    allow_pointer_lock("allow-pointer-lock"),
    allow_popups("allow-popups"),
    allow_same_origin("allow-same-origin"),
    allow_scripts("allow-scripts"),
    allow_top_navigation("allow-top-navigation"),
    ;
    private String value;
    sandbox(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }}
