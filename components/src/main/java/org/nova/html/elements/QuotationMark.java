package org.nova.html.elements;

public enum QuotationMark 
{
    SINGLE("'"), 
    DOUBLE("\""), 
    APOS("&apos;"), 
    QOUT("&quot;") 
    ;
    private String value;

    QuotationMark(String value)
    {
        this.value = value;
    }

    public String toString()
    {
        return this.value;
    }}
