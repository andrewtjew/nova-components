package org.nova.html.enums;

public enum link_rel
{
    alternate("alternate"),
    author("author"),
    dns_prefetch("dns-prefetch"),
    help("help"),
    license("license"),
    next("next"),
    pingback("pingback"),
    preconnect("preconnect"),
    prefetch("prefetch"),
    preload("preload"),
    prerender("prerender"),
    prev("prev"),
    search("search"),
    stylesheet("stylesheet"),   
    ;
    private String value;
    link_rel(String value)
    {
        this.value=value;
    }
    public String toString()
    {
        return this.value;
    }
}
