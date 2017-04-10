package org.nova.html.tags;

public enum formenctype
{
    urlencoded("application/x-www-form-urlencoded"),
    form_data("multipart/form-data"),
    plain("text/plain"),
   ;
    
    private String value;
    
    formenctype(String value)
    {
        this.value=value;
    }
    
}
