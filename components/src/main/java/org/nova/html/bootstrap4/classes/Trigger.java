package org.nova.html.bootstrap4.classes;


public enum Trigger
{
    click("click"), 
    hover("hover"), 
    focus("focus"), 
    manual("manual"), 
    ;
    
    private String value;

    Trigger(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }
}

