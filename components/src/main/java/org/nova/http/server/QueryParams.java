package org.nova.http.server;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpRequest;

public class QueryParams
{
    final private HttpServletRequest request;
    QueryParams(HttpServletRequest request)
    {
        this.request=request;
    }
    public boolean containsName(String name) throws Exception
    {
        String value=this.request.getParameter(name);
        return value!=null;
    }
    public String getValue(String name) throws Exception
    {
        return this.request.getParameter(name);
    }
    public String[] getNames() throws Exception
    {
        return this.request.getParameterMap().keySet().toArray(new String[this.request.getParameterMap().size()]);
    }
}
