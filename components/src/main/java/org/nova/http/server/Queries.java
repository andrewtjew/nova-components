package org.nova.http.server;

import javax.servlet.http.HttpServletRequest;

public class Queries
{
    final private HttpServletRequest request;
    Queries(HttpServletRequest request)
    {
        this.request=request;
    }
    public boolean containsName(String name)
    {
        return this.request.getParameter(name)!=null;
    }
    public String getValue(String name)
    {
        return this.request.getParameter(name);
    }
    
}
